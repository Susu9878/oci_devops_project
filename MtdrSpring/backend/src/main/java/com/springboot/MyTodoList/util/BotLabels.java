package com.springboot.MyTodoList.util;

public enum BotLabels {

	SHOW_MAIN_SCREEN("Main Screen"),
	HIDE_MAIN_SCREEN("Hide Menu"),

	LIST_ACTIVE_TASKS("Active Sprint Tasks"),
	ADD_NEW_ITEM("Add Item"),

	START("START"),
	DONE("DONE"),
	RESET("RESET"),
	UNDO("UNDO"),
	DELETE("DELETE"),

	ACTIVE_SPRINT("ACTIVE SPRINT"),
	DASH("-");

	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
