package task.jack.me.shanbay.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import task.jack.me.shanbay.ImageViewHolder;
import task.jack.me.shanbay.model.ImageModel;

/**
 * Created by zjchai on 2016/12/12.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<ImageModel> data = new ArrayList<>();

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(new ImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(@NonNull ImageModel imageModel) {
        data.add(imageModel);
    }

    public void clear() {
        data.clear();
    }
}
