package com.example.chamandryfruits;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.file.StandardWatchEventKinds;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText signUpEmail;
    private EditText signUpName;
    private EditText signUpPassword;
    private EditText signUpConfirmPassword;

    private Button signUp;
    private ImageButton close;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SignUpFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.sign_up_signin);
        parentFrameLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.register_framelayout);

        signUpEmail = view.findViewById(R.id.sign_up_email);
        signUpName = view.findViewById(R.id.sign_up_fullname);
        signUpPassword = view.findViewById(R.id.sign_up_password);
        signUpConfirmPassword = view.findViewById(R.id.sign_up_confirm_password);

        signUp = view.findViewById(R.id.sign_up_button);
        close = view.findViewById(R.id.signup_imageButton);

        progressBar = view.findViewById(R.id.signup_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        signUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChechkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChechkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChechkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUpConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChechkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void ChechkInputs(){
        if(!TextUtils.isEmpty(signUpEmail.getText())){
            if(!TextUtils.isEmpty(signUpName.getText())){
                if(!TextUtils.isEmpty(signUpPassword.getText()) && signUpPassword.length() >= 8){
                    if(!TextUtils.isEmpty(signUpConfirmPassword.getText())){
                        signUp.setEnabled(true);
                        signUp.setTextColor(Color.rgb(255,255,255));
                    }
                    else{
                        signUp.setEnabled(false);
                        signUp.setTextColor(Color.argb(50,255,255,255));
                    }
                }
                else{
                    signUp.setEnabled(false);
                    signUp.setTextColor(Color.argb(50,255,255,255));
                }
            }
            else{
                signUp.setEnabled(false);
                signUp.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else{
            signUp.setEnabled(false);
            signUp.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void CheckEmailAndPassword(){
        if(signUpEmail.getText().toString().matches(emailPattern)){
            if(signUpPassword.getText().toString().equals(signUpConfirmPassword.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                signUp.setEnabled(false);
                signUp.setTextColor(Color.argb(50,255,255,255));


                firebaseAuth.createUserWithEmailAndPassword(signUpEmail.getText().toString(),signUpPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Intent intent = new Intent(getContext(), Home.class);
                                    startActivity(intent);
                                    Objects.requireNonNull(getActivity()).finish();
                                }
                                else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    String error = Objects.requireNonNull(task.getException()).toString();
                                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                                    signUp.setEnabled(true);
                                    signUp.setTextColor(Color.rgb(255,255,255));
                                }
                            }
                        });
            }
            else{
               signUpConfirmPassword.setError("Password Does not match!");
            }
        }else{
            signUpEmail.setError("Invalid Email!");
        }
    }

}
