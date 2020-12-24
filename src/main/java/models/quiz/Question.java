package models.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {
	private final String question;
	private final ArrayList<String> variants;
	private final String rightAnswer;

	public Question(String question, ArrayList<String> variants, String rightAnswer) {
		this.question = question;
		this.variants = variants;
		this.rightAnswer = rightAnswer;
	}

	public String getQuestion() { return question; }

	public List<String> getVariants() { return variants; }

	public boolean checkChosenVariant(String chosenVariant) {
		return rightAnswer.equals(chosenVariant);
	}

	public void deleteOneWrongVariant() {
		ArrayList<String> wrongVariants = new ArrayList<>();
		for (String variant : variants) {
			if (!checkChosenVariant(variant)) {
				wrongVariants.add(variant);
			}
		}
		Random randomizer = new Random();
		int indexDeletedVariant = randomizer.nextInt(wrongVariants.size()-1);
		variants.remove(indexDeletedVariant);
	}
}
