package com.softdesign.devintensive.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by AlexFrei on 06.07.16.
 */
public class AuthActivity extends AppCompatActivity{

    @BindViews({R.id.auth_login, R.id.auth_password})
    List<EditText> mAuthFields;

    Button mAuthButton;

    @BindView(R.id.forgot_pass)
    TextView mForgotPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        //временно, запускает главную активность через 10 секунд, после первого запуска этой активности 
        //заглушка проверить 08.07
        //проверено

        // TODO: 08.07.16 заглушка запускаеть активити через 10 секунд 
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 10000);
    }

    public void onClick(View v) {
        String login, password;

        switch (v.getId()) {
            case R.id.auth_button:
                login = mAuthFields.get(0).getText().toString();
                password = mAuthFields.get(1).getText().toString();

                // TODO: 06.07.16 заглушка проверки авторизации на сервере 


                break;

            case R.id.forgot_pass:

                // TODO: 06.07.16 заглушка забыли пароль восстановление пароля 

        }
    }
}
