package course.labs.locationlab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class PlaceDownloaderTask extends AsyncTask<Location, Void, PlaceRecord> {

	// TODO - put your www.geonames.org account name here.
	private static String USERNAME = "YOUR USERNAME HERE";

	private HttpURLConnection mHttpUrl;
	private WeakReference<PlaceViewActivity> mParent;

	public PlaceDownloaderTask(PlaceViewActivity parent) {
		super();
		mParent = new WeakReference<PlaceViewActivity>(parent);
	}

	@Override
	protected PlaceRecord doInBackground(Location... location) {

		PlaceRecord place = getPlaceFromURL(generateURL(USERNAME, location[0]));

		if ("" != place.getCountryName()) {
			place.setLocation(location[0]);
			place.setFlagBitmap(getFlagFromURL(place.getFlagUrl()));
		} else {
			place = null;
		}
		
		return place;

	}

	@Override
	protected void onPostExecute(PlaceRecord result) {

		if (null != result && null != mParent.get()) {
			mParent.get().addNewPlace(result);
		}
	}

	private PlaceRecord getPlaceFromURL(String... params) {
		String result = null;
		BufferedReader in = null;

		try {
			URL url = new URL(params[0]);
			mHttpUrl = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(
					mHttpUrl.getInputStream()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();

		} catch (MalformedURLException e) {
			
		} catch (IOException e) {
			
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mHttpUrl.disconnect();
		}

		return placeDataFromXml(result);
	}

	private Bitmap getFlagFromURL(String flagUrl) {

		InputStream in = null;

		try {
			URL url = new URL(flagUrl);
			mHttpUrl = (HttpURLConnection) url.openConnection();
			in = mHttpUrl.getInputStream();

			return BitmapFactory.decodeStream(in);

		} catch (MalformedURLException e) {
			Log.e("DEBUG",e.toString());
		} catch (IOException e) {
			Log.e("DEBUG",e.toString());
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mHttpUrl.disconnect();
		}
		
		return BitmapFactory.decodeResource(mParent.get().getResources(), R.drawable.stub);
	}

	private static PlaceRecord placeDataFromXml(String xmlString) {
		DocumentBuilder builder;
		String countryName = "";
		String countryCode = "";
		String placeName = "";
		String elevation = "";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xmlString)));
			NodeList list = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node curr = list.item(i);

				NodeList list2 = curr.getChildNodes();

				for (int j = 0; j < list2.getLength(); j++) {

					Node curr2 = list2.item(j);
					if (curr2.getNodeName() != null) {

						if (curr2.getNodeName().equals("countryName")) {
							countryName = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("countryCode")) {
							countryCode = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("name")) {
							placeName = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("elevation")) {
							elevation = curr2.getTextContent();
						}
					}
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new PlaceRecord(
				generateFlagURL(countryCode.toLowerCase()),
				countryName, placeName, elevation);
	}

	private static String generateURL(String username, Location location) {
		
		return "http://www.geonames.org/findNearbyPlaceName?username="
				+ username + "&style=full&lat=" + location.getLatitude()
				+ "&lng=" + location.getLongitude();
	}

	private static String generateFlagURL(String countryCode) {
		return "http://www.geonames.org/flags/x/" + countryCode + ".gif";
	}

}
