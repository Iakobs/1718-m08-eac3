package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.view.View;

import ibanez.jacob.cat.xtec.ioc.gallery.model.Gallery;

/**
 * The interface Gallery view providing a contract for the view layer of the application.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public interface GalleryView {

    /**
     * This interface must be implemented by whatever class handling click events on a single element
     * of the list.
     */
    interface OnMultimediaElementClickListener {
        /**
         * Callback to be invoked when a specific element of the list has been clicked.
         *
         * @param id the id of the clicked element
         */
        void onMultimediaElementClicked(long id);
    }

    /**
     * This interface must be implemented by whatever class handling click events on the record video
     * button.
     */
    interface OnRecordVideoClickListener {
        /**
         * Callback invoked when the button is clicked.
         */
        void onRecordVideoClicked();
    }

    /**
     * This interface must be implemented by whatever class handling click events on the take picture
     * button.
     */
    interface OnTakePictureClickListener {
        /**
         * Callback invoked when the button is clicked.
         */
        void onTakePictureClicked();
    }

    /**
     * This interface must be implemented by whatever class handling swipe events on a single element
     * of the list.
     */
    interface OnMultimediaElementSwipeListener {
        /**
         * Callback to be invoked when a specific element of the list has been clicked.
         *
         * @param id the id of the swiped element
         */
        void onMultimediaElementSwiped(long id);
    }

    /**
     * Sets multimedia element click listener.
     *
     * @param multimediaElementClickListener the multimedia element click listener
     */
    void setMultimediaElementClickListener(OnMultimediaElementClickListener multimediaElementClickListener);

    /**
     * Sets take picture click listener.
     *
     * @param takePictureListener the take picture listener
     */
    void setTakePictureClickListener(OnTakePictureClickListener takePictureListener);

    /**
     * Sets record video click listener.
     *
     * @param recordVideoListener the record video listener
     */
    void setRecordVideoClickListener(OnRecordVideoClickListener recordVideoListener);

    /**
     * Sets multimedia element swipe listener.
     *
     * @param multimediaElementSwipeListener the multimedia element swipe listener
     */
    void setMultimediaElementSwipeListener(OnMultimediaElementSwipeListener multimediaElementSwipeListener);

    /**
     * Gets root view.
     *
     * @return the root view
     */
    View getRootView();

    /**
     * Enable camera.
     */
    void enableCamera();

    /**
     * Disable camera.
     */
    void disableCamera();

    /**
     * Bind gallery.
     *
     * @param gallery the gallery
     */
    void bindGallery(Gallery gallery);
}
