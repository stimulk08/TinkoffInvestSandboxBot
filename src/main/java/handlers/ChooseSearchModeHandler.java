package handlers;

import kotlin.jvm.internal.Lambda;
import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ChooseSearchModeHandler implements Handler {
    public static final String SEARCH_BY_TICKER = "/searchByTicker";
    public static final String SEARCH_BY_LIST = "/searchByList";
    public static final String TO_MENU = "/toMenu";
    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();

    static {
        replyButtonsToCommands.put("Поиск актива по тикеру", SEARCH_BY_TICKER);
        replyButtonsToCommands.put("Поиск по списку компаний", SEARCH_BY_LIST);
        replyButtonsToCommands.put("Вернуться в меню", TO_MENU);
    }


    @Override
    public List<Message> handleMessage(User user, WrappedUpdate message) {
        String text = message.getMessageData();
        List<Message> messages = new ArrayList<>();

        if (replyButtonsToCommands.containsKey(text)) {
            String command = replyButtonsToCommands.get(text);
            if (command.equals(SEARCH_BY_TICKER)) {
                messages = handleSearchByTicket(user);
            } else if (command.equals(SEARCH_BY_LIST)) {
                messages = handleSearchByList(user);
            } else if (command.equals(TO_MENU)) {
                messages = handleToMenu(user);
            }

            user.setLastQueryTime();
        }

        return messages;
    }

    public List<Message> handleSearchByTicket(User user) {
        user.setState(State.SEARCH_ASSET);
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(),
                "Введите тикер");
        return List.of(message);
    }

    public List<Message> handleSearchByList(User user) {
        user.setState(State.SHOW_ASSET_LIST);
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(),
                "Выберите тип ценных бумаг",
                Keyboard.getChooseCountryAssets());
        return List.of(message);
    }

    private List<Message> handleToMenu(User user) {
        user.setState(State.MAIN_MENU);
        Message message = new WrappedSendMessage(
                user.getChatId(),
                "Хорошо",
                Keyboard.getMenuKeyboard());
        return List.of(message);
    }

    @Override
    public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
        return Collections.emptyList();
    }

    @Override
    public State handledState() {
        return State.CHOOSE_SEARCH_MODE;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}
