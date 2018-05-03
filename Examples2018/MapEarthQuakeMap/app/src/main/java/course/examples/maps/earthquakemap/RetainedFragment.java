package course.examples.maps.earthquakemap;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetainedFragment extends Fragment {
    static final String TAG = "RetainedFragment";
    private OnFragmentInteractionListener mListener;
    private List<EarthQuakeRec> mData;

    public RetainedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void onButtonPressed() {
        new HttpGetTask(this).execute();
    }

    private void onDownloadFinished(List<EarthQuakeRec> result) {
        mData = result;
        if (null != mListener) {
            mListener.onDownloadfinished();
        }
    }

    List<EarthQuakeRec> getData() {
        return mData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onDownloadfinished();
    }

    static class HttpGetTask extends AsyncTask<Void, Void, List<EarthQuakeRec>> {

        private static final String TAG = "HttpGetTask";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";

        private static final String HOST = "api.geonames.org";
        private static final String URL = "http://" + HOST + "/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
                + USER_NAME;

        private final WeakReference<RetainedFragment> mListener;


        HttpGetTask(RetainedFragment retainedFragment) {
            mListener = new WeakReference<>(retainedFragment);
        }


        @Override
        protected List<EarthQuakeRec> doInBackground(Void... params) {
            String data = null;
            HttpURLConnection httpUrlConnection = null;

            try {
                // 1. Get connection. 2. Prepare request (URI)
                httpUrlConnection = (HttpURLConnection) new URL(URL)
                        .openConnection();

                // 3. This app does not use a request body
                // 4. Read the response
                InputStream in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());

                data = readStream(in);
            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (null != httpUrlConnection) {
                    // 5. Disconnect
                    httpUrlConnection.disconnect();
                }
            }
            // Parse the JSON-formatted response
            return parseJsonString(data);
        }


        @Override
        protected void onPostExecute(List<EarthQuakeRec> result) {
            if (null != mListener.get()) {
                mListener.get().onDownloadFinished(result);
            }
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder data = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }
                }
            }
            return data.toString();
        }

        private List<EarthQuakeRec> parseJsonString(String data) {

            String LONGITUDE_TAG = "lng";
            String LATITUDE_TAG = "lat";
            String MAGNITUDE_TAG = "magnitude";
            String EARTHQUAKE_TAG = "earthquakes";

            List<EarthQuakeRec> result = new ArrayList<>();

            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        data).nextValue();

                // Extract value of "earthquakes" key -- a List
                JSONArray earthquakes = responseObject
                        .getJSONArray(EARTHQUAKE_TAG);

                // Iterate over earthquakes list
                for (int idx = 0; idx < earthquakes.length(); idx++) {

                    // Get single earthquake mData - a Map
                    JSONObject earthquake = (JSONObject) earthquakes.get(idx);

                    // Summarize earthquake mData as a string and add it to
                    // result
                    result.add(new EarthQuakeRec(
                            Double.valueOf(earthquake.get(LATITUDE_TAG).toString()),
                            Double.valueOf(earthquake.get(LONGITUDE_TAG).toString()),
                            Double.valueOf(earthquake.get(MAGNITUDE_TAG).toString())));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
