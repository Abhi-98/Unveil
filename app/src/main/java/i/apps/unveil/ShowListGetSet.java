package i.apps.unveil;

import android.text.Spanned;

public class ShowListGetSet {

    String title;
    Spanned body;
    String image;
    String rating;

    public ShowListGetSet() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Spanned getBody() {
        return body;
    }

    public void setBody(Spanned body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
