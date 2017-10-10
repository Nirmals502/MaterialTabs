package info.androidhive.materialtabs.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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


public class Product_list extends AppCompatActivity {
    ListView Lv_product_listview;
    ImageView Img_Back;
    private ProgressDialog pDialog;
    String status = "", Message, Access_tocken, Device_id;
    String ID;
    String Cat_name;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    TextView Txt_tittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                ID = null;
                Cat_name = null;
            } else {
                ID = extras.getString("id");
                Cat_name = extras.getString("Cat_Name");
            }
        } else {
            ID = (String) savedInstanceState.getSerializable("id");
            Cat_name = (String) savedInstanceState.getSerializable("Cat_Name");
        }
        Lv_product_listview = (ListView) findViewById(R.id.LV_product_lv);
        Txt_tittle = (TextView) findViewById(R.id.textView4);
        Img_Back = (ImageView) findViewById(R.id.imageView6);
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });

        Txt_tittle.setText(Cat_name);
        new Product_list_().execute();
        Lv_product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i1 = new Intent(getActivity(), Detail_screen.class);
//                getActivity().startActivity(i1);
                String id_ = List_Subscription.get(position).get("id");
                String Category_name = List_Subscription.get(position).get("Category_name");
                Intent i1 = new Intent(Product_list.this, Detail_screen.class);
                i1.putExtra("id", id_);
                i1.putExtra("Cat_Name", Category_name);
                startActivity(i1);
               overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });
    }

    private class Product_list_ extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Product_list.this);
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
            nameValuePairs.add(new BasicNameValuePair("category", ID));


            String jsonStr = sh.makeServiceCall(SERVER.PRODUCT_LIST,
                    ServiceHandler.POST, nameValuePairs);

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
//                    Json_category = new JSONObject(Str_response);
//                    Str_response = Json_category.getString("categories");
                    jArr = new JSONArray(Str_response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
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
                            String image = jsonObjj.getString("image");
                            String category_name = jsonObjj.getString("category_name");


                            String id = jsonObjj.getString("id");
                            HashMap<String, String> Search_result = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            Search_result.put("image", image);
                            Search_result.put("Category_name", category_name);


                            Search_result.put("id", id);


                            // adding contact to contact list
                            List_Subscription.add(Search_result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }catch (java.lang.NullPointerException e){
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
                Product_list_listview adapter = new Product_list_listview(Product_list.this,
                        List_Subscription
                );
                Lv_product_listview.setAdapter(adapter);
            } else {

            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
