package models;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class UpdateReceiver {

	private final List<Handler> handlers;
	private final ConcurrentHashMap<Long, User> chatIdToUser;
	private final static String helpers = "*Я здесь чтобы помочь тебе*" +
			"\n\n*Базовые команды*\n" +
			"/start - начало работы\n" +
			"/help - вывод этой справки" +
			"\n\n*Как пользоваться?*\n" +
			"Боту нужен токен для торговли в режиме песочницы." +
			"Ниже о том, как его получить." +
			"Бот предложит создать новый портфель или продолжить со старым." +
			"После завершения настроек, " +
			"используйте кнопки в клавиатуре для взаимодействия с портфелем\n" +
			"Сейчас вам доступны:\n" +
			"    - Просмотр портфеля" +
			"\n\n*Как получить токен?*\n" +
			"*1.*Зайдите в свой аккаунт на tinkoff.ru\n" +
			"*2.*Перейдите в раздел инвестиций.\n" +
			"*3.*Перейдите в настройки.\n" +
			"*4.*Функция \"Подтверждение сделок кодом\" должна быть отключена\n" +
			"*5.*Выпустите токен OpenAPI для Sandbox. " +
			"Возможно, система попросит вас авторизоваться еще раз. " +
			"Не беспокойтесь, это необходимо для подключения.\n" +
			"*6.*Скопируйте и сохраните токен. Вводите его в диалоге с ботом при авторизации." +
			"Токен отображается только один раз, просмотреть его позже не получится." +
			"Тем не менее вы можете выпускать неограниченное количество токенов.";

	public UpdateReceiver(List<Handler> handlers) {
		this.handlers = handlers;
		chatIdToUser = new ConcurrentHashMap<>();
	}

	public List<Message> handle(WrappedUpdate update) {
		long chatId = update.getChatId();
		String username = update.getUsername();

		if (!chatIdToUser.containsKey(chatId)) {
			chatIdToUser.put(chatId, new User(chatId, username));
		}
		User user = chatIdToUser.get(chatId);

		long timeFromLastQuery = new Date().getTime() - user.getLastQueryTime();
		if (timeFromLastQuery > 1.8e+6 && !user.getState().equals(State.NON_AUTHORIZED)) {
			user.setState(State.NON_AUTHORIZED);
		}

		try {
			if (!update.hasCallBackQuery() && update.getMessageData() != null) {
				if (update.getMessageData().equals("/start")
						&& !user.getState().equals(State.NONE)) {
					user.setState(State.NONE);
				}
				return getHandlerByState(user.getState()).handleMessage(user, update);
			}
			if (update.hasCallBackQuery()) {
				return getHandlerByCallBackQuery(update.getMessageData())
						.handleCallbackQuery(user, update);
			}
			throw new UnsupportedOperationException();
		} catch (UnsupportedOperationException e) {
			return Collections.emptyList();
		}
	}

	private Handler getHandlerByState(State state) {
		return handlers.stream()
				.filter(handler -> handler.handledState() != null)
				.filter(handler -> handler.handledState().equals(state))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}

	private Handler getHandlerByCallBackQuery(String query) {
		return handlers.stream()
				.filter(h -> h.handledCallBackQuery().stream()
						.anyMatch(query::startsWith))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}

	public static List<Message> handleHelp(WrappedUpdate update) {
		long chatId = update.getChatId();
		WrappedSendMessage helpMessage = new WrappedSendMessage(chatId, helpers);
		helpMessage.setEnableMarkdown();
		return List.of(helpMessage);
	}
}
