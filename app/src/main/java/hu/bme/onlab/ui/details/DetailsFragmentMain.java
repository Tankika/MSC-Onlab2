package hu.bme.onlab.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.network.NetworkSessionStore;
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

        setupDetailsSection(rootView);
        setupAdvertiserSection(rootView);

        TextView descriptionView = (TextView) rootView.findViewById(R.id.description);
        descriptionView.setText(getPostResponse.getDescription());

        return rootView;
    }

    private void setupDetailsSection(View rootView) {
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setText(getPostResponse.getTitle());

        TextView cityView = (TextView) rootView.findViewById(R.id.city);
        cityView.setText(getPostResponse.getCity());

        TextView postalCodeView = (TextView) rootView.findViewById(R.id.postal_code);
        postalCodeView.setText(getPostResponse.getPostalCode());

        TextView priceView = (TextView) rootView.findViewById(R.id.price);
        LinearLayout priceContainer = (LinearLayout) rootView.findViewById(R.id.price_container);
        String priceString = "";
        String formattedPriceMin = getPostResponse.getPriceMin() != null && getPostResponse.getPriceMin() != 0 ? NumberFormat.getInstance().format(getPostResponse.getPriceMin()) : "";
        String formattedPriceMax = getPostResponse.getPriceMax() != null && getPostResponse.getPriceMax() != 0 ? NumberFormat.getInstance().format(getPostResponse.getPriceMax()) : "";
        priceString += !formattedPriceMin.isEmpty() ? formattedPriceMin + " Ft-tól" : "";
        priceString += " ";
        priceString += !formattedPriceMax.isEmpty() ? formattedPriceMax + " Ft-ig" : "";
        priceString = priceString.trim();
        priceView.setText(priceString);
        if(priceString.isEmpty()) {
            priceContainer.setVisibility(View.GONE);
        } else {
            priceContainer.setVisibility(View.VISIBLE);
        }

        TextView categoryView = (TextView) rootView.findViewById(R.id.category);
        categoryView.setText(getPostResponse.getCategoryName());
    }

    private void setupAdvertiserSection(View rootView) {
        if(NetworkSessionStore.getUser() == null) {
            TextView advertiserPlaceholderView = (TextView) rootView.findViewById(R.id.advertiser_placeholder_text);
            advertiserPlaceholderView.setVisibility(View.VISIBLE);
        } else {
            LinearLayout advertiserDetailsSection = (LinearLayout) rootView.findViewById(R.id.advertiser_details);
            advertiserDetailsSection.setVisibility(View.VISIBLE);

            TextView nameView = (TextView) rootView.findViewById(R.id.name);
            nameView.setText(getPostResponse.getName());

            TextView phoneView = (TextView) rootView.findViewById(R.id.phone);
            phoneView.setText(getPostResponse.getPhone());
        }
    }
}
