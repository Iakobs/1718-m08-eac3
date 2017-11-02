package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
        GalleryView.OnTakePictureClickListener,
        GalleryView.OnRecordVideoClickListener,
        GalleryView.OnMultimediaElementSwipeListener,
        MultimediaElementRepository.OnGalleryChangedListener,
        LocationListener {

    private static final int ACCESS_FINE_LOCATION_PERMISSION = 1;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION = 3;

    private GalleryView mGalleryView;
    private MultimediaElementRepository mRepository;
    private Toast mToast;
    private LocationManager mLocationManager;
    private Location mLastLocation;

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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkPermissions();
    }

    @Override
    public void onTakePictureClicked() {

    }

    @Override
    public void onPictureTaken() {
        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setName("Picture." + new Date().toString() + ".jpg");
        multimediaElement.setPath("/");
        multimediaElement.setType(MultimediaElementType.PICTURE.getType());
        LatLng latLng = new LatLng(0.0, 0.0);
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
        multimediaElement.setLatLng(latLng);
        mRepository.addMultimediaElement(multimediaElement);
    }

    @Override
    public void onRecordVideoClicked() {

    }

    @Override
    public void onVideoRecorded() {
        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setName("Video." + new Date().toString() + ".mp4");
        multimediaElement.setPath("/");
        multimediaElement.setType(MultimediaElementType.VIDEO.getType());
        LatLng latLng = new LatLng(0.0, 0.0);
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
        multimediaElement.setLatLng(latLng);
        mRepository.addMultimediaElement(multimediaElement);
    }

    @Override
    public void onMultimediaElementClicked(long id) {
        MultimediaElement multimediaElement = mRepository.getMultimediaElementById(id);

        if (multimediaElement != null) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, multimediaElement.toString(), Toast.LENGTH_SHORT);
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
        mLastLocation = location;
        switchCameraStatus();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switchCameraStatus();
    }

    @Override
    public void onProviderEnabled(String provider) {
        switchCameraStatus();
    }

    @Override
    public void onProviderDisabled(String provider) {
        switchCameraStatus();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                }
                break;
        }
    }

    private void checkPermissions() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION);
        } else {
            requestLocationUpdates();
        }

        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    private void requestLocationUpdates() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    private void switchCameraStatus() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation == null) {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (mLastLocation == null) {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (mLastLocation != null) {
            mGalleryView.enableCamera();
        } else {
            mGalleryView.disableCamera();
        }
    }
}
