package ibanez.jacob.cat.xtec.ioc.gallery.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for encapsulating a collection of {@link MultimediaElement}s.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class Gallery {

    private List<MultimediaElement> mGallery;

    /**
     * Instantiates a new {@link Gallery}.
     */
    public Gallery() {
        this.mGallery = new ArrayList<>();
    }

    /**
     * Sets a {@link Gallery}.
     *
     * @param gallery the {@link Gallery}
     */
    public void setGallery(List<MultimediaElement> gallery) {
        this.mGallery = gallery;
    }

    /**
     * Returns if the gallery is empty or not.
     *
     * @return {@code true} if is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return mGallery != null && mGallery.isEmpty();
    }

    /**
     * Get a {@link MultimediaElement} by its position.
     *
     * @param position the position
     * @return the {@link MultimediaElement}
     */
    public MultimediaElement get(int position) {
        if (mGallery != null) {
            return mGallery.get(position);
        }
        return null;
    }

    /**
     * Add a new {@link MultimediaElement} to the bottom of the gallery.
     *
     * @param multimediaElement the new {@link MultimediaElement}
     */
    public void add(MultimediaElement multimediaElement) {
        if (mGallery != null) {
            mGallery.add(multimediaElement);
        }
    }

    /**
     * Returns the size of the {@link Gallery}.
     *
     * @return the the size of the {@link Gallery}
     */
    public int size() {
        if (mGallery != null) {
            return mGallery.size();
        }
        return 0;
    }

    /**
     * Gets the collection inside the {@link Gallery}.
     *
     * @return the collection
     */
    public Collection<MultimediaElement> getCollection() {
        return this.mGallery;
    }
}
