package com.springboot.MyTodoList.util;

public enum BotCommands {

	START_COMMAND("/start"),
	HIDE_COMMAND("/hide"),
	LOGIN("/login"),
	TASK_LIST("/tasks"),
	ADD_ITEM("/additem"),
	REGISTER_WORK_HOURS("/registerworkhours"),
	LLM_REQ("/llm");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
