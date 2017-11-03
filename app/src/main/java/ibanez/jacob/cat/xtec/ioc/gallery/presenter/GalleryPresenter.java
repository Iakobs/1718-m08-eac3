package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
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

    //Tag for logging purposes
    private static final String TAG = GalleryPresenter.class.getSimpleName();
    private static final int LOCATION_AND_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SHOOT_VIDEO = 2;

    private GalleryView mGalleryView;
    private MultimediaElementRepository mRepository;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private String mCurrentMultimediaElementPath;

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

    private File createMultimediaElementFile(MultimediaElementType type) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String multimediaElementFileName = null;
        String suffix = null;
        switch (type) {
            case PICTURE:
                multimediaElementFileName = "JPEG_" + timeStamp + "_";
                suffix = ".jpg";
                break;
            case VIDEO:
                multimediaElementFileName = "MP4_" + timeStamp + "_";
                suffix = ".mp4";
                break;
        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File multimediaElementFile = File.createTempFile(
                multimediaElementFileName,
                suffix,
                storageDir
        );
        mCurrentMultimediaElementPath = multimediaElementFile.getAbsolutePath();
        return multimediaElementFile;
    }

    @Override
    public void onTakePictureClicked() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMultimediaElementFile(MultimediaElementType.PICTURE);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "An error occured while creating the file for storing the picture", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.provider_authority),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onRecordVideoClicked() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMultimediaElementFile(MultimediaElementType.VIDEO);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "An error occured while creating the file for storing the picture", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.provider_authority),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_SHOOT_VIDEO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    boolean hasNameAndPath = false;
                    boolean hasLocation = false;
                    String name = null;
                    String path = null;
                    LatLng latLng = null;
                    if (mCurrentMultimediaElementPath != null) {
                        name = mCurrentMultimediaElementPath.substring(mCurrentMultimediaElementPath.lastIndexOf("/") + 1, mCurrentMultimediaElementPath.length());
                        path = mCurrentMultimediaElementPath.substring(0, mCurrentMultimediaElementPath.lastIndexOf("/") + 1);
                        hasNameAndPath = true;
                    }
                    if (mLastLocation != null) {
                        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        hasLocation = true;
                    }
                    if (hasNameAndPath && hasLocation) {
                        MultimediaElement multimediaElement = new MultimediaElement();
                        multimediaElement.setName(name);
                        multimediaElement.setPath(path);
                        multimediaElement.setType(MultimediaElementType.PICTURE.getType());
                        multimediaElement.setLatLng(latLng);
                        mRepository.addMultimediaElement(multimediaElement);
                    } else {
                        Log.e(TAG, "");
                    }
                }
                break;
            case REQUEST_SHOOT_VIDEO:
                if (resultCode == RESULT_OK) {
                    boolean hasNameAndPath = false;
                    boolean hasLocation = false;
                    String name = null;
                    String path = null;
                    LatLng latLng = null;
                    if (mCurrentMultimediaElementPath != null) {
                        name = mCurrentMultimediaElementPath.substring(mCurrentMultimediaElementPath.lastIndexOf("/") + 1, mCurrentMultimediaElementPath.length());
                        path = mCurrentMultimediaElementPath.substring(0, mCurrentMultimediaElementPath.lastIndexOf("/") + 1);
                        hasNameAndPath = true;
                    }
                    if (mLastLocation != null) {
                        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        hasLocation = true;
                    }
                    if (hasNameAndPath && hasLocation) {
                        MultimediaElement multimediaElement = new MultimediaElement();
                        multimediaElement.setName(name);
                        multimediaElement.setPath(path);
                        multimediaElement.setType(MultimediaElementType.VIDEO.getType());
                        multimediaElement.setLatLng(latLng);
                        mRepository.addMultimediaElement(multimediaElement);
                    } else {
                        Log.e(TAG, "");
                    }
                }
                break;
        }
    }

    @Override
    public void onMultimediaElementClicked(long id) {
        Intent intent = new Intent(this, MultimediaElementDetailPresenter.class);
        Bundle extras = new Bundle();
        extras.putLong(MultimediaElementDetailPresenter.EXTRA_MULTIMEDIA_ELEMENT_ID, id);

        //put the bundle in the intent and start the new activity
        intent.putExtras(extras);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
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
            case LOCATION_AND_STORAGE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                }
                break;
        }
    }

    private void checkPermissions() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, LOCATION_AND_STORAGE_PERMISSIONS);
        } else {
            requestLocationUpdates();
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
