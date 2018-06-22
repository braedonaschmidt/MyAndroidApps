package com.braedonaschmidt.weathernotif;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Weather> {
	private TextView textView;
	String url = "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&units=imperial&appid=33bbf998eca44c15a7e43045379bce96";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		
		textView = (TextView) findViewById(R.id.textView);
		
		if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
		else {
			LoaderManager loaderManager = getLoaderManager();
			loaderManager.initLoader(1, null, this);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public Loader<Weather> onCreateLoader(int i, Bundle bundle) {
		return new WeatherLoader(this, url); //todo: option for metric
	}
	
	@Override
	public void onLoadFinished(Loader<Weather> loader, Weather weather) {
		//todo: progress bar
		
		textView.setText("high: " + weather.getHigh() + "\nlow: " + weather.getLow() + "\ndesc: " + weather.getDesc());
		
		//todo: emptyStateTextView
	}
	
	@Override
	public void onLoaderReset(Loader<Weather> loader) {
		//todo: something about location?
		textView.setText("Hello World!");
	}
}

//todo: get location more accurately (city id?)
