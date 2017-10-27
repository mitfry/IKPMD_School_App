package s1080488.ikpmd_app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import s1080488.ikpmd_app.Models.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the actionbar title
        getSupportActionBar().setTitle("Enter your account details below:");

        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etFirstName = (EditText) findViewById(R.id.etFirstname);
        final EditText etLastName = (EditText) findViewById(R.id.etLastname);
        final EditText etIGN = (EditText) findViewById(R.id.etIGN);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        //Back to login/start screen
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noEmptyFields;
                final String username = String.valueOf(etUsername.getText()).trim();
                final String email = String.valueOf(etEmail.getText()).trim();
                final String password = String.valueOf(etPassword.getText());
                final String firstName = String.valueOf(etFirstName.getText()).trim();
                final String lastName = String.valueOf(etLastName.getText()).trim();
                final String IGN = String.valueOf(etIGN.getText()).trim();

                if (username.equals("")) {
                    MainActivity.toastMessage(getApplicationContext(), "Please enter an username.");
                    noEmptyFields = 0;
                } else if (email.equals("")) {
                    MainActivity.toastMessage(getApplicationContext(), "Please enter an email.");
                    noEmptyFields = 0;
                } else if (password.equals("")) {
                    MainActivity.toastMessage(getApplicationContext(), "Please enter a password.");
                    noEmptyFields = 0;
                } else {
                    noEmptyFields = 1;
                }
                if (noEmptyFields == 1) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    Toast.makeText(getApplicationContext(), "Successfully registered " + username, Toast.LENGTH_LONG);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register failed!").setNegativeButton("Retry", null).create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(username, email, firstName, lastName, IGN, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
