package info.androidhive.materialtabs.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;

public class Shipping_adress extends AppCompatActivity {
    EditText Name, Email, Adress, City, Province, Zip_code, Phone_number;
    String status = "", Message, Access_tocken, Device_id, Str_Description, Category_name, Str_name, Str_Email, Str_Adress, Str_City, Str_Province, Str_Zip_code, Str_phone, order_id;
    Button Btn_continue;
    String Str_ng_Country;
    private ProgressDialog pDialog;
    SharedPreferences shared;
    Spinner citizenship;
    ImageView Img_Back;
    String Str_transaction_id;
    private static final String TAG = "paymentExample";
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;



    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AQ12GcNoUXCyYUJXOIhFv69-T5Knl57X5kk3P6ht1Y26tVycU_GJFS3-BxXLRQX6eA9n_NjVJj4wakR8";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    String Price, Category, Payment_satus,Confirm_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_adress);
        Locale[] locale = Locale.getAvailableLocales();
        final ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
        Access_tocken = (shared.getString("Acess_tocken", "nologin"));
        Device_id = (shared.getString("device_id", "nologin"));
        order_id = (shared.getString("Order_id", "nologin"));
        Price = (shared.getString("Total_price", "nologin"));
        Str_transaction_id = (shared.getString("trnsacton_id", "nologin"));
        citizenship = (Spinner) findViewById(R.id.editText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);

        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_ng_Country = countries.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent i = getIntent();
        //  Price = i.getStringExtra("price");
        Category = i.getStringExtra("Category");
        Name = (EditText) findViewById(R.id.editText1);
        Email = (EditText) findViewById(R.id.editText2);
        Adress = (EditText) findViewById(R.id.editText3);
        City = (EditText) findViewById(R.id.editText4);
        Province = (EditText) findViewById(R.id.editText5);
        Zip_code = (EditText) findViewById(R.id.editText6);
        Phone_number = (EditText) findViewById(R.id.editText7);
        Btn_continue = (Button) findViewById(R.id.button2);
        Img_Back = (ImageView) findViewById(R.id.imageView6);
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(Shipping_adress.this, view_cart.class);

                startActivity(i1);
                finish();

                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_right);


            }
        });
        new Get_adress().execute();

        Btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Name.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Name.startAnimation(anm);
                    //citizenship.setPrompt("rgrgrg");
                } else if (Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Email.startAnimation(anm);

                } else if (!Email.getText().toString().matches(emailPattern)) {
                    Animation anm = Shake_Animation();
                    Email.startAnimation(anm);
                    Email.setError("Invalid email address");
                } else if (Adress.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Adress.startAnimation(anm);

                } else if (City.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    City.startAnimation(anm);

                } else if (Province.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Province.startAnimation(anm);

                } else if (Zip_code.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Zip_code.startAnimation(anm);

                } else if (Phone_number.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Phone_number.startAnimation(anm);

                } else {
                    Str_name = Name.getText().toString();
                    Str_Email = Email.getText().toString();
                    Str_Adress = Adress.getText().toString();
                    Str_City = City.getText().toString();
                    Str_Province = Province.getText().toString();
                    Str_Zip_code = Zip_code.getText().toString();
                    Str_phone = Phone_number.getText().toString();
//                    Intent intent = new Intent(Shipping_adress.this, PayPalService.class);
//                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                    startService(intent);
//
//                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//
//                    Intent intent1 = new Intent(Shipping_adress.this, PaymentActivity.class);
//
//                    // send the same configuration for restart resiliency
//                    intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//                    intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//                    startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
                    new Set_update_adress().execute();
//
                }
            }
        });

    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(Price), "USD", "SkyMobile",
                paymentIntent);
    }

    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        PayPalItem[] items =
                {
                        new PayPalItem("sample item #1", 2, new BigDecimal("87.50"), "USD",
                                "sku-12345678"),
                        new PayPalItem("free sample item #2", 1, new BigDecimal("0.00"),
                                "USD", "sku-zero-price"),
                        new PayPalItem("sample item #3 with a longer name", 6, new BigDecimal("37.99"),
                                "USD", "sku-33333")
                };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("7.21");
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "sample item", paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        displayResultText("PaymentConfirmation info received from PayPal");
                        Payment_satus = "1";
                        Confirm_code = confirm.getPayment().toJSONObject().toString(4);
                        new Update_Payment().execute();


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
                Payment_satus = "2";
                new Update_Payment().execute();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Payment_satus = "2";
                new Update_Payment().execute();
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Future Payment code received from PayPal");
                        Payment_satus = "1";
                        Confirm_code=authorization_code;
                        new Update_Payment().execute();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
                Payment_satus = "2";
                new Update_Payment().execute();
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Payment_satus = "2";
                new Update_Payment().execute();
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Profile Sharing code received from PayPal");
                        Payment_satus = "1";
                        Confirm_code=auth.toJSONObject().toString(4);
                        new Update_Payment().execute();
                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
                displayResultText("The user canceled.");
                Payment_satus = "2";
                new Update_Payment().execute();
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Payment_satus = "2";
                new Update_Payment().execute();
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    /*
     * Add app-provided shipping address to payment
     */
    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
        paypalPayment.providedShippingAddress(shippingAddress);
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(Shipping_adress.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(Shipping_adress.this, PayPalProfileSharingActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    }

    protected void displayResultText(String result) {
        // ((TextView)findViewById(R.id.txtResult)).setText("Result : " + result);
        Toast.makeText(
                getApplicationContext(),
                result, Toast.LENGTH_LONG)
                .show();
        Intent i1 = new Intent(Shipping_adress.this, CustomViewIconTextTabsActivity.class);

        startActivity(i1);

        finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);

    }

    private class Get_adress extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Shipping_adress.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.GET_ADRESS,
                    ServiceHandler.POST, nameValuePairs, Access_tocken, Device_id);

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
                    String str = jsonObj.getString("data");
                    JSONObject jsonObjj = null;
                    jsonObjj = jsonObj.getJSONObject("data");


                    Str_name = jsonObjj.getString("name");
                    Str_Email = jsonObjj.getString("email");
                    Str_Adress = jsonObjj.getString("address");
                    Str_City = jsonObjj.getString("city");
                    Str_ng_Country = jsonObjj.getString("country");
                    Str_Zip_code = jsonObjj.getString("zipcode");
                    Str_phone = jsonObjj.getString("contact");

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
//                Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(Shipping_adress.this, PayPalService.class);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                startService(intent);
//
//                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//
//                Intent intent1 = new Intent(Shipping_adress.this, PaymentActivity.class);
//
//                // send the same configuration for restart resiliency
//                intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//                intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//                startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
                //  Toast.makeText(sign_up.this, status, Toast.LENGTH_LONG).show();


