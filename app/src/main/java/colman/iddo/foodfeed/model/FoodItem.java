package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodItem {

    private String fid;
    private String name;
    private String type;
    private String description;
    private Boolean vegetarian;
    private String userId;

    private String imageUrl;
    private double lastUpdateDate;

    public FoodItem(String fid, String name, String type, String description, Boolean vegetarian, String imageUrl, String userId) {
        this.setFid(fid);
        this.setName(name);
        this.setType(type);
        this.setDescription(description);
        this.setVegetarian(vegetarian);
        this.setImageUrl(imageUrl);
        this.setUserId(userId);
    }

    // Default CTOR, used for initial foods generation
    public FoodItem(){
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FoodItem) {
            FoodItem toCompare = (FoodItem) obj;
            return (this.fid.equalsIgnoreCase(toCompare.getFid()));
        }
        return false;
    }

    /**
     * Getters & Setters
     */
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public void setType(String foodType) { this.type = foodType; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public Boolean getVegetarian() { return vegetarian; }

    public void setVegetarian(Boolean vegetarian) { this.vegetarian = vegetarian; }

    public double getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(double lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
