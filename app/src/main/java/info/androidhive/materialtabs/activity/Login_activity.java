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
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;

public class Login_activity extends AppCompatActivity {
    Button Btn_login;
    EditText Edt_Txt_Username, Edt_password;
    private ProgressDialog pDialog;
    String status, Message;
    String Device_id, Acess_tocken;
    String Str_name, Str_email, Str_password;
    ImageView Img_back;
    TextView Txt_sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Btn_login = (Button) findViewById(R.id.button2);
        Img_back =(ImageView) findViewById(R.id.imageView4);
        Edt_Txt_Username = (EditText) findViewById(R.id.editText1);
        Edt_password = (EditText) findViewById(R.id.editText2);
        Txt_sign_up=(TextView)  findViewById(R.id.textView);
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edt_Txt_Username.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_Txt_Username.startAnimation(anm);
                } else if (Edt_password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_password.startAnimation(anm);
                } else {


                    Str_email = Edt_Txt_Username.getText().toString();
                    Str_password = Edt_password.getText().toString();
                    new Login().execute();
                }

            }
        });
        Txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Login_activity.this, sign_up.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });
        Img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Login_activity.this, CustomViewIconTextTabsActivity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_right);
            }
        });
    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Login_activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("email", Str_email));
            nameValuePairs.add(new BasicNameValuePair("password", Str_password));
            nameValuePairs.add(new BasicNameValuePair("device_id", "dfdgrrfndn458u348rnienfen"));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", "6.0"));
            nameValuePairs.add(new BasicNameValuePair("hardware", "Samsung"));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1"));


            String jsonStr = sh.makeServiceCall(SERVER.LOGIN,
                    ServiceHandler.POST, nameValuePairs);

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
                        JSONObject jsonObj_data = null;
                        jsonObj_data = jsonObj.getJSONObject("data");
                        Device_id = jsonObj_data.getString("device_id");
                        Acess_tocken = jsonObj_data.getString("access_token");
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
                SharedPreferences shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Acess_tocken", Acess_tocken);
                editor.putString("login_status", "loged_in");
                editor.putString("device_id", Device_id);
                editor.commit();
                //Toast.makeText(Login_screen.this, status, Toast.LENGTH_LONG).show();

                Intent i1 = new Intent(Login_activity.this, CustomViewIconTextTabsActivity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            } else {
                Toast.makeText(Login_activity.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
