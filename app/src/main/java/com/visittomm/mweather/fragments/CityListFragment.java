package com.visittomm.mweather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.visittomm.mweather.R;
import com.visittomm.mweather.interfaces.FragmentListener;
import com.visittomm.mweather.models.City;
import com.visittomm.mweather.models.SelectedCity;
import com.visittomm.mweather.utils.Utils;
import com.visittomm.mweather.views.adapters.CityListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by monmon on 12/27/15.
 */
public class CityListFragment extends Fragment {
    public static final String TAG              = "com.visittomm.mweather.fragments.CityListFragment";
    private static final String JSON_CACHE_FILE = "city-cache.json";
    private Context mContext                    = null;
    private View mView                          = null;
    ListView mListView                          = null;
    CityListAdapter dataAdapter                 = null;
    ArrayList<String> stringArrayList;

    public CityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, " =>> CityListFragment");

        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = this.getActivity();

        displayListView();
        checkButtonClick();

        return mView;
    }

    private void displayListView() {

        //Array list of countries
        ArrayList<City> cityList = new ArrayList<City>();
        City city = new City("Seoul",false);
        cityList.add(city);
        city = new City("Yangon",false);
        cityList.add(city);
        city = new City("Singapore",false);
        cityList.add(city);
        city = new City("NewYork",false);
        cityList.add(city);
        city = new City("Tokyo",false);
        cityList.add(city);
        city = new City("Thailand",false);
        cityList.add(city);
        city = new City("London",false);
        cityList.add(city);
        city = new City("Karachi",false);
        cityList.add(city);
        city = new City("Shanghai",false);
        cityList.add(city);
        city = new City("Delhi",false);
        cityList.add(city);

        /**
         * Read selected cities object from json
         */
        String jsonContent = null;
        try {
            FileInputStream jsonCache = mContext.openFileInput(JSON_CACHE_FILE);
            jsonContent = Utils.readStream(jsonCache);

                JSONArray jsonArray = new JSONArray(jsonContent);
                stringArrayList = new ArrayList<String>();
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    stringArrayList.add(json_data.getString("name")); // add to arraylist
                }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        ArrayList<City> cityList1 = cityList;
        if (stringArrayList != null) { // no cities in cache
            for(int i = 0; i < cityList1.size(); i++){
                City city1 = cityList1.get(i);

                if (stringArrayList.contains(city1.getName())) {
                    city1.setSelected(true);
                }
            }
        }

        // create an ArrayAdaptar from the String Array
        dataAdapter = new CityListAdapter(mContext, R.layout.city_list_item, cityList1);
        ListView listView = (ListView) mView.findViewById(R.id.lvRecordListing);
        listView.setAdapter(dataAdapter);
    }

    private void checkButtonClick() {


        Button myButton = (Button) mView.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<City> cityList = dataAdapter.cityList;
                ArrayList<SelectedCity> selectedCityArrayList = new ArrayList<SelectedCity>();

                for(int i = 0; i< cityList.size(); i++){
                    City city = cityList.get(i);
                    if(city.isSelected()){
                        SelectedCity selectedCity = new SelectedCity(city.getName());
                        selectedCityArrayList.add(selectedCity);
                    }
                }
                if (selectedCityArrayList.isEmpty()) {
                    Toast.makeText(mContext.getApplicationContext(),
                        "Please select a city for Weather Inof", Toast.LENGTH_LONG).show();
                } else {
                    // Write selected cities to disk as json
                    Gson gson = new Gson ();
                    byte[] jsonBytes = gson.toJson(selectedCityArrayList).getBytes();

                    // save json file
                    try {
                        mContext.deleteFile(JSON_CACHE_FILE);
                        FileOutputStream jsonCache = mContext.openFileOutput(JSON_CACHE_FILE, Context.MODE_PRIVATE);
                        jsonCache.write(jsonBytes);
                        jsonCache.flush();
                        jsonCache.close();
                    } catch (Exception exception) {
                        Log.e(TAG, exception.getMessage());
                        return;
                    }

                    CachedCityListFragment fragment = new CachedCityListFragment();
                    FragmentListener fragmentListener = (FragmentListener) getActivity();
                    fragmentListener.startMainFragment(fragment, true);
                }
            }
        });
    }
}
