package hu.bme.onlab.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

        holder.getTitle().setText(post.getTitle());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView mapImage;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            mapImage = (ImageView) itemView.findViewById(R.id.post_map_thumbnail);
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getMapImage() {
            return mapImage;
        }
    }

    public List<Post> getPosts() {
        return posts;
    }
}
