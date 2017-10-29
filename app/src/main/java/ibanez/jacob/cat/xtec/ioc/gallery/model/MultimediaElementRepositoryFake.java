package ibanez.jacob.cat.xtec.ioc.gallery.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */

public class MultimediaElementRepositoryFake implements MultimediaElementRepository {

    private Gallery mFakeDatabase = new Gallery();
    private OnMultimediaElementCreatedListener mMultimediaElementCreatedListener;

    public MultimediaElementRepositoryFake() {
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
    public void setMultimediaElementCreatedListener(OnMultimediaElementCreatedListener multimediaElementCreatedListener) {
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
        notifyMultimediaElementCreationListener();
    }

    private void notifyMultimediaElementCreationListener() {
        if (mMultimediaElementCreatedListener != null) {
            mMultimediaElementCreatedListener.onMultimediaElementCreation();
        }
    }
}
