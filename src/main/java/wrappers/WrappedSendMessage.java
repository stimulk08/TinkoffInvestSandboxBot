package wrappers;

import models.keyboards.InlineKeyboard;
import models.keyboards.KeyboardFactory;
import models.keyboards.KeyboardType;
import models.keyboards.SimpleKeyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WrappedSendMessage implements Message {

	private final long chatId;
	private final String message;
	private final KeyboardType keyboardType;
	private InlineKeyboard inlineKeyboard;
	private SimpleKeyboard simpleKeyboard;
	private boolean enableMarkdown;

	public WrappedSendMessage(long chatId, String message, InlineKeyboard keyboard) {
		this.chatId = chatId;
		this.message = message;
		keyboardType = KeyboardType.INLINE;
		inlineKeyboard = keyboard;
	}

	public WrappedSendMessage(long chatId, String message, SimpleKeyboard keyboard) {
		this.chatId = chatId;
		this.message = message;
		keyboardType = KeyboardType.SIMPLE;
		simpleKeyboard = keyboard;
	}

	public WrappedSendMessage(long chatId, String message) {
		this.chatId = chatId;
		this.message = message;
		keyboardType = KeyboardType.NONE;
	}

	public String getMessage() {
		return message;
	}

	public void setEnableMarkdown() {
		enableMarkdown = true;
	}

	@Override
	public BotApiMethod createMessage() {
		SendMessage sendMessage = new SendMessage(chatId, message);
		if (keyboardType.equals(KeyboardType.SIMPLE)) {
			sendMessage.setReplyMarkup(
					KeyboardFactory.makeReplyKeyboard(
							simpleKeyboard.getKeyboardRows()));
		} else if (keyboardType.equals(KeyboardType.INLINE)) {
			sendMessage.setReplyMarkup(
					KeyboardFactory.makeInlineKeyboard(
							inlineKeyboard.getKeyboardRows()));
		}
		sendMessage.enableMarkdown(enableMarkdown);
		return sendMessage;
	}
}
