package edu.purdue.voltag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.purdue.voltag.data.VoltagDB;
import edu.purdue.voltag.fragments.RegistrationFragment;


public class MainActivity extends Activity {

    public static final String LOG_TAG = "voltag_log";
    public static final String PREFS_NAME = "voltag_prefs";
    public static final String PREF_CURRENT_GAME_ID = "current_game_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this,RegisterActivity.class);
    }

    public void beginButton(View v)
    {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new RegistrationFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
