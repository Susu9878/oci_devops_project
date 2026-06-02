package com.springboot.MyTodoList.util;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.WorkLog;
import com.springboot.MyTodoList.service.DeepSeekService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.UserService;
import com.springboot.MyTodoList.service.WorkedHourRegistrationService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class BotActions {

    private static final Logger logger = LoggerFactory.getLogger(BotActions.class);

    String requestText;
    long chatId;
    TelegramClient telegramClient;
    boolean exit;

    ToDoItemService todoService;
    DeepSeekService deepSeekService;
    WorkedHourRegistrationService workedHourRegistrationService;
    UserService userService;

    private static final Map<Long, WorkHoursRegistrationContext> workHoursRegistrationContexts = new ConcurrentHashMap<>();

    enum WorkHoursRegistrationStage { NONE, ASK_TASK_ID, ASK_USER_ID, ASK_WORKED_DAY, ASK_WORKED_HOURS }

    static class WorkHoursRegistrationContext {
        WorkHoursRegistrationStage stage = WorkHoursRegistrationStage.NONE;
        Integer taskId;
        User user;
        OffsetDateTime workedDay;
    }

    public BotActions(TelegramClient tc, ToDoItemService ts, DeepSeekService ds, WorkedHourRegistrationService whrsvc, UserService usvc){
        telegramClient = tc;
        todoService = ts;
        deepSeekService = ds;
        workedHourRegistrationService = whrsvc;
        userService = usvc;
        exit  = false;
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

    public void setWorkedHourRegistrationService(WorkedHourRegistrationService whrsvc){
        workedHourRegistrationService = whrsvc;
    }

    public WorkedHourRegistrationService getWorkedHourRegistrationService(){
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
                                new KeyboardRow(BotLabels.LIST_ALL_ITEMS.getLabel(), BotLabels.ADD_NEW_ITEM.getLabel()))
                        .keyboardRow(new KeyboardRow(BotLabels.SHOW_MAIN_SCREEN.getLabel(),
                                BotLabels.HIDE_MAIN_SCREEN.getLabel()))
                        .build());
        exit = true;
    }

    public void fnDone() {
        if (!(requestText.indexOf(BotLabels.DONE.getLabel()) != -1) || exit)
            return;

        String done = requestText.substring(0, requestText.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(done);

        try {

            ToDoItem item = todoService.getToDoItemById(id);
            item.setDone(true);
            todoService.updateToDoItem(id, item);
            BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), telegramClient);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        exit = true;
    }

    public void fnUndo() {
        if (requestText.indexOf(BotLabels.UNDO.getLabel()) == -1 || exit)
            return;

        String undo = requestText.substring(0,
                requestText.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(undo);

        try {

            ToDoItem item = todoService.getToDoItemById(id);
            item.setDone(false);
            todoService.updateToDoItem(id, item);
            BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), telegramClient);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        exit = true;
    }

    public void fnDelete() {
        if (requestText.indexOf(BotLabels.DELETE.getLabel()) == -1 || exit)
            return;

        String delete = requestText.substring(0,
                requestText.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(delete);

        try {
            todoService.deleteToDoItem(id);
            BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), telegramClient);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        exit = true;
    }

    public void fnHide() {
        if (requestText.equals(BotCommands.HIDE_COMMAND.getCommand())
                || requestText.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel()) && !exit)
            BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), telegramClient);
        else
            return;
        exit = true;
    }

    public void fnListAll() {
        if (!(requestText.equals(BotCommands.TODO_LIST.getCommand())
                || requestText.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
                || requestText.equals(BotLabels.MY_TODO_LIST.getLabel())) || exit)
            return;
        logger.info("todoSvc: " + todoService);
        List<ToDoItem> allItems = todoService.findAll();
        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .selective(true)
                .build();

        List<KeyboardRow> keyboard = new ArrayList<>();

        // command back to main screen
        KeyboardRow mainScreenRowTop = new KeyboardRow();
        mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
        keyboard.add(mainScreenRowTop);

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
        keyboard.add(firstRow);

        KeyboardRow myTodoListTitleRow = new KeyboardRow();
        myTodoListTitleRow.add(BotLabels.MY_TODO_LIST.getLabel());
        keyboard.add(myTodoListTitleRow);

        List<ToDoItem> activeItems = allItems.stream().filter(item -> item.isDone() == false)
                .collect(Collectors.toList());

        for (ToDoItem item : activeItems) {
            KeyboardRow currentRow = new KeyboardRow();
            currentRow.add(item.getDescription());
            currentRow.add(item.getTaskId() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
            keyboard.add(currentRow);
        }

        List<ToDoItem> doneItems = allItems.stream().filter(item -> item.isDone() == true)
                .collect(Collectors.toList());

        for (ToDoItem item : doneItems) {
            KeyboardRow currentRow = new KeyboardRow();
            currentRow.add(item.getDescription());
            currentRow.add(item.getTaskId() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
            currentRow.add(item.getTaskId() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
            keyboard.add(currentRow);
        }

        // command back to main screen
        KeyboardRow mainScreenRowBottom = new KeyboardRow();
        mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
        keyboard.add(mainScreenRowBottom);

        keyboardMarkup.setKeyboard(keyboard);

        //
        BotHelper.sendMessageToTelegram(chatId, BotLabels.MY_TODO_LIST.getLabel(), telegramClient, keyboardMarkup);//
        exit = true;
    }

    public void fnAddItem() {
        logger.info("Adding item");
        if (!(requestText.contains(BotCommands.ADD_ITEM.getCommand())
                || requestText.contains(BotLabels.ADD_NEW_ITEM.getLabel())) || exit)
            return;
        logger.info("Adding item by BotHelper");
        BotHelper.sendMessageToTelegram(chatId, BotMessages.TYPE_NEW_TODO_ITEM.getMessage(), telegramClient);
        exit = true;
    }

    public void fnElse() {
        if (exit)
            return;
        ToDoItem newItem = new ToDoItem();
        newItem.setDescription(requestText);
        newItem.setCreatedAt(OffsetDateTime.now());
        newItem.setDone(false);
        todoService.addToDoItem(newItem);

        BotHelper.sendMessageToTelegram(chatId, BotMessages.NEW_ITEM_ADDED.getMessage(), telegramClient, null);
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

    public void fnRegisterWorkHours(){
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
        if (ctx.stage == WorkHoursRegistrationStage.NONE || exit)
            return;

        try {
            switch (ctx.stage) {
                case ASK_TASK_ID: {
                    int taskId = Integer.parseInt(requestText.trim());
                    ToDoItem task = todoService.getToDoItemById(taskId);
                    if (task == null) {
                        BotHelper.sendMessageToTelegram(chatId, "Task ID not found. Please enter a valid task ID.", telegramClient);
                        exit = true;
                        return;
                    }
                    ctx.taskId = taskId;
                    ctx.stage = WorkHoursRegistrationStage.ASK_USER_ID;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_USER_ID.getMessage(), telegramClient);
                    break;
                }
                case ASK_USER_ID: {
                    int userId = Integer.parseInt(requestText.trim());
                    var userResponse = userService.getUserById(userId);
                    if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
                        BotHelper.sendMessageToTelegram(chatId, "User ID not found. Please enter a valid user ID.", telegramClient);
                        exit = true;
                        return;
                    }
                    ctx.user = userResponse.getBody();
                    ctx.stage = WorkHoursRegistrationStage.ASK_WORKED_DAY;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_WORKED_DAY.getMessage(), telegramClient);
                    break;
                }
                case ASK_WORKED_DAY: {
                    LocalDate workedDay = LocalDate.parse(requestText.trim());
                    ctx.workedDay = workedDay.atStartOfDay().atOffset(ZoneOffset.UTC);
                    ctx.stage = WorkHoursRegistrationStage.ASK_WORKED_HOURS;
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.REGISTER_WORK_HOURS_WORKED_HOURS.getMessage(), telegramClient);
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
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.WORK_HOURS_REGISTERED.getMessage(), telegramClient);
                    clearRegistrationContext();
                    break;
                }
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            BotHelper.sendMessageToTelegram(chatId, "Invalid input format. Please enter the value again.", telegramClient);
        }
        exit = true;
    }


}