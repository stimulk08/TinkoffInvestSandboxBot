package models;

import java.util.List;
import wrappers.Message;
import wrappers.WrappedUpdate;

public interface Handler {

	List<Message> handleMessage(User user, WrappedUpdate message);

	List<Message> handleCallbackQuery(User user, WrappedUpdate callbackQuery);

	State handledState();

	List<String> handledCallBackQuery();
}
