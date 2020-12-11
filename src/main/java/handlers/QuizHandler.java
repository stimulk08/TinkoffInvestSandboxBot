package handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import models.GoogleDocks;
import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import models.quiz.Question;
import models.quiz.Quiz;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class QuizHandler implements Handler {

	public static final Integer MAX_NUMBER_TEST = 5;
	private final ConcurrentHashMap<Long, Quiz> chatIdToQuiz;
	private static final HashMap<String, String> replyButtonsToCommands
			= new HashMap<>();

	public static final String FINISH_QUIZ = "/finish";
	public static final String RETAKING_WRONG_ANSWERS = "/retake";
	public static final String READY_RETAKE_QUIZ = "/ready_retake";

	public QuizHandler() {
		this.chatIdToQuiz = new ConcurrentHashMap<>();
	}

	static {
		replyButtonsToCommands.put("✅Завершить тест✅", FINISH_QUIZ);
		replyButtonsToCommands.put("✏Прорешать неверно отвеченные вопросы✏",
				RETAKING_WRONG_ANSWERS);
		replyButtonsToCommands.put("Готов", READY_RETAKE_QUIZ);
	}

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		String text = message.getMessageData();
		List<Message> messages = new ArrayList<>();

		if (replyButtonsToCommands.containsKey(text)) {
			String command = replyButtonsToCommands.get(text);
			if (command.equalsIgnoreCase(FINISH_QUIZ)) {
				messages = handleEndTest(user);
			} else if (command.equalsIgnoreCase(RETAKING_WRONG_ANSWERS)) {
				messages = handleRetakeWrongAnswers(user);
			} else if (command.equalsIgnoreCase(READY_RETAKE_QUIZ)) {
				messages = handleGetNextQuestion(user);
			}
		} else {
			Long chatId = user.getChatId();
			int numberTest;
			try {
				numberTest = Integer.parseInt(text);
			} catch (NumberFormatException exception) {
				numberTest = -1;
			}
			if (numberTest > 0 && numberTest <= MAX_NUMBER_TEST) {
				Quiz quiz = GoogleDocks.getQuizByNumber(numberTest);
				chatIdToQuiz.put(chatId, quiz);
				messages = handleGetNextQuestion(user);
			}
		}
		return messages;
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		String chosenVariant = callbackQuery.getMessageData();

		Long chatId = user.getChatId();
		Quiz quiz = chatIdToQuiz.get(chatId);
		Question currentQuestion = quiz.getCurrentQuestion();
		boolean isRightAnswer = currentQuestion.checkChosenVariant(chosenVariant);
		quiz.addUserAnswer(isRightAnswer);

		return handleGetNextQuestion(user);
	}

	private String getUserResult(Long chatId) {
		List<Boolean> result = chatIdToQuiz.get(chatId).getUserAnswers();
		int countRightAnswers = 0;
		for (Boolean isRightAnswer : result) {
			if (isRightAnswer) {
				countRightAnswers++;
			}
		}
		return countRightAnswers + "/" + result.size();
	}

	private List<Message> handleGetNextQuestion(User user) {
		Long chatId = user.getChatId();
		Quiz quiz = chatIdToQuiz.get(chatId);
		quiz.goToNextQuestion();
		Question question = quiz.getCurrentQuestion();
		WrappedSendMessage message;
		if (question == null) {
			if (!quiz.hasQuizResultUploaded()) {
				quiz.uploadQuizResult(chatId);
			}
			message = new WrappedSendMessage(
					chatId,
					"Тест пройден!\nПравильных ответов: " + getUserResult(chatId),
					Keyboard.getEndQuizKeyboard(!quiz.isAllAnswerRight()));
		} else {
			message = new WrappedSendMessage(
					chatId, question.getQuestion(),
					Keyboard.getVariantsQuestionKeyboard(question.getVariants()));
		}
		return List.of(message);
	}

	private List<Message> handleEndTest(User user) {
		user.setState(State.MAIN_MENU);
		Quiz quiz = chatIdToQuiz.get(user.getChatId());
		user.setCompletedTest(quiz.getTestNumber());
		WrappedSendMessage message = new WrappedSendMessage(
				user.getChatId(), "Вы молодцы!",
				Keyboard.getMenuKeyboard());
		return List.of(message);
	}

	private List<Message> handleRetakeWrongAnswers(User user) {
		long chatId = user.getChatId();
		Quiz quiz = chatIdToQuiz.get(chatId);
		quiz.retakeWrongAnswers();
		WrappedSendMessage message = new WrappedSendMessage(chatId,
				"Как будете готовы, нажмите \"Готов\"",
				Keyboard.getReadyRetakeQuizKeyboard());
		return List.of(message);
	}

	@Override
	public State handledState() {
		return State.TAKING_QUIZ;
	}

	@Override
	public List<String> handledCallBackQuery() {
		List<String> commands = new ArrayList<>();
		for (int i = 0; i < MAX_NUMBER_TEST; i++) {
			commands.add(Integer.toString(i + 1));
		}
		return commands;
	}
}
