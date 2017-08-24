package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodListItem {
    private FoodItem food;
    private String userId;
    // TODO: Location, Date?

    public FoodListItem(FoodItem food, String userId) {
        this.food = food;
        this.userId = userId;
    }

    public FoodItem getFood() {
        return food;
    }

    public void setFood(FoodItem food) {
        this.food = food;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
