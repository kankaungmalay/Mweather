package com.visittomm.mweather.utils;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.visittomm.mweather.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by monmon on 1/3/16.
 */
public class Utils {
    /**
     * read input stream and return contents as string
     */
    public static String readStream (FileInputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder content = new StringBuilder();
        String line = null;

        while ( (line = reader.readLine()) != null) {
            content.append(line);
        }

        return content.toString();
    }
}
