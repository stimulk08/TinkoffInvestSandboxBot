package models;

import java.util.ArrayList;
import java.util.List;

import wrappers.WrappedApi;

import java.math.BigDecimal;
import java.util.Date;

public class User {

    private final long chatId;
    private State state;
    private long lastQueryTime;
    private WrappedApi api;
    private BigDecimal startUSDAmount = new BigDecimal(0);
    private final List<Boolean> completedTests;
    private final String username;

    public User(long chatId, String username) {
        this.chatId = chatId;
        this.username = username;
        this.state = State.NONE;
        this.completedTests = GoogleDocks.getUserCompletedTests(username);
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

    public List<Boolean> getCompletedTests() {
        return completedTests;
    }

    public void setCompletedTest(Integer testNumber) {
        completedTests.set(testNumber - 1, true);
    }

    public String getUsername() {
        return username;
    }
}
