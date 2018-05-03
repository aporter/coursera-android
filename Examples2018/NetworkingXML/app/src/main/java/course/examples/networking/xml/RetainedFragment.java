package course.examples.networking.xml;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetainedFragment extends Fragment {
    static final String TAG = "RetainedFragment";
    private OnFragmentInteractionListener mListener;
    private List<String> mData;

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

    private void onDownloadFinished(List<String> result) {
        mData = result;
        if (null != mListener) {
            mListener.onDownloadfinished();
        }
    }

    List<String> getData() {
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

    static class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        private static final String TAG = "HttpGetTask";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";

        private static final String HOST = "api.geonames.org";
        private static final String URL = "http://" + HOST + "/earthquakes?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
                + USER_NAME;
        public static final String EARTHQUAKE_TAG = "earthquake";

        private final WeakReference<RetainedFragment> mListener;


        private static final String MAGNITUDE_TAG = "magnitude";
        private static final String LONGITUDE_TAG = "lng";
        private static final String LATITUDE_TAG = "lat";
        private String mLat, mLng, mMag;
        private boolean mIsParsingLat, mIsParsingLng, mIsParsingMag;
        private final List<String> mResults = new ArrayList<>();


        HttpGetTask(RetainedFragment retainedFragment) {
            mListener = new WeakReference<>(retainedFragment);
        }


        @Override
        protected List<String> doInBackground(Void... params) {
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
            return parseXmlString(data);
        }


        @Override
        protected void onPostExecute(List<String> result) {

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

        private List<String> parseXmlString(String data) {

            try {

                // Create the Pull Parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(data));

                // Get the first Parser event and start iterating over the XML document
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        startTag(xpp.getName());
                    } else if (eventType == XmlPullParser.END_TAG) {
                        endTag(xpp.getName());
                    } else if (eventType == XmlPullParser.TEXT) {
                        text(xpp.getText());
                    }
                    eventType = xpp.next();
                }
                return mResults;
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void startTag(String localName) {
            switch (localName) {
                case LATITUDE_TAG:
                    mIsParsingLat = true;
                    break;
                case LONGITUDE_TAG:
                    mIsParsingLng = true;
                    break;
                case MAGNITUDE_TAG:
                    mIsParsingMag = true;
                    break;
            }
        }

        private void text(String text) {
            if (mIsParsingLat) {
                mLat = text.trim();
            } else if (mIsParsingLng) {
                mLng = text.trim();
            } else if (mIsParsingMag) {
                mMag = text.trim();
            }
        }

        private void endTag(String localName) {
            switch (localName) {
                case LATITUDE_TAG:
                    mIsParsingLat = false;
                    break;
                case LONGITUDE_TAG:
                    mIsParsingLng = false;
                    break;
                case MAGNITUDE_TAG:
                    mIsParsingMag = false;
                    break;
                case EARTHQUAKE_TAG:
                    mResults.add(MAGNITUDE_TAG + ":" + mMag + "," + LATITUDE_TAG + ":"
                            + mLat + "," + LONGITUDE_TAG + ":" + mLng);
                    mLat = null;
                    mLng = null;
                    mMag = null;
                    break;
            }
        }
    }
}
