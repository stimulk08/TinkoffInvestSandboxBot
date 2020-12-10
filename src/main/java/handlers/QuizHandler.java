package handlers;

import java.util.List;
import models.Handler;
import models.State;
import models.User;
import wrappers.Message;
import wrappers.WrappedUpdate;

public class QuizHandler implements Handler {

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		return null;
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		return null;
	}

	@Override
	public State handledState() {
		return null;
	}

	@Override
	public List<String> handledCallBackQuery() {
		return null;
	}
}
