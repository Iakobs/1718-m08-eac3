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

    interface OnRecordVideoListener {
        void onVideoRecorded();
    }

    interface OnTakePictureListener {
        void onPictureTaken();
    }

    interface OnMultimediaElementSwipeListener {
        void onMultimediaElementSwiped(long id);
    }

    void setMultimediaElementClickListener(OnMultimediaElementClickListener multimediaElementClickListener);

    void setTakePictureListener(OnTakePictureListener takePictureListener);

    void setRecordVideoListener(OnRecordVideoListener recordVideoListener);

    void setMultimediaElementSwipeListener(OnMultimediaElementSwipeListener multimediaElementSwipeListener);

    View getRootView();

    void enableCamera();

    void disableCamera();

    void bindGallery(Gallery gallery);
}
