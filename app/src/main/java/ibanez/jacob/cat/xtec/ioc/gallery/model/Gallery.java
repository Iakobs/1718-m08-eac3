package ibanez.jacob.cat.xtec.ioc.gallery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for encapsulating a collection of {@link MultimediaElement}s.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class Gallery {

    private List<MultimediaElement> mGallery;

    public Gallery() {
        this.mGallery = new ArrayList<>();
    }

    public void setGallery(List<MultimediaElement> gallery) {
        this.mGallery = gallery;
    }

    public boolean isEmpty() {
        return mGallery != null && mGallery.isEmpty();
    }

    public MultimediaElement get(int position) {
        if (mGallery != null) {
            return mGallery.get(position);
        }
        return null;
    }

    public void add(MultimediaElement multimediaElement) {
        if (mGallery != null) {
            mGallery.add(multimediaElement);
        }
    }

    public int size() {
        if (mGallery != null) {
            return mGallery.size();
        }
        return 0;
    }
}
