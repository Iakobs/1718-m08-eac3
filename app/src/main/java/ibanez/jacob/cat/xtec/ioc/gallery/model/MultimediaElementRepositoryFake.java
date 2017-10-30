package ibanez.jacob.cat.xtec.ioc.gallery.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Fake implementation of {@link MultimediaElementRepository} for test purposes.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MultimediaElementRepositoryFake implements MultimediaElementRepository {

    private Gallery mFakeDatabase = new Gallery();
    private OnGalleryChangedListener mMultimediaElementCreatedListener;

    public MultimediaElementRepositoryFake() {
        //add 10 elements to the fake database
        for (int i = 1; i <= 10; i++) {
            int type = new Random().nextBoolean() ? 0 : 1;
            String name = "Picture ";
            if (type == 1) {
                name = "Video ";
            }

            MultimediaElement multimediaElement = new MultimediaElement();
            multimediaElement.setId(i);
            multimediaElement.setName(name + i);
            multimediaElement.setPath("/");
            multimediaElement.setType(type);
            multimediaElement.setLatLng(new LatLng(0, 0));
            mFakeDatabase.add(multimediaElement);
        }
    }

    @Override
    public void setGalleryChangedListener(OnGalleryChangedListener multimediaElementCreatedListener) {
        this.mMultimediaElementCreatedListener = multimediaElementCreatedListener;
    }

    @Override
    public MultimediaElement getMultimediaElementById(long id) {
        return mFakeDatabase.get((int) id);
    }

    @Override
    public Gallery getGallery() {
        return mFakeDatabase;
    }

    @Override
    public void addMultimediaElement(MultimediaElement multimediaElement) {
        mFakeDatabase.add(multimediaElement);
        notifyGalleryChangedListener();
    }

    @Override
    public void removeMultimediaElementById(long id) {
        Gallery newGallery = new Gallery();

        for (MultimediaElement multimediaElement : mFakeDatabase.getCollection()) {
            if (multimediaElement.getId() != id) {
                newGallery.add(multimediaElement);
            }
        }

        this.mFakeDatabase = newGallery;
        notifyGalleryChangedListener();
    }

    private void notifyGalleryChangedListener() {
        if (mMultimediaElementCreatedListener != null) {
            mMultimediaElementCreatedListener.onGalleryChange();
        }
    }
}
