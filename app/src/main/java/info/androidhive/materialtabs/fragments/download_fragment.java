package info.androidhive.materialtabs.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;
import info.androidhive.materialtabs.activity.Product_list;
import info.androidhive.materialtabs.adapter.Product_list_listview;


public class download_fragment extends Fragment {
    ListView Lv_product_listview;
    String Pdf_name;

    private ProgressDialog pDialog;
    String status = "", Message, Access_tocken, Device_id;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    String[] itemname ={
            "Price List 6/20",
            "New customer registration form",
            "ST-120",

    };
    public download_fragment() {
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
        return inflater.inflate(R.layout.download_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lv_product_listview = (ListView) view.findViewById(R.id.LV_product_lv);
//        Lv_product_listview.setListAdapter(new ArrayAdapter<String>(
//                this, R.layout.fragment_nine,
//                R.id.item,itemname));
        Lv_product_listview.setAdapter(new ArrayAdapter<String>(
                getActivity(), R.layout.fragment_nine,
                R.id.item,itemname));
        Lv_product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Pdf_name="price_list.pdf" ;
                }else  if(position==1){
                    Pdf_name="new_registration.pdf" ;
                }else  if(position==2){
                    Pdf_name="st.pdf" ;
                }
                CopyAssets();
            }
        });


    }
    private void CopyAssets() {

        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), Pdf_name);
        try {
            in = assetManager.open(Pdf_name);
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/"+Pdf_name),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
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
