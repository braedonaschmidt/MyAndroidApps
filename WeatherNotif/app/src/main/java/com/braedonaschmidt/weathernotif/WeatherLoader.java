package com.braedonaschmidt.weathernotif;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class WeatherLoader extends AsyncTaskLoader<Weather> {
	private String mUrl;
	LocationManager locationManager;
	Context mContext;
	
	public WeatherLoader(Context context, String url) {
		super(context);
		
		mUrl = url;
		getLocation();
		mContext = context;
	}
	
	@Override
	protected void onStartLoading() {
		forceLoad();
	}
	
	@Override
	public Weather loadInBackground() {
		return (mUrl == null)? null: QueryUtils.retrieveWeather(mUrl);
	}
	
	private void getLocation() {
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());
				int latIndex = mUrl.indexOf("{lat}");
				mUrl = mUrl.substring(0, latIndex) + lat + mUrl.substring(latIndex + 5);
				
				int lonIndex = mUrl.indexOf("{lon}");
				mUrl = mUrl.substring(0, lonIndex) + lon + mUrl.substring(lonIndex + 5);
			}
			
			@Override
			public void onStatusChanged(String s, int i, Bundle bundle) {
			
			}
			
			@Override
			public void onProviderEnabled(String s) {
			
			}
			
			@Override
			public void onProviderDisabled(String s) {
			
			}
		};
		
		try {
			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
			assert locationManager != null;
			locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
