package info.androidhive.materialtabs.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.materialtabs.R;

public class viewPager_adapter extends PagerAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    private LayoutInflater inflater;
    private Context context;

    public viewPager_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
        this.context = context;
        this.subscriptionarray=Array_subscription;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return subscriptionarray.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        String Str_Pic_image = subscriptionarray.get(position).get("image");
       // myImage.setImageResource(subscriptionarray.get(position));
        Picasso.with(context).load(Str_Pic_image).into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}