package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.co.erailway.erailway.Constants;

public class HttpHandler {
	//Context context;
	private String TAG = "Http error";
	private static HttpHandler mInstance;

	public interface HttpDataListener{
		public void onDataAvailable(String response);

		public void onError(Exception e);
	}

	public static HttpHandler getSharedInstance(){
		if(mInstance == null) {
			mInstance = new HttpHandler();
		}
		return mInstance;
	}

	public void getPNRStatus(String pnr_number, HttpDataListener dataListener){
		final String url = Constants.HOST + "/railways/pnr_status?pnr=" + pnr_number;
//		String res = "{\"train_number\":\"12632\",\"train_name\":\"NELLAI EXPRESS \",\"doj\":\" 2- 1-2017\",\"from\":\"MDU \",\"to\":\"MS\",\n" +
//				"\t\"reservation_upto\":\"MS  \",\"boarding_point\":\"MDU \",\"class\":\" SL\",\n" +
//				"\t\"passengers\":[{\"number\":\"1\",\"booking_status\":\"W/L18,PQWL  \",\"current_status\":\"W/L   13\"}],\n" +
//				"\t\"pnr\":\"4745954614\",\"fare\":\"315\",\"status\":\" CHART NOT PREPARED \"}";
//		dataListener.onDataAvailable(res);
		new AsyncHttpTask(dataListener).execute(url, "GET");
	}

	public void getVersionCode(HttpDataListener dataListener) {
		final String url = Constants.HOST + "/android/min_version";
		new AsyncHttpTask(dataListener).execute(url, "GET");

	}

	private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

		String response;
		HttpDataListener dataListener;
		private AsyncHttpTask(HttpDataListener dataListener){
			this.dataListener = dataListener;
		}

		@Override
		protected Integer doInBackground(String... params) {
			InputStream inputStream = null;
			HttpURLConnection urlConnection = null;
			Integer result = 0;
			try {

                /* forming th java.net.URL object */
				URL url = new URL(params[0]);
				String httpMethod = params[1];
				urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
				urlConnection.setRequestProperty("Content-Type", "application/json");
				urlConnection.setRequestProperty("Accept", "application/json");
				urlConnection.setRequestMethod(httpMethod);

				if(httpMethod.equals("POST")){
					byte[] outputInBytes = params[2].getBytes("UTF-8");
					OutputStream os = urlConnection.getOutputStream();
					os.write( outputInBytes );
					os.close();
				}

				Log.d("Request url", url.toString());
				int statusCode = urlConnection.getResponseCode();

				if (statusCode >= 200 && statusCode < 300) {
					inputStream = new BufferedInputStream(urlConnection.getInputStream());
					response = convertInputStreamToString(inputStream);
					Log.d("response", response);

					result = 1;

					//hack
					if(response.contains("error")) {
						result = 0;
					}
				} else {
					Log.e(TAG, "Response code not ok " + statusCode);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				dataListener.onDataAvailable(response);
			} else {
				dataListener.onError(new Exception("Status code not OK."));
			}
		}
	}

	private String convertInputStreamToString(InputStream inputStream) throws IOException {

		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

		String line = "";
		String result = "";

		while((line = bufferedReader.readLine()) != null){
			result += line;
		}

            /* Close Stream */
		if(null!=inputStream){
			inputStream.close();
		}

		return result;
	}

	public static boolean hasWorkingInternet(Context context){
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}

