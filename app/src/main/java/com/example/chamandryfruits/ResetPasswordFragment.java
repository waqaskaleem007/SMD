package com.example.chamandryfruits;

import android.annotation.SuppressLint;
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
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.security.PrivateKey;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    private EditText Email;
    String email;
    private Button resetButton;
    private TextView goBack;
    private FrameLayout parentFrameLayout;

    private FirebaseAuth firebaseAuth;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailText;
    private ProgressBar progressBar;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reset_password, container, false);
        Email = view.findViewById(R.id.reset_Email);
        email = Email.getText().toString();

        resetButton = view.findViewById(R.id.reset_button);
        goBack = view.findViewById(R.id.reset_Goback);
        parentFrameLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.register_framelayout);

        firebaseAuth = FirebaseAuth.getInstance();

        emailIconContainer = view.findViewById(R.id.forget_email_cointainer);
        emailIcon = view.findViewById(R.id.reset_emailIcon);
        emailText = view.findViewById(R.id.reset_emailText);
        progressBar = view.findViewById(R.id.reset_progressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetButton.setEnabled(false);
                resetButton.setTextColor(Color.rgb(255, 255, 255));
                firebaseAuth.sendPasswordResetEmail(Email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("SetTextI18n")
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                emailIcon.setImageResource(R.mipmap.group_375);
                                emailText.setText("Recovery email sent successfully ! check your inbox");
                                emailText.setTextColor(getResources().getColor(R.color.successfulGreen));
                                TransitionManager.beginDelayedTransition(emailIconContainer);
                                emailText.setVisibility(View.VISIBLE);
                            }else{
                                String error = Objects.requireNonNull(task.getException()).toString();
                                resetButton.setEnabled(true);
                                resetButton.setTextColor(Color.rgb( 255, 255, 255));
                                emailText.setText(error);
                                emailText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                TransitionManager.beginDelayedTransition(emailIconContainer);
                                emailText.setVisibility(View.VISIBLE);
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });
            }
        });

    }

    private void CheckEmail(){
        if(TextUtils.isEmpty(Email.getText())){
            resetButton.setEnabled(false);
            resetButton.setTextColor(Color.argb(255, 255, 255, 255));

        }else{
            emailIcon.setVisibility(View.GONE);
            emailText.setVisibility(View.GONE);
            resetButton.setEnabled(true);
            resetButton.setTextColor(Color.rgb( 255, 255, 255));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }



}
