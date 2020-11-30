package models.keyboards;

import java.util.List;

public class SimpleKeyboard {

	private final List<List<String>> keyboardRows;

	public SimpleKeyboard(List<List<String>> keyboardRows) {
		this.keyboardRows = keyboardRows;
	}

	public List<List<String>> getKeyboardRows() {
		return keyboardRows;
	}
}
