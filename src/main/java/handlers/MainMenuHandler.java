package handlers;

import models.*;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

public class MainMenuHandler implements Handler {

    private static final String SHOW_PORTFOLIO = "/show";
    private static final String FIND_ASSET = "/find";
    private static final String RESET_PORTFOLIO = "/reset";
    private static final String TAKE_QUIZ = "/start_quiz";
    private static final String VIEW_RATING = "/view_rating";

    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();

    static {
        replyButtonsToCommands.put("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC", SHOW_PORTFOLIO);
        replyButtonsToCommands.put("❌Сбросить портфель❌", RESET_PORTFOLIO);
        replyButtonsToCommands.put("\uD83D\uDD0EНайти актив\uD83D\uDD0D", FIND_ASSET);
        replyButtonsToCommands.put("✏Пройти тест✏", TAKE_QUIZ);
        replyButtonsToCommands.put("\uD83D\uDCCAПосмотреть рейтинг пользователей\uD83D\uDCCA", VIEW_RATING);
    }

    @Override
    public List<Message> handleMessage(User user, WrappedUpdate message) {
        String text = message.getMessageData();
        List<Message> messages = new ArrayList<>();

        if (replyButtonsToCommands.containsKey(text)) {
            String command = replyButtonsToCommands.get(text);
            if (command.equalsIgnoreCase(SHOW_PORTFOLIO)) {
                messages = handleShow(user);
            } else if (command.equalsIgnoreCase(FIND_ASSET)) {
                messages = handleFindAssets(user);
            } else if (command.equalsIgnoreCase(RESET_PORTFOLIO)) {
                throw new UnsupportedOperationException();
            } else if (command.equalsIgnoreCase(TAKE_QUIZ)) {
                messages = handleChooseTests(user);
            } else if (command.equalsIgnoreCase(VIEW_RATING)) {
                messages = handleViewRating(user);
            }
            user.setLastQueryTime();
        }

        return messages;
    }

    @Override
    public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
        return Collections.emptyList();
    }

    private List<Message> handleViewRating(User user) {
        Rating rating = GoogleDocks.getRating();
        String messageText;
        if (!rating.isEmpty()) {
            messageText = String.format(
                    "Вы можете связаться с кем-то из этих пользователей, чтобы попросить совет\n%s",
                    rating.getFormatRating());
        } else {
            messageText = "Рейтинг пуст";
        }
        return List.of(new WrappedSendMessage(
                user.getChatId(), messageText));
    }

    private List<Message> handleShow(User user) {
        List<Message> messages = new ArrayList<>();

        Portfolio portfolio = user.getApi().getPortfolio();

        StringBuilder positions = new StringBuilder("*Position  Balance*\n\n");
        for (Portfolio.PortfolioPosition position : portfolio.positions) {
            positions
                    .append(String.format("%-20s", position.name))
                    .append(position.balance.intValue())
                    .append("\n");
        }
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(), positions.toString());
        message.setEnableMarkdown();
        messages.add(message);

        return messages;
    }

    private List<Message> handleFindAssets(User user) {
        user.setState(State.CHOOSE_SEARCH_MODE);
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(),
                "Выберите метод поиска актива",
                Keyboard.getChooseSearchModeKeyboard());
        return List.of(message);
    }

    private List<Message> handleChooseTests(User user) {
        user.setState(State.TAKING_QUIZ);
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(),
                "Выберите номера теста",
                Keyboard.getChooseTestKeyboard(user.getCompletedTests()));
        return List.of(message);
    }

    @Override
    public State handledState() {
        return State.MAIN_MENU;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}