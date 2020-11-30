package wrappers;

import models.keyboards.InlineKeyboard;
import models.keyboards.KeyboardFactory;
import models.keyboards.KeyboardType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class WrappedEditMessage implements Message {

	private final long chatId;
	private final String message;
	private final Integer messageId;
	private final KeyboardType keyboardType;
	private final InlineKeyboard inlineKeyboard;

	public WrappedEditMessage(long chatId, String message, Integer messageId,
			InlineKeyboard keyboard) {
		this.chatId = chatId;
		this.message = message;
		this.messageId = messageId;
		keyboardType = KeyboardType.INLINE;
		inlineKeyboard = keyboard;
	}

	@Override
	public BotApiMethod createMessage() {
		EditMessageText editMessageText = new EditMessageText()
				.setChatId(chatId)
				.setMessageId(messageId)
				.setText(message);
		if (keyboardType.equals(KeyboardType.INLINE)) {
			editMessageText.setReplyMarkup(
					KeyboardFactory.makeInlineKeyboard(
							inlineKeyboard.getKeyboardRows()));
		}
		return editMessageText;
	}
}
