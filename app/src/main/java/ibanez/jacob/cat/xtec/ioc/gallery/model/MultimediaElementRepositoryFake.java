package ibanez.jacob.cat.xtec.ioc.gallery.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */

public class MultimediaElementRepositoryFake implements MultimediaElementRepository {

    private List<MultimediaElement> mFakeDatabase = new ArrayList<>();
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
    public MultimediaElement getItemById(long id) {
        return mFakeDatabase.get((int) id);
    }

    @Override
    public List<MultimediaElement> getAllItems() {
        return mFakeDatabase;
    }

    @Override
    public void addItem(MultimediaElement multimediaElement) {
        mFakeDatabase.add(multimediaElement);
        notifyMultimediaElementCreationListener();
    }

    private void notifyMultimediaElementCreationListener() {
        if (mMultimediaElementCreatedListener != null) {
            mMultimediaElementCreatedListener.onMultimediaElementCreation();
        }
    }
}
