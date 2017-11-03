package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementType;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * View layer of the detail screen.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MultimediaElementDetailView implements OnMapReadyCallback {

    private static final float MAP_ZOOM = 17.0f;

    private View mRootView;
    private MultimediaElement mMultimediaElement;

    /**
     * Instantiates a new Multimedia element detail view.
     *
     * @param fragmentActivity  the fragment activity
     * @param container         the container
     * @param multimediaElement the multimedia element
     */
    public MultimediaElementDetailView(FragmentActivity fragmentActivity, ViewGroup container, MultimediaElement multimediaElement) {
        mRootView = LayoutInflater.from(fragmentActivity).inflate(R.layout.multimedia_element_detail_view, container);
        mMultimediaElement = multimediaElement;

        //get the container which will hold the multimedia element depending on its type
        FrameLayout multimediaElementContainer = mRootView.findViewById(R.id.fl_container);

        if (mMultimediaElement.getType() == MultimediaElementType.PICTURE.getType()) {
            //the element is an image, so display the file on an image view
            ImageView imageView = new ImageView(fragmentActivity);
            imageView.setImageBitmap(BitmapFactory.decodeFile(mMultimediaElement.getPath() +
                    mMultimediaElement.getName()));
            multimediaElementContainer.addView(imageView);
        } else if (mMultimediaElement.getType() == MultimediaElementType.VIDEO.getType()) {
            //the element is a video, so display the file in a video view with a media controller
            VideoView videoView = new VideoView(fragmentActivity);
            Uri videoUri = Uri.fromFile(new File(mMultimediaElement.getPath() + mMultimediaElement.getName()));

            //set parameters programmatically to center the video in the screen
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    MATCH_PARENT,
                    MATCH_PARENT
            );
            layoutParams.gravity = CENTER;
            videoView.setLayoutParams(layoutParams);

            videoView.setVideoURI(videoUri);
            videoView.setMediaController(new MediaController(fragmentActivity));
            //automatically start the video
            videoView.start();
            multimediaElementContainer.addView(videoView);
        }

        //get the map fragment and attach the listener to it
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //add marker depending on multimedia element location
        googleMap.addMarker(new MarkerOptions()
                .position(mMultimediaElement.getLatLng())
        );
        googleMap.getUiSettings().setCompassEnabled(true);
        //zoom the map and move the camera to the marker
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMultimediaElement.getLatLng(), MAP_ZOOM);
        googleMap.moveCamera(cameraUpdate);
    }

    /**
     * Gets root view.
     *
     * @return the root view
     */
    public View getRootView() {
        return this.mRootView;
    }
}
