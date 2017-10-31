package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
        GalleryView.OnMultimediaElementSwipeListener,
        MultimediaElementRepository.OnGalleryChangedListener,
        LocationListener {

    private static final int FINE_LOCATION_PERMISSION = 1;

    private GalleryView mGalleryView;
    private MultimediaElementRepository mRepository;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGalleryView = new GalleryViewReclyclerView(this, null);
        setContentView(mGalleryView.getRootView());

        mGalleryView.setMultimediaElementClickListener(this);
        mGalleryView.setTakePictureListener(this);
        mGalleryView.setRecordVideoListener(this);
        mGalleryView.setMultimediaElementSwipeListener(this);
        mGalleryView.disableCamera();

        mRepository = new MultimediaElementRepositorySqlLite(this);
        mRepository.setGalleryChangedListener(this);
        mGalleryView.bindGallery(mRepository.getGallery());

        checkPermissions();
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
    public void onMultimediaElementClicked(long id) {
        MultimediaElement multimediaElement = mRepository.getMultimediaElementById(id);

        if (multimediaElement != null) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, multimediaElement.toString(), Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    @Override
    public void onMultimediaElementSwiped(long id) {
        mRepository.removeMultimediaElementById(id);
    }

    @Override
    public void onGalleryChange() {
        mGalleryView.bindGallery(mRepository.getGallery());
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGalleryView.enableCamera();
                }
                break;
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
        } else {
            mGalleryView.enableCamera();
        }
    }
}
