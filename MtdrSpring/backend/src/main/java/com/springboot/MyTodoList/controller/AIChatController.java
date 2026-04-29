package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.DTO.UserKpiDTO;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.SprintRepository;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AIChatController {

    @Value("${anthropic.api.key}")
    private String anthropicApiKey;

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SprintRepository sprintRepository;

    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";
    private static final String CLAUDE_MODEL = "claude-sonnet-4-6";

    // ── DTOs ──────────────────────────────────────────────────────────────

    public static class ChatRequest {
        public String username;
        public String option;
        public String freeText;
    }

    public static class ChatResponse {
        public String reply;
        public boolean userNotFound;
        public List<RoadmapStep> roadmap;

        public ChatResponse(String reply, boolean userNotFound, List<RoadmapStep> roadmap) {
            this.reply        = reply;
            this.userNotFound = userNotFound;
            this.roadmap      = roadmap;
        }
    }

    public static class RoadmapStep {
        public int    order;
        public String task;
        public String reason;

        public RoadmapStep(int order, String task, String reason) {
            this.order  = order;
            this.task   = task;
            this.reason = reason;
        }
    }

    // ── GET /ai/greet ─────────────────────────────────────────────────────

    @GetMapping("/greet")
    public ResponseEntity<Map<String, Object>> greet(@RequestParam String username) {
        Map<String, Object> body = new HashMap<>();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            body.put("found", false);
            body.put("message", "User \"" + username + "\" was not found in the system. Please check your username and try again.");
            return ResponseEntity.ok(body);
        }

        body.put("found", true);
        body.put("message", "Hello " + username + "! What would you like to do?");
        return ResponseEntity.ok(body);
    }

    // ── POST /ai/chat ──────────────────────────────────────────────────────

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {

        // 1. Validate user exists
        Optional<User> userOpt = userRepository.findByUsername(req.username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(new ChatResponse(
                    "User \"" + req.username + "\" is not in the database. Please restart and enter a valid username.",
                    true, null
            ));
        }

        User user    = userOpt.get();
        int teamId   = user.getTeam().getTeamId();
        int sprintId = getLatestSprintId();

        // 2. Fetch KPI data
        Optional<UserKpiDTO> userKpiOpt = toDoItemRepository
                .getUserKpisRaw(sprintId, teamId)
                .stream()
                .map(this::mapRow)
                .filter(k -> k.getUsername().equalsIgnoreCase(req.username))
                .findFirst();

        if (userKpiOpt.isEmpty()) {
            return ResponseEntity.ok(new ChatResponse(
                    "I couldn't find any sprint data for " + req.username + " in the current sprint.",
                    false, null
            ));
        }

        // 3. Fetch actual tasks for this user in this sprint
        List<ToDoItem> activeTasks = toDoItemRepository
                .findActiveTasksByUserAndSprint(user.getUserId(), sprintId);

        // 4. Build prompt and call Claude
        String prompt      = buildPrompt(req, userKpiOpt.get(), activeTasks);
        String claudeReply = callClaude(prompt);

        // 5. Parse roadmap if option A
        List<RoadmapStep> roadmap = "A".equalsIgnoreCase(req.option)
                ? parseRoadmap(claudeReply)
                : null;

        return ResponseEntity.ok(new ChatResponse(claudeReply, false, roadmap));
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private int getLatestSprintId() {
        return sprintRepository.findAll()
                .stream()
                .filter(s -> s.getStatus() == Sprint.SprintStatus.ACTIVE)
                .max(Comparator.comparingInt(Sprint::getSprintId))
                .map(Sprint::getSprintId)
                .orElseGet(() -> sprintRepository.findAll()
                        .stream()
                        .max(Comparator.comparingInt(Sprint::getSprintId))
                        .map(Sprint::getSprintId)
                        .orElse(1));
    }

    private UserKpiDTO mapRow(Object[] row) {
        UserKpiDTO dto = new UserKpiDTO();
        dto.setUserId(((Number) row[0]).intValue());
        dto.setUsername((String) row[1]);
        dto.setTasksCompleted(((Number) row[2]).longValue());
        dto.setTasksInProgress(((Number) row[3]).longValue());
        dto.setTasksNotStarted(((Number) row[4]).longValue());
        dto.setTasksNotDone(row[5] != null ? ((Number) row[5]).longValue() : 0);
        dto.setHoursWorked(((Number) row[6]).doubleValue());
        return dto;
    }

    private String buildPrompt(ChatRequest req, UserKpiDTO kpi, List<ToDoItem> tasks) {

        StringBuilder taskList = new StringBuilder();
        for (ToDoItem t : tasks) {
            taskList.append(String.format(
                    "- [%s] \"%s\" | Priority: %s | Expected hours: %s | Story points: %s | Description: %s\n",
                    t.getStatus(),
                    t.getTaskName(),
                    t.getPriority(),
                    t.getExpectedHours() != null ? t.getExpectedHours() : "?",
                    t.getStoryPoints()   != null ? t.getStoryPoints()   : "?",
                    t.getDescription()   != null ? t.getDescription()   : "none"
            ));
        }

        if ("A".equalsIgnoreCase(req.option)) {
            return "You are a helpful project assistant for a software development team.\n\n"
                    + "User \"" + req.username + "\" has these open tasks this sprint:\n"
                    + taskList
                    + "\nHours logged so far this sprint: " + kpi.getHoursWorked() + "\n\n"
                    + "Suggest a prioritized roadmap of what they should tackle first and why. "
                    + "Base your reasoning on priority level, complexity (story points and expected hours), and current status. "
                    + "Give one practical tip per task. "
                    + "Reply ONLY as a numbered list. Each item: \"N. [task name] — [one sentence reason + tip]\". "
                    + "No preamble, no headers, no markdown.";
        }

        return "You are TaskBot, a friendly assistant for a software dev team.\n"
                + "User: \"" + req.username + "\"\n"
                + "Their open tasks this sprint:\n" + taskList
                + "\nHours logged so far: " + kpi.getHoursWorked() + "\n\n"
                + "User says: \"" + req.freeText + "\"\n\n"
                + "Reply helpfully in 2-3 sentences referencing their actual tasks by name.";
    }

    private String callClaude(String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", anthropicApiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userPrompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", CLAUDE_MODEL);
        body.put("max_tokens", 1024);
        body.put("messages", List.of(message));

        try {
            ResponseEntity<Map> response = new RestTemplate().exchange(
                    CLAUDE_API_URL, HttpMethod.POST,
                    new HttpEntity<>(body, headers), Map.class);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content =
                    (List<Map<String, Object>>) response.getBody().get("content");

            return content.stream()
                    .filter(c -> "text".equals(c.get("type")))
                    .map(c -> (String) c.get("text"))
                    .collect(Collectors.joining("\n"));

        } catch (Exception e) {
            return "Sorry, I couldn't reach the AI service right now. Please try again.";
        }
    }

    private List<RoadmapStep> parseRoadmap(String text) {
        List<RoadmapStep> steps = new ArrayList<>();
        for (String line : text.split("\n")) {
            line = line.trim();
            if (!line.matches("^\\d+\\..*")) continue;
            String body    = line.replaceFirst("^\\d+\\.\\s*", "");
            String[] parts = body.split(" [—-] ", 2);
            steps.add(new RoadmapStep(steps.size() + 1, parts[0].trim(),
                    parts.length > 1 ? parts[1].trim() : ""));
        }
        return steps;
    }
}