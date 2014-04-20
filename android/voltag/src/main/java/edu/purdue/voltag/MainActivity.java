package edu.purdue.voltag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;

import edu.purdue.voltag.data.ParseConstants;
import edu.purdue.voltag.data.VoltagDB;
import edu.purdue.voltag.fragments.CreateGameFragment;
import edu.purdue.voltag.fragments.GameChoiceFragment;
import edu.purdue.voltag.fragments.GameLobbyFragment;
import edu.purdue.voltag.fragments.RegistrationFragment;
import edu.purdue.voltag.lobby.BitmapCacheHost;


public class MainActivity extends Activity implements BitmapCacheHost {

    public static final String LOG_TAG = "voltag_log";
    public static final String PREFS_NAME = "voltag_prefs";
    public static final String PREF_ISIT = "player_is_it";
    public static final String PREF_CURRENT_GAME_ID = "current_game_id";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_EMAIL = "user_email";
    public static final String PREF_ISREGISTERED = "is_registered";

    private LruCache<String, Bitmap> mMemoryCache;

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        assert(mMemoryCache != null);
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        assert(mMemoryCache != null);
        return mMemoryCache.get(key);
    }

    public void initMemoryCache()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 15;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Parse.initialize(this, ParseConstants.PARSE_APPLICATION_KEY, ParseConstants.PARSE_CLIENT_KEY);
        new Thread(new Runnable() {
            public void run() {
                Parse.initialize(MainActivity.this, ParseConstants.PARSE_APPLICATION_KEY, ParseConstants.PARSE_CLIENT_KEY);
            }
        }).start();
        initMemoryCache();
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        String gameId;
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME,0);
        gameId = settings.getString(MainActivity.PREF_CURRENT_GAME_ID,"");
        if(!(gameId.equals(""))){
            closeSplash();
            getFragmentManager().beginTransaction().replace(android.R.id.content, new GameLobbyFragment()).commit();

        }
    }

    public void testClick(View view)
    {
        closeSplash();

        getFragmentManager().beginTransaction().replace(android.R.id.content, new GameLobbyFragment()).commit();
    }

    public void beginButton(View view)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        boolean isRegistered = settings.getBoolean(PREF_ISREGISTERED,false);
        Log.d("debug","isRegistered="+isRegistered);

        closeSplash();

        if(!isRegistered) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new RegistrationFragment()).commit();
        }
        else{
            getFragmentManager().beginTransaction().replace(android.R.id.content, new GameChoiceFragment()).commit();
        }
    }

    private void closeSplash() {
        View v = (View) findViewById(R.id.splash);
        v.setVisibility(View.GONE);
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
