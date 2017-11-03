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
 * The activity which acts as a presenter layer object for the main screen of the app.
 *
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
        //get the reference to the view layer of the main screen
        mGalleryView = new GalleryViewReclyclerView(this, null);
        setContentView(mGalleryView.getRootView());

        //register yourself as a listener for events on the view layer
        mGalleryView.setMultimediaElementClickListener(this);
        mGalleryView.setTakePictureClickListener(this);
        mGalleryView.setRecordVideoClickListener(this);
        mGalleryView.setMultimediaElementSwipeListener(this);

        //get the reference to the model layer of the app
        mRepository = new MultimediaElementRepositorySqlLite(this);
        //register yourself as a listener on database changes
        mRepository.setGalleryChangedListener(this);
        //bind the elements in the database to the view layer
        mGalleryView.bindGallery(mRepository.getGallery());

        //get a reference to the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check for location to enable or disable camera, and ask for needed permissions
        switchCameraStatus();
        checkPermissions();
    }

    @Override
    public void onTakePictureClicked() {
        //create the intent
        requestCameraIntent(MultimediaElementType.PICTURE);
    }

    @Override
    public void onRecordVideoClicked() {
        //create the intent
        requestCameraIntent(MultimediaElementType.VIDEO);
    }

    private void requestCameraIntent(MultimediaElementType type) {
        //create the intent and the result code depending on the type
        Intent requestCameraIntent = null;
        int resultCode = 0;
        switch (type) {
            case PICTURE:
                requestCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultCode = REQUEST_TAKE_PHOTO;
                break;
            case VIDEO:
                requestCameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                resultCode = REQUEST_SHOOT_VIDEO;
                break;
        }

        //check if the intent can be handled
        if (requestCameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the multimedia element should go
            File multimediaElementFile = null;
            try {
                multimediaElementFile = createMultimediaElementFile(type);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "An error occured while creating the file for storing the multimedia element", ex);
            }
            // Continue only if the File was successfully created
            if (multimediaElementFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.provider_authority),
                        multimediaElementFile);
                requestCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //start activity for result depending on the type
                startActivityForResult(requestCameraIntent, resultCode);
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean resultIsOk = resultCode == RESULT_OK;
        boolean requestIsTakePhotoOrShootVideo = requestCode == REQUEST_TAKE_PHOTO
                || requestCode == REQUEST_SHOOT_VIDEO;

        if (resultIsOk && requestIsTakePhotoOrShootVideo) {
            //get type depending on the request
            int type = 0;
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    type = MultimediaElementType.PICTURE.getType();
                    break;
                case REQUEST_SHOOT_VIDEO:
                    type = MultimediaElementType.VIDEO.getType();
                    break;
            }

            boolean hasNameAndPath = false;
            boolean hasLocation = false;

            //get values from the request
            String name = null;
            String path = null;
            LatLng latLng = null;
            if (mCurrentMultimediaElementPath != null) {
                name = mCurrentMultimediaElementPath.substring(
                        mCurrentMultimediaElementPath.lastIndexOf("/") + 1,
                        mCurrentMultimediaElementPath.length()
                );
                path = mCurrentMultimediaElementPath.substring(
                        0,
                        mCurrentMultimediaElementPath.lastIndexOf("/") + 1
                );
                hasNameAndPath = true;
            }
            if (mLastLocation != null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                hasLocation = true;
            }
            if (hasNameAndPath && hasLocation) {
                //if we have all values, creates the new element
                MultimediaElement multimediaElement = new MultimediaElement();
                multimediaElement.setName(name);
                multimediaElement.setPath(path);
                multimediaElement.setType(type);
                multimediaElement.setLatLng(latLng);
                mRepository.addMultimediaElement(multimediaElement);
            }
        }
    }

    @Override
    public void onMultimediaElementClicked(long id) {
        //create the intent to show the details of a single multimedia element
        Intent intent = new Intent(this, MultimediaElementDetailPresenter.class);

        //create the extras and put the id on it
        Bundle extras = new Bundle();
        extras.putLong(MultimediaElementDetailPresenter.EXTRA_MULTIMEDIA_ELEMENT_ID, id);

        //put the extras in the intent and start the new activity
        intent.putExtras(extras);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
        }
    }

    @Override
    public void onMultimediaElementSwiped(long id) {
        //remove the selected element on swiped
        mRepository.removeMultimediaElementById(id);
    }

    @Override
    public void onGalleryChange() {
        //whenever a change on the database is notified, bind the new data to the view layer
        mGalleryView.bindGallery(mRepository.getGallery());
    }

    @Override
    public void onLocationChanged(Location location) {
        //store new location in member variable and switch camera status
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
        //check if we miss any of the permissions required to run the app
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //if any permission is missed, we request them all
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
            //request location updates at minimum interval on both providers, to assure better functionality
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    private void switchCameraStatus() {
        //check if we have location permission
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //try to get last location from both providers
            if (mLastLocation == null) {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (mLastLocation == null) {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        //if after all there's no location, disable camera. Enable it otherwise.
        if (mLastLocation == null) {
            mGalleryView.disableCamera();
        } else {
            mGalleryView.enableCamera();
        }
    }
}
