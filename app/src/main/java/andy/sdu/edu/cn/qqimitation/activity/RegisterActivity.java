package andy.sdu.edu.cn.qqimitation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import andy.sdu.edu.cn.qqimitation.R;

/**
 * Created by Andy.
 * Register QQ account. Set username, password and check password input type.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText et_register_userName;
    EditText et_register_password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_register_userName = (EditText) findViewById(R.id.et_register_username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        btn_register = (Button) findViewById(R.id.btn_register);

        /*
        Check if input type is legal.
         */
        et_register_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If nick name is not null and password length is more than 6, enable register button.
                if ((et_register_password.getText().toString().length() >= 6)
                        && (!et_register_userName.getText().toString().equals(""))) {
                    btn_register.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary));
                    btn_register.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                    btn_register.setEnabled(true);
                }
                if ((et_register_password.getText().toString().length() < 6)
                        || (et_register_userName.getText().toString().equals(""))) {
                    btn_register.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.gray));
                    btn_register.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                    btn_register.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_register_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If nick name is not null and password length is more than 6, enable register button.
                if ((et_register_password.getText().toString().length() >= 6)
                        && (!et_register_userName.getText().toString().equals(""))) {
                    btn_register.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary));
                    btn_register.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                    btn_register.setEnabled(true);
                }
                if ((et_register_password.getText().toString().length() < 6)
                        || (et_register_userName.getText().toString().equals(""))) {
                    btn_register.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.gray));
                    btn_register.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                    btn_register.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /*
        Return to previous activity.
         */
        ImageButton ibtn_register_arrow_left = (ImageButton) findViewById(R.id.ibtn_register_arrow_left);
        ibtn_register_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*
        Click the button to register a new user.
         */
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser user = new AVUser();
                final String username = et_register_userName.getText().toString();
                final String password = et_register_password.getText().toString();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            //Registration Success. 把用户对象赋值给当前用户 AVUser.getCurrentUser()
                            Intent intent = new Intent(RegisterActivity.this, RegisterSuccussActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //Store the username and password.
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();

                            startActivity(intent);
                            RegisterActivity.this.finish();
                        } else {
                            // Registration fail. userName may exists.
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
