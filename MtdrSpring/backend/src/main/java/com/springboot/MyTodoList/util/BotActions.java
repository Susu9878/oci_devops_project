package com.springboot.MyTodoList.util;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.ToDoItem.TaskStatus;
import com.springboot.MyTodoList.service.DeepSeekService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.UserService;

import static org.mockito.Mockito.calls;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    public BotActions(TelegramClient tc, ToDoItemService ts, DeepSeekService ds, UserService us, SprintService ss) {
        telegramClient = tc;
        todoService = ts;
        deepSeekService = ds;
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
        if (exit)
            return;

        try {
            String[] parts = requestText.split("-");
            if (parts.length != 2)
                return;

            Integer id = Integer.valueOf(parts[0]);
            String action = parts[1].trim().toUpperCase();
            ToDoItem item = todoService.getToDoItemById(id);

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

        List<ToDoItem> tasks = todoService.findTasksByUserAndActiveSprint(userId);

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

            KeyboardRow row = new KeyboardRow();

            row.add(item.getTaskName());

            switch (item.getStatus()) {

                case NOT_STARTED, NOT_DONE:
                    row.add(item.getTaskId() + "-START");
                    break;

                case IN_PROGRESS:
                    row.add(item.getTaskId() + "-DONE");
                    row.add(item.getTaskId() + "-RESET");
                    break;

                case DONE:
                    row.add(item.getTaskId() + "-UNDO");
                    break;
            }

            row.add(item.getTaskId() + "-DELETE");

            keyboard.add(row);
        }

        KeyboardRow bottomRow = new KeyboardRow();
        bottomRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
        keyboard.add(bottomRow);

        keyboardMarkup.setKeyboard(keyboard);

        BotHelper.sendMessageToTelegram(
                chatId,
                "Active Sprint Tasks",
                telegramClient,
                keyboardMarkup);

        exit = true;
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

}