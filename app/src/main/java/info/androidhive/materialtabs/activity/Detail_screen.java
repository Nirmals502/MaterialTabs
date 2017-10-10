package info.androidhive.materialtabs.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.Service_handler.SERVER;
import info.androidhive.materialtabs.Service_handler.ServiceHandler;
import info.androidhive.materialtabs.adapter.Product_list_listview;
import info.androidhive.materialtabs.adapter.viewPager_adapter;
import info.androidhive.materialtabs.paypal.paypal_activity;
import me.relex.circleindicator.CircleIndicator;

public class Detail_screen extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    Button Btn_buy_now, Btn_color;
    Dialog dialog = null;
    String ID;
    String Cat_name;
    SharedPreferences shared;
    private ProgressDialog pDialog;
    String status = "", Message, Access_tocken, Device_id, Str_Description, Price, Category_name, Color, Access_tockenm, Device_idd;
    private static final Integer[] XMEN = {R.drawable.apple_screen, R.drawable.apple_parts, R.drawable.fix_motherboard, R.drawable.samsung_screen, R.drawable.repair_tools};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    private static final String TAG = "paymentExample";
    TextView Text_description, Text_price, Txt_tittle;
    ImageView Img_Back;
    ListView Spinner;
    String[] separated;
    String[] separated_color_code;
    int int_amount = 1;
    String amountGlobal, String_static_amount = "";
    String quantiity = "1";
    String Str_color_code="1";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AQL4xTOoE3zan-871fuh7-B0I4l0QvcNXqrG1Sg0QNTvjzFr11HGCGRPH8779jS7liK0wM90CwB6lDGG";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen_layout);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
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
        init();
    }

    private void init() {
//        for(int i=0;i<XMEN.length;i++)
//            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        Btn_buy_now = (Button) findViewById(R.id.button);
        Text_description = (TextView) findViewById(R.id.textView6);
        Text_price = (TextView) findViewById(R.id.textView7);
        Txt_tittle = (TextView) findViewById(R.id.textView4);
        Img_Back = (ImageView) findViewById(R.id.imageView6);
        Spinner = (ListView) findViewById(R.id.lv_color);
        Btn_color = (Button) findViewById(R.id.button3);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
        Access_tockenm = (shared.getString("Acess_tocken", "nologin"));
        Device_idd = (shared.getString("device_id", "nologin"));

        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i1 = new Intent(Detail_screen.this, Product_list.class);
//
//                startActivity(i1);
                finish();

//                overridePendingTransition(R.anim.slide_in_right,
//                        R.anim.slide_out_right);
            }
        });
        Btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner.setVisibility(View.VISIBLE);
            }
        });
        Spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spinner.setVisibility(View.GONE);

                Btn_color.setText(separated[position]);
                Str_color_code = separated_color_code[position];
            }
        });
        Btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i1 = new Intent(Detail_screen.this, paypal_activity.class);
