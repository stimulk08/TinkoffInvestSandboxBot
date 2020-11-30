import handlers.*;
import models.Handler;
import models.UpdateReceiver;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import wrappers.Message;
import wrappers.WrappedUpdate;

public class Bot extends TelegramLongPollingBot {

	private final String token;
	private final UpdateReceiver updateReceiver;

	public Bot(String token) {
		this.token = token;
		List<Handler> handlers = List.of(
				new StartHandler(),
				new AuthorizationHandler(),
				new MainMenuHandler(),
				new SearchAssetHandler(),
				new ChoosePortfolioHandler());
		updateReceiver = new UpdateReceiver(handlers);
	}

	private synchronized void sendMessages(List<Message> messages) {
		for (Message message : messages) {
			try {
				execute(message.createMessage());
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		WrappedUpdate wrappedUpdate = new WrappedUpdate(update);
		if (!wrappedUpdate.hasCallBackQuery() &&
				wrappedUpdate.getMessageData().equals("/help")) {
			sendMessages(UpdateReceiver.handleHelp(wrappedUpdate));
			return;
		}

		List<Message> responseMessages = updateReceiver.handle(wrappedUpdate);
		sendMessages(responseMessages);
	}

	@Override
	public String getBotUsername() {
		return "InvestBot";
	}

	@Override
	public String getBotToken() {
		return token;
	}
}
