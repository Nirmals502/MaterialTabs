package info.androidhive.materialtabs.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import info.androidhive.materialtabs.adapter.Product_list_listview;
import info.androidhive.materialtabs.adapter.view_cart_adapter;


public class view_cart extends AppCompatActivity {
    ListView Lv_product_listview;
    ImageView Img_Back;
    private ProgressDialog pDialog;
    String status = "", Message, Access_tocken, Device_id;
    String ID, Access_tockenm, Device_idd, totalQuantity, totalPrice;
    String Cat_name;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    TextView Txt_tittle, Total_price;
    SharedPreferences shared;
    Button Btn_Checkout;
    String Str_order_id, Str_transatn_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        Lv_product_listview = (ListView) findViewById(R.id.LV_product_lv);
        Txt_tittle = (TextView) findViewById(R.id.textView4);
        Img_Back = (ImageView) findViewById(R.id.imageView6);
        Btn_Checkout = (Button) findViewById(R.id.button6);
        Total_price = (TextView) findViewById(R.id.textView10);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
        Access_tockenm = (shared.getString("Acess_tocken", "nologin"));
        Device_idd = (shared.getString("device_id", "nologin"));
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(view_cart.this, CustomViewIconTextTabsActivity.class);

                startActivity(i1);
                finish();

                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_right);


            }
        });
        Btn_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Str_order_id = List_Subscription.get(0).get("order_id");
                    new Check_out().execute();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }
        });
        new viewcart_background_task().execute();
        Lv_product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // RelativeLayout rl = (RelativeLayout)view.getParent();
                View parentRow = (View) view.getParent();


                Button Btn_edit = (Button) parentRow.findViewById(R.id.button4);
                Btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view_cart.this, "clicked:", Toast.LENGTH_LONG).show();
                    }
                });
//                Intent i1 = new Intent(getActivity(), Detail_screen.class);
//                getActivity().startActivity(i1);
//                String id_ = List_Subscription.get(position).get("id");
//                String Category_name = List_Subscription.get(position).get("Category_name");
//                Intent i1 = new Intent(view_cart.this, Detail_screen.class);
//                i1.putExtra("id", id_);
//                i1.putExtra("Cat_Name", Category_name);
//                startActivity(i1);
//                overridePendingTransition(R.anim.slide_in_left,
//                        R.anim.slide_out_left);
            }
        });
    }

    private class viewcart_background_task extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(view_cart.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            List_Subscription.clear();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.VIEW_CART,
                    ServiceHandler.POST, nameValuePairs, Access_tockenm, Device_idd);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                JSONObject Json_category = null;

                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jArr = null;
                try {
                    String Str_response = jsonObj.getString("data");
                    Json_category = new JSONObject(Str_response);
                    Str_response = Json_category.getString("cart");
                    totalQuantity = Json_category.getString("totalQuantity");
                    totalPrice = Json_category.getString("totalPrice");
                    jArr = new JSONArray(Str_response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    status = jsonObj.getString("status");
                    status = jsonObj.getString("status");

                    Message = jsonObj.getString("message");

//
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.contentEquals("true")) {
                    try {


                        for (int count = 0; count < jArr.length(); count++) {
                            JSONObject jsonObjj = null;
                            try {
                                jsonObjj = jArr.getJSONObject(count);
                                String image = jsonObjj.getString("media_thumb_url");
                                String category_name = jsonObjj.getString("name");


                                String id = jsonObjj.getString("id");
                                String order_id = jsonObjj.getString("order_id");
                                String price = jsonObjj.getString("price");
                                String color = jsonObjj.getString("color");
                                String pid = jsonObjj.getString("pid");
                                String quantity = jsonObjj.getString("quantity");
                                //String pid = jsonObjj.getString("pid");
                                HashMap<String, String> Search_result = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                Search_result.put("image", image);
                                Search_result.put("Category_name", category_name);


                                Search_result.put("id", id);
                                Search_result.put("order_id", order_id);
                                Search_result.put("price", price);
                                Search_result.put("color", color);
                                Search_result.put("pid", pid);
                                Search_result.put("quantity", quantity);


                                // adding contact to contact list
                                List_Subscription.add(Search_result);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();
                    }
                    // Getting JSON Array node
                    // JSONArray array1 = null;

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
                view_cart_adapter adapter = new view_cart_adapter(view_cart.this,
                        List_Subscription
                );
                Lv_product_listview.setAdapter(adapter);
                Total_price.setText("Total Price: " + "$" + totalPrice);
            } else {

            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Check_out extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(view_cart.this);
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
            nameValuePairs.add(new BasicNameValuePair("order_id", Str_order_id));


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.CHECKOUT,
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
                    String str = jsonObj.getString("data");

                    Message = jsonObj.getString("message");
                    if (status.contentEquals("true")) {
                        JSONObject jsonObjj = null;
                        jsonObjj = jsonObj.getJSONObject("data");

                        Str_transatn_id = jsonObjj.getString("txn_id");
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
               // Toast.makeText(view_cart.this, Str_transatn_id, Toast.LENGTH_LONG).show();
                SharedPreferences shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("trnsacton_id", Str_transatn_id);
                editor.putString("Order_id", Str_order_id);
                editor.putString("Total_price", totalPrice);

                editor.commit();
                Intent i1 = new Intent(view_cart.this, Shipping_adress.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                //Toast.makeText(context, Message, Toast.LENGTH_LONG).show();

            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}