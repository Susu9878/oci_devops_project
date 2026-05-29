package com.springboot.MyTodoList.util;

public enum BotMessages {

	HELLO_MYTODO_BOT("Hello! I'm Sprint Task Bot!\n" + "Use /login <userId> to access your tasks."),
	BOT_REGISTERED_STARTED("Bot registered and started successfully!"),
	LOGIN_SUCCESS("Login successful!"),
	LOGIN_REQUIRED("Please login first using:\n/login <userId>"),
	TASK_UPDATED("Task status updated successfully!"),
	TASK_DELETED("Task deleted successfully!"),
	TYPE_NEW_TASK("Type the new task description below."),
	TASK_ADDED("New task added successfully!"),
	NO_ACTIVE_SPRINT("No active sprint tasks found."),
	BYE("Menu hidden. Type /start to resume.");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
