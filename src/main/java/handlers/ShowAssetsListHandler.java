package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.InlineKeyboard;
import models.keyboards.Keyboard;
import models.Asset.AssetInfo;
import models.Asset.AssetPagesType;
import models.Asset.AssetsCollector;

import wrappers.Message;
import wrappers.WrappedEditMessage;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShowAssetsListHandler implements Handler {
    private final AssetsCollector collector = new AssetsCollector();
    private final List<String> allFigies = collector.getAllFigies();
    private AssetPagesType pagesType = AssetPagesType.NONE;
    public static final String RU = "/ru";
    public static final String FOREIGN = "/foreign";
    public static final String TO_MENU = "/toMenu";
    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();
    private final ConcurrentHashMap<Long, Integer> chatIdToCurrentPage =
            new ConcurrentHashMap<>();

    static {
        replyButtonsToCommands.put("Отечественные", RU);
        replyButtonsToCommands.put("Зарубежные", FOREIGN);
        replyButtonsToCommands.put("Вернуться в меню", TO_MENU);
    }

    @Override
    public List<Message> handleMessage(User user, WrappedUpdate message) {
        String text = message.getMessageData();
        List<Message> messages = new ArrayList<>();

        if (replyButtonsToCommands.containsKey(text)) {
            String command = replyButtonsToCommands.get(text);
            setCurrentPageNumber(user.getChatId(), 1);
            if (command.equalsIgnoreCase(RU)) {
                pagesType = AssetPagesType.RU;
                messages = handleShow(user, pagesType);
            } else if (command.equalsIgnoreCase(FOREIGN)) {
                pagesType = AssetPagesType.FOREIGN;
                messages = handleShow(user, pagesType);
            } else if (command.equalsIgnoreCase(TO_MENU))
                messages = handleToMenu(user);

            user.setLastQueryTime();
        }

        return messages;
    }

    private Integer getCurrentPageNumber(Long chatId) {
        return chatIdToCurrentPage.get(chatId);
    }

    private void setCurrentPageNumber(Long chatId, Integer pageNumber) {
        chatIdToCurrentPage.put(chatId, pageNumber);
    }

    private List<Message> handleShow(User user, AssetPagesType type) {
        Integer currentPageNumber = getCurrentPageNumber(user.getChatId());
        List<AssetInfo> infoList = collector.getAssetsPageByNumber(currentPageNumber, type);
        InlineKeyboard keyboard = Keyboard.getKeyboardByAssetInfo(infoList, currentPageNumber);
        WrappedSendMessage message = new WrappedSendMessage(
                user.getChatId(),
                "Выберите компанию",
                keyboard);
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
        String command = callbackQuery.getMessageData();
        List<Message> messages = new ArrayList<>();


        //TODO: replace with HashMap
        if (command.equals(">")) {
            messages = handleNext(user, callbackQuery);
        } else if (command.equals("<")) {
            messages = handlePrevious(user, callbackQuery);
        } else if (allFigies.contains(command)) {
            messages = handleFigi(user, callbackQuery);
        }

        user.setLastQueryTime();
        return messages;
    }

    private List<Message> handleFigi(User user, WrappedUpdate callbackQuery) {
        String figi = callbackQuery.getMessageData();
        String lastPrice = user.getApi().getLastPriceAsset(figi).toString();
        String currency = user.getApi().getCurrencyByFigi(figi);
        String text = String.format("Последняя цена инструмента: %s %s", lastPrice, currency);
        return List.of(new WrappedSendMessage(user.getChatId(), text));
    }

    private Message getMessagePage(User user, WrappedUpdate callbackQuery) {
        Integer currentPageNumber = getCurrentPageNumber(user.getChatId());
        List<AssetInfo> info = collector.getAssetsPageByNumber(currentPageNumber, pagesType);
        InlineKeyboard keyboard = Keyboard.getKeyboardByAssetInfo(info, currentPageNumber);

        return new WrappedEditMessage(user.getChatId(),
                callbackQuery.getMessageText(), callbackQuery.getMessageId(), keyboard);
    }

    private List<Message> handleNext(User user, WrappedUpdate callbackQuery) {
        Integer currentPageNumber = getCurrentPageNumber(user.getChatId());
        List<Message> messages = new ArrayList<>();

        if (!collector.IsLastPage(currentPageNumber, pagesType)) {
            setCurrentPageNumber(user.getChatId(), currentPageNumber + 1);

            messages.add(getMessagePage(user, callbackQuery));
        }

        return messages;
    }

    private List<Message> handlePrevious(User user, WrappedUpdate callbackQuery) {
        Integer currentPageNumber = getCurrentPageNumber(user.getChatId());
        List<Message> messages = new ArrayList<>();

        if (currentPageNumber != 1) {
            setCurrentPageNumber(user.getChatId(), currentPageNumber - 1);
            messages.add(getMessagePage(user, callbackQuery));
        }

        return messages;
    }

    @Override
    public State handledState() {
        return State.SHOW_ASSET_LIST;
    }

    @Override
    public List<String> handledCallBackQuery() {
        List<String> commands = new ArrayList<>();
        commands.add("<");
        commands.add(">");
        commands.addAll(allFigies);
        return commands;
    }
}
