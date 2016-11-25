package andy.sdu.edu.cn.qqimitation.Adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import andy.sdu.edu.cn.qqimitation.User;

/**
 * Created by andy on 11/22/16.
 * To inflate the listview in friend list window.
 */

public class FriendsListAdapter extends SimpleAdapter {
    private User user;

    public FriendsListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        for (int i = 0; i < data.size(); i++) {
            user = new User((String) new HashMap<String, Object>().get("username"));
        }
    }

}
