package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.SandboxContext;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.sandbox.CurrencyBalance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wrappers.Message;
import wrappers.WrappedEditMessage;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class ChoosePortfolioHandler implements Handler {

	public static final String CREATE_NEW_PORTFOLIO = "/create_new";
	public static final String CONTINUE_WITH_OLD_PORTFOLIO = "/continue";
	public static final String USD = "/USD";
	public static final String ACCEPT = "/accept";
	private static final BigDecimal addUSDStep = new BigDecimal(50);

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		return Collections.emptyList();
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		String command = callbackQuery.getMessageData();
		List<Message> messages = new ArrayList<>();

		//TODO: replace with HashMap
		if (command.equalsIgnoreCase(CONTINUE_WITH_OLD_PORTFOLIO)) {
			messages = handleContinue(user);
		} else if (command.equalsIgnoreCase(CREATE_NEW_PORTFOLIO)) {
			messages = handleCreateNewPortfolio(user);
		} else if (command.equalsIgnoreCase(USD)) {
			messages = handleAddCurrency(user, callbackQuery);
		} else if (command.equalsIgnoreCase(ACCEPT)) {
			messages = handleAccept(user);
		}

		user.setLastQueryTime();
		return messages;
	}

	private List<Message> handleContinue(User user) {
		List<Message> messages = new ArrayList<>();

		messages.add(new WrappedSendMessage(
				user.getChatId(), "Вы работаете со старым портфелем",
				Keyboard.getMenuKeyboard()));
		user.setState(State.MAIN_MENU);
		return messages;
	}

	private List<Message> handleCreateNewPortfolio(User user) {
		List<Message> messages = new ArrayList<>();
		messages.add(new WrappedSendMessage(user.getChatId(),
				"Добавьте валюту или подтвердите создание портфеля",
				Keyboard.getAddCurrencyKeyboard()));
		return messages;
	}

	private List<Message> handleAddCurrency(User user, WrappedUpdate message) {
		user.increaseUSDAmount(addUSDStep);

		String text = String.format("Количество валюты обновлено \nUSD: %s\n\n" +
						"Добавьте валюту или подтвердите создание портфеля",
				user.getStartUSDAmount());
		return List.of(new WrappedEditMessage(
				user.getChatId(), text, message.getMessageId(),
				Keyboard.getAddCurrencyKeyboard()));
	}

	private List<Message> handleAccept(User user) {
		CurrencyBalance currencyBalanceUSD = new CurrencyBalance(
				Currency.USD, user.getStartUSDAmount());

		SandboxContext context = user.getApi().getSandboxContext();
		context.clearAll(null).join();
		context.setCurrencyBalance(currencyBalanceUSD, null).join();

		user.setState(State.MAIN_MENU);
		return List.of(new WrappedSendMessage(user.getChatId(),
				"Новый портфель успешно создан", Keyboard.getMenuKeyboard()));
	}

	@Override
	public State handledState() {
		return State.CHOOSE_PORTFOLIO;
	}

	@Override
	public List<String> handledCallBackQuery() {
		return List.of(CONTINUE_WITH_OLD_PORTFOLIO, CREATE_NEW_PORTFOLIO, USD, ACCEPT);
	}
}
