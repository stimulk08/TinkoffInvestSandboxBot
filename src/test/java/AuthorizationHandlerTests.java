import handlers.AuthorizationHandler;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationHandlerTests {

	private User user;
	private WrappedUpdate update;
	private AuthorizationHandler handler;

	@Before
	public void setUp() {
		user = new User(11, "username");
		update = mock(WrappedUpdate.class);
		handler = new AuthorizationHandler();
	}

	@Test
	public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
		when(update.getMessageData()).thenReturn("token");
		long queryTimeBefore = user.getLastQueryTime();
		handler.handleCallbackQuery(user, update);
		Assert.assertNotSame(queryTimeBefore, user.getLastQueryTime());
	}
}
