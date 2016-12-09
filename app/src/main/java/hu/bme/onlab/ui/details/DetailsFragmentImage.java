package hu.bme.onlab.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.network.NetworkConfig;
import hu.bme.onlab.onlab2.R;

public class DetailsFragmentImage extends DetailsFragmentBase {

    private SliderLayout sliderShow;

    protected static Fragment newInstance(GetPostResponse getPostResponse) {
        return DetailsFragmentBase.newInstance(new DetailsFragmentImage(), getPostResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.setupArguments();

        View rootView = inflater.inflate(R.layout.fragment_details_image, container, false);

        sliderShow = (SliderLayout) rootView.findViewById(R.id.slider);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        for (Long id : getPostResponse.getImageIds()) {
            DefaultSliderView slideView = new DefaultSliderView(getContext());
            slideView.image(NetworkConfig.host + "post/downloadImage/" + id);
            sliderShow.addSlider(slideView);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        sliderShow.stopAutoCycle();
        super.onDestroy();
    }
}
