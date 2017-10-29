package ibanez.jacob.cat.xtec.ioc.gallery.model;

import java.util.List;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public interface MultimediaElementRepository {

    interface OnMultimediaElementCreatedListener {
        void onMultimediaElementCreation();
    }

    void setMultimediaElementCreatedListener(OnMultimediaElementCreatedListener multimediaElementCreatedListener);

    MultimediaElement getItemById(long id);

    List<MultimediaElement> getAllItems();

    void addItem(MultimediaElement multimediaElement);
}
