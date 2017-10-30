package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.content.Context;
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
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class GalleryViewReclyclerView implements GalleryView {

    private static final float ALPHA_OPAQUE = 1f;
    private static final float ALPHA_DISABLED = 0.6f;

    private View mRootView;
    private GalleryRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private OnTakePictureListener mTakePictureListener;
    private OnRecordVideoListener mRecordVideoListener;
    private OnMultimediaElementSwipeListener mMultimediaElementSwipeListener;
    private FloatingActionButton mFabTakePicture;
    private FloatingActionButton mFabRecordVideo;

    public GalleryViewReclyclerView(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.gallery_view, container);

        mAdapter = new GalleryRecyclerViewAdapter();
        mFabTakePicture = mRootView.findViewById(R.id.fab_take_picture);
        mFabRecordVideo = mRootView.findViewById(R.id.fab_record_video);

        RecyclerView recyclerView = mRootView.findViewById(R.id.rv_gallery);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mRootView.getContext()); //default is set to vertical
        mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        //add a decorator to separate items
        DividerItemDecoration decoration = new DividerItemDecoration(context, mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        //add item touch helper
        getItemTouchHelper().attachToRecyclerView(recyclerView);

        mFabTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTakePictureListener != null) {
                    mTakePictureListener.onPictureTaken();
                }
            }
        });
        mFabRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordVideoListener != null) {
                    mRecordVideoListener.onVideoRecorded();
                }
            }
        });
    }

    @Override
    public void setMultimediaElementClickListener(OnMultimediaElementClickListener multimediaElementClickListener) {
        mAdapter.setMultimediaElementClickListener(multimediaElementClickListener);
    }

    @Override
    public void setTakePictureListener(OnTakePictureListener takePictureListener) {
        this.mTakePictureListener = takePictureListener;
    }

    @Override
    public void setRecordVideoListener(OnRecordVideoListener recordVideoListener) {
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
        mAdapter.setGallery(gallery);
        mLayoutManager.scrollToPosition(gallery.size() - 1);
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (mMultimediaElementSwipeListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    MultimediaElement multimediaElement = mAdapter.getMultimediaElementByPosition(position);

                    mMultimediaElementSwipeListener.onMultimediaElementSwiped(multimediaElement.getId());
                }
            }
        });
    }
}
