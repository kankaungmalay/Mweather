package com.visittomm.mweather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.visittomm.mweather.JSONWeatherParser;
import com.visittomm.mweather.R;
import com.visittomm.mweather.WeatherHttpClient;
import com.visittomm.mweather.models.Weather;
import org.json.JSONException;

/**
 * Created by monmon on 12/27/15.
 */
public class WeatherDetailFragment extends Fragment {

    public static final String TAG  = "com.visittomm.mweather.fragments.WeatherDetailFragment";
    private Context mContext        = null;
    private View mView              = null;
    private TextView cityText;
    private TextView condDescr;
    private ImageView imgView;
    private TextView temp;
    String selectedCity;


    public WeatherDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "=>> WeatherDetailFragment");

        mView = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        mContext = this.getActivity();
        Bundle args = getArguments();
        selectedCity = args.getString("selectedCity");

        cityText = (TextView) mView.findViewById(R.id.cityText);
        condDescr = (TextView) mView.findViewById(R.id.condDescr);
        temp = (TextView) mView.findViewById(R.id.temp);
        imgView = (ImageView) mView.findViewById(R.id.condIcon);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{selectedCity});

        return mView;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            // ((AppCompatActivity) mContext).getSupportActionBar().setTitle(weather.location.getCity());
            cityText.setText(weather.location.getCity() + ", " + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "\u00B0C");
        }
    }
}
