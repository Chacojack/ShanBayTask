package task.jack.me.shanbay;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import task.jack.me.shanbay.model.ImageModel;

/**
 * Created by zjchai on 2016/12/12.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView;
    }

    public void bind(ImageModel image){
        // TODO: 2016/12/12 load image
        Picasso.with(imageView.getContext()).load(image.getUrl()).into(imageView);
    }
}
