package models;

import wrappers.WrappedApi;

import java.math.BigDecimal;
import java.util.Date;

public class User {

	private final long chatId;
	private State state;
	private long lastQueryTime;
	private WrappedApi api;
	private BigDecimal startUSDAmount = new BigDecimal(0);

	public User(long chatId) {
		this.chatId = chatId;
		this.state = State.NONE;
		setLastQueryTime();
	}

	public void setApi(WrappedApi api) {
		this.api = api;
	}

	public WrappedApi getApi() {
		return this.api;
	}

	public long getChatId() {
		return chatId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setLastQueryTime() {
		this.lastQueryTime = new Date().getTime();
	}

	public long getLastQueryTime() {
		return this.lastQueryTime;
	}

	public void increaseUSDAmount(BigDecimal addingValue) {
		startUSDAmount = startUSDAmount.add(addingValue);
	}

	public BigDecimal getUSDAmount() {
		return startUSDAmount;
	}
}
