import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import models.Handler;
import models.State;
import models.UpdateReceiver;
import org.junit.Assert;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.mockito.Mockito.*;


import java.io.*;
import java.util.Collections;
import java.util.List;

import wrappers.Message;
import wrappers.WrappedUpdate;

public class UpdateReceiverTest {
    private final WrappedUpdate updateWithCallbackQuery = getUpdateByPathJson("UpdateWithCallbackQuery");
    private final WrappedUpdate updateWithoutCallbackQuery = getUpdateByPathJson("UpdateWithoutCallbackQuery");
    private final WrappedUpdate updateWithUnsupportedCommand = getUpdateByPathJson("UpdateWithUnsupportedCommand");

    private WrappedUpdate getUpdateByPathJson(String path) {
        File input = new File("src\\test\\resources\\"+ path + ".JSON");
        JsonElement fileElement = null;
        try {
            fileElement = com.google.gson.JsonParser.parseReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String jsonString = fileElement.getAsJsonObject().toString();
        StringReader reader = new StringReader(jsonString);
        ObjectMapper mapper = new ObjectMapper();
        Update update = null;
        try {
            update = mapper.readValue(reader, Update.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WrappedUpdate(update);
    }

    @Test
    public void getHandlerByState_CallHandler() {
        Handler mock = mock(Handler.class);
        when(mock.handledState()).thenReturn(State.NONE);
        UpdateReceiver updateReceiver = new UpdateReceiver(List.of(mock));

        updateReceiver.handle(updateWithoutCallbackQuery);

        verify(mock).handleMessage(any(), any());
    }

    @Test
    public void getHandlerByCallBackQuery_CallHandler() {
        Handler mock = mock(Handler.class);
        when(mock.handledCallBackQuery()).thenReturn(List.of("/Command"));
        UpdateReceiver updateReceiver = new UpdateReceiver(List.of(mock));

        updateReceiver.handle(updateWithCallbackQuery);

        verify(mock).handleCallbackQuery(any(), any());
    }

    @Test
    public void getHandlerByCallBackQuery_ReturnEmptyCollection() {
        Handler mock = mock(Handler.class);
        when(mock.handledCallBackQuery()).thenReturn(List.of("/Command"));
        UpdateReceiver updateReceiver = new UpdateReceiver(List.of(mock));

        List<Message> result = updateReceiver.handle(updateWithUnsupportedCommand);

        Assert.assertEquals(Collections.emptyList(), result);
    }
}
