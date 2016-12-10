package andy.sdu.edu.cn.qqimitation.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
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
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import andy.sdu.edu.cn.qqimitation.Adapter.DialogAdapter;
import andy.sdu.edu.cn.qqimitation.R;
import andy.sdu.edu.cn.qqimitation.SQLiteToStoreConversationHelper;

import static java.lang.Thread.sleep;

/**
 * The main window when you chat with your friend. Receive message and send message.
 */

public class ConversationActivity extends AppCompatActivity {

    public static String staticMsg = "";    //To store the received message.
    public static String staticFriendName="";

    private String username;
    private String currentUsername = AVUser.getCurrentUser().getUsername();
    private EditText et_message_to_send;
    private Button btn_send_massage;

    private ArrayList<HashMap<String, Object>> chatList = new ArrayList<>();
    private String[] from = {"text"};//键名称数组
    private int[] to = {R.id.tv_user_words, R.id.tv_friend_words};//赋值数据的控件id数组
    private int[] item_layout = {R.layout.bubble_mine, R.layout.bubble_friend};//布局文件数组
    private ListView lv_main_dialog;
    private DialogAdapter dialogAdapter;
    private final MsgThread receiveMsgThread = new MsgThread();
    Handler mHandler;

    private SQLiteToStoreConversationHelper mDBHelper;
    WriteConversation writeConversation = new WriteConversation();
    ReadConversation readConversation = new ReadConversation();

