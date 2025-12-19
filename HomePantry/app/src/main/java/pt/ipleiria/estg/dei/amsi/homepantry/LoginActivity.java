package pt.ipleiria.estg.dei.amsi.homepantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;

    // Nome do ficheiro de SharedPreferences
    public static final String PREFS_NAME = "APP_PREFS";
    public static final String KEY_EMAIL = "EMAIL_LOGGED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ligação aos componentes do layout
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // ➤ Se já está logado, salta o login!
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String emailGuardado = prefs.getString(KEY_EMAIL, null);

        if (emailGuardado != null) {
            edtEmail.setText(emailGuardado);
            // NÃO abre Main automaticamente
        }

        // ➤ Clicar em LOGIN
        btnLogin.setOnClickListener(view -> doLogin());

        // ➤ Clicar em NOVO UTILIZADOR (ainda não implementado)
//        btnRegister.setOnClickListener(view ->
//                Toast.makeText(this, "Registo ainda não implementado", Toast.LENGTH_SHORT).show()
//        );
        btnRegister.setOnClickListener(view -> abrirCriarNovoUtilizador());
    }

    // Função chamada ao clicar em LOGIN
    private void doLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // ➤ LOGIN OFFLINE: qualquer email + password serve (até termos API)
        guardarSessao(email);
        abrirMain();
    }

    // Guardar o email no telemóvel (SharedPreferences)
    private void guardarSessao(String email) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Mudar para a MenuMainActivity
    private void abrirMain() {
        Intent i = new Intent(this, MenuMainActivity.class);
        startActivity(i);
        finish(); // impede voltar ao login com back
    }

    private void abrirCriarNovoUtilizador() {
        Intent i = new Intent(this, MenuMainActivity.class);
        i.putExtra(MainActivity.EXTRA_DESTINO, R.id.CriarNovoUtilizadorFragment);
        startActivity(i);
    }


}
