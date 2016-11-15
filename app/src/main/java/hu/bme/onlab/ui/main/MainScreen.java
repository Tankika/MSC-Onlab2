package hu.bme.onlab.ui.main;

import java.util.List;

import hu.bme.onlab.model.post.Post;

public interface MainScreen {

    void refreshPostList(List<Post> posts);

    void startLoading();

    void stopLoading();
}
