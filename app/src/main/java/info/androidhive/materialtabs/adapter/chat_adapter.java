package info.androidhive.materialtabs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.materialtabs.R;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class chat_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public chat_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
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
            convertView = inflater.inflate(R.layout.chat_lv_xml_layout, parent, false);
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
        // String Str_Pic_image = subscriptionarray.get(position).get("image");
        String Str_message = subscriptionarray.get(position).get("Message");
        String Str_type = subscriptionarray.get(position).get("type");
        if (Str_type.contentEquals("0")) {
            mViewHolder.rlv_send.setVisibility(View.INVISIBLE);
            mViewHolder.Rlv_get.setVisibility(View.VISIBLE);
            mViewHolder.txt_get.setText(Str_message);
        } else if (Str_type.contentEquals("1")) {
            mViewHolder.rlv_send.setVisibility(View.VISIBLE);
            mViewHolder.Rlv_get.setVisibility(View.INVISIBLE);
            mViewHolder.txt_send.setText(Str_message);
        }


        return convertView;
    }

    private class MyViewHolder {
        TextView txt_send, txt_get;
        ImageView Restaurant_image;
        LinearLayout rlv_send, Rlv_get;

        public MyViewHolder(View item) {

            txt_send = (TextView) item.findViewById(R.id.message_text_send);
            txt_get = (TextView) item.findViewById(R.id.message_text);
            rlv_send = (LinearLayout) item.findViewById(R.id.Mssage_send);
            Rlv_get = (LinearLayout) item.findViewById(R.id.contentWithBackground);

//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }
    }
}