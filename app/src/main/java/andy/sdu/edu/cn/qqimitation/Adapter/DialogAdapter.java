package andy.sdu.edu.cn.qqimitation.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import andy.sdu.edu.cn.qqimitation.User;

/**
 * Created by andy on 11/24/16.
 */

public class DialogAdapter extends SimpleAdapter {

    public DialogAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
