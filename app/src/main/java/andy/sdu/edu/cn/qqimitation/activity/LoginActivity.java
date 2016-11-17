package andy.sdu.edu.cn.qqimitation.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import andy.sdu.edu.cn.qqimitation.MainActivity;
import andy.sdu.edu.cn.qqimitation.R;

/**
 *  登录界面，验证密码
 */

public class LoginActivity extends AppCompatActivity {

    LinearLayout ll_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*
        点击登录如果用户名密码正确则进入主界面
         */
        ll_login = (LinearLayout)findViewById(R.id.ll_login);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        登陆界面向上滑动的动画
         */
        WindowManager windowManager = (WindowManager)LoginActivity.this.getSystemService(LoginActivity.this.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
//        int screenWidth = size.x;
        int screenHidth = size.y;
        ObjectAnimator.ofFloat(ll_login, "translationY", -(screenHidth/4)).setDuration(1000).start();
    }
}
