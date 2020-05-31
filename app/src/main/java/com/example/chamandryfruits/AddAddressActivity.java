package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {

    private Button save;
    private EditText city;
    private EditText locality;
    private EditText flatNumber;
    private EditText pinCode;
    private EditText landMark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateMobileNo;
    private Spinner stateSpinner;
    private String selectedState;
    private String[] stateList;
    private Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///loading Dialog
        loadingDialog = new Dialog(Objects.requireNonNull(AddAddressActivity.this));
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading Dialog
        stateList = getResources().getStringArray(R.array.pakistan_states);

        city = findViewById(R.id.city);
        locality = findViewById(R.id.Locality);
        flatNumber = findViewById(R.id.flatNo);
        pinCode = findViewById(R.id.pincode);
        landMark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternateMobileNo = findViewById(R.id.alternate_mobile_no);
        stateSpinner = findViewById(R.id.state_spiner);


        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save = findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatNumber.getText())) {
                            if (!TextUtils.isEmpty(pinCode.getText()) && pinCode.length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 11) {
                                        loadingDialog.show();
                                        Map<String, Object> addAddress = new HashMap<>();
                                        final String fullAddress = flatNumber.getText().toString() + " " + locality.getText().toString() + " " + landMark.getText().toString() + " " + city.getText().toString() + " " + selectedState;

                                        addAddress.put("list_size", (long) DBQueries.addressesModels.size() + 1);
                                        if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                            addAddress.put("fullname_" + String.valueOf((long) DBQueries.addressesModels.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString());
                                        } else {
                                            addAddress.put("fullname_" + String.valueOf((long) DBQueries.addressesModels.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString() + " or " + alternateMobileNo.getText().toString());
                                        }
                                        addAddress.put("address_" + String.valueOf((long) DBQueries.addressesModels.size() + 1), fullAddress);
                                        addAddress.put("pincode_" + String.valueOf((long) DBQueries.addressesModels.size() + 1), pinCode.getText().toString());
                                        addAddress.put("selected_" + String.valueOf((long) DBQueries.addressesModels.size() + 1), true);
                                        if (DBQueries.addressesModels.size() > 0) {
                                            addAddress.put("selected_" + (DBQueries.selectedAddress + 1), false);
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (DBQueries.addressesModels.size() > 0) {
                                                        DBQueries.addressesModels.get(DBQueries.selectedAddress).setSelected(false);
                                                    }
                                                    if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                                        DBQueries.addressesModels.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString(), fullAddress, pinCode.getText().toString(), true));
                                                    } else {
                                                        DBQueries.addressesModels.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString() + " or " + alternateMobileNo.getText().toString(), fullAddress, pinCode.getText().toString(), true));
                                                    }


                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    } else {
                                                        MyAddressesActivity.RefreshItem(DBQueries.selectedAddress, DBQueries.addressesModels.size() - 1);
                                                    }
                                                    DBQueries.selectedAddress = DBQueries.addressesModels.size() - 1;
                                                    finish();
                                                } else {
                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });

                                    } else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please provide valid Mobile No.!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    name.requestFocus();
                                }
                            } else {
                                pinCode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please provide valid pin code!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            flatNumber.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }


            }
        });

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
}
