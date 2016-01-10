package com.visittomm.mweather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.visittomm.mweather.R;
import com.visittomm.mweather.interfaces.FragmentListener;
import com.visittomm.mweather.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by monmon on 1/4/16.
 */
public class CachedCityListFragment extends Fragment {

    public static final String TAG              = "com.visittomm.mweather.fragments.CachedCityListFragment";
    private static final String JSON_CACHE_FILE = "city-cache.json";
    private Context mContext                    = null;
    private View mView                          = null;
    ListView mListView                          = null;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> stringArrayList;

    public CachedCityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, " =>> CachedCityListFragment");

        mView = inflater.inflate(R.layout.fragment_cached_city_list, container, false);
        mContext = this.getActivity();

        // change toolbar title of current Fragment
        Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.fragment);
        if (f instanceof CachedCityListFragment)
            ((AppCompatActivity) mContext).getSupportActionBar().setTitle("Mweather");

        mListView = (ListView) mView.findViewById(R.id.lvCachedRecordListing);

        // Read selected cities object from cache
        String jsonContent = null;
        try {
            FileInputStream jsonCache = mContext.openFileInput(JSON_CACHE_FILE);
            jsonContent = Utils.readStream(jsonCache);
            JSONArray jsonArray = new JSONArray(jsonContent);
            stringArrayList = new ArrayList<String>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json_data = jsonArray.getJSONObject(i);
                stringArrayList.add(json_data.getString("name")); // add to arraylist
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }

        mAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_2, android.R.id.text2, stringArrayList);
        mListView.setAdapter(mAdapter);

        onCityClick();
        checkAddEditBtnClick();

        return mView;
    }

    private void onCityClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.setAlpha(1);
                            }
                        });
                String value = (String)parent.getItemAtPosition(position);
                Log.d(TAG, "clicked value >> " + value);

                WeatherDetailFragment fragment = new WeatherDetailFragment();
                Bundle fragBundle = new Bundle();
                fragBundle.putString("selectedCity", value);
                fragment.setArguments(fragBundle);
                FragmentListener fragmentListener = (FragmentListener) getActivity();
                fragmentListener.startMainFragment(fragment, true);
            }
        });
    }

    private void checkAddEditBtnClick() {

        Button myButton = (Button) mView.findViewById(R.id.addEditCity);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CityListFragment fragment = new CityListFragment();
                FragmentListener fragmentListener = (FragmentListener) getActivity();
                fragmentListener.startMainFragment(fragment, true);
            }
        });
    }
}
