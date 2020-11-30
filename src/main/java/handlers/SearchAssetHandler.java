package handlers;

import models.Handler;

import models.State;
import models.User;

import java.util.Collections;
import java.util.List;
import wrappers.Message;
import wrappers.WrappedUpdate;

public class SearchAssetHandler implements Handler {

	@Override
	public List<Message> handleMessage(User user, WrappedUpdate message) {
		return Collections.emptyList();
	}

	@Override
	public List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
		return Collections.emptyList();
	}

	@Override
	public State handledState() {
		return State.SEARCH_ASSET;
	}

	@Override
	public List<String> handledCallBackQuery() {
		return Collections.emptyList();
	}
}
