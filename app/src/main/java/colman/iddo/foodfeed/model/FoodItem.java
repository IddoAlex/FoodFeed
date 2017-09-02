package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodItem {

    private String id;
    private String foodName;
    private String foodType;
    private String description;
    private Boolean vegetarian;

    private String imageUrl;
    private double lastUpdateDate;

    public FoodItem(String id, String foodName, String foodType, String description, Boolean vegetarian) {
        this.setId(id);
        this.setFoodName(foodName);
        this.setFoodType(foodType);
        this.setDescription(description);
        this.setVegetarian(vegetarian);
    }

    // Default CTOR, used for initial foods generation
    public FoodItem(){
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FoodItem) {
            FoodItem toCompare = (FoodItem) obj;
            return (this.id.equalsIgnoreCase(toCompare.getId()));
        }
        return false;
    }

    /**
     * Getters & Setters
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodType() { return foodType; }

    public void setFoodType(String foodType) { this.foodType = foodType; }

    public Boolean getVegetarian() { return vegetarian; }

    public void setVegetarian(Boolean vegetarian) { this.vegetarian = vegetarian; }

    public double getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(double lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
