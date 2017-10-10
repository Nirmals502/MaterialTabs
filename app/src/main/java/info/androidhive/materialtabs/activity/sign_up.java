package info.androidhive.materialtabs.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;

public class sign_up extends Activity {
    TextView Txt_signin;
    EditText Email, Edittxt_password, Edttxt_name, Edttxt_confirm_password;
    Button Btn_Signup;
    private ProgressDialog pDialog;
    String status, Message;
    String Str_name, Str_email, Str_password;
    String Device_id, Acess_tocken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        Txt_signin = (TextView) findViewById(R.id.textView);
        Btn_Signup = (Button) findViewById(R.id.button2);

        Email = (EditText) findViewById(R.id.editText2);
        Edittxt_password = (EditText) findViewById(R.id.editText);
        Edttxt_name = (EditText) findViewById(R.id.editText1);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(sign_up.this, Login_activity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });
        Btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                Btn_Signup.startAnimation(buttonClick);
                // final String email = emailValidate.getText().toString().trim();
                if (Edttxt_name.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edttxt_name.startAnimation(anm);
                } else if (Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Email.startAnimation(anm);
                } else if (!Email.getText().toString().matches(emailPattern)) {
                    Animation anm = Shake_Animation();
                    Email.startAnimation(anm);
                    Email.setError("Invalid email address");
                } else if (Edittxt_password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edittxt_password.startAnimation(anm);
                } else {
//                    Intent i1 = new Intent(Login_screen.this, MainActivity.class);
//                    startActivity(i1);
//                    finish();
//                    overridePendingTransition(R.anim.slide_in_left,
//                            R.anim.slide_out_left);
                    Str_name = Edttxt_name.getText().toString();
                    Str_email = Email.getText().toString();
                    Str_password = Edittxt_password.getText().toString();
                    new signup().execute();
                }
            }
        });
        // onClick of button perform this simplest code.


    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class signup extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(sign_up.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("email", Str_email));
            nameValuePairs.add(new BasicNameValuePair("password", Str_password));
            nameValuePairs.add(new BasicNameValuePair("device_id", "dfdgrrfndn458u348rnienfen"));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", "6.0"));
            nameValuePairs.add(new BasicNameValuePair("hardware", "Samsung"));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1"));


            String jsonStr = sh.makeServiceCall(SERVER.REGISTER,
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
                        JSONArray jsonObj_data = null;
                        jsonObj_data = jsonObj.getJSONArray("data");
                        for (int l = 0; l < jsonObj_data.length(); l++) {
                            JSONObject jsonObjjJ = null;
                            try {
                                jsonObjjJ = jsonObj_data.getJSONObject(l);
                                Device_id = jsonObjjJ.getString("device_id");
                                Acess_tocken = jsonObjjJ.getString("access_token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

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

                //  Toast.makeText(sign_up.this, status, Toast.LENGTH_LONG).show();

                SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Acess_tocken", Acess_tocken);
                editor.putString("device_id", Device_id);
                editor.commit();
                //Toast.makeText(Login_screen.this, status, Toast.LENGTH_LONG).show();

                Intent i1 = new Intent(sign_up.this, Login_activity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            } else {
                Toast.makeText(sign_up.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
