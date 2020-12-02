package models.keyboards;

import handlers.ChoosePortfolioHandler;
import models.Asset.AssetInfo;

import java.util.ArrayList;
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
	public static InlineKeyboard getKeyboardByAssetInfo(List<AssetInfo> infoList, Integer pageNumber){
		InlineButtonInfo previousPageButton = new InlineButtonInfo("◀️", "<");
		InlineButtonInfo nextPageButton = new InlineButtonInfo("▶️", ">");
		InlineButtonInfo pageNumberButton = new InlineButtonInfo(pageNumber.toString(), "page");

		List<InlineButtonInfo> serviceRow = new ArrayList<>();
		List<List<InlineButtonInfo>> keyboardInfo = new ArrayList<>();

		for(AssetInfo asset : infoList){
			InlineButtonInfo buttonInfo = new InlineButtonInfo(asset.getFullName(), asset.getFigi());
			keyboardInfo.add(List.of(buttonInfo));
		}

		serviceRow.add(previousPageButton);
		serviceRow.add(pageNumberButton);
		serviceRow.add(nextPageButton);
		keyboardInfo.add(serviceRow);

		return new InlineKeyboard(keyboardInfo);
	}

	public static SimpleKeyboard getChooseCountryAssets(){
		List<String> firstRow = List.of("Отечественные");
		List<String> secondRow = List.of("Зарубежные");
		List<String> thirdRow = List.of("Вернуться в меню");
		List<List<String>> rows = List.of(firstRow, secondRow, thirdRow);
		return new SimpleKeyboard(rows);
	}

	public static SimpleKeyboard getChooseSearchModeKeyboard() {
		List<String> firstRow = List.of("Поиск актива по тикеру");
		List<String> secondRow = List.of("Поиск по списку компаний");
		List<String> thirdRow = List.of("Вернуться в меню");
		List<List<String>> rows = List.of(firstRow, secondRow, thirdRow);
		return new SimpleKeyboard(rows);
	}
}
