package ibanez.jacob.cat.xtec.ioc.gallery.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepository;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementRepositorySqlLite;
import ibanez.jacob.cat.xtec.ioc.gallery.view.MultimediaElementDetailView;

/**
 * The activity which acts as a presenter layer object for the detail screen of the app.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MultimediaElementDetailPresenter extends AppCompatActivity {

    /**
     * The constant EXTRA_MULTIMEDIA_ELEMENT_ID for passing a {@link MultimediaElement#id} to this activity.
     */
    public static final String EXTRA_MULTIMEDIA_ELEMENT_ID = MultimediaElementDetailPresenter.class
            .getCanonicalName() + ".MULTIMEDIA_ELEMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_MULTIMEDIA_ELEMENT_ID)) {
            long id = extras.getLong(EXTRA_MULTIMEDIA_ELEMENT_ID);

            MultimediaElementRepository repository = new MultimediaElementRepositorySqlLite(this);
            MultimediaElement multimediaElement = repository.getMultimediaElementById(id);

            MultimediaElementDetailView multimediaElementDetailView = new MultimediaElementDetailView(
                    this,
                    null,
                    multimediaElement
            );
            setContentView(multimediaElementDetailView.getRootView());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
