package models;

import java.util.ArrayList;

public class Rating {
    private final ArrayList<RatingInfo> rating;

    public Rating(ArrayList<RatingInfo> rating) {
        this.rating = rating;
    }

    public String getFormatRating() {
        if (rating.size() == 0) return "Рейтинг пуст";
        StringBuilder formatRating = new StringBuilder();
        for (int i = 0; i < rating.size(); i++) {
            RatingInfo ratingInfo = rating.get(i);
            formatRating.append(String.format("%d) @%s - %d\n", i + 1,
                    ratingInfo.getUsername(), ratingInfo.getCountPoints()));
        }
        return formatRating.toString();
    }

    public boolean isEmpty() {
        return rating.size() == 0;
    }
}
