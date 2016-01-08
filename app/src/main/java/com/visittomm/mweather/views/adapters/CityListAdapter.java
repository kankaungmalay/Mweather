package com.visittomm.mweather.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.visittomm.mweather.R;
import com.visittomm.mweather.models.City;
import java.util.ArrayList;

/**
 * Created by monmon on 1/3/16.
 */
public class CityListAdapter extends ArrayAdapter<City> {

    public ArrayList<City> cityList;

    public CityListAdapter(Context context, int textViewResourceId,
                           ArrayList<City> cityList) {
        super(context, textViewResourceId, cityList);
        this.cityList = new ArrayList<City>();
        this.cityList.addAll(cityList);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.city_list_item, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.city);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    City country = (City) cb.getTag();
                    country.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        City city = cityList.get(position);
        holder.name.setText(city.getName());
        holder.name.setChecked(city.isSelected());
        holder.name.setTag(city);

        return convertView;

    }
}
