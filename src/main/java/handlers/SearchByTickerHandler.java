package handlers;

import models.Handler;

import models.State;
import models.User;
import models.keyboards.InlineButtonInfo;
import models.keyboards.InlineKeyboard;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.models.market.*;
import wrappers.Message;
import wrappers.WrappedSendMessage;
import wrappers.WrappedUpdate;

import java.util.*;

public class SearchByTickerHandler implements Handler {
    public static final String TO_MENU = "В главное меню";
    public final List<String> InstrumentFigies = Collections.synchronizedList(new ArrayList<>());
    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();

    static {
        replyButtonsToCommands.put("Вернуться в меню", TO_MENU);
    }

    @Override
    public List<Message> handleMessage(User user, WrappedUpdate wrapper) {

        String text = wrapper.getMessageData();
        List<Message> messages;

        if (text.equalsIgnoreCase(TO_MENU))
            messages = handleToMenu(user);
        else
            messages = handleSearchAsset(user, text);

        user.setLastQueryTime();

        return messages;
    }

    private List<Message> handleToMenu(User user) {
        user.setState(State.MAIN_MENU);
        return List.of(new WrappedSendMessage(user.getChatId(), "Хорошо", Keyboard.getMenuKeyboard()));
    }

    private List<Message> handleSearchAsset(User user, String ticker) {
        List<Instrument> instruments = user.getApi().getInstrumentsByTicker(ticker);
        if(instruments.size() == 0){
            return List.of(new WrappedSendMessage(
                    user.getChatId(),
                    "Инструмент не был найден\nПопробуйте ввести ещё раз"));
        }

        List<List<InlineButtonInfo>> keyboardButtons = new ArrayList<>();

        for (Instrument instr : instruments) {
            keyboardButtons.add(List.of(new InlineButtonInfo(instr.name, instr.figi)));
            InstrumentFigies.add(instr.figi);
        }

        InlineKeyboard inlineKeyboard = new InlineKeyboard(keyboardButtons);
        return List.of(new WrappedSendMessage(user.getChatId(), "Выберите инструмент:", inlineKeyboard));
    }

    @Override
    public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
        user.setState(State.CHOOSE_SEARCH_MODE);
        String figi = callbackQuery.getMessageData();
        String lastPrice = user.getApi().getLastPriceAsset(figi).toString();
        String currency = user.getApi().getCurrencyByFigi(figi);
        String text = "Последняя цена инструмента: " + lastPrice + currency;
        return List.of(new WrappedSendMessage(user.getChatId(), text));
    }

    @Override
    public State handledState() {
        return State.SEARCH_BY_TICKER;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return InstrumentFigies;
    }
}