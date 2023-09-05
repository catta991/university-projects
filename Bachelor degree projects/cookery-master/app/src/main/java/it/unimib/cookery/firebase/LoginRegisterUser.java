package it.unimib.cookery.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.ui.MainActivity;

public class LoginRegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private ImageView back;
    private FirebaseAuth mAuth;
    private Costants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        constants = new Costants();

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginEmail = findViewById(R.id.login_mail);
        loginPassword = findViewById(R.id.login_password);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        progressBar = findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back_button_login);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.login_button:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if(email.isEmpty()) {
            loginEmail.setError(constants.EMAIL_REQUIRED);
            loginEmail.requestFocus();
            return;
        }

        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            loginEmail.setError(constants.VALID_EMAIL);
            loginEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            loginPassword.setError(constants.PASSWORD_REQUIRED);
            loginPassword.requestFocus();
            return;
        }

        if(password.length() < 9) {
            loginPassword.setError(constants.PASSWORD_LENGTH);
            loginPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        //redirect to user activity
                        finish();
                        MainActivity.sharedPreferences.edit().putBoolean(constants.LOGGED, true).commit();
                        startActivity(new Intent(LoginRegisterUser.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginRegisterUser.this, R.string.verify_account, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(LoginRegisterUser.this, R.string.wrong_credentials, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

}