package models.quiz;

import java.util.List;

public class Question {
	private final String question;
	private final List<String> variants;
	private final String rightAnswer;

	public Question(String question, List<String> variants, String rightAnswer) {
		this.question = question;
		this.variants = variants;
		this.rightAnswer = rightAnswer;
	}

	public String getQuestion() { return question; }

	public List<String> getVariants() { return variants; }

	public boolean checkChosenVariant(String chosenVariant) {
		return rightAnswer.equals(chosenVariant);
	}
}
