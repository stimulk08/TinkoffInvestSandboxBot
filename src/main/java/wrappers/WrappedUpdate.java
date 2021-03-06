package wrappers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class WrappedUpdate {

	private long chatId;
	private String messageData;
	private Integer messageId;
	private boolean hasCallBackQuery;
	private String messageText;

	public WrappedUpdate(Update update) {
		if (update.hasCallbackQuery()) {
			hasCallBackQuery = true;
			chatId = update.getCallbackQuery().getFrom().getId();
			messageText = update.getCallbackQuery().getMessage().getText();
			messageData = update.getCallbackQuery().getData();
			messageId = update.getCallbackQuery().getMessage().getMessageId();
		} else if (update.hasMessage() && update.getMessage().hasText()) {
			chatId = update.getMessage().getChatId();
			messageData = update.getMessage().getText();
		}
	}

	public boolean hasCallBackQuery() {
		return hasCallBackQuery;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public String getMessageData() {
		return messageData;
	}

	public Long getChatId() {
		return chatId;
	}

	public String getMessageText(){
		return messageText;
	}
}
