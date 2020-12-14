import java.util.ArrayList;
import models.quiz.Question;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuestionTests {
	private Question question;

	@Before
	public void setUp() {
		ArrayList<String> answers = new ArrayList<>();
		answers.add("1");
		answers.add("2");
		answers.add("3");
		question = new Question("ABC", answers, "2");
	}

	@Test
	public void checkChosenVariant_shouldWork() {
		Assert.assertTrue(question.checkChosenVariant("2"));
		Assert.assertFalse(question.checkChosenVariant("1"));
	}
}
