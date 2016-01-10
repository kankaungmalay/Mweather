package com.visittomm.mweather;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.visittomm.mweather.fragments.CachedCityListFragment;
import com.visittomm.mweather.fragments.CityListFragment;
import com.visittomm.mweather.interfaces.FragmentListener;
import com.visittomm.mweather.utils.Utils;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements FragmentListener, FragmentManager.OnBackStackChangedListener {

    public static final String TAG              = "com.visittomm.mweather.MainActivity";
    private static final String JSON_CACHE_FILE = "city-cache.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
//        toolbar.setTitle("Mweather");
//        setSupportActionBar(toolbar);


        // NOTED: Listen for changes in the back stack
        getFragmentManager().addOnBackStackChangedListener(this);
        // NOTED: Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        if (savedInstanceState == null) { // application is started for the first time
            // NOTED: Read selected cities object from json
            String jsonContent = null;

            try {
                FileInputStream jsonCache = this.openFileInput(JSON_CACHE_FILE);
                jsonContent = Utils.readStream(jsonCache);
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
            if (jsonContent == null) { // no city in json file
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment, new CityListFragment())
                        .commit();
            } else {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment, new CachedCityListFragment())
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //     return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startMainFragment(Fragment fragment, boolean addToBackState) {
        FragmentManager fragmentManager = getFragmentManager();
        int container_id = R.id.fragment;

        FragmentTransaction tran = fragmentManager.beginTransaction().replace(container_id, fragment, "main_fragment");
        if (addToBackState) {
            tran.addToBackStack(null);
        }
        tran.commitAllowingStateLoss();
    }

    public void shouldDisplayHomeUp(){

        //Enable Up button only  if there are entries in the back stack
        boolean canback = getFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Really Exit from MWeather?")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    MainActivity.super.onBackPressed();
                }
            }).create().show();
    }
}
