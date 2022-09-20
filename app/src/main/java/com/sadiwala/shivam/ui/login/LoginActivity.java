package com.sadiwala.shivam.ui.login;


import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_USERS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.User;
import com.sadiwala.shivam.network.FirebaseDatabaseController;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.main.MainActivity;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getName();

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DataController.getPrefUser() != null) {
            MainActivity.start(LoginActivity.this);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            init();
        }

    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private void init() {
        progressBar = findViewById(R.id.progressbar);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    progressBar.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setEmail(editEmail.getText().toString());
                    user.setPassword(editPassword.getText().toString());

                    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_USERS);
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            progressBar.setVisibility(View.GONE);

                            User fireStoreUser = FirebaseDatabaseController.getUser(queryDocumentSnapshots, user);
                            if (fireStoreUser != null) {
                                DataController.setPrefUser(fireStoreUser);
                                MainActivity.start(LoginActivity.this);
                            } else {
                                Log.e(TAG, "Please enter correct email and password.");
                                Toast.makeText(getApplicationContext(), "Please enter correct email and password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    private boolean isValid() {

        if (TextUtils.isEmpty(editEmail.getText())) {
            editEmail.setError(getString(R.string.required));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            editEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }

        if (TextUtils.isEmpty(editPassword.getText())) {
            editPassword.setError(getString(R.string.required));
            return false;
        }

        return true;
    }


}