//                Intent i1 = new Intent(Shipping_adress.this, Login_activity.class);
//                startActivity(i1);
//                finish();
//                overridePendingTransition(R.anim.slide_in_left,
//                        R.anim.slide_out_left);
                // EditText Name, Email, Adress, City, Province, Zip_code, Phone_number;
                Name.setText(Str_name);
                Email.setText(Str_Email);
                Adress.setText(Str_Adress);
                City.setText(Str_City);
                Zip_code.setText(Str_Zip_code);
                Phone_number.setText(Str_phone);
                citizenship.setPrompt(Str_ng_Country);
//                Name.setText(Str_name);
//                Str_name = jsonObjj.getString("name");
//                Str_Email = jsonObjj.getString("email");
//                Str_Adress = jsonObjj.getString("address");
//                Str_City = jsonObjj.getString("city");
//                Str_ng_Country = jsonObjj.getString("country");
//                Str_Zip_code = jsonObjj.getString("zipcode");
//                Str_phone = jsonObjj.getString("contact");

            } else {
                //Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Set_update_adress extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Shipping_adress.this);
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
            nameValuePairs.add(new BasicNameValuePair("name", Str_name));
            nameValuePairs.add(new BasicNameValuePair("email", Str_Email));
            nameValuePairs.add(new BasicNameValuePair("address", Str_Adress));
            nameValuePairs.add(new BasicNameValuePair("city", Str_City));
            // nameValuePairs.add(new BasicNameValuePair("city", Str_City));
            nameValuePairs.add(new BasicNameValuePair("country", Str_ng_Country));
            nameValuePairs.add(new BasicNameValuePair("zipcode", Str_Zip_code));
            nameValuePairs.add(new BasicNameValuePair("contact", Str_phone));
            nameValuePairs.add(new BasicNameValuePair("order_id", order_id));
            nameValuePairs.add(new BasicNameValuePair("sameadd", "1"));


            String jsonStr = sh.makeServiceCall_withHeader(SERVER.SET_UPDATE,
                    ServiceHandler.POST, nameValuePairs, Access_tocken, Device_id);

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
                Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Shipping_adress.this, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(intent);

                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);


                Intent intent1 = new Intent(Shipping_adress.this, PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
                //  Toast.makeText(sign_up.this, status, Toast.LENGTH_LONG).show();


//                Intent i1 = new Intent(Shipping_adress.this, Login_activity.class);
//                startActivity(i1);
//                finish();
//                overridePendingTransition(R.anim.slide_in_left,
//                        R.anim.slide_out_left);
            } else {
                Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Update_Payment
            extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Shipping_adress.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
            nameValuePairs.add(new BasicNameValuePair("txn_id", Str_transaction_id));
            nameValuePairs.add(new BasicNameValuePair("status", Payment_satus));
            nameValuePairs.add(new BasicNameValuePair("payu_id", Confirm_code));

            String jsonStr = sh.makeServiceCall_withHeader(SERVER.UPDATE_PAYMENT,
                    ServiceHandler.POST, nameValuePairs, Access_tocken, Device_id);

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
                    String str = jsonObj.getString("data");
                    JSONObject jsonObjj = null;


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
                Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();
//                Intent i1 = new Intent(Shipping_adress.this, Login_activity.class);
//                startActivity(i1);
//                finish();
//                overridePendingTransition(R.anim.slide_in_left,
//                        R.anim.slide_out_left);
                // EditText Name, Email, Adress, City, Province, Zip_code, Phone_number;

            } else {
                Toast.makeText(Shipping_adress.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
