package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.config.BotProps;
import com.springboot.MyTodoList.service.DeepSeekService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.UserService;
import com.springboot.MyTodoList.util.BotActions;
import com.springboot.MyTodoList.util.BotHelper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class ToDoItemBotController implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private DeepSeekService deepSeekService;
	private SprintService sprintService;
	private UserService userService;
	private final TelegramClient telegramClient;

	private final BotProps botProps;

	private final Map<Long, Long> loggedUsers = new HashMap<>();

	@Value("${telegram.bot.token}")
	private String telegramBotToken;

	@Override
	public String getBotToken() {
		if (telegramBotToken != null && !telegramBotToken.trim().isEmpty()) {
			return telegramBotToken;
		} else {
			return botProps.getToken();
		}
	}

	public ToDoItemBotController(BotProps bp, ToDoItemService tsvc, DeepSeekService ds, UserService us,
			SprintService ss) {
		this.botProps = bp;
		telegramClient = new OkHttpTelegramClient(getBotToken());
		toDoItemService = tsvc;
		deepSeekService = ds;
		userService = us;
		sprintService = ss;
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	@Override
	public void consume(Update update) {

		if (!update.hasMessage() || !update.getMessage().hasText())
			return;

		String messageTextFromTelegram = update.getMessage().getText();
		long chatId = update.getMessage().getChatId();

		if (messageTextFromTelegram.startsWith("/login")) {
			try {
				String[] parts = messageTextFromTelegram.split(" ");
				Long userId = Long.valueOf(parts[1]);

				loggedUsers.put(chatId, userId);
				BotHelper.sendMessageToTelegram(
						chatId,
						"Logged in as user " + userId + " type Main Screen to see more options.",
						telegramClient);
			} catch (Exception e) {
				BotHelper.sendMessageToTelegram(
						chatId,
						"Usage: /login <userId>",
						telegramClient);
			}
			return;
		}

		Long userId = loggedUsers.get(chatId);

		BotActions actions = new BotActions(telegramClient, toDoItemService, deepSeekService, userService,
				sprintService);
		actions.setRequestText(messageTextFromTelegram);
		actions.setChatId(chatId);
		if (userId != null) {
			actions.setUserId(userId.intValue());
		}

		if (actions.getTodoService() == null) {
			logger.info("todosvc error");
			actions.setTodoService(toDoItemService);
		}

		actions.fnStart();
		actions.fnUpdateStatus();
		actions.fnDelete();
		actions.fnHide();
		actions.fnListActiveSprintTasks();
		actions.fnAddItem();
		actions.fnLLM();
		actions.fnElse();

	}

	@AfterBotRegistration
	public void afterRegistration(BotSession botSession) {
		System.out.println("Registered bot running state is: " + botSession.isRunning());
	}

}
