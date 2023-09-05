package it.unimib.cookery.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail, editTextPassword, editTextReenterPass;
    private Button registerButton;
    private ProgressBar progressBar;
    private ImageView back;
    private Costants costants = new Costants();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(this);

        editTextEmail = findViewById(R.id.register_email);
        editTextPassword = findViewById(R.id.register_password);
        editTextReenterPass = findViewById(R.id.register_password_again);
        back=findViewById(R.id.back_button_login);

        progressBar = findViewById(R.id.progress_bar_reg);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }




    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }

    public void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String reenterPassword = editTextReenterPass.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError(costants.EMAIL_REQUIRED);
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError(costants.PASSWORD_REQUIRED);
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 9) {
            editTextPassword.setError(costants.PASSWORD_LENGTH);
            editTextPassword.requestFocus();
            return;
        }

        if(reenterPassword.isEmpty()) {
            editTextReenterPass.setError(costants.REENTER_PASSWORD);
            editTextReenterPass.requestFocus();
            return;
        }

        if(!reenterPassword.equals(password)) {
            editTextReenterPass.setError(costants.NO_MATCH_PASSWORD);
            editTextReenterPass.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(costants.INVALID_EMAIL);
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            User user = new User(email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, R.string.register_success, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), LoginRegisterUser.class ));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterUser.this, R.string.register_fail, Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, R.string.register_fail, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
