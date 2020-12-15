import handlers.AuthorizationHandler;
import handlers.SearchByListHandler;
import handlers.SearchByTickerHandler;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.tinkoff.invest.openapi.models.market.Instrument;
import ru.tinkoff.invest.openapi.models.market.InstrumentsList;
import wrappers.Message;
import wrappers.WrappedApi;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchByTickerHandlerTest {
    private User user;
    private WrappedUpdate update;
    private SearchByTickerHandler handler;
    private WrappedApi api;

    @Before
    public void setUp() {
        user = new User(0, "username");
        update = mock(WrappedUpdate.class);
        api = mock(WrappedApi.class);
        when(api.getInstrumentsByTicker(any())).thenReturn(Collections.emptyList());
        when(update.getMessageData()).thenReturn("ticker");
        user.setApi(api);
        handler = new SearchByTickerHandler();
    }

    @Test
    public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
        long queryTimeBefore = user.getLastQueryTime();
        handler.handleMessage(user, update);
        Assert.assertNotSame(queryTimeBefore, user.getLastQueryTime());
    }

    @Test
    public void test2() {
        WrappedSendMessage message = (WrappedSendMessage) handler.handleMessage(
                user, update).get(0);
        Assert.assertTrue(message.getMessage()
                .contains("Инструмент не был найден\nПопробуйте ввести ещё раз"));
    }
}
