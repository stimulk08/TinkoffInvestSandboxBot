import java.util.ArrayList;
import models.quiz.Question;
import models.quiz.Quiz;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuizTests {
	private Quiz quiz;

	private Question getQuestion() {
		ArrayList<String> answers = new ArrayList<>();
		answers.add("1");
		answers.add("2");
		answers.add("3");
		return new Question("ABC", answers, "2");
	}

	@Before
	public void setUp() {
		ArrayList<Question> questions = new ArrayList<>();
		questions.add(getQuestion());
		quiz = new Quiz(1, questions);
	}

	@Test
	public void getCurrentQuestion_shouldReturnRightValues() {
		quiz.goToNextQuestion();
		Question current_question = quiz.getCurrentQuestion();
		Assert.assertEquals(getQuestion().getQuestion(), current_question.getQuestion());
		quiz.goToNextQuestion();
		Assert.assertNull(quiz.getCurrentQuestion());
	}
}
