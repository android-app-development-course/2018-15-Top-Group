package com.example.liyixun.TopGroup;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context mcontext;
    private List<Gallery> mGalleryList;

    static class  ViewHolder extends RecyclerView.ViewHolder {
        View galleryview;
        CardView cardView;
        ImageView gallery_image;
        TextView gallery_title;
        CircleImageView gallery_author_icon;
        TextView getGallery_author_name;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            gallery_image = (ImageView) view.findViewById(R.id.gallery_image);
            gallery_title = (TextView) view.findViewById(R.id.gallery_title);
            gallery_author_icon = (CircleImageView) view.findViewById(R.id.gallery_author_icon);
            getGallery_author_name = (TextView) view.findViewById(R.id.gallery_author_name);
        }
    }

    public GalleryAdapter(List<Gallery> galleryList) {
        mGalleryList = galleryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        if ( mcontext == null ) {
            mcontext = parent.getContext();
        }

        View view = LayoutInflater.from(mcontext).inflate(R.layout.gallery_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        //点击
        holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int position = holder.getAdapterPosition();
               Gallery gallery = mGalleryList.get(position);
               Toast.makeText(v.getContext(),"you clicked image" + gallery.getTitle(),Toast.LENGTH_LONG).show();
           }
        });

        //长按
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Gallery gallery = mGalleryList.get(position);
                Toast.makeText(v.getContext(),"you longclicked image" + gallery.getTitle(),Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gallery gallery = mGalleryList.get(position);
        //holder.gallery_image.setImageResource(gallery.getImageId());
        Glide.with(mcontext).load(gallery.getImageId()).into(holder.gallery_image);
        holder.gallery_title.setText(gallery.getTitle());
        Glide.with(mcontext).load(gallery.getUserimageId()).into(holder.gallery_author_icon);
        holder.getGallery_author_name.setText(gallery.getAuthor());
    }

    @Override
    public int getItemCount(){
        return mGalleryList.size();
    }
}

/*public class GalleryAdapter extends ArrayAdapter<Gallery>{
    private int resourceId;
    public GalleryAdapter(Context context, int textViewResourceId, List<Gallery> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        Gallery gallery = getItem(position);
        View view;
        if ( converView == null ) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }
        else {
            view = converView;
        }
        ImageView gallery_image = (ImageView) view.findViewById(R.id.gallery_image);
        TextView gallery_title = (TextView) view.findViewById(R.id.gallery_title);
        gallery_image.setImageResource(gallery.getImageId());
        gallery_title.setText(gallery.getTitle());
        return view;
    }
    class ViewHolder {
        ImageView gallery_image;
        TextView gallery_title;
    }
}*/


