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

import java.sql.Struct;
import java.util.Objects;

import static com.example.chamandryfruits.RegisterActivity.onResetPasswordFragment;


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
    private ImageButton close;

    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

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
        close = view.findViewById(R.id.signin_close);

        firebaseAuth = FirebaseAuth.getInstance();


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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChechEmailAndPassword();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Home.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
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
    private void ChechEmailAndPassword(){
        if(Email.getText().toString().matches(emailPattern)){
            if(password.length() >= 8){
                progressBar.setVisibility(View.VISIBLE);
                signIn.setEnabled(false);
                signIn.setTextColor(Color.argb(50, 255, 255, 255));
                firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getContext(),Home.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signIn.setEnabled(true);
                                    signIn.setTextColor(Color.rgb( 255, 255, 255));
                                    String error = Objects.requireNonNull(task.getException()).toString();
                                    Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getContext(),"Incorrect Email or Password",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(),"Incorrect Email or Password",Toast.LENGTH_SHORT).show();
        }
    }

}
