package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepository;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepositorySqlLite;
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

        mRepository = new MultimediaElementRepositorySqlLite(this);
        mRepository.setMultimediaElementCreatedListener(this);
        mGalleryView.bindMultimediaElements(mRepository.getGallery());
    }

    @Override
    public void onVideoRecorded() {
        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setName("Video." + new Date().toString() + ".mp4");
        multimediaElement.setPath("/");
        multimediaElement.setType(MultimediaElementType.VIDEO.getType());
        multimediaElement.setLatLng(new LatLng(0, 0));
        mRepository.addMultimediaElement(multimediaElement);
    }

    @Override
    public void onPictureTaken() {
        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setName("Picture." + new Date().toString() + ".jpg");
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
