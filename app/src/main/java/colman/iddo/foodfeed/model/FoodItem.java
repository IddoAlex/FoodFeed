package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodItem {

    private String id;
    private String name;
    private String type;
    private String description;
    private String price;
    private Boolean discount;
    private String imageUrl;
    private String userId;
    private double lastUpdateDate;

    public FoodItem(String id, String name, String type, String description, int price, Boolean discount, String imageUrl, String userId, double lastUpdateDate) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setDescription(description);
        this.setPrice(price);
        this.setDiscount(discount);
        this.setImageUrl(imageUrl);
        this.setUserId(userId);
        this.setLastUpdateDate(lastUpdateDate);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() { return type; }

    public void setFoodType(String foodType) { this.type = foodType; }

    public String getPrice() { return price; }

    public void setPrice(int price) { this.price = Integer.toString(price); }

    public Boolean getDiscount() { return discount; }

    public void setDiscount(Boolean discount) { this.discount = discount; }

    public double getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(double lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
