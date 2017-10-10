package info.androidhive.materialtabs.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;
import info.androidhive.materialtabs.activity.CustomViewIconTextTabsActivity;
import info.androidhive.materialtabs.activity.Detail_screen;
import info.androidhive.materialtabs.activity.view_cart;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class view_cart_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;
    Dialog dialog = null;
    private ProgressDialog pDialog;
    String quantiity = "1";
    SharedPreferences shared;
    int int_amount = 1;
    String amountGlobal, String_static_amount = "";
    String Strn_pid, color, Access_tockenm, Device_idd;
        String status = "", Message,ID;

    public view_cart_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_cart_layout, parent, false);
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
        shared = context.getSharedPreferences("Sky_mobile", context.MODE_PRIVATE);
        Access_tockenm = (shared.getString("Acess_tocken", "nologin"));
        Device_idd = (shared.getString("device_id", "nologin"));
        String Str_Pic_image = subscriptionarray.get(position).get("image");
        String Str_tittle = subscriptionarray.get(position).get("Category_name");
        mViewHolder.Tittle.setText(Str_tittle);
        mViewHolder.Qty.setText("Qty: " + subscriptionarray.get(position).get("quantity"));
        mViewHolder.Price.setText("Price: " + subscriptionarray.get(position).get("price"));
        mViewHolder.Tittle.setText(Str_tittle);
        mViewHolder.Btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Strn_pid = subscriptionarray.get(position).get("pid");
                color = subscriptionarray.get(position).get("color");
                ID =  subscriptionarray.get(position).get("id");
                Dialog(subscriptionarray.get(position).get("price"), subscriptionarray.get(position).get("Category_name"));
            }
        });
//        Tittle = (TextView) item.findViewById(R.id.txt_tittle);
//        Qty= (TextView) item.findViewById(R.id.textView5);
//        Price= (TextView) item.findViewById(R.id.textView8);
        Picasso.with(context).load(Str_Pic_image).into(mViewHolder.Restaurant_image);


        return convertView;
    }

    private class MyViewHolder {
        TextView Tittle, Qty, Price;
        ImageView Restaurant_image;
        Button Btn_edit,Remove_;

        public MyViewHolder(View item) {

            Tittle = (TextView) item.findViewById(R.id.txt_tittle);
            Qty = (TextView) item.findViewById(R.id.textView5);
            Price = (TextView) item.findViewById(R.id.textView8);
            Btn_edit = (Button) item.findViewById(R.id.button4);

            Restaurant_image = (ImageView) item.findViewById(R.id.imageView5);

//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }
    }

    private void Dialog(String amount, String tittle) {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        dialog = new Dialog(context, R.style.DialoueBox);
        dialog.setContentView(R.layout.view_cart_edit);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        Button Remove_ = (Button) dialog.findViewById(R.id.button5);
        Button img_close = (Button) dialog.findViewById(R.id.Btn_dismiss);
        TextView Txt_tittle = (TextView) dialog.findViewById(R.id.txtvw_tittle);
        final TextView Txt_vw_amount = (TextView) dialog.findViewById(R.id.textView4);
        ImageView increment = (ImageView) dialog.findViewById(R.id.nbinc1);
        ImageView decrement = (ImageView) dialog.findViewById(R.id.nbdec1);
        final TextView txt_amount = (TextView) dialog.findViewById(R.id.nbincdec);
        Txt_tittle.setText(tittle);
        String_static_amount = amount;
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 9) {
                    int_amount++;
                    int integer_amount = 0;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value = Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount;
                    String value_after_multiply = String.valueOf(integer_amount);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("$" + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;

                }

            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 1) {
                    int_amount--;
                    int integer_amount = 0;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value = Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount;
                    String value_after_multiply = String.valueOf(integer_amount);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("$" + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;
                }

            }
        });
        Txt_vw_amount.setText("$" + amount);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_amount = 1;
                dialog.cancel();
            }
        });
        otpconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Remove_cart().execute();

                // dialog.dismiss();
                //  new Detail_screen.Add_to_cart().execute();
                // Toast.makeText(Detail_screen.this, "Successfully added to basket", Toast.LENGTH_LONG).show();
                // onStartTransaction();
                // new Regular_package.Sbscribe_data().execute();
            }
        });
        Remove_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Remove_cartt().execute();
            }
        });

    }

    private class Add_to_cart extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("product_id", Strn_pid));
            nameValuePairs.add(new BasicNameValuePair("quantity", quantiity));
            nameValuePairs.add(new BasicNameValuePair("color", color));


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.ADD_TO_CART,
                    ServiceHandler.POST, nameValuePairs, Access_tockenm, Device_idd);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    status = jsonObj.getString("status");

                    Message = jsonObj.getString("message");
                    if (status.contentEquals("true")) {
                        JSONArray jsonObj_data = null;
                        jsonObj_data = jsonObj.getJSONArray("data");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            if (status.contentEquals("true")) {
                Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(context, view_cart.class);
                context.startActivity(i1);

            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Remove_cart extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("cid", ID));



            String jsonStr = sh.makeServiceCall_withHeader(SERVER.DELETE_CART,
                    ServiceHandler.POST, nameValuePairs, Access_tockenm, Device_idd);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    status = jsonObj.getString("status");

                    Message = jsonObj.getString("message");
                    if (status.contentEquals("true")) {
                        JSONArray jsonObj_data = null;
                        jsonObj_data = jsonObj.getJSONArray("data");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
           // if (status.contentEquals("true")) {
               // Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
                new Add_to_cart().execute();

          //  }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    private class Remove_cartt extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("cid", ID));



            String jsonStr = sh.makeServiceCall_withHeader(SERVER.DELETE_CART,
                    ServiceHandler.POST, nameValuePairs, Access_tockenm, Device_idd);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    status = jsonObj.getString("status");

                    Message = jsonObj.getString("message");
                    if (status.contentEquals("true")) {
                        JSONArray jsonObj_data = null;
                        jsonObj_data = jsonObj.getJSONArray("data");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            // if (status.contentEquals("true")) {
            // Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
            //Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(context, view_cart.class);
            context.startActivity(i1);

            //  }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}