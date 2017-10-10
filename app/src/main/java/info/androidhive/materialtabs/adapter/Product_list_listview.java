package info.androidhive.materialtabs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.materialtabs.R;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class Product_list_listview extends BaseAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public Product_list_listview(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
        this.subscriptionarray = Array_subscription;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return subscriptionarray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_adapter_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        try {
            //if(Str_profile_image!=null) {
            //img_loader.DisplayImage(fl,vh.Img_profilepic);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }
        String Str_Pic_image = subscriptionarray.get(position).get("image");
        String Str_tittle = subscriptionarray.get(position).get("Category_name");
        mViewHolder.Tittle.setText(Str_tittle);
        Picasso.with(context).load(Str_Pic_image).into(mViewHolder.Restaurant_image);
//        mViewHolder.Tittle.setText(Str_tittle);
//        if (Str_Pic_image.contentEquals("apple_screen")) {
//            Picasso.with(context).load(R.drawable.apple_screen).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("apple_parts")) {
//            Picasso.with(context).load(R.drawable.apple_parts).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("samsung_screen")) {
//            Picasso.with(context).load(R.drawable.samsung_screen).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("samsung_parts")) {
//            Picasso.with(context).load(R.drawable.samsung_parts).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("lgscreens")) {
//            Picasso.with(context).load(R.drawable.lgscreens).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("recycle_screens")) {
//            Picasso.with(context).load(R.drawable.recycle_screens).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("fix_motherboard")) {
//            Picasso.with(context).load(R.drawable.fix_motherboard).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("repair_tools")) {
//            Picasso.with(context).load(R.drawable.repair_tools).into(mViewHolder.Restaurant_image);
//        } else if (Str_Pic_image.contentEquals("equipment")) {
//            Picasso.with(context).load(R.drawable.equipment).into(mViewHolder.Restaurant_image);
//        }


        return convertView;
    }

    private class MyViewHolder {
        TextView Tittle;
        ImageView Restaurant_image;

        public MyViewHolder(View item) {

            Tittle = (TextView) item.findViewById(R.id.txt_tittle);
            Restaurant_image = (ImageView) item.findViewById(R.id.imageView5);

//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }
    }
}