package hu.bme.onlab.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.onlab2.R;

public class DetailsFragmentMain extends DetailsFragmentBase {

    public DetailsFragmentMain() {
    }

    protected static Fragment newInstance(GetPostResponse getPostResponse) {
        return DetailsFragmentBase.newInstance(new DetailsFragmentMain(), getPostResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.setupArguments();

        View rootView = inflater.inflate(R.layout.fragment_details_main, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getPostResponse.getTitle());

        return rootView;
    }
}
