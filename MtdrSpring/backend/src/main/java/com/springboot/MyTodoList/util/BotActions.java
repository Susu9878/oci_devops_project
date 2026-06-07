package com.springboot.MyTodoList.util;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.ToDoItem.TaskStatus;
import com.springboot.MyTodoList.model.WorkLog;
import com.springboot.MyTodoList.service.DeepSeekService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.UserService;

import com.springboot.MyTodoList.service.WorkLogService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class BotActions {

    private static final Logger logger = LoggerFactory.getLogger(BotActions.class);

    String requestText;
    long chatId;
    TelegramClient telegramClient;
    boolean exit;
    private Integer userId;

    ToDoItemService todoService;
    DeepSeekService deepSeekService;
    UserService userService;
    SprintService sprintService;

    WorkLogService workedHourRegistrationService;
    private static final Map<Long, WorkHoursRegistrationContext> workHoursRegistrationContexts = new ConcurrentHashMap<>();

    enum WorkHoursRegistrationStage {
        NONE,
        ASK_TASK_ID,
        ASK_USER_ID,
        ASK_WORKED_DAY,
        ASK_WORKED_HOURS
    }

    static class WorkHoursRegistrationContext {
        WorkHoursRegistrationStage stage = WorkHoursRegistrationStage.NONE;
        Integer taskId;
        User user;
        OffsetDateTime workedDay;
    }

    enum TaskCreationStage {
        NONE,
        ASK_NAME,
        ASK_DESCRIPTION,
        ASK_STORY_POINTS,
        ASK_EXPECTED_HOURS,
        ASK_PRIORITY
    }

    static class TaskCreationContext {
        TaskCreationStage stage = TaskCreationStage.NONE;

        String name;
        String description;
        Integer storyPoints;
        Double expectedHours;
    }

    public BotActions(TelegramClient tc, ToDoItemService ts, DeepSeekService ds, UserService us, SprintService ss,
            WorkLogService whrsvc) {
        telegramClient = tc;
        todoService = ts;
        deepSeekService = ds;
        workedHourRegistrationService = whrsvc;
        userService = us;
        sprintService = ss;

        exit = false;
    }

    public void setRequestText(String cmd) {
        requestText = cmd;
    }

    public void setChatId(long chId) {
        chatId = chId;
    }

    public void setTelegramClient(TelegramClient tc) {
        telegramClient = tc;
    }

    public void setTodoService(ToDoItemService tsvc) {
        todoService = tsvc;
    }

    public ToDoItemService getTodoService() {
        return todoService;
    }

    public void setDeepSeekService(DeepSeekService dssvc) {
        deepSeekService = dssvc;
    }

    public DeepSeekService getDeepSeekService() {
        return deepSeekService;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setWorkedLogService(WorkLogService whrsvc) {
        workedHourRegistrationService = whrsvc;
    }

    public WorkLogService getWorkLogService() {
        return workedHourRegistrationService;
    }

    public void setUserService(UserService usvc) {
        userService = usvc;
    }

    public UserService getUserService() {
        return userService;
    }

    private WorkHoursRegistrationContext getRegistrationContext() {
        return workHoursRegistrationContexts.computeIfAbsent(chatId, k -> new WorkHoursRegistrationContext());
    }

    private void clearRegistrationContext() {
        workHoursRegistrationContexts.remove(chatId);
    }

    public void fnStart() {
        if (!(requestText.equals(BotCommands.START_COMMAND.getCommand())
                || requestText.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) || exit)
            return;

        BotHelper.sendMessageToTelegram(chatId, BotMessages.HELLO_MYTODO_BOT.getMessage(), telegramClient,
                ReplyKeyboardMarkup
                        .builder()
                        .keyboardRow(
                                new KeyboardRow(BotLabels.LIST_ACTIVE_TASKS.getLabel(),
                                        BotLabels.ADD_NEW_ITEM.getLabel()))
                        .keyboardRow(new KeyboardRow(BotLabels.SHOW_MAIN_SCREEN.getLabel(),
                                BotLabels.HIDE_MAIN_SCREEN.getLabel()))
                        .build());
        exit = true;
    }

    public void fnUpdateStatus() {
        logger.info("fnUpdateStatus called with [{}]", requestText);

        if (exit)
            return;

        try {
            String[] parts = requestText.split("-");
            // logger.info("parts.length={}", parts.length);
            if (parts.length != 2)
                return;

            Integer id = Integer.valueOf(parts[0]);
            String action = parts[1].trim().toUpperCase();
            // logger.info("id={} action={}", id, action);
            ToDoItem item = todoService.getToDoItemById(id);
            // logger.info("before status={}", item.getStatus());

            switch (action) {
                case "DONE":
                    item.setStatus(TaskStatus.DONE);
                    item.setCompletionDate(OffsetDateTime.now());
                    break;
                case "UNDO":
                    item.setStatus(TaskStatus.NOT_STARTED);
                    item.setCompletionDate(null);
                    break;
                case "START":
                    item.setStatus(TaskStatus.IN_PROGRESS);
                    if (item.getStartDate() == null) {
                        item.setStartDate(OffsetDateTime.now());
                    }
                    break;
                case "RESET":
                    item.setStatus(TaskStatus.NOT_STARTED);
                    item.setStartDate(null);
                    item.setCompletionDate(null);
                    break;
                default:
                    return;
            }

            todoService.updateToDoItem(id, item);

            BotHelper.sendMessageToTelegram(
                    chatId,
                    "Task updated to: " + item.getStatus(),
                    telegramClient);

            exit = true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }

    public void fnDelete() {
        if (requestText.indexOf(BotLabels.DELETE.getLabel()) == -1 || exit)
            return;

        String delete = requestText.substring(0,
                requestText.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(delete);

        try {
            todoService.deleteToDoItem(id);
            BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_DELETED.getMessage(), telegramClient);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        exit = true;
    }

    public void fnHide() {
        if ((requestText.equals(BotCommands.HIDE_COMMAND.getCommand())
                || requestText.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel()))
                && !exit)
            BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), telegramClient);
        else
            return;
        exit = true;
    }

    public void fnListActiveSprintTasks() {
        logger.info("requestText=[{}]", requestText);
        logger.info("expected=[{}]", BotLabels.LIST_ACTIVE_TASKS.getLabel());
        // logger.info("userId={}", userId);
        try {
            if (userId == null) {
                BotHelper.sendMessageToTelegram(
                        chatId,
                        "Please login first using:\n/login <userId>",
                        telegramClient);

                exit = true;
                return;
            }
            if (!(requestText.equals(BotCommands.TASK_LIST.getCommand())
                    || requestText.equals(BotLabels.LIST_ACTIVE_TASKS.getLabel())))
                return;
            logger.info("debugBeforeQuery");
            List<ToDoItem> tasks = todoService.findTasksByUserAndActiveSprint(userId)
                    .stream()
                    .sorted(Comparator.comparing(ToDoItem::getStatus))
                    .toList();
            logger.info("Found {} tasks", tasks.size());

            ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(false)
                    .selective(true)
                    .build();
            List<KeyboardRow> keyboard = new ArrayList<>();

            // top controls
            KeyboardRow topRow = new KeyboardRow();
            topRow.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
            keyboard.add(topRow);

            for (ToDoItem item : tasks) {
                KeyboardRow taskrow = new KeyboardRow();
                taskrow.add("[" + item.getStatus() + "] " + item.getTaskName());
                keyboard.add(taskrow);

                KeyboardRow actionsRow = new KeyboardRow();
                switch (item.getStatus()) {

                    case NOT_STARTED, NOT_DONE:
                        actionsRow.add(item.getTaskId() + "-START");
                        break;

                    case IN_PROGRESS:
                        actionsRow.add(item.getTaskId() + "-DONE");
                        actionsRow.add(item.getTaskId() + "-RESET");
                        break;

                    case DONE:
                        actionsRow.add(item.getTaskId() + "-UNDO");
                        break;
                }

                // actionsRow.add(item.getTaskId() + "-DELETE");

                keyboard.add(actionsRow);
            }

            KeyboardRow bottomRow = new KeyboardRow();
            bottomRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
            keyboard.add(bottomRow);
            keyboardMarkup.setKeyboard(keyboard);

            logger.info("step1.h");
            BotHelper.sendMessageToTelegram(
                    chatId,
                    "Active Sprint Tasks",
                    telegramClient,
                    keyboardMarkup);
            exit = true;

        } catch (Exception e) {
            logger.error("Telegram send failed", e);
            exit = true;
        }
    }

    // TODO DEPRECATE OR REFACTOR
    public void fnAddItem() {
        logger.info("Adding item");
        if (!(requestText.contains(BotCommands.ADD_ITEM.getCommand())
                || requestText.contains(BotLabels.ADD_NEW_ITEM.getLabel())) || exit)
            return;
        logger.info("Adding item by BotHelper");
        BotHelper.sendMessageToTelegram(chatId, BotMessages.TYPE_NEW_TASK.getMessage(), telegramClient);
        exit = true;
    }

    public void fnElse() {
        if (exit)
            return;
        ToDoItem newItem = new ToDoItem();

        ResponseEntity<User> response = userService.getUserById(userId);
        User user = response.getBody();

        Sprint activeSprint = sprintService.findActiveSprintByUserId(userId);

        newItem.setUser(user);
        newItem.setSprint(activeSprint);

        newItem.setTaskName(requestText);
        newItem.setDescription(requestText);
        newItem.setCreatedAt(OffsetDateTime.now());
        newItem.setStatus(TaskStatus.NOT_STARTED);
        todoService.addToDoItem(newItem);

        BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_ADDED.getMessage(), telegramClient, null);
    }

    public void fnLLM() {
        logger.info("Calling LLM");
        if (!(requestText.contains(BotCommands.LLM_REQ.getCommand())) || exit)
            return;

        String prompt = "Dame los datos del clima en mty";
        String out = "<empty>";
        try {
            out = deepSeekService.generateText(prompt);
        } catch (Exception exc) {

        }

        BotHelper.sendMessageToTelegram(chatId, "LLM: " + out, telegramClient, null);

    }

    public void fnRegisterWorkHours() {
        logger.info("Registering work hours");
        WorkHoursRegistrationContext ctx = getRegistrationContext();
        if (ctx.stage != WorkHoursRegistrationStage.NONE || exit)
            return;
        if (!(requestText.contains(BotCommands.REGISTER_WORK_HOURS.getCommand())
                || requestText.contains(BotLabels.REGISTER_WORK_HOURS.getLabel())))
            return;

        ctx.stage = WorkHoursRegistrationStage.ASK_TASK_ID;
        BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_TASK_ID.getMessage(), telegramClient);
        exit = true;
    }

    public void fnRegisterWorkHoursResponse() {
        WorkHoursRegistrationContext ctx = getRegistrationContext();
        if (exit)
            return;

        try {
            switch (ctx.stage) {
                case NONE: {
                    return;
                }
                case ASK_TASK_ID: {
                    int taskId = Integer.parseInt(requestText.trim());
                    ToDoItem task = todoService.getToDoItemById(taskId);
                    if (task == null) {
                        BotHelper.sendMessageToTelegram(chatId, "Task ID not found. Please enter a valid task ID.",
                                telegramClient);
                        exit = true;
                        return;
                    }
                    ctx.taskId = taskId;
                    ctx.stage = WorkHoursRegistrationStage.ASK_USER_ID;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_USER_ID.getMessage(),
                            telegramClient);
                    break;
                }
                case ASK_USER_ID: {
                    int userId = Integer.parseInt(requestText.trim());
                    var userResponse = userService.getUserById(userId);
                    if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
                        BotHelper.sendMessageToTelegram(chatId, "User ID not found. Please enter a valid user ID.",
                                telegramClient);
                        exit = true;
                        return;
                    }
                    ctx.user = userResponse.getBody();
                    ctx.stage = WorkHoursRegistrationStage.ASK_WORKED_DAY;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_WORKED_DAY.getMessage(),
                            telegramClient);
                    break;
                }
                case ASK_WORKED_DAY: {
                    LocalDate workedDay = LocalDate.parse(requestText.trim());
                    ctx.workedDay = workedDay.atStartOfDay().atOffset(ZoneOffset.UTC);
                    ctx.stage = WorkHoursRegistrationStage.ASK_WORKED_HOURS;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_WORKED_HOURS.getMessage(),
                            telegramClient);
                    break;
                }
                case ASK_WORKED_HOURS: {
                    double workedHours = Double.parseDouble(requestText.trim());
                    WorkLog workLog = new WorkLog();
                    workLog.setTaskId(todoService.getToDoItemById(ctx.taskId));
                    workLog.setUserId(ctx.user);
                    workLog.setWorkedDay(ctx.workedDay);
                    workLog.setWorkedHours(workedHours);
                    workLog.setCreatedAt(OffsetDateTime.now());
                    workedHourRegistrationService.addWorkLog(workLog);
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.WORK_HOURS_REGISTERED.getMessage(),
                            telegramClient);
                    clearRegistrationContext();
                    break;
                }
                default: {
                    return;
                }
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            BotHelper.sendMessageToTelegram(chatId, "Invalid input format. Please enter the value again.",
                    telegramClient);
        }
        exit = true;
    }

}