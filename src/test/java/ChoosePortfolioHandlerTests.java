import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import handlers.ChoosePortfolioHandler;
import java.math.BigDecimal;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

public class ChoosePortfolioHandlerTests {

	private User user;
	private WrappedUpdate update;
	private ChoosePortfolioHandler handler;

	@Before
	public void setUp() {
		user = new User(11, "username");
		update = mock(WrappedUpdate.class);
		handler = new ChoosePortfolioHandler();
	}

	@Test
	public void handleAddCurrency_ShouldIncreaseUSDAmount() {
		when(update.getMessageData()).thenReturn(ChoosePortfolioHandler.USD);
		BigDecimal amountBefore = user.getUSDAmount();
		handler.handleCallbackQuery(user, update);
		Assert.assertNotSame(amountBefore, user.getUSDAmount());
	}

	@Test
	public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
		when(update.getMessageData()).thenReturn(
				ChoosePortfolioHandler.CONTINUE_WITH_OLD_PORTFOLIO);
		long queryTimeBefore = user.getLastQueryTime();
		handler.handleCallbackQuery(user, update);
		Assert.assertNotSame(queryTimeBefore, user.getLastQueryTime());
	}
}