    AVIMClient myClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);



        myClient = AVIMClient.getInstance(currentUsername);
        mDBHelper = new SQLiteToStoreConversationHelper(getApplicationContext());

        mHandler = new Handler(Looper.getMainLooper());


        //Get friend name.
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        lv_main_dialog = (ListView) findViewById(R.id.lv_main_dialog);
        et_message_to_send = (EditText) findViewById(R.id.et_message_to_send);
        btn_send_massage = (Button) findViewById(R.id.btn_send_message);

        //To show friend username.
        TextView tv_conversation_username = (TextView) findViewById(R.id.tv_conversation_username);
        tv_conversation_username.setText(username);

        dialogAdapter = new DialogAdapter(this, chatList, item_layout, from, to);

        //Click the button to send message to friend.
        btn_send_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将我发送的信息内容添加到listview中，并传输到对方
                if (et_message_to_send.getText().toString().equals("")) {

                } else {
                    String chat = et_message_to_send.getText().toString();
                    addChatToListview(0, chat);
                    String[] chatInformation = {username, "0", chat};
//                    writeConversation.doInBackground(chatInformation);
                    Log.d("White in DB", "Call writeConversation");
                    sendMessage(et_message_to_send.getText().toString());
                    et_message_to_send.setText("");
                    lv_main_dialog.setSelection(dialogAdapter.getCount() - 1);  //Scrolling to the latest message.
                }
            }
        });

        lv_main_dialog.setAdapter(dialogAdapter);

        ImageButton ibtn_conversation_arrow_left = (ImageButton) findViewById(R.id.ibtn_conversation_arrow_left);
        ibtn_conversation_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        //Get conversation history.
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String[][] conversationHistory = readConversation.doInBackground(username);
////                String[][] conversationHistory= {{"1", "haha"}, {"0", "heihei"}};
//                if(conversationHistory != null) {
//                    int arrayLength = conversationHistory.length;
//                    int arrayWidth = conversationHistory[0].length;
//                    for(int i = 0; i < arrayLength; i++) {
//                        if(conversationHistory[i][0].equals("0")) {
//                            addChatToListview(0, conversationHistory[i][1]);
//                        } else if (conversationHistory[i][0].equals("1")) {
//                            addChatToListview(1, conversationHistory[i][1]);
//                        }
//                    }
//                } else {
//                    Log.d("Error", "conversationHistory is null");
//                }
//            }
//        }).start();
    }

    /*
    将数据添加到chatList的函数实现
     */
    public void addChatToListview(int who, String chat) {

//        String[] array = new String[3];
//        if(who == 0) {
//            array[0] = username;
//            array[1] = currentUsername;
//            array[2] = chat;
//        } else if (who == 1) {
//            array[0] = username;
//            array[1] = username;
//            array[2] = chat;
//        }
//        writeConversation.doInBackground(array);

        HashMap<String, Object> map = new HashMap<>();
        map.put("who", who);//通过who来决定chatList中项的布局，0为我发送的，1为好友发来的
        map.put("chat", chat);
        chatList.add(map);
        dialogAdapter.notifyDataSetChanged();
        lv_main_dialog.setSelection(dialogAdapter.getCount() - 1);  //Scrolling to the latest message.
//        if(who == 0) {
//            String[] chatInformation = {username,"0", chat};
//            writeConversation.doInBackground(chatInformation);
//        } else if (who == 1) {
//            String[] chatInformation = {username,"1", chat};
//            writeConversation.doInBackground(chatInformation);
//        }
    }


    /*
    Send my message to Leancloud server and then transfer to my friend.
     */
    public void sendMessage(final String message) {
        //用自己的名字作为clientId，获取AVIMClient对象实例
//        myClient = AVIMClient.getInstance(currentUsername);
        // 与服务器连接
        myClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与username之间的对话
                    client.createConversation(Arrays.asList(username), (currentUsername + "to" + username), null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setMessageId(currentUsername + "to" + username);
                                        msg.setText(message);
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("me to " + username, "发送成功！");
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

    /*
    Receive friend message from leancloud server and then show in dialog window.
     */
    public void receiveMessage(final String message, final String friendName) {

        /*
        Open client to
         */
//        myClient.open(new AVIMClientCallback() {
//            @Override
//            public void done(AVIMClient avimClient, AVIMException e) {
//                Log.d("TAG", "Call addCharToListview");
//                addChatToListview(1, message);
//            }
//        });

        /*
        Method 1: runOnUiThread to pass parameters to UI thread.
         */
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("TAG", "Call addCharToListview");
//                    addChatToListview(1, message);
//                }
//            });
        /*
        Method 2: Handler to pass parameters to UI thread.
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "Call addCharToListview");
                addChatToListview(1, message);
            }
        });
    }

    /*
    If open a chat window of a friend, new a thread to get the message by checking if "staticMsg"
    is changed. After getting message, set "staticMsg" to "".
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d("ConversationActivity", "onResume");
        myClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                //Start to connect.
                //登录成功,获取聊天记录

//                AVIMConversation conv = myClient.getConversation(username);
//                int limit = 10;// limit 取值范围 1~1000 之内的整数
//                // 不使用 limit 默认返回 20 条消息
//                conv.queryMessages(limit, new AVIMMessagesQueryCallback() {
//                    @Override
//                    public void done(List<AVIMMessage> messages, AVIMException e) {
//                        if (e == null) {
//                            //成功获取最新10条消息记录
//                            for(int i = 0; i < messages.size(); i++) {
//                                if(messages.get(1).getMessageId().equals(currentUsername + "to" + username)) {
//                                    addChatToListview(0, ((AVIMTextMessage) messages.get(i)).getText());
//                                } else {
//                                    addChatToListview(1, ((AVIMTextMessage) messages.get(i)).getText());
//                                }
//                            }
//                        }
//                    }
//                });
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        receiveMsgThread.start();
    }

    /*
    If the chat window call onPause(), stop receive message.
     */
    @Override
    public void onPause() {
        super.onPause();
        myClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                //Stop receive message.
            }
        });
        receiveMsgThread.interrupt();
    }

    /*
    Thread to receive message by checking if 'staticMsg' has message.
     */
    public class MsgThread extends Thread {
        public void run() {
            while (true) {
                if ("".equals(staticMsg)) {
                    //do nothing.
                } else if (!staticMsg.equals(null)){
                    receiveMessage(staticMsg, staticFriendName);
                    Log.d("TAG", "After receiveMessage");
//                    String[] chatInformation = {staticFriendName,"1", staticMsg};
//                    writeConversation.doInBackground(chatInformation);
                    staticMsg = "";
                    staticFriendName = "";
                }
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public synchronized void start() {
            super.start();
        }
    }

    /*
    SQLite operation to store conversation locally.
     */
    private class WriteConversation extends AsyncTask<String[], Void, Void> {
        @Override
        protected Void doInBackground(String[]... params) {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            String[] array = params[0];//To get the String[] from params.
            ContentValues values = new ContentValues();
            values.put(SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_WHO, array[0]);
            values.put(SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_FROM_TO, array[1]);
            values.put(SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_CONTENT, array[2]);

            long newRowId = db.insert(SQLiteToStoreConversationHelper.ConversationEntry.TABLE_NAME, null, values);
            Log.d("Database", "write to DB success");
            return null;
        }
    }

    private class ReadConversation extends AsyncTask<String, Void, String[][]> {
        @Override
        protected String[][] doInBackground(String... params) {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String[] projection = {
//                    SQLiteToStoreConversationHelper.ConversationEntry._ID,
                    SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_FROM_TO,
                    SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_CONTENT
            };

            String selection = SQLiteToStoreConversationHelper.ConversationEntry.COLUMN_NAME_WHO + " = ?" ;
            String arg = params[0];
            Log.d("Params is:" , arg);
            String[] selectionArgs = {arg};

            Cursor cursor  = db.query(
                    SQLiteToStoreConversationHelper.ConversationEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                    );
            cursor.moveToFirst();
            int cursorLength = cursor.getCount();
            int cursorWidth = cursor.getColumnCount();
            if(cursorLength > 0) {
                String[][] result = new String[cursorLength][cursorWidth];
                for(int i  = 0; i < cursorLength; i++) {
                    for(int j = 0; j < cursorWidth; j++) {
                        result[i][j] = cursor.getString(j);
                    }
                    if(cursor.getPosition() < cursorLength) {
                        cursor.moveToNext();
                    }
                }
                return result;
            }
            return null;
        }
    }
}
