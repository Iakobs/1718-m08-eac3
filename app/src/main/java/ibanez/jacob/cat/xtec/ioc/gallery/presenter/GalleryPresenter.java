package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import ibanez.jacob.cat.xtec.ioc.gallery.model.Gallery;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepository;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepositoryFake;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementType;
import ibanez.jacob.cat.xtec.ioc.gallery.view.GalleryView;
import ibanez.jacob.cat.xtec.ioc.gallery.view.GalleryViewReclyclerView;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class GalleryPresenter extends AppCompatActivity implements
        GalleryView.OnMultimediaElementClickListener,
        GalleryView.OnTakePictureListener,
        GalleryView.OnRecordVideoListener,
        MultimediaElementRepository.OnMultimediaElementCreatedListener {

    private GalleryView mGalleryView;
    private MultimediaElementRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGalleryView = new GalleryViewReclyclerView(this, null);
        setContentView(mGalleryView.getRootView());

        mGalleryView.setMultimediaElementClickListener(this);
        mGalleryView.setTakePictureListener(this);
        mGalleryView.setRecordVideoListener(this);

        mRepository = new MultimediaElementRepositoryFake();
        mRepository.setMultimediaElementCreatedListener(this);
        mGalleryView.bindMultimediaElements(mRepository.getGallery());
    }

    @Override
    public void onVideoRecorded() {
        Gallery gallery = mRepository.getGallery();
        long newId = gallery.get(gallery.size() - 1).getId() + 1;

        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setId(newId);
        multimediaElement.setName("Video " + newId);
        multimediaElement.setPath("/");
        multimediaElement.setType(MultimediaElementType.VIDEO.getType());
        multimediaElement.setLatLng(new LatLng(0, 0));
        mRepository.addMultimediaElement(multimediaElement);
    }

    @Override
    public void onPictureTaken() {
        Gallery gallery = mRepository.getGallery();
        long newId = gallery.get(gallery.size() - 1).getId() + 1;

        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setId(newId);
        multimediaElement.setName("Picture " + newId);
        multimediaElement.setPath("/");
        multimediaElement.setType(MultimediaElementType.PICTURE.getType());
        multimediaElement.setLatLng(new LatLng(0, 0));
        mRepository.addMultimediaElement(multimediaElement);
    }

    @Override
    public void onMultimediaElementClicked(long multimediaElementId) {

    }

    @Override
    public void onMultimediaElementCreation() {
        mGalleryView.bindMultimediaElements(mRepository.getGallery());
    }
}
