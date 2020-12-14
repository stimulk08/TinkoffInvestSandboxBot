import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import handlers.QuizHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

public class QuizHandlerTests {
	private User user;
	private WrappedUpdate update;
	private QuizHandler handler;

	@Before
	public void setUp() {
		user = new User(11);
		update = mock(WrappedUpdate.class);
		handler = new QuizHandler();
		when(update.getMessageData()).thenReturn("1");
		handler.handleMessage(user, update);
	}

	@Test
	public void handleEndTest_shouldChangeStateUser() {
		when(update.getMessageData()).thenReturn("✅Завершить тест✅");
		handler.handleMessage(user, update);
		Assert.assertNotSame(State.TAKING_QUIZ, user.getState());
		Assert.assertTrue(user.getCompletedTests().get(0));
	}
}