//
//                startActivity(i1);
//
//                finish();
//                overridePendingTransition(R.anim.slide_in_left,
//                        R.anim.slide_out_left);
                Access_tocken = (shared.getString("login_status", "nologin"));
                // if (Access_tocken.contentEquals("loged_in")) {
                if (Access_tocken.contentEquals("loged_in")) {
                    // Txt_Vw_login.setText("Logout");

//                    Intent intent = new Intent(Detail_screen.this, PayPalService.class);
//                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                    startService(intent);
//
//                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//        /*
//         * See getStuffToBuy(..) for examples of some available payment options.
//         */
//
//                    Intent intent1 = new Intent(Detail_screen.this, PaymentActivity.class);
//
//                    // send the same configuration for restart resiliency
//                    intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//                    intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//                    startActivityForResult(intent1, REQUEST_CODE_PAYMENT);

                    // Txt_Vw_login.setText("Login/Signup");
//                    Intent i1 = new Intent(Detail_screen.this, Shipping_adress.class);
//                    i1.putExtra("price", Price);
//                    i1.putExtra("Category", Category_name);
//                    startActivity(i1);
//                    finish();
//
//                    overridePendingTransition(R.anim.slide_in_left,
//                            R.anim.slide_out_left);
                    Dialog(Price, Cat_name);
                } else {
                    Intent i1 = new Intent(Detail_screen.this, Login_activity.class);

                    startActivity(i1);
                    finish();

                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
            }


        });


        new Product_Detail_().execute();

    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(Price), "USD", Category_name,
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
        Intent intent = new Intent(Detail_screen.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(Detail_screen.this, PayPalProfileSharingActivity.class);

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
        Intent i1 = new Intent(Detail_screen.this, CustomViewIconTextTabsActivity.class);

        startActivity(i1);

        finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);

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


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
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

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
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

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
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

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Client Metadata ID from the SDK
        String metadataId = PayPalConfiguration.getClientMetadataId(this);

        Log.i("FuturePaymentExample", "Client Metadata ID: " + metadataId);

        // TODO: Send metadataId and transaction details to your server for processing with
        // PayPal...
        displayResultText("Client Metadata Id received from SDK");
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private class Product_Detail_ extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Detail_screen.this);
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
            nameValuePairs.add(new BasicNameValuePair("id", ID));


            String jsonStr = sh.makeServiceCall(SERVER.PRODUCT_DETAIL_,
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
                    Str_response = Json_category.getString("images");
                    jArr = new JSONArray(Str_response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    status = Json_category.getString("status");
                    Str_Description = Json_category.getString("summary");
                    Price = Json_category.getString("price");
                    Category_name = Json_category.getString("category_name");
                    Color = Json_category.getString("color");
                    // Message = Json_category.getString("message");

//
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // if (status.contentEquals("true")) {
                for (int count = 0; count < jArr.length(); count++) {
                    JSONObject jsonObjj = null;
                    try {
                        jsonObjj = jArr.getJSONObject(count);
                        String image = jsonObjj.getString("media_url");
                        //  String category_name = jsonObjj.getString("category_name");


                        String id = jsonObjj.getString("id");
                        HashMap<String, String> Search_result = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Search_result.put("image", image);
                        //  Search_result.put("Category_name", category_name);


                        Search_result.put("id", id);


                        // adding contact to contact list
                        List_Subscription.add(Search_result);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Getting JSON Array node
                // JSONArray array1 = null;

                //  }

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
//            Text_description=(TextView)findViewById(R.id.textView6);
//            Text_price=(TextView)findViewById(R.id.textView7);
//            Txt_tittle=(TextView)findViewById(R.id.textView4);
//            Str_Description= Json_category.getString("description");
//            Price= Json_category.getString("price");
//            Category_name= Json_category.getString("category_name");
            Text_description.setText(Str_Description);
            Text_price.setText(Price);
            Txt_tittle.setText(Category_name);
            mPager.setAdapter(new viewPager_adapter(Detail_screen.this, List_Subscription));
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
            // String spinner = "Fruit: they taste good";
            separated = Color.split(",");
            separated_color_code = Color.split(",");
            if (separated.length == 1) {
                String[] separated2 = new String[1];
                separated2[0] = "white";
                separated = separated2.clone();
            } else if (separated.length == 2) {
                String[] separated2 = new String[2];
                separated2[0] = "white";
                separated2[1] = "blue";
                separated = separated2.clone();
            } else if (separated.length == 3) {
                String[] separated2 = new String[3];
                separated2[0] = "white";
                separated2[1] = "blue";
                separated2[2] = "black";
                separated = separated2.clone();
            } else if (separated.length == 4) {
                String[] separated2 = new String[4];
                separated2[0] = "white";
                separated2[1] = "blue";
                separated2[2] = "black";
                separated2[3] = "gray";
                separated = separated2.clone();
            }

            // separated.r
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Detail_screen.this, android.R.layout.simple_spinner_item, separated);
            Spinner.setAdapter(adapter);

            // Auto start of viewpager
//            final Handler handler = new Handler();
//            final Runnable Update = new Runnable() {
//                public void run() {
//                    if (currentPage == List_Subscription.size()) {
//                        currentPage = 0;
//                    }
//                    mPager.setCurrentItem(currentPage++, true);
//                }
//            };
//            Timer swipeTimer = new Timer();
//            swipeTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, 2500, 2500);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Add_to_cart extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Detail_screen.this);
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
            nameValuePairs.add(new BasicNameValuePair("product_id", ID));
            nameValuePairs.add(new BasicNameValuePair("quantity", quantiity));
            nameValuePairs.add(new BasicNameValuePair("color", Str_color_code));


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

                //  Toast.makeText(sign_up.this, status, Toast.LENGTH_LONG).show();


                Intent i1 = new Intent(Detail_screen.this, CustomViewIconTextTabsActivity.class);
                startActivity(i1);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            } else {
                Toast.makeText(Detail_screen.this, Message, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private void Dialog(String amount, String tittle) {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        dialog = new Dialog(Detail_screen.this, R.style.DialoueBox);
        dialog.setContentView(R.layout.order_confirmation_dialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog.findViewById(R.id.btn_confirm);
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
                dialog.dismiss();
                new Add_to_cart().execute();
               // Toast.makeText(Detail_screen.this, "Successfully added to basket", Toast.LENGTH_LONG).show();
                // onStartTransaction();
                // new Regular_package.Sbscribe_data().execute();
            }
        });

    }
}