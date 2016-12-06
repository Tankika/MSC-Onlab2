package hu.bme.onlab.ui.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;

import hu.bme.onlab.model.post.GetPostResponse;

public class DetailsSectionsPagerAdapter extends FragmentPagerAdapter {

    private GetPostResponse getPostResponse;

    public DetailsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public GetPostResponse getGetPostResponse() {
        return getPostResponse;
    }

    public void setGetPostResponse(GetPostResponse getPostResponse) {
        this.getPostResponse = getPostResponse;
    }

    @Override
    public Fragment getItem(int position) {
        if(getPostResponse != null) {
            switch (position) {
                case 0:
                    return DetailsFragmentMain.newInstance(getPostResponse);
                case 1:
                    return DetailsFragmentMap.newInstance(getPostResponse);
                case 2:
                    return DetailsActivity.PlaceholderFragment.newInstance(position + 1);
                default:
                    return null;
            }
        } else {
            return new Fragment();
        }
    }

    @Override
    public int getCount() {
        if(getPostResponse != null && getPostResponse.getImageIds().size() > 0) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Leírás";
            case 1:
                return "Térkép";
            case 2:
                return "Képek";
        }
        return null;
    }
}
