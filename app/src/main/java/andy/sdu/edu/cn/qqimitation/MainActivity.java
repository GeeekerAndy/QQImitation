package andy.sdu.edu.cn.qqimitation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

import andy.sdu.edu.cn.qqimitation.activity.LoginActivity;
import andy.sdu.edu.cn.qqimitation.activity.RegisterActivity;
import andy.sdu.edu.cn.qqimitation.fragment.ContactFragment;
import andy.sdu.edu.cn.qqimitation.fragment.ConversationFragment;
import andy.sdu.edu.cn.qqimitation.fragment.PluginFragment;

/**
 * Created by Andy.
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private String[] mPlanetTitles = {"关于", "退出帐号"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"fDREVkCDOBGnezfCQgQLdBih-gzGzoHsz","yi9HPB8FhGpGzk4BaJDeGeQn");
        setContentView(R.layout.activity_main);

        /*
        Initialize the navigation drawer list.
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("About")
                            .setMessage("Author: Andy" + "\n" + "Time: Nov 2016" + "\n" + "Version: 1.0")
                            .setPositiveButton("OK", null)
                            .show();
                }
                if(id == 1) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("退出帐号")
                            .setMessage("确定退出当前帐号？")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Delete the local username and password.
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", "");
                                    editor.putString("password", "");
                                    editor.apply();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    AVUser.getCurrentUser().logOut();
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

        /*
        Three main fragments.
         */
        final ConversationFragment conversationFragment = new ConversationFragment();
        final ContactFragment contactFragment = new ContactFragment();
        final PluginFragment pluginFragment = new PluginFragment();

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, conversationFragment);
        transaction.commit();

        RadioButton conversationRadioButton = (RadioButton)findViewById(R.id.rb_conversation);
        conversationRadioButton.setChecked(true);
        conversationRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.fragment_container, conversationFragment);
                transaction1.commit();
            }
        });
        RadioButton contactRadioButton = (RadioButton)findViewById(R.id.rb_contact);
        contactRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.fragment_container, contactFragment);
                transaction2.commit();
            }
        });
        RadioButton pluginRadioButton = (RadioButton)findViewById(R.id.rb_plugin);
        pluginRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.fragment_container, pluginFragment);
                transaction3.commit();
            }
        });

    }

}
