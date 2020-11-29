package models;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import wrappers.WrappedUpdate;

public interface Handler {
    List<BotApiMethod> handleMessage(User user, WrappedUpdate message);

    List<BotApiMethod> handleCallbackQuery(User user, WrappedUpdate callbackQuery);

    State handledState();

    List<String> handledCallBackQuery();
}
