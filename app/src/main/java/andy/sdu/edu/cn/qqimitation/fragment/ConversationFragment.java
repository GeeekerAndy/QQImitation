package andy.sdu.edu.cn.qqimitation.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import andy.sdu.edu.cn.qqimitation.Adapter.FriendsListAdapter;
import andy.sdu.edu.cn.qqimitation.R;

/**
 * Created by Andy.
 * Conversation fragment in main activity.
 */
public class ConversationFragment extends Fragment {


    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_conversation, container, false);

        ImageButton ibtn_add_conversation = (ImageButton)v.findViewById(R.id.ibtn_add_conversation);
        ibtn_add_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = getActivity();
//                final EditText et_conversation_name = new EditText(context);
//                et_conversation_name.setHint("用户名");
//                AlertDialog dialog = new AlertDialog.Builder(context)
//                        .setTitle("请输入谈话对方用户名")
//                        .setView(et_conversation_name)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if(AVUser.getCurrentUser().getUsername().equals(et_conversation_name.getText().toString())) {
//                                    Toast.makeText(getContext(), "不能跟自己谈话", Toast.LENGTH_LONG).show();
//                                } else if (et_conversation_name.getText().toString().equals("")) {
//                                    Toast.makeText(getContext(), "用户名为空", Toast.LENGTH_LONG).show();
//                                } else {
//
//                                }
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .show();
            }
        });
        return v;
    }

}
