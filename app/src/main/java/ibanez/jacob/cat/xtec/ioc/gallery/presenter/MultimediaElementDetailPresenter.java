package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepository;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepositorySqlLite;

public class MultimediaElementDetailPresenter extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_MULTIMEDIA_ELEMENT_ID = MultimediaElementDetailPresenter.class
            .getCanonicalName() + ".MULTIMEDIA_ELEMENT_ID";

    private MultimediaElement mMultimediaElement;
    private FrameLayout mMultimediaElementContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimedia_element_detail_view);

        MultimediaElementRepository repository = new MultimediaElementRepositorySqlLite(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_MULTIMEDIA_ELEMENT_ID)) {
            long id = extras.getLong(EXTRA_MULTIMEDIA_ELEMENT_ID);
            mMultimediaElement = repository.getMultimediaElementById(id);

            mMultimediaElementContainer = findViewById(R.id.fl_container);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_image_black_24dp);
            imageView.setImageTintList(this.getColorStateList(R.color.grey_image_tint));
            mMultimediaElementContainer.addView(imageView);

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMultimediaElement.getLatLng(), 17.0f);
        googleMap.moveCamera(cameraUpdate);
    }
}
