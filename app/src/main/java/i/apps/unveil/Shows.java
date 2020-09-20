package i.apps.unveil;

import android.text.Spanned;

public class Shows {

    String title;
    Spanned body;
    String image;
    String rating;

    public Shows() {

    }

    public Shows(String title, Spanned body, String image, String rating) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
}
