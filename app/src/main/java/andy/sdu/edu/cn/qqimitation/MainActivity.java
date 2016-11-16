package andy.sdu.edu.cn.qqimitation;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import andy.sdu.edu.cn.qqimitation.fragment.ContactFragment;
import andy.sdu.edu.cn.qqimitation.fragment.ConversationFragment;
import andy.sdu.edu.cn.qqimitation.fragment.PluginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
