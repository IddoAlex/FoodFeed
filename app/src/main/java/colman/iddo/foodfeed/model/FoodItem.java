package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodItem {
    String id;
    String name;
    String description;
    String imageUrl;

    public FoodItem(String id, String name, String description, String imageUrl) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setImageUrl(imageUrl);
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


}
