import handlers.SearchByListHandler;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchByListHandlerTest {
    private WrappedUpdate update;
    private User user;
    private final SearchByListHandler handler =  new SearchByListHandler();

    @Before
    public void setUp(){
        update = mock(WrappedUpdate.class);
        user = new User(0, "username");
    }
    @Test
    public void test1() {
        when(update.getMessageData()).thenReturn("Отечественные");
        handler.handleMessage(user, update);
        when(update.getMessageData()).thenReturn(">");
        Integer expected = 2;

        handler.handleCallbackQuery(user, update);

        Assert.assertEquals(expected,handler.getCurrentPageNumber((long) 0));
    }

    @Test
    public void test2() {
        when(update.getMessageData()).thenReturn("Отечественные");
        handler.handleMessage(user, update);
        when(update.getMessageData()).thenReturn("<");
        Integer expected = 1;

        handler.handleCallbackQuery(user, update);

        Assert.assertEquals(expected,handler.getCurrentPageNumber((long) 0));
    }

    @Test
    public void test3() {
        when(update.getMessageData()).thenReturn("Отечественные");
        handler.handleMessage(user, update);
        when(update.getMessageData()).thenReturn(">");
        for(int i=0;i<2;i++)
            handler.handleCallbackQuery(user, update);
        when(update.getMessageData()).thenReturn("<");
        Integer expected = 2;

        handler.handleCallbackQuery(user, update);

        Assert.assertEquals(expected, handler.getCurrentPageNumber((long) 0));
    }
}
