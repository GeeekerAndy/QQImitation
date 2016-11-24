package andy.sdu.edu.cn.qqimitation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import andy.sdu.edu.cn.qqimitation.Adapter.DialogAdapter;
import andy.sdu.edu.cn.qqimitation.R;

public class ConversationActivity extends AppCompatActivity {

    String username;
    String currentUsername = AVUser.getCurrentUser().getUsername();
    EditText et_message_to_send;
    Button btn_send_massage;
    ListView lv_main_dialog;
    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
    static String staticMsg;
    DialogAdapter adapter;
    int layout[] = {R.layout.bubble_mine, R.layout.bubble_friend};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        lv_main_dialog = (ListView) findViewById(R.id.lv_main_dialog);

        et_message_to_send = (EditText) findViewById(R.id.et_message_to_send);
        btn_send_massage = (Button) findViewById(R.id.btn_send_message);
        btn_send_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将我发送的信息内容添加到listview中，并传输到对方
                if (et_message_to_send.getText().toString().equals("")) {

                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(username, et_message_to_send.getText().toString());
                    arrayList.add(map);
                    String[] from = {username};
                    int[] to = {R.id.tv_user_words};
                    adapter = new DialogAdapter(ConversationActivity.this, arrayList, R.layout.bubble_mine, from, to);
                    lv_main_dialog.setAdapter(adapter);
                    sendMessage(et_message_to_send.getText().toString());
                    et_message_to_send.setText("");
                }
            }
        });

        TextView tv_conversation_username = (TextView) findViewById(R.id.tv_conversation_username);
        tv_conversation_username.setText(username);

        ImageButton ibtn_conversation_arrow_left = (ImageButton) findViewById(R.id.ibtn_conversation_arrow_left);
        ibtn_conversation_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void sendMessage(final String message) {
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例

        AVIMClient myClient = AVIMClient.getInstance(currentUsername);
        // 与服务器连接
        myClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与username之间的对话
                    client.createConversation(Arrays.asList(username), null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText(message);
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("me & friend", "发送成功！");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }

    public void receiveMessage(final String message){

        //currentUsername 登录
        AVIMClient myClient = AVIMClient.getInstance(currentUsername);
        myClient.open(new AVIMClientCallback(){

            @Override
            public void done(AVIMClient client,AVIMException e){
                if(e==null){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(username, message);
                    arrayList.add(map);
                    String[] from = {username};
                    int[] to = {R.id.tv_friend_words};
                    DialogAdapter adapter = new DialogAdapter(ConversationActivity.this, arrayList, R.layout.bubble_friend, from, to);
                    adapter.notifyDataSetChanged();
                    lv_main_dialog.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if("".equals(staticMsg)) {

                    } else {
                        System.out.println("staticMsg(before)is:" + staticMsg);
                        receiveMessage(staticMsg);
                        System.out.println("staticMsg(after) is:" + staticMsg);
                        staticMsg = "";

                    }
                }

            }
        }).start();
    }

    public static class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client){
            if(message instanceof AVIMTextMessage){
                String friendMsg = ((AVIMTextMessage) message).getText();
                staticMsg = friendMsg;
                Log.d("friend & me", friendMsg);
            }
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client){

        }
    }
}
