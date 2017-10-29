package ibanez.jacob.cat.xtec.ioc.gallery.model;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public interface MultimediaElementRepository {

    interface OnMultimediaElementCreatedListener {
        void onMultimediaElementCreation();
    }

    void setMultimediaElementCreatedListener(OnMultimediaElementCreatedListener multimediaElementCreatedListener);

    MultimediaElement getMultimediaElementById(long id);

    Gallery getGallery();

    void addMultimediaElement(MultimediaElement multimediaElement);
}
