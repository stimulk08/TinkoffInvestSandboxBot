package models.keyboards;

import handlers.ChoosePortfolioHandler;
import models.Asset.AssetInfo;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

	public static SimpleKeyboard getMenuKeyboard() {
		List<String> firstRow = List.of("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC",
				"❌Сбросить портфель❌");
		List<String> secondRow = List.of("\uD83D\uDD0EНайти актив\uD83D\uDD0D",
				"✏Пройти тест✏");
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

	public static InlineKeyboard getChooseTestKeyboard(List<Boolean> completedTests) {
		List<InlineButtonInfo> tests = new ArrayList<>();
		for (int i = 0; i < completedTests.size(); i++) {
			String testNumber = Integer.toString(i + 1);
			if (completedTests.get(i)) {
				tests.add(new InlineButtonInfo(testNumber + " ✅", testNumber));
			} else {
				tests.add(new InlineButtonInfo(testNumber, testNumber));
			}
		}
		List<List<InlineButtonInfo>> rows = List.of(tests);
		return new InlineKeyboard(rows);
	}

	public static InlineKeyboard getVariantsQuestionKeyboard(List<String> variants) {
		List<List<InlineButtonInfo>> rows = new ArrayList<>();
		for (String variant : variants) {
			rows.add(List.of(new InlineButtonInfo(variant, variant)));
		}
		return new InlineKeyboard(rows);
	}
}
