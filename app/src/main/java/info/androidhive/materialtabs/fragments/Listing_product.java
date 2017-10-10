package info.androidhive.materialtabs.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;
import info.androidhive.materialtabs.activity.Product_list;
import info.androidhive.materialtabs.adapter.Product_list_listview;


public class Listing_product extends Fragment {
    ListView Lv_product_listview;

    private ProgressDialog pDialog;
    String status = "", Message, Access_tocken, Device_id;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();

    public Listing_product() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lv_product_listview = (ListView) view.findViewById(R.id.LV_product_lv);
//        HashMap<String, String> Explorer = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer.put("pic", "apple_screen");
//        Explorer.put("Tittle", "Apple Screens");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer);
//        HashMap<String, String> Explorer2 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer2.put("pic", "apple_parts");
//        Explorer2.put("Tittle", "Apple Parts");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer2);
//        HashMap<String, String> Explorer3 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer3.put("pic", "samsung_screen");
//        Explorer3.put("Tittle", "Samsung Screen");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer3);
//        HashMap<String, String> Explorer4 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer4.put("pic", "samsung_parts");
//        Explorer4.put("Tittle", "Samsung Parts");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer4);
//        HashMap<String, String> Explorer5 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer5.put("pic", "lgscreens");
//        Explorer5.put("Tittle", "LG Screens");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer5);
//
//        HashMap<String, String> Explorer6 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer6.put("pic", "recycle_screens");
//        Explorer6.put("Tittle", "Recycle & Exchange");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer6);
//        HashMap<String, String> Explorer7 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer7.put("pic", "fix_motherboard");
//        Explorer7.put("Tittle", "Fix Motherboard");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer7);
//
//        HashMap<String, String> Explorer8 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer8.put("pic", "repair_tools");
//        Explorer8.put("Tittle", "Repair Tool");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer8);
//
//        HashMap<String, String> Explorer9 = new HashMap<String, String>();
//
//        // adding each child node to HashMap key => value
//        Explorer9.put("pic", "equipment");
//        Explorer9.put("Tittle", "Equipment & Supplies");
//
//
//        // adding contact to contact list
//        List_Subscription.add(Explorer9);
//        Product_list_listview adapter = new Product_list_listview(getActivity(),
//                List_Subscription
//        );
//        Lv_product_listview.setAdapter(adapter);
        Lv_product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i1 = new Intent(getActivity(), Detail_screen.class);
//                getActivity().startActivity(i1);
                String id_ = List_Subscription.get(position).get("id");
                String Category_name = List_Subscription.get(position).get("Category_name");
                Intent i1 = new Intent(getActivity(), Product_list.class);
                i1.putExtra("id", id_);
                i1.putExtra("Cat_Name", Category_name);
                getActivity().startActivity(i1);
                getActivity().overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });
        new Product_list_().execute();
    }

    private class Product_list_ extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
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
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));


            String jsonStr = sh.makeServiceCall(SERVER.CATEGORY_LIST,
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
                    Json_category = new JSONObject(Str_response);
                    Str_response = Json_category.getString("categories");
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
                    for (int count = 0; count < jArr.length(); count++) {
                        JSONObject jsonObjj = null;
                        try {
                            jsonObjj = jArr.getJSONObject(count);
                            String image = jsonObjj.getString("cat_img");
                            String category_name = jsonObjj.getString("name");


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
                Product_list_listview adapter = new Product_list_listview(getActivity(),
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
