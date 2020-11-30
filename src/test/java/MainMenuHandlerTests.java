import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import handlers.MainMenuHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class MainMenuHandlerTests {

	private User user;
	private WrappedUpdate update;
	private MainMenuHandler handler;

	@Before
	public void setUp() {
		user = new User(11);
		update = mock(WrappedUpdate.class);
		handler = new MainMenuHandler();
	}

	@Test
	public void handleFindAsset_ShouldResponseOnValidRequest() {
		when(update.getMessageData()).thenReturn("\uD83D\uDD0EНайти актив\uD83D\uDD0D");
		WrappedSendMessage wrappedSendMessage = (WrappedSendMessage) handler.handleMessage(
				user, update).get(0);
		Assert.assertTrue(wrappedSendMessage.getMessage()
				.contains("Введите тикер инструмента:"));
	}

	@Test
	public void handleFindAsset_ShouldChangeUserState() {
		when(update.getMessageData()).thenReturn("\uD83D\uDD0EНайти актив\uD83D\uDD0D");
		State stateBefore = user.getState();
		handler.handleMessage(user, update);
		Assert.assertNotSame(stateBefore, user.getState());
	}
}
