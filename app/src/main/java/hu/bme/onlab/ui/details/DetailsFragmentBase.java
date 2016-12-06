package hu.bme.onlab.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import hu.bme.onlab.model.post.GetPostResponse;

public abstract class DetailsFragmentBase extends Fragment {

    public final static String ARG_POST = "ARG_POST";

    protected GetPostResponse getPostResponse;

    protected static Fragment newInstance(Fragment fragment, GetPostResponse getPostResponse) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, getPostResponse);
        fragment.setArguments(args);
        return fragment;
    }

    protected final void setupArguments() {
        getPostResponse = (GetPostResponse) getArguments().getSerializable(ARG_POST);
    }
}
