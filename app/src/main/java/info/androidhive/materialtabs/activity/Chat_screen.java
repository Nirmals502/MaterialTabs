package info.androidhive.materialtabs.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
import info.androidhive.materialtabs.adapter.chat_adapter;


public class Chat_screen extends AppCompatActivity {
    ListView Lv_Chat;
    private ProgressDialog pDialog;
    SharedPreferences shared;
    String status = "", Message, Access_tocken, Device_id;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    EditText Edt_txt_message;
    Button Btn_Send;
    String Strng_message;
    ImageView Imgback;
    Handler myHandler = new Handler();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Lv_Chat = (ListView) findViewById(R.id.msgListView);
        Edt_txt_message = (EditText) findViewById(R.id.messageEditText);
        Btn_Send = (Button) findViewById(R.id.sendMessageButton);
        Imgback= (ImageView) findViewById(R.id.imageView6);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
        Access_tocken = (shared.getString("Acess_tocken", "nologin"));
        Device_id = (shared.getString("device_id", "nologin"));
        Imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler.removeCallbacksAndMessages(null);
                Intent i1 = new Intent(Chat_screen.this, CustomViewIconTextTabsActivity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });

        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edt_txt_message.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_txt_message.startAnimation(anm);
                } else {
                    Strng_message = Edt_txt_message.getText().toString();
                    Edt_txt_message.setText("");
                    new Send_message().execute();
                }
            }
        });

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Get_messages().execute();
              //  new Get_messages().execute();
                //Do something after 20 seconds
                //call the method which is schedule to call after 20 sec
            }
        }, 200000);
        new Get_messages().execute();

    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class Send_message extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Chat_screen.this);
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
            nameValuePairs.add(new BasicNameValuePair("message", Strng_message));


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.Send_support_message,
                    ServiceHandler.POST, nameValuePairs, Access_tocken, Device_id);

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
//                    String Str_message = Json_category.getString("messages");
//                    String Type = Json_category.getString("type");
//                    HashMap<String, String> Search_result = new HashMap<String, String>();
//
//                    // adding each child node to HashMap key => value
//
//
//                    Search_result.put("Message", Str_message);
//                    Search_result.put("type", Type);
//
//
//                    // adding contact to contact list
//                    List_Subscription.add(Search_result);

//                    Str_response = Json_category.getString("categories");
                    // jArr = new JSONArray(Str_response);
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
//                if (status.contentEquals("true")) {
//                    try {
//
//
//                        for (int count = 0; count < jArr.length(); count++) {
//                            JSONObject jsonObjj = null;
//                            try {
//                                jsonObjj = jArr.getJSONObject(count);
//                                String image = jsonObjj.getString("image");
//                                String category_name = jsonObjj.getString("category_name");
//
//
//                                String id = jsonObjj.getString("id");
//                                HashMap<String, String> Search_result = new HashMap<String, String>();
//
//                                // adding each child node to HashMap key => value
//                                Search_result.put("image", image);
//                                Search_result.put("Category_name", category_name);
//
//
//                                Search_result.put("id", id);
//
//
//                                // adding contact to contact list
//                                List_Subscription.add(Search_result);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (java.lang.NullPointerException e) {
//                        e.printStackTrace();
//                    }
                // Getting JSON Array node
                // JSONArray array1 = null;

                //     }

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
//                chat_adapter adapter = new chat_adapter(Chat_screen.this,
//                        List_Subscription
//                );
//                Lv_Chat.setAdapter(adapter);
                new Get_messages().execute();
            } else {

            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Get_messages extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Chat_screen.this);
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
            // nameValuePairs.add(new BasicNameValuePair("category", ID));


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.Get_support_messages,
                    ServiceHandler.POST, nameValuePairs, Access_tocken, Device_id);


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
                                String Str_message = jsonObjj.getString("messages");
                                String Type = jsonObjj.getString("type");
                                HashMap<String, String> Search_result = new HashMap<String, String>();

                                // adding each child node to HashMap key => value


                                Search_result.put("Message", Str_message);
                                Search_result.put("type", Type);


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
                chat_adapter adapter = new chat_adapter(Chat_screen.this,
                        List_Subscription
                );
                Lv_Chat.setAdapter(adapter);
            } else {

            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
