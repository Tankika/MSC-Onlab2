package hu.bme.onlab.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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

import hu.bme.onlab.model.post.Post;
import hu.bme.onlab.onlab2.R;
import hu.bme.onlab.ui.details.DetailsActivity;

class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    final private List<Post> posts = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Post post = posts.get(position);

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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.INTENT_EXTRA_POST_ID, post.getId().intValue());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        ImageView mapImage;
        TextView price;
        TextView city;
        TextView date;
        TextView category;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            mapImage = (ImageView) itemView.findViewById(R.id.post_map_thumbnail);
            price = (TextView) itemView.findViewById(R.id.post_price);
            city = (TextView) itemView.findViewById(R.id.post_city);
            date = (TextView) itemView.findViewById(R.id.post_date);
            category = (TextView) itemView.findViewById(R.id.post_category);
            cardView = (CardView) itemView.findViewById(R.id.post_cardview);
        }
    }

    List<Post> getPosts() {
        return posts;
    }
}
