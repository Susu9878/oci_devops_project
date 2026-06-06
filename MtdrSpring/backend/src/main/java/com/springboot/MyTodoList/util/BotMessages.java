package com.springboot.MyTodoList.util;

public enum BotMessages {

	HELLO_MYTODO_BOT(
			"Hello! I'm Sprint Task Bot!\n" +
					"Use /login <userId> to access your tasks."),

	BOT_REGISTERED_STARTED("Bot registered and started successfully!"),

	LOGIN_SUCCESS("Login successful!"),
	LOGIN_REQUIRED("Please login first using:\n/login <userId>"),

	TASK_UPDATED("Task status updated successfully!"),
	TASK_DELETED("Task deleted successfully!"),

	TYPE_NEW_TASK("Type the new task description below."),
	TASK_ADDED("New task added successfully!"),

	NO_ACTIVE_SPRINT("No active sprint tasks found."),

	REGISTER_WORK_HOURS_TASK_ID(
			"Please enter the task ID you wish to save hours to, or /start to go to the main screen."),
	REGISTER_WORK_HOURS_USER_ID(
			"Please enter the user ID you wish to save hours to, or /start to go to the main screen."),
	REGISTER_WORK_HOURS_WORKED_DAY(
			"Please enter the day you wish to save hours for, or /start to go to the main screen."),
	REGISTER_WORK_HOURS_WORKED_HOURS(
			"Please enter the number of hours worked and press send, or /start to go to the main screen."),
	WORK_HOURS_REGISTERED(
			"Worked hours registered! Select /start to go to the main screen."),

	BYE("Menu hidden. Type /start to resume.");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}
}
