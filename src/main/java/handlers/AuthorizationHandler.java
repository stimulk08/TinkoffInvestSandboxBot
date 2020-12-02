package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wrappers.Message;
import wrappers.WrappedApi;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class AuthorizationHandler implements Handler {

    @Override
    public List<Message> handleMessage(User user, WrappedUpdate message) {
        String token = message.getMessageData();
        WrappedApi api = new WrappedApi(token);

        boolean isValidToken = api.checkTokenValidity();

        List<Message> messages = addAuthorisationResultMessages(
                user.getChatId(), isValidToken, token);

        if (isValidToken) {
            user.setApi(api);
            user.setState(State.CHOOSE_PORTFOLIO);
        }

        user.setLastQueryTime();
        return messages;
    }

    @Override
    public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
        return Collections.emptyList();
    }

    private List<Message> addAuthorisationResultMessages(long chatId, boolean isAuth,
                                                         String token) {
        List<wrappers.Message> messages = new ArrayList<>();

        if (isAuth) {
            messages.add(new WrappedSendMessage(
                    chatId, "Успешная авторизация", Keyboard.getAuthKeyboard()));
        } else {
            messages.add(new WrappedSendMessage(
                    chatId, String.format("Невалидный токен: %s\n", token)));
            messages.add(new WrappedSendMessage(chatId, "Введите свой токен"));
        }
        return messages;
    }


    @Override
    public State handledState() {
        return State.NON_AUTHORIZED;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}
