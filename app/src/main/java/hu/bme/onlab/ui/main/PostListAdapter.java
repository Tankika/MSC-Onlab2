package hu.bme.onlab.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.bme.onlab.model.Post;
import hu.bme.onlab.onlab2.R;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    final private List<Post> posts = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.title.setText(post.getTitle());
        holder.category.setText(post.getCategoryName());
        holder.city.setText(post.getCity());

        Glide.with(holder.mapImage.getContext())
                .load(post.getMapUrl())
                .into(holder.mapImage);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date creationDate = new Date(post.getCreationDateTime().longValue());
        holder.date.setText(dateFormat.format(creationDate));

        String priceString = "";
        String formattedPriceMin = post.getPriceMin() != null ? NumberFormat.getInstance().format(post.getPriceMin()) : "";
        String formattedPriceMax = post.getPriceMax() != null ? NumberFormat.getInstance().format(post.getPriceMax()) : "";
        priceString += !formattedPriceMin.isEmpty() ? formattedPriceMin + " Ft-tól" : "";
        priceString += " ";
        priceString += !formattedPriceMax.isEmpty() ? formattedPriceMax + " Ft-ig" : "";
        priceString = priceString.trim();
        holder.price.setText(priceString);
        if(priceString.isEmpty()) {
            holder.price.setVisibility(View.GONE);
        } else {
            holder.price.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView mapImage;
        public TextView price;
        public TextView city;
        public TextView date;
        public TextView category;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            mapImage = (ImageView) itemView.findViewById(R.id.post_map_thumbnail);
            price = (TextView) itemView.findViewById(R.id.post_price);
            city = (TextView) itemView.findViewById(R.id.post_city);
            date = (TextView) itemView.findViewById(R.id.post_date);
            category = (TextView) itemView.findViewById(R.id.post_category);
        }
    }

    public List<Post> getPosts() {
        return posts;
    }
}
