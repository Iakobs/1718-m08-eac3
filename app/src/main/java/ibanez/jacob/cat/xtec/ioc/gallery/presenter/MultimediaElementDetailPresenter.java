package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepository;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepositorySqlLite;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementType;

public class MultimediaElementDetailPresenter extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_MULTIMEDIA_ELEMENT_ID = MultimediaElementDetailPresenter.class
            .getCanonicalName() + ".MULTIMEDIA_ELEMENT_ID";

    private static final float MAP_ZOOM = 17.0f;

    private MultimediaElement mMultimediaElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimedia_element_detail_view);

        MultimediaElementRepository repository = new MultimediaElementRepositorySqlLite(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_MULTIMEDIA_ELEMENT_ID)) {
            long id = extras.getLong(EXTRA_MULTIMEDIA_ELEMENT_ID);
            mMultimediaElement = repository.getMultimediaElementById(id);

            FrameLayout multimediaElementContainer = findViewById(R.id.fl_container);

            if (mMultimediaElement.getType() == MultimediaElementType.PICTURE.getType()) {
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(BitmapFactory.decodeFile(mMultimediaElement.getPath() + mMultimediaElement.getName()));
                multimediaElementContainer.addView(imageView);
            } else if (mMultimediaElement.getType() == MultimediaElementType.VIDEO.getType()) {
                VideoView videoView = new VideoView(this);
                Uri videoUri = Uri.fromFile(new File(mMultimediaElement.getPath() + mMultimediaElement.getName()));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;
                videoView.setLayoutParams(layoutParams);
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(new MediaController(this));
                videoView.start();
                multimediaElementContainer.addView(videoView);
            }

            SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(mMultimediaElement.getLatLng())
        );
        googleMap.getUiSettings().setCompassEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMultimediaElement.getLatLng(), MAP_ZOOM);
        googleMap.moveCamera(cameraUpdate);
    }
}
