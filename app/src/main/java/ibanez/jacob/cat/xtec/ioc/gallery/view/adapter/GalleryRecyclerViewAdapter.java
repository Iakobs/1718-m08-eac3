package ibanez.jacob.cat.xtec.ioc.gallery.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ibanez.jacob.cat.xtec.ioc.gallery.R;
import ibanez.jacob.cat.xtec.ioc.gallery.model.Gallery;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElement;
import ibanez.jacob.cat.xtec.ioc.gallery.model.MultimediaElementType;
import ibanez.jacob.cat.xtec.ioc.gallery.view.GalleryView;

/**
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class GalleryRecyclerViewAdapter extends
        RecyclerView.Adapter<GalleryRecyclerViewAdapter.MultimediaElementViewHolder> {

    private Gallery mGallery;
    private GalleryView.OnMultimediaElementClickListener mMultimediaElementClickListener;

    @Override
    public MultimediaElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multimedia_element, parent, false);
        return new MultimediaElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultimediaElementViewHolder holder, int position) {
        MultimediaElement multimediaElement = mGallery.get(position);

        if (multimediaElement.getType() == MultimediaElementType.PICTURE.getType()) {
            holder.mImageViewType.setImageResource(R.drawable.ic_image_black_24dp);
        } else if (multimediaElement.getType() == MultimediaElementType.VIDEO.getType()) {
            holder.mImageViewType.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        } else {
            holder.mImageViewType.setImageResource(R.drawable.ic_image_black_24dp);
        }

        holder.mTextViewName.setText(multimediaElement.getName());
    }

    @Override
    public int getItemCount() {
        return mGallery != null && !mGallery.isEmpty() ? mGallery.size() : 0;
    }

    public void setGallery(Gallery gallery) {
        this.mGallery = gallery;
        this.notifyDataSetChanged();
    }

    public void setMultimediaElementClickListener(GalleryView.OnMultimediaElementClickListener multimediaElementClickListener) {
        this.mMultimediaElementClickListener = multimediaElementClickListener;
    }

    class MultimediaElementViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewType;
        private TextView mTextViewName;

        MultimediaElementViewHolder(View itemView) {
            super(itemView);

            mImageViewType = itemView.findViewById(R.id.iv_type);
            mTextViewName = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMultimediaElementClickListener != null) {
                        int position = getAdapterPosition();
                        MultimediaElement multimediaElement = mGallery.get(position);
                        long id = multimediaElement.getId();

                        mMultimediaElementClickListener.onMultimediaElementClicked(id);
                    }
                }
            });
        }
    }
}
