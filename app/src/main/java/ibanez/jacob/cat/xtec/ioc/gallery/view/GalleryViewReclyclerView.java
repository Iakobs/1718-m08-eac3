package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
import ibanez.jacob.cat.xtec.ioc.gallery.model.Gallery;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.view.adapter.GalleryRecyclerViewAdapter;

/**
 * Class implementing the {@link GalleryView} with a Recycler View as an adapter.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class GalleryViewReclyclerView implements GalleryView {

    private static final float ALPHA_OPAQUE = 1f;
    private static final float ALPHA_DISABLED = 0.6f;

    private Context mContext;
    private View mRootView;
    private GalleryRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private OnTakePictureClickListener mTakePictureListener;
    private OnRecordVideoClickListener mRecordVideoListener;
    private OnMultimediaElementSwipeListener mMultimediaElementSwipeListener;
    private FloatingActionButton mFabTakePicture;
    private FloatingActionButton mFabRecordVideo;

    /**
     * Instantiates a new Gallery view reclycler view.
     *
     * @param context   the context
     * @param container the container
     */
    public GalleryViewReclyclerView(Context context, ViewGroup container) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.gallery_view, container);

        //get references to member variables
        mAdapter = new GalleryRecyclerViewAdapter();
        mFabTakePicture = mRootView.findViewById(R.id.fab_take_picture);
        mFabRecordVideo = mRootView.findViewById(R.id.fab_record_video);

        //create the recycler view and set its layout manager
        RecyclerView recyclerView = mRootView.findViewById(R.id.rv_gallery);
        recyclerView.setHasFixedSize(true); //size is equals for every element of the list
        mLayoutManager = new LinearLayoutManager(mRootView.getContext()); //default is set to vertical
        mLayoutManager.setReverseLayout(true); //the order is reversed, so the latest element is on top
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        //add a decorator to separate items
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

        //add item touch helper to add swipe functionality
        getItemTouchHelper().attachToRecyclerView(recyclerView);

        mFabTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTakePictureListener != null) {
                    mTakePictureListener.onTakePictureClicked();
                }
            }
        });
        mFabRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordVideoListener != null) {
                    mRecordVideoListener.onRecordVideoClicked();
                }
            }
        });
    }

    @Override
    public void setMultimediaElementClickListener(OnMultimediaElementClickListener multimediaElementClickListener) {
        mAdapter.setMultimediaElementClickListener(multimediaElementClickListener);
    }

    @Override
    public void setTakePictureClickListener(OnTakePictureClickListener takePictureListener) {
        this.mTakePictureListener = takePictureListener;
    }

    @Override
    public void setRecordVideoClickListener(OnRecordVideoClickListener recordVideoListener) {
        this.mRecordVideoListener = recordVideoListener;
    }

    @Override
    public void setMultimediaElementSwipeListener(OnMultimediaElementSwipeListener multimediaElementSwipeListener) {
        this.mMultimediaElementSwipeListener = multimediaElementSwipeListener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void enableCamera() {
        mFabTakePicture.setAlpha(ALPHA_OPAQUE);
        mFabTakePicture.setEnabled(true);
        mFabRecordVideo.setAlpha(ALPHA_OPAQUE);
        mFabRecordVideo.setEnabled(true);
    }

    @Override
    public void disableCamera() {
        mFabTakePicture.setAlpha(ALPHA_DISABLED);
        mFabTakePicture.setEnabled(false);
        mFabRecordVideo.setAlpha(ALPHA_DISABLED);
        mFabRecordVideo.setEnabled(false);
    }

    @Override
    public void bindGallery(Gallery gallery) {
        //set a new gallery
        mAdapter.setGallery(gallery);
        //scroll the view to the last item
        mLayoutManager.scrollToPosition(gallery.size() - 1);
    }

    private ItemTouchHelper getItemTouchHelper() {
        //this class provides swipe functionality to the recycler view
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; //do nothing
            }

            /**
             * This method is overriden to take a single {@link MultimediaElement} and notify the
             * listener in order to delete it.
             *
             * @param viewHolder The view holding the element
             * @param direction The direction of the swipe event
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (mMultimediaElementSwipeListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    MultimediaElement multimediaElement = mAdapter.getMultimediaElementByPosition(position);

                    //notify listener that a multimedia element has been swiped
                    mMultimediaElementSwipeListener.onMultimediaElementSwiped(multimediaElement.getId());
                }
            }

            /**
             * This method is overriden to draw a child view below the view holder, showing a trash
             * can icon.
             *
             * @param canvas The canvas
             * @param recyclerView The recycler view
             * @param viewHolder The view holder
             * @param dX The amount of horizontal displacement caused by user's action
             * @param dY The amount of vertical displacement caused by user's action
             * @param actionState The type of interaction on the View. Is either
             * {@link ItemTouchHelper#ACTION_STATE_DRAG} or {@link ItemTouchHelper#ACTION_STATE_SWIPE}.
             * @param isCurrentlyActive True if this view is currently being controlled by the
             *                          user or false it is simply animating back to its original state.
             */
            @Override
            public void onChildDraw(Canvas canvas,
                                    RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder,
                                    float dX,
                                    float dY,
                                    int actionState,
                                    boolean isCurrentlyActive
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_sweep_white_24dp);
                    Paint paint = new Paint();
                    paint.setColor(mContext.getColor(R.color.grey_swipe_background));

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    RectF background;
                    RectF iconDest;

                    if (dX > 0) {
                        background = new RectF(
                                (float) itemView.getLeft(),
                                (float) itemView.getTop(),
                                dX,
                                (float) itemView.getBottom()
                        );
                        iconDest = new RectF(
                                (float) itemView.getLeft() + width,
                                (float) itemView.getTop() + width,
                                (float) itemView.getLeft() + 2 * width,
                                (float) itemView.getBottom() - width
                        );
                    } else {
                        background = new RectF(
                                (float) itemView.getRight() + dX,
                                (float) itemView.getTop(),
                                (float) itemView.getRight(),
                                (float) itemView.getBottom()
                        );
                        iconDest = new RectF(
                                (float) itemView.getRight() - 2 * width,
                                (float) itemView.getTop() + width,
                                (float) itemView.getRight() - width,
                                (float) itemView.getBottom() - width
                        );
                    }
                    canvas.drawRect(background, paint);
                    canvas.drawBitmap(icon, null, iconDest, paint);
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
    }
}
