package models.quiz;

import models.GoogleDocks;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

	private final Integer testNumber;
	private final List<Question> questions;
	private Integer indexInRow;
	private List<Integer> rowQuestions;
	private List<Boolean> userAnswers;
	private Boolean hasQuizResultUploaded;

	public Quiz(Integer testNumber, List<Question> questions) {
		this.testNumber = testNumber;
		this.questions = questions;
		this.indexInRow = -1;
		this.rowQuestions = new ArrayList<>();
		this.userAnswers = new ArrayList<>();
		for (int i = 0; i < questions.size(); i++) {
			this.rowQuestions.add(i);
		}
		this.hasQuizResultUploaded = false;
	}

	public Integer getTestNumber() {
		return testNumber;
	}

	public List<Boolean> getUserAnswers() {
		return userAnswers;
	}

	public Boolean hasQuizResultUploaded() {
		return hasQuizResultUploaded;
	}

	public void addUserAnswer(boolean isRightAnswer) {
		userAnswers.add(isRightAnswer);
	}

	public void goToNextQuestion() {
		indexInRow++;
	}

	public Question getCurrentQuestion() {
		if (indexInRow >= rowQuestions.size()) {
			return null;
		}
		return questions.get(rowQuestions.get(indexInRow));
	}

	public void uploadQuizResult(Long chatId) {
		GoogleDocks.uploadQuizResult(chatId, testNumber, userAnswers);
		hasQuizResultUploaded = true;
	}

	public boolean isAllAnswerRight() {
		for (Boolean userAnswer : userAnswers) {
			if (!userAnswer) {
				return false;
			}
		}
		return true;
	}

	public void retakeWrongAnswers() {
		rowQuestions = new ArrayList<>();
		for (int i = 0; i < userAnswers.size(); i++) {
			if (!userAnswers.get(i)) {
				rowQuestions.add(i);
			}
		}
		indexInRow = -1;
		userAnswers = new ArrayList<>();
	}
}
