package models;

public class RatingInfo {
    private final String username;
    private final Integer countPoints;

    public RatingInfo(String username, Integer countPoints) {
        this.username = username;
        this.countPoints = countPoints;
    }

    public String getUsername() {
        return username;
    }

    public Integer getCountPoints() {
        return countPoints;
    }

}
