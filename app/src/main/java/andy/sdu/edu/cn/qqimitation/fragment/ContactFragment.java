package andy.sdu.edu.cn.qqimitation.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import andy.sdu.edu.cn.qqimitation.Adapter.FriendsListAdapter;
import andy.sdu.edu.cn.qqimitation.R;
import andy.sdu.edu.cn.qqimitation.activity.ConversationActivity;

/**
 * Created by Andy.
 * Contact fragment in main activity.
 */
public class ContactFragment extends Fragment {
    List<Map<String, Object>> friendList;

    final String currentUsername = AVUser.getCurrentUser().getUsername();
    String[] friendsNameList;
    ListView lv_friend_list;
    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        TextView tv_add_user = (TextView) v.findViewById(R.id.tv_add_user);
        tv_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                final EditText et_username_contactFragment = new EditText(context);
                et_username_contactFragment.setHint("好友用户名");
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("请输入")
                        .setView(et_username_contactFragment)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (currentUsername.equals(et_username_contactFragment.getText().toString())) {
                                    Toast.makeText(getContext(), "不能添加自己", Toast.LENGTH_LONG).show();
                                } else if (et_username_contactFragment.getText().toString().equals("")) {
                                    Toast.makeText(getContext(), "用户名为空", Toast.LENGTH_LONG).show();
                                } else {
                                    addFriend(et_username_contactFragment.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
//        RoundedImageView riv_red_dot = (RoundedImageView)v.findViewById(R.id.riv_red_dot);
//        riv_red_dot.setBackground(null);

        lv_friend_list = (ListView) v.findViewById(R.id.lv_friend_list);
        lv_friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String currentFriend = view
                HashMap<String, Object> map = (HashMap) friendList.get(position);
                String username = (String) map.get("username");
                Intent intent = new Intent(getContext(), ConversationActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        AVQuery<AVObject> query = new AVQuery<>(currentUsername);
        query.whereContains("Friends", "");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list == null) {

                } else {
                    friendsNameList = new String[list.size()];
                    for(int i = 0; i < list.size(); i++) {
                        friendsNameList[i] = list.get(i).getString("Friends");
                    }
                    //  final String[] onlineStatus = {"[离线]", "[在线]", "[离线]", "[在线]"};
//        final String[] onlineStatus = null;
//        final int[] avatars = {
//                R.mipmap.avatar,
//                R.mipmap.ic_launcher,
//                R.mipmap.new_friend,
//                R.mipmap.navigation_background
//        };
                    friendList = new ArrayList<>();
                    for (int i = 0; i < friendsNameList.length; i++) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("username", friendsNameList[i]);
//            map.put("onlineStatus", onlineStatus[i]);
//            map.put("avatar", avatars[i]);
                        friendList.add(map);
                    }

//        String[] from = {"username", "onlineStatus", "avatar"};
//        int[] to = {R.id.tv_username_in_list, R.id.tv_online_status, R.id.iv_avator_in_list};
                    String[] from = {"username"};
                    int[] to = {R.id.tv_username_in_list};

                    FriendsListAdapter friendsListAdapter = new FriendsListAdapter(getContext(), friendList, R.layout.one_user_in_list, from, to);
                    lv_friend_list.setAdapter(friendsListAdapter);
                }

            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    /*
    To store the relationship to the database.
    If one does not want to add the friend, he/she will delete the data on the database.
    username is the one you want to add as a friend.
     */
    public void addFriend(final String username) {

        /*
        To check if the user exists.
        If the user exists, he/she will never login because I use "12" as password,
        and when registering, the password length is more than "12". Though it will cause AVException.USERNAME_PASSWORD_MISMATCH
        But it is a efficient way by now. Just ignore the exception.
         */
        AVUser.logInInBackground(username, "12", new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {

                if(e.getCode() == AVException.USER_DOESNOT_EXIST) {
                    Toast.makeText(getContext(), "该用户不存在！", Toast.LENGTH_LONG).show();
                }

                //If the user exists, add he/she as a friend.
                if(e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH) {

                    AVQuery<AVObject> queryMyFriendList = new AVQuery<>(currentUsername);
                    queryMyFriendList.whereEqualTo("Friends", username);
                    queryMyFriendList.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (list == null || list.size() == 0) {
                                final AVObject myObject = new AVObject(currentUsername);
                                myObject.put("Friends", username);
                                myObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            //存储成功
                                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //存储失败
                                            Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                AVObject friendObject = new AVObject(username);
                                friendObject.put("Friends", currentUsername);
                                friendObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            //对方数据库存储成功
                                            Toast.makeText(getContext(), "对方添加成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //对方数据库存储失败
                                            Toast.makeText(getContext(), "对方添加失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                System.out.println("list长度：" + list.size());
                                Toast.makeText(getContext(), "已经是好友了！", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
