package ibanez.jacob.cat.xtec.ioc.gallery.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.view.adapter.MultimediaElementRecyclerViewAdapter;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class GalleryViewReclyclerView implements GalleryView {

    private static final float ALPHA_OPAQUE = 1f;
    private static final float ALPHA_DISABLED = 0.6f;

    private View mRootView;
    private MultimediaElementRecyclerViewAdapter mAdapter;
    private OnTakePictureListener mTakePictureListener;
    private OnRecordVideoListener mRecordVideoListener;
    private FloatingActionButton mFabTakePicture;
    private FloatingActionButton mFabRecordVideo;

    public GalleryViewReclyclerView(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.gallery_view, container);

        mAdapter = new MultimediaElementRecyclerViewAdapter();
        mFabTakePicture = mRootView.findViewById(R.id.fab_take_picture);
        mFabRecordVideo = mRootView.findViewById(R.id.fab_record_video);

        RecyclerView recyclerView = mRootView.findViewById(R.id.rv_gallery);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext()); //default is set to vertical
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

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
    public void bindMultimediaElements(List<MultimediaElement> multimediaElements) {
        mAdapter.setGallery(multimediaElements);
    }
}