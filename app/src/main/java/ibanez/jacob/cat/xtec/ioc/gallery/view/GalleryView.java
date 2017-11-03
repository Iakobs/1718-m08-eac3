package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.view.View;

import ibanez.jacob.cat.xtec.ioc.gallery.model.Gallery;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */

public interface GalleryView {

    interface OnMultimediaElementClickListener {
        void onMultimediaElementClicked(long id);
    }

    interface OnRecordVideoClickListener {
        void onRecordVideoClicked();
    }

    interface OnTakePictureClickListener {
        void onTakePictureClicked();
    }

    interface OnMultimediaElementSwipeListener {
        void onMultimediaElementSwiped(long id);
    }

    void setMultimediaElementClickListener(OnMultimediaElementClickListener multimediaElementClickListener);

    void setTakePictureListener(OnTakePictureClickListener takePictureListener);

    void setRecordVideoListener(OnRecordVideoClickListener recordVideoListener);

    void setMultimediaElementSwipeListener(OnMultimediaElementSwipeListener multimediaElementSwipeListener);

    View getRootView();

    void enableCamera();

    void disableCamera();

    void bindGallery(Gallery gallery);
}
