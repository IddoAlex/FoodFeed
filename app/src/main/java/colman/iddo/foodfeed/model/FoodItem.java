package colman.iddo.foodfeed.model;

/**
 * Created by Iddo on 24-Aug-17.
 */

public class FoodItem {
    private String id;
    private String name;
    private String descrption;
    private String imageUrl;

    public FoodItem(String id, String name, String descrption, String imageUrl) {
        this.setId(id);
        this.setName(name);
        this.setDescrption(descrption);
        this.setImageUrl(imageUrl);
    }

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

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
