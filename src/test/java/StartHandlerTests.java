import static org.mockito.Mockito.mock;

import handlers.StartHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

public class StartHandlerTests {

	private User user;
	private WrappedUpdate update;
	private StartHandler handler;

	@Before
	public void setUp() {
		user = new User(11);
		update = mock(WrappedUpdate.class);
		handler = new StartHandler();
	}

	@Test
	public void handleMessage_ShouldChangeUserState() {
		handler.handleMessage(user, update);
		Assert.assertSame(State.NON_AUTHORIZED, user.getState());
	}

	@Test
	public void handleMessage_ShouldChangeUserLastQueryTime() {
		long queryTimeBefore = user.getLastQueryTime();
		handler.handleMessage(user, update);
		Assert.assertNotSame(queryTimeBefore, user.getLastQueryTime());
	}
}
