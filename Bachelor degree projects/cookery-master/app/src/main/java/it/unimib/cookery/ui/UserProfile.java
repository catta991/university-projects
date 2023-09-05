package it.unimib.cookery.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;

public class UserProfile extends AppCompatActivity {

    private EditText profileEmail, profilePassword;
    private Button mSaveButton;
    private FirebaseUser user;
    private ImageView back_button;
    Costants costants = new Costants();
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        profileEmail = findViewById(R.id.profileEmail);
        profileEmail.setHint(user.getEmail());

        profilePassword = findViewById(R.id.profilePassword);

        back_button = findViewById(R.id.back_button_login_2);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mSaveButton = findViewById(R.id.saveButton_user);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 email = profileEmail.getText().toString().trim();
                 password = profilePassword.getText().toString().trim();

                if(email.isEmpty()) {
                    profileEmail.setError(costants.EMAIL_REQUIRED);
                    profileEmail.requestFocus();
                }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                    profileEmail.setError(costants.INVALID_EMAIL);
                    profileEmail.requestFocus();
                }else if(password.isEmpty()) {
                    profilePassword.setError(costants.PASSWORD_REQUIRED);
                    profilePassword.requestFocus();
                }else if(password.length() < 9) {
                    profilePassword.setError(costants.PASSWORD_LENGTH);
                } else {
                    updateUser();
                    finish();
                }
            }
        });

    }

    private void updateUser() {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, R.string.update_mail, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UserProfile.this, R.string.update_mail_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, R.string.update_password, Toast.LENGTH_LONG).show();
                            }else
                                Toast.makeText(UserProfile.this, R.string.update_password_fail, Toast.LENGTH_LONG).show();
                        }
                    });
    }
}