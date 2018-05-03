package course.examples.networking.sockets;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class RetainedFragment extends Fragment {
    static final String TAG = "RetainedFragment";
    private OnFragmentInteractionListener mListener;

    public RetainedFragment() {
        // Required empty public constructor
    }

    void onButtonPressed() {
        new HttpGetTask(this).execute();
    }

    private void onDownloadFinished(String result) {
        if (null != mListener) {
            mListener.onDownloadfinished(result);
        }
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
        void onDownloadfinished(String result);
    }


    static class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "HttpGetTask";
        private static final String HOST = "api.geonames.org";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";
        private static final String HTTP_GET_COMMAND = "GET /earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
                + USER_NAME + " HTTP/1.1" + "\n" + "Host: " + HOST + "\n"
                + "Connection: close" + "\n\n";

        private final WeakReference<RetainedFragment> mListener;

        HttpGetTask(RetainedFragment retainedFragment) {
            mListener = new WeakReference<>(retainedFragment);
        }

        @Override
        protected String doInBackground(Void... params) {
            Socket socket = null;
            String data = "";

            try {
                socket = new Socket(HOST, 80);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream()), true);
                pw.println(HTTP_GET_COMMAND);

                data = readStream(socket.getInputStream());

            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (null != socket)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
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
    }
}
