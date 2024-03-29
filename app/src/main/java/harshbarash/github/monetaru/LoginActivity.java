package harshbarash.github.monetaru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText emailEt, passEt;
    Button register_btn, login_btn;
    CheckBox checkBox;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView forgotpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotpass = findViewById(R.id.tv_forgot_pass);
        emailEt = findViewById(R.id.login_email_et);
        passEt = findViewById(R.id.login_password_et);
        register_btn = findViewById(R.id.login_to_signup);
        login_btn = findViewById(R.id.button_login);
        checkBox = findViewById(R.id.login_checkbox);
        progressBar = findViewById(R.id.progressbar_login);
        mAuth = FirebaseAuth.getInstance();


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //  confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendtoMain();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Данные неверны", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    Toast.makeText(LoginActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }

            }
        });


        forgotpass.setOnClickListener(view -> {

            String email = emailEt.getText().toString();

            if (!TextUtils.isEmpty(email)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Сброс пароля")
                        .setMessage("Сбросить пароль?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mAuth.sendPasswordResetEmail(email)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "Ссылка успешно отправлена", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Ошибка", Toast.LENGTH_SHORT).show());

                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();
            } else {
                Toast.makeText(LoginActivity.this, "Заполните почту", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void sendtoMain() {
        Intent intent = new Intent(LoginActivity.this, Splashscreen.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}



