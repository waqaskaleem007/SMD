package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModels;
    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullName;
    private TextView fullAddress;
    private TextView pinCode;
    private Button continueButton;

    public static CartAdapter cartAdapter;

    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageView creditCart, cod;

    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingButton;
    private TextView orderId;
    private boolean success = true;
    private String order_id;
    public static boolean fromCart;
    private String name, mobileNo;
    public static boolean codOrderConfirmed = false;

    private FirebaseFirestore firebaseFirestore;
    public static boolean getQtyIds = true;
    private String paymentMethod = "PAYTM";

    OTPService dataService;
    Messenger messenger;
    //Messenger incomingMessenger = new Messenger(new IncomingHandler());
    boolean bound = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        ///loading Dialog
        loadingDialog = new Dialog(Objects.requireNonNull(DeliveryActivity.this));
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading Dialog

        ///payment Method Dialog
        paymentMethodDialog = new Dialog(Objects.requireNonNull(DeliveryActivity.this));
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        creditCart = paymentMethodDialog.findViewById(R.id.credit_card_button);
        cod = paymentMethodDialog.findViewById(R.id.cod_button);
        ////payment Method Dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIds = true;


        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_button);
        totalAmount = findViewById(R.id.total_cart_amount);
        continueButton = findViewById(R.id.cart_continue_btn);

        orderConfirmationLayout = findViewById(R.id.order_conformation_layout);
        continueShoppingButton = findViewById(R.id.continue_shopping_button);
        orderId = findViewById(R.id.order_id);


        fullName = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.adress);
        pinCode = findViewById(R.id.pincode);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartItemModels, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIds = false;
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "cod";
                placeOrderDetails();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allProductsAvailable = true;
                for (CartItemModel cartItemModel : cartItemModels) {
                    if (cartItemModel.isQtyError()) {
                        allProductsAvailable = false;
                    }
                }
                if (allProductsAvailable) {
                    paymentMethodDialog.show();
                }
            }
        });
        creditCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "CreditCard";
                placeOrderDetails();
            }

        });

        startDataSyncService();

    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Message message = Message.obtain(null, 0);
            // may send some data along in form of Bundle
            try {
                messenger.send(message);
            } catch (RemoteException ignored) {
            }
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private void startDataSyncService() {
        SharedPreferences preferences = getSharedPreferences("service", Context.MODE_PRIVATE);
        boolean started = preferences.getBoolean("started", false);
        SharedPreferences.Editor editor = preferences.edit();

        Intent serviceIntent = new Intent(this, OTPService.class);
        startService(serviceIntent);

        editor.putBoolean("started", true);
        editor.apply();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, OTPService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


        cartAdapter.notifyDataSetChanged();
        if (getQtyIds) {
            loadingDialog.show();
            /////code to access quantity
            for (int i = 0; i < cartItemModels.size() - 1; i++) {
                for (int j = 0; j < cartItemModels.get(i).getProductQuantity(); j++) {
                    final String qtyDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    final int finalI = i;
                    final int finalJ = j;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModels.get(i).getProductId()).collection("QUANTITY")
                            .document(qtyDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        cartItemModels.get(finalI).getQtyIds().add(qtyDocumentName);
                                        if (finalJ + 1 == cartItemModels.get(finalI).getProductQuantity()) {
                                            firebaseFirestore.collection("PRODUCTS").document(cartItemModels.get(finalI).getProductId()).collection("QUANTITY")
                                                    .orderBy("time", Query.Direction.ASCENDING)
                                                    .limit(cartItemModels.get(finalI).getStockQuantity())
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<String> serverQty = new ArrayList<>();

                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                            serverQty.add(queryDocumentSnapshot.getId());
                                                        }
                                                        long availableQty = 0;
                                                        boolean noLongerAvailable = true;
                                                        for (String qtyId : cartItemModels.get(finalI).getQtyIds()) {
                                                            cartItemModels.get(finalI).setQtyError(false);
                                                            if (!serverQty.contains(qtyId)) {
                                                                if (noLongerAvailable) {
                                                                    cartItemModels.get(finalI).setInStock(false);
                                                                } else {
                                                                    cartItemModels.get(finalI).setQtyError(true);
                                                                    cartItemModels.get(finalI).setMaxQuantity(availableQty);
                                                                    Toast.makeText(DeliveryActivity.this, "All products May not be available in required quantity", Toast.LENGTH_SHORT).show();
                                                                }

                                                            } else {
                                                                availableQty++;
                                                                noLongerAvailable = false;
                                                            }

                                                        }
                                                        cartAdapter.notifyDataSetChanged();

                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.dismiss();
                                                }
                                            });
                                        }
                                    } else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }


            }
        } else {
            getQtyIds = true;
        }

        //if (DBQueries.selectedAddress >= 0) {
        name = DBQueries.addressesModels.get(DBQueries.selectedAddress).getFullName();
        mobileNo = DBQueries.addressesModels.get(DBQueries.selectedAddress).getMobileNo();
        fullName.setText(name + " - " + mobileNo);
        fullAddress.setText(DBQueries.addressesModels.get(DBQueries.selectedAddress).getAddress());
        pinCode.setText(DBQueries.addressesModels.get(DBQueries.selectedAddress).getPinCode());
        /*
        } else {
            fullName.setText(DBQueries.addressesModels.get(DBQueries.selectedAddress + 1).getFullName());
            fullAddress.setText(DBQueries.addressesModels.get(DBQueries.selectedAddress + 1).getAddress());
            pinCode.setText(DBQueries.addressesModels.get(DBQueries.selectedAddress + 1).getPinCode());
        }
         */
        if (codOrderConfirmed) {
            codOrderConfirmed = false;
            ShowConfirmationLayout();
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        success = false;
        if (getQtyIds) {
            for (int i = 0; i < cartItemModels.size() - 1; i++) {
                if (!success) {

                    for (final String qtyId : cartItemModels.get(i).getQtyIds()) {
                        /// agr user beech ma product ko chor de aur buy na kare
                        final int finalI = i;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModels.get(i).getProductId()).collection("QUANTITY")
                                .document(qtyId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyId.equals(cartItemModels.get(finalI).getQtyIds().get(cartItemModels.get(finalI).getQtyIds().size() - 1))) {
                                            cartItemModels.get(finalI).getQtyIds().clear();

                                        }
                                    }
                                });
                    }
                } else {
                    cartItemModels.get(i).getQtyIds().clear();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (success) {
            ShowConfirmationLayout();
        } else {
            orderConfirmationLayout.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ShowConfirmationLayout() {

        getQtyIds = false;
        for (int i = 0; i < cartItemModels.size() - 1; i++) {
            for (String qtyId : cartItemModels.get(i).getQtyIds()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModels.get(i).getProductId()).collection("QUANTITY")
                        .document(qtyId).update("user_ID", FirebaseAuth.getInstance().getUid());
            }

        }

        if (Home.mainActivity != null) {
            Home.mainActivity.finish();
            Home.mainActivity = null;
            Home.showCart = false;
        } else {
            Home.restHome = true;
        }

        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }
        if (fromCart) {
            loadingDialog.show();
            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();                            ///jo remove karne hain products
            for (int i = 0; i < DBQueries.cartList.size(); i++) {
                if (!cartItemModels.get(i).isInStock()) {
                    updateCartList.put("product_ID_" + cartListSize, cartItemModels.get(i).getProductId());
                    cartListSize++;
                } else {
                    indexList.add(i);
                }
            }
            updateCartList.put("list_size", cartListSize);
            FirebaseFirestore.getInstance().collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < indexList.size(); i++) {
                            DBQueries.cartList.remove(indexList.get(i).intValue());
                            DBQueries.cartItemModels.remove(indexList.get(i).intValue());
                            DBQueries.cartItemModels.remove(DBQueries.cartItemModels.size() - 1);
                        }

                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });

        }


        continueButton.setEnabled(false);
        changeOrAddNewAddressBtn.setEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID: " + order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class CartBroadcast extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "CLEAR_CART")) {
                String clear = intent.getStringExtra("clear");
                startActivity(intent);
            }
        }
    }

    private void placeOrderDetails() {
        String userId = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel : cartItemModels) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {

                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID", order_id);
                orderDetails.put("Product ID", cartItemModel.getProductId());
                orderDetails.put("User ID", userId);
                orderDetails.put("Product QUANTITY", cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted PRICE", cartItemModel.getCuttedPrice());
                }else {
                    orderDetails.put("Cutted PRICE", "");
                }
                orderDetails.put("Product Price", cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCouponId() != null) {
                    orderDetails.put("Coupon ID", cartItemModel.getSelectedCouponId());
                }else {
                    orderDetails.put("Coupon ID", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted price", cartItemModel.getDiscountedPrice());
                }else {
                    orderDetails.put("Discounted price", "");
                }
                orderDetails.put("Date", FieldValue.serverTimestamp());
                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("FullName", fullName.getText());
                orderDetails.put("Pincode", pinCode.getText());
                orderDetails.put("Free Coupons", cartItemModel.getFreeCoupons());


                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartItemModel.getProductId())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items", cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price", cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price", cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount", cartItemModel.getCartTotalAmount());
                orderDetails.put("Saved Amount", cartItemModel.getSaveAmount());
                orderDetails.put("Payment Status", "Not Paid");
                orderDetails.put("Order Status", "Canceled");
                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (paymentMethod.equals("CreditCard")) {
                                patym();
                            } else {
                                Cod();
                            }
                        } else {
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void patym() {
        getQtyIds = false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        final String M_id = "YlutwW84809962366406";
        final String customer_id = FirebaseAuth.getInstance().getUid();
        order_id = UUID.randomUUID().toString().substring(0, 28);
        final String url = "https://planned-finishes.000webhostapp.com/paytm/generateChecksum.php";
        final String callBack = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");
                        PaytmPGService paytmPGService = PaytmPGService.getStagingService(url);
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBack);
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder(paramMap);

                        paytmPGService.initialize(order, null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                if (success) {
                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("Payment Status", "Paid");
                                    updateStatus.put("Order Status", "Ordered");

                                    firebaseFirestore.collection("ORDERS").document(order_id)
                                            .update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ShowConfirmationLayout();
                                            } else {
                                                Toast.makeText(DeliveryActivity.this, "Order Canceled", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    orderConfirmationLayout.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onBackPressedCancelTransaction() {
                                //Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                                //if (success) {
                                ShowConfirmationLayout();
                                //} else {
                                //orderConfirmationLayout.setVisibility(View.GONE);
                                //}
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", M_id);
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", customer_id);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("CALLBACK_URL", callBack);
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void Cod() {
        getQtyIds = false;
        paymentMethodDialog.dismiss();
        Intent otpIntent = new Intent(DeliveryActivity.this, OTPVerificationActivity.class);
        otpIntent.putExtra("mobileNo", mobileNo.substring(0, 10));
        otpIntent.putExtra("OrderId", order_id);
        startActivity(otpIntent);
    }

}
