package models.keyboards;

import handlers.ChoosePortfolioHandler;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

public class Keyboard {

	public static SimpleKeyboard getMenuKeyboard() {
		List<String> firstRow = List.of("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC",
				"❌Сбросить портфель❌");
		List<String> secondRow = List.of("\uD83D\uDD0EНайти актив\uD83D\uDD0D");
		List<List<String>> rows = List.of(firstRow, secondRow);
		return new SimpleKeyboard(rows);
	}

	public static InlineKeyboard getAuthKeyboard() {
		InlineButtonInfo choosePortfolioButtonInfo = new InlineButtonInfo(
				"Продолжить со старым портфелем",
				ChoosePortfolioHandler.CONTINUE_WITH_OLD_PORTFOLIO);
		InlineButtonInfo createPortfolioButtonInfo = new InlineButtonInfo(
				"Создать новый",
				ChoosePortfolioHandler.CREATE_NEW_PORTFOLIO);
		List<InlineButtonInfo> firstRowInfo = List.of(choosePortfolioButtonInfo);
		List<InlineButtonInfo> secondRowInfo = List.of(createPortfolioButtonInfo);
		return new InlineKeyboard(List.of(firstRowInfo, secondRowInfo));
	}

	public static InlineKeyboard getAddCurrencyKeyboard() {
		InlineButtonInfo addUSDButtonInfo = new InlineButtonInfo(
				"Добавить 50$",
				ChoosePortfolioHandler.USD);
		InlineButtonInfo acceptButtonInfo = new InlineButtonInfo(
				"Готово",
				ChoosePortfolioHandler.ACCEPT);
		List<InlineButtonInfo> keyboardInfo = List.of(addUSDButtonInfo, acceptButtonInfo);
		return new InlineKeyboard(List.of(keyboardInfo));
	}

	public static InlineKeyboard getToMenuKeyboard() {
		//TODO
		throw new NullPointerException();
	}
}
