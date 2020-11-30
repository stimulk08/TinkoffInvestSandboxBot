package handlers;

import models.Handler;
import models.State;
import models.User;

import java.util.Collections;
import java.util.List;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class StartHandler implements Handler {

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		String startText = "*Привет! Я InvestBot*\n" +
				"Я помогу тебе улучшить твои навыки в инвестировании и трейдинге" +
				"Для начала введи свой api-ключ для Tinkoff песочницы.\n" +
				"Успехов! \n\n" +
				"*Тут помощь* - /help";
		WrappedSendMessage startMessage = new WrappedSendMessage(user.getChatId(), startText);
		startMessage.setEnableMarkdown();
		WrappedSendMessage authMessage = new WrappedSendMessage(
				user.getChatId(), "Введите свой токен");
		user.setState(State.NON_AUTHORIZED);
		user.setLastQueryTime();
		return List.of(startMessage, authMessage);
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		return Collections.emptyList();
	}

	@Override
	public State handledState() {
		return State.NONE;
	}

	@Override
	public List<String> handledCallBackQuery() {
		return Collections.emptyList();
	}
}
