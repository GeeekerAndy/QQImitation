package andy.sdu.edu.cn.qqimitation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import andy.sdu.edu.cn.qqimitation.MainActivity;
import andy.sdu.edu.cn.qqimitation.R;

/**
 * Created by Andy.
 * To connect leancloud server.
 * Show an image during initializing app.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        /*
        Storing username and password locally to enter app without logging.
         */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = sharedPreferences.getString("username", "");
        final String password = sharedPreferences.getString("password", "");
        if(username.equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else {
            //Show login interface.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {

                                //If login successful, then save the username and password.
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.apply();

                            } else {
                                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    SplashActivity.this.finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }, 2000);
        }
    }
}












