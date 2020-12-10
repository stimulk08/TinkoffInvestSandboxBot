package handlers;

import java.util.Collections;
import java.util.List;
import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class ChooseTestHandler implements Handler {

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		return Collections.emptyList();
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		String command = callbackQuery.getMessageData();
		return handleTakeQuiz(user, command);
	}

	private List<Message> handleTakeQuiz(User user, String numberTest) {
		user.setState(State.TAKING_QUIZ);
		// Quiz quiz = GoogleDocks.getQuizByNumber(numberTest);
		// Question firstQuestion = quiz.getQuestionByNumber(1);
		WrappedSendMessage message = new WrappedSendMessage(
				user.getChatId(),
				firstQuestion.getQuestion(),
				Keyboard.getVariantsQuestionKeyboard(firstQuestion.getVariants()));
		return List.of(message);
	}

	@Override
	public State handledState() { return State.CHOOSE_TEST; }

	@Override
	public List<String> handledCallBackQuery() {
		return List.of("1", "2", "3", "4", "5");
	}
}
