package com.example.chamandryfruits;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText Email;
    private EditText password;
    private TextView forgetPassword;
    private Button signIn;
    private ProgressBar progressBar;


    public SignInFragment() {
        // Required empty public constructor
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.sign_in_newuser);
        parentFrameLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.register_framelayout);

        Email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        forgetPassword = view.findViewById(R.id.sign_in_forget_password);
        signIn = view.findViewById(R.id.sign_in_button);
        progressBar = view.findViewById(R.id.sign_progressBar);



        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void CheckInputs(){
        if(!TextUtils.isEmpty(Email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signIn.setEnabled(true);
                signIn.setTextColor(Color.rgb(255,255,255));
            }else{
                signIn.setEnabled(false);
                signIn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signIn.setEnabled(false);
            signIn.setTextColor(Color.argb(50,255,255,255));
        }
    }


}
