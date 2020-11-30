package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.exceptions.WrongTokenException;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class AuthorizationHandler implements Handler {

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		String text = message.getMessageData();
		final Logger logger;
		try {
			logger = initLogger();
		} catch (IOException ex) {
			System.err.println("При инициализации логгера произошла ошибка: "
					+ ex.getLocalizedMessage());
			return Collections.emptyList();
		}

		OkHttpOpenApiFactory factory = new OkHttpOpenApiFactory(text, logger);
		SandboxOpenApi api = factory.createSandboxOpenApiClient(
				Executors.newSingleThreadExecutor());

		boolean isValidToken = checkTokenValidity(api);
		List<Message> messages = addAuthorisationResultMessages(
				user.getChatId(), isValidToken, text);

		if (isValidToken) {
			user.setApi(api);
			user.setState(State.CHOOSE_PORTFOLIO);
		}

		user.setLastQueryTime();
		return messages;
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		return Collections.emptyList();
	}

	private List<Message> addAuthorisationResultMessages(long chatId, boolean isAuth,
			String token) {
		List<wrappers.Message> messages = new ArrayList<>();

		if (isAuth) {
			messages.add(new WrappedSendMessage(
					chatId, "Успешная авторизация", Keyboard.getAuthKeyboard()));
		} else {
			messages.add(new WrappedSendMessage(
					chatId, String.format("Невалидный токен: %s\n", token)));
			messages.add(new WrappedSendMessage(chatId, "Введите свой токен"));
		}
		return messages;
	}


	private boolean checkTokenValidity(SandboxOpenApi api) {
		boolean isValidToken = false;
		try {
			api.getSandboxContext().performRegistration(null).get();
			isValidToken = true;
		} catch (ExecutionException e) {
            if (e.getCause() instanceof WrongTokenException) {
                try {
                    api.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return isValidToken;
	}

	private static Logger initLogger() throws IOException {
		final LogManager logManager = LogManager.getLogManager();
		final ClassLoader classLoader = AuthorizationHandler.class.getClassLoader();

		try (final InputStream input = classLoader.getResourceAsStream("logging.properties")) {

			if (input == null) {
				throw new FileNotFoundException();
			}

			Files.createDirectories(Paths.get("./logs"));
			logManager.readConfiguration(input);
		}

		return Logger.getLogger(AuthorizationHandler.class.getName());
	}

	@Override
	public State handledState() {
		return State.NON_AUTHORIZED;
	}

	@Override
	public List<String> handledCallBackQuery() {
		return Collections.emptyList();
	}
}
