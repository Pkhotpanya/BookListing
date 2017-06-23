package com.example.android.booklisting;

/**
 * Created by pkhotpanya on 6/23/17.
 */

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public final class QueryUtils {

    private static final String ITEMS = "items";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String VOLUMEINFO = "volumeInfo";

    private QueryUtils() {
    }

    public static List<Book> extractBooks(String url) {
        return fetchBookData(url);
    }

    public static ArrayList<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Error closing input stream", e);
        }

        ArrayList<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(bookJSON);
            JSONArray featuresArray = jsonObject.optJSONArray(ITEMS);
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject temp = featuresArray.optJSONObject(i);
                JSONObject volumeInfo = temp.getJSONObject(VOLUMEINFO);
                String title = volumeInfo.getString(TITLE);
                JSONArray authors = volumeInfo.optJSONArray(AUTHORS);
                List<String> authorList = new ArrayList<String>();
                for (int authorsIndex = 0; authorsIndex < authors.length(); authorsIndex++) {
                    authorList.add(authors.getString(authorsIndex));
                }
                books.add(new Book(title, authorList));
            }
            return books;

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return null;
    }

}