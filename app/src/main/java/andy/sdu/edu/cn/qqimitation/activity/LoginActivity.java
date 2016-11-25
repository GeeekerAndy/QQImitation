package andy.sdu.edu.cn.qqimitation.activity;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import andy.sdu.edu.cn.qqimitation.MainActivity;
import andy.sdu.edu.cn.qqimitation.R;

/**
 * Created by Andy.
 * Login activity to check user name and password.
 */

public class LoginActivity extends AppCompatActivity {

    EditText et_username;
    LinearLayout ll_login;
    EditText et_password;
    TextView tv_register;
    Button btn_login;
    CheckBox ck_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        ck_agree = (CheckBox) findViewById(R.id.ck_agree);


        /*
        Register a new qq account.
         */
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*
        Check if account information is correct. If true, then login.
         */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();
                AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {

                            //If login successful, then save the username and password.
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();

                            LoginActivity.this.finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        /*
        If username and password are legal(& I agree checked), enable login button.
         */
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_username.getText().toString().equals("") && (et_password.getText().toString().length() >= 6) && ck_agree.isChecked()) {
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.blueLight));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_username.getText().toString().equals("") && (et_password.getText().toString().length() >= 6) && ck_agree.isChecked()) {
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.blueLight));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ck_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!et_username.getText().toString().equals("") && (et_password.getText().toString().length() >= 6) && ck_agree.isChecked()) {
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.blueLight));
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
        Show login layout dynamically when the activity is calling onResume() method.
         */
        WindowManager windowManager = (WindowManager) LoginActivity.this.getSystemService(LoginActivity.this.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenHidth = size.y;
        ObjectAnimator.ofFloat(ll_login, "translationY", -(screenHidth / 4)).setDuration(1000).start();
    }
}
