package com.macnicol.popularmoviesproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.ViewHolder> {

    private static final String BASE_IMG_URL = Constants.BASE_IMG_URL + Constants.SIZE_185;

    private Context mContext;
    private ArrayList<MovieItem> mDataArray;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MainGridAdapter(Context context, ArrayList<MovieItem> dataArray) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataArray = dataArray;
    }

    @Override
    public MainGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_main_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainGridAdapter.ViewHolder holder, int position) {
        String posterPath = mDataArray.get(position).getPosterPath();
        //---Adding placeholders and errors
        Picasso.with(mContext)
                .load(BASE_IMG_URL + posterPath)
                .placeholder(R.color.colorPrimary)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cell_grid_placeholder);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
