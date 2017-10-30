package ibanez.jacob.cat.xtec.ioc.gallery.model;

/**
 * This interface provides a contract to communication between the presenter and the model of the
 * application.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public interface MultimediaElementRepository {

    /**
     * This interface must be implemented by whatever class which wants to be notified when the
     * gallery is modificated.
     */
    interface OnGalleryChangedListener {
        /**
         * This callback must be invoked when any modification of the gallery occurs
         */
        void onGalleryChange();
    }

    /**
     * Attaches a new listener to the model
     *
     * @param multimediaElementCreatedListener A class implementing {@link OnGalleryChangedListener}
     */
    void setGalleryChangedListener(OnGalleryChangedListener multimediaElementCreatedListener);

    /**
     * Gets a single {@link MultimediaElement} by it's id.
     *
     * @param id The {@link MultimediaElement} identifier.
     * @return The selected {@link MultimediaElement} or {@code null} if not found.
     */
    MultimediaElement getMultimediaElementById(long id);

    /**
     * Gets the collection of all {@link MultimediaElement}s
     *
     * @return The {@link Gallery} containing the collection of {@link MultimediaElement}s
     */
    Gallery getGallery();

    /**
     * Adds a new {@link MultimediaElement} to the {@link Gallery}.
     * <p>
     * Each implementation has to deal
     * with duplicates properly to ensure consistency.
     *
     * @param multimediaElement The {@link MultimediaElement} to be inserted.
     */
    void addMultimediaElement(MultimediaElement multimediaElement);

    /**
     * Removes a {@link MultimediaElement} from its {@link Gallery}.
     *
     * @param id he {@link MultimediaElement} identifier.
     */
    void removeMultimediaElementById(long id);
}
