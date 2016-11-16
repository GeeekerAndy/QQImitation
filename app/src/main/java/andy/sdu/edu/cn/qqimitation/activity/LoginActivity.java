package andy.sdu.edu.cn.qqimitation.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import andy.sdu.edu.cn.qqimitation.MainActivity;
import andy.sdu.edu.cn.qqimitation.R;

public class LoginActivity extends AppCompatActivity {

    LinearLayout ll_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        ObjectAnimator.ofFloat(ll_login, "translationY", -600).setDuration(1000).start();
    }
}
