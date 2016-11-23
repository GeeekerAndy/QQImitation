package andy.sdu.edu.cn.qqimitation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import andy.sdu.edu.cn.qqimitation.R;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        TextView tv_conversation_username = (TextView)findViewById(R.id.tv_conversation_username);
        tv_conversation_username.setText(username);

        ImageButton ibtn_conversation_arrow_left = (ImageButton)findViewById(R.id.ibtn_conversation_arrow_left);
        ibtn_conversation_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
