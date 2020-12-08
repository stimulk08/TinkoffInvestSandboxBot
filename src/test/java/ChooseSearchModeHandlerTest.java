import handlers.ChooseSearchModeHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import wrappers.WrappedUpdate;
public class ChooseSearchModeHandlerTest {
    private WrappedUpdate update;
    private User user;
    private final ChooseSearchModeHandler handler =  new ChooseSearchModeHandler();

    @Before
    public void setUp(){
        update = mock(WrappedUpdate.class);
        user = new User(0);
    }

    @Test
    public void findByTicker_ShouldChangeUserState(){
        when(update.getMessageData()).thenReturn("Поиск актива по тикеру");
        State expected = State.SEARCH_BY_TICKER;

        handler.handleMessage(user, update);

        Assert.assertEquals(expected, user.getState());
    }

    @Test
    public void findByList_ShouldChangeUserState(){
        when(update.getMessageData()).thenReturn("Поиск по списку компаний");
        State expected = State.SEARCH_BY_LIST;

        handler.handleMessage(user, update);

        Assert.assertEquals(expected, user.getState());
    }
}
