package andy.sdu.edu.cn.qqimitation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import andy.sdu.edu.cn.qqimitation.R;


/**
 * Created by andy on 11/24/16.
 * Adapter to inflate messages to listview in the dialog window.
 */

public class DialogAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, Object>> chatList = null;
    int[] layout;
    String[] from;
    int[] to;
    LayoutInflater listContainer;

    public DialogAdapter(Context context, ArrayList<HashMap<String, Object>> chatList, int[] layout, String[] from, int[] to) {
        super();
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.chatList = chatList;
        this.layout = layout;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前子项的who和chat
        int who = (Integer) chatList.get(position).get("who");
        String msg = (String) chatList.get(position).get("chat");
        //根据who设置当前子项的布局和文本，0为自己发送的布局，1为好友发送的布局
        if (who == 0) {
            convertView = listContainer.inflate(R.layout.bubble_mine, null);
            ((TextView) convertView.findViewById(R.id.tv_user_words)).setText(msg);
            return convertView;
        } else {
            convertView = listContainer.inflate(R.layout.bubble_friend, null);
            ((TextView) convertView.findViewById(R.id.tv_friend_words)).setText(msg);
            return convertView;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}




















