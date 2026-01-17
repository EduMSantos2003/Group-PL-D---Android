package pt.ipleiria.estg.dei.amsi.homepantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.api.SessionManager;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.LoginRequest;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;

    // só para guardar o email digitado (não é sessão!)
    public static final String PREFS_NAME = "APP_PREFS";
    public static final String KEY_EMAIL = "EMAIL_LAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // se já tiver token guardado -> entra direto
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
           abrirMain();
            return;
        }

        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // preencher email guardado (apenas conveniência)
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String emailGuardado = prefs.getString(KEY_EMAIL, null);
        if (emailGuardado != null) {
            edtEmail.setText(emailGuardado);
        }

        btnLogin.setOnClickListener(view -> doLogin());
    }

    private void doLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService(this)
                .login(new LoginRequest(email, password))
                .enqueue(new retrofit2.Callback<LoginResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<LoginResponse> call,
                                           retrofit2.Response<LoginResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            // guardar token
                            SessionManager session = new SessionManager(LoginActivity.this);
                            session.saveLogin(
                                    response.body().getUser_id(),
                                    response.body().getUsername(),
                                    email,
                                    response.body().getToken()
                            );

                            // guardar email por conveniência
                            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            prefs.edit().putString(KEY_EMAIL, email).apply();

                            Toast.makeText(LoginActivity.this,
                                    "Login OK: " + response.body().getUsername(),
                                    Toast.LENGTH_SHORT).show();

                            abrirMain();

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Login inválido (" + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,
                                "Erro: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void abrirMain() {
        Intent i = new Intent(this, MenuMainActivity.class);
        startActivity(i);
        finish();
    }

    private void abrirCriarNovoUtilizador() {
        Intent i = new Intent(this, MenuMainActivity.class);
        i.putExtra(MainActivity.EXTRA_DESTINO, R.id.CriarNovoUtilizadorFragment);
        startActivity(i);
    }
}
