package uni.brighton.gpstracker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.sourceforge.jtds.jdbc.*;


public class MainActivity extends ActionBarActivity {

	CheckBox SendGPSLoc;
	GoogleMap MapLoc;
	EditText GPSName;
	EditText GPSLat;
	EditText GPSLon;
	LocationManager mlocManager=null;
	LocationListener mlocListener;
	AlertDialog.Builder alert;

	ArrayList<Double> Lat = new ArrayList<Double>();
	ArrayList<Double> Lon = new ArrayList<Double>();
	ArrayList<String> GPSnm = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Activates functions once button is pressed
		SendGPSLoc();
		ViewMap();

		// Here a editbox is created and a default value is added
		EditText inputTxt = (EditText) findViewById(R.id.GPSName);
		String Name = inputTxt.getText().toString().trim();
		String Name2 = "Key";
		GPSnm.add(0, Name2); // current value is added to an array
		inputTxt.setText(Name2);

		//Creates EditText boxes where gps will be stored
		alert  = new AlertDialog.Builder(this);
		alert.setCancelable(true);
		GPSLat=(EditText)findViewById(R.id.GPSLat);
		GPSLon=(EditText)findViewById(R.id.GPSLon);

		// GPS of device is established by implementing values from another class
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();	        
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
		mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Button bnt = (Button) findViewById(R.id.bnt1);
		bnt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) 
				{  
					if(MyLocationListener.latitude>0)  
					{  
						// Clears last gps locations from array 
						Lat.clear();
						Lon.clear();

						// defines the gps locations from another class
						double Latitude = MyLocationListener.latitude;
						double Longitude = MyLocationListener.longitude;

						// values are re-added to the array  
						Lat.add(Latitude);
						Lon.add(Longitude);

						// locations are made into a string then added to the text box
						String Latitude1 = String.valueOf(Lat.get(0)); 
						String Longitude1 = String.valueOf(Lon.get(0));
						GPSLat.setText(Latitude1);  
						GPSLon.setText(Longitude1); 

					}  
					else  
					{  
						// incase no gps available, a warning will appear
						alert.setTitle("Loading");  
						alert.setMessage("GPS in progress, May take a minute, Please Wait.");  
						alert.setPositiveButton("OK", null);  
						alert.show();  
					}  
				} 
				else 
				{  
					GPSLat.setText("GPS is not turned on...");
					GPSLon.setText("GPS is not turned on...");
				}  

			}  
		});}



	public void SendGPSLoc() {

		// EditText boxes are defined
		final EditText inputTxt = (EditText) findViewById(R.id.GPSName);
		final String Name = inputTxt.getText().toString().trim();
		final EditText inputTxt2 = (EditText) findViewById(R.id.GPSLat);
		final String LAT = inputTxt2.getText().toString().trim();
		final EditText inputTxt3 = (EditText) findViewById(R.id.GPSLon);
		final String LON = inputTxt3.getText().toString().trim();
		
		Button bnt2 = (Button) findViewById(R.id.bnt2);
		bnt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// these if statement check to see if there is a value missing when trying to 
				// add the entry to the database, if something is empty, a Toast is appear stating it
				if (inputTxt.length() <= 0 && inputTxt2.length() <= 0 && inputTxt3.length() <= 0 )
				{
					Toast.makeText(MainActivity.this, "Please fill in Text", Toast.LENGTH_SHORT).show();
				}
				else if (inputTxt.length() <= 0 || inputTxt2.length() <= 0 || inputTxt3.length() <= 0 )
				{
					Toast.makeText(MainActivity.this, "Please fill in Text", Toast.LENGTH_SHORT).show();
				}
				else if ( inputTxt.length() > 0 || inputTxt2.length() > 0 || inputTxt3.length() > 0 )
				{
					// if all fields are filled, this function will run
					tryGPS();
				}
			}
		});
	}

	// the function creates and starts the map activity 
	public void ViewMap() {

		// creates an intent of the map class
		final Intent ViewOnMap = new Intent(this, ViewonMap.class); 
		Button bnt2 = (Button) findViewById(R.id.bnt3);
		bnt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Starts the map activity
				startActivity(ViewOnMap);
			}
		});
	}

	// This function will add the entry to the database 
	public void tryGPS() {

		// instances are created to classes 
		MySQLiteHelper db = new MySQLiteHelper(this);
		List<GPSdb> Locdbs = db.getAllLocs();

		// EditText is created by id, then value of EditTText is added to the array
		EditText inputTxt = (EditText) findViewById(R.id.GPSName);
		String Name = inputTxt.getText().toString().trim();
		String Name2 = "Key";
		GPSnm.add(0, Name);

		// location values are brought from array
		String LatList = String.valueOf(Lat.get(0)); 
		String LonList = String.valueOf(Lon.get(0));

		// gets current date and time, and is formatted
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => "+c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());
		
		// user is notified if adding entry was successful 
		Toast.makeText(this, "Added to Database: \n Item: " + GPSnm.get(0) + " \n Lat: " + LatList + " \n Lon: " + LonList + " \n Date: " + formattedDate, Toast.LENGTH_LONG).show();

		// this if statement prvents more then 3 entries to be stored at one time
		if (db.getAllLocs().size() <= 2)
		{
			db.addgps(new GPSdb(GPSnm.get(0) , LatList , LonList, formattedDate));  
		}
		else
		{
			// entries are deleted if more then 3 were made
			db.deleteLoc(Locdbs.get(0));
			db.deleteLoc(Locdbs.get(1));
			db.addgps(new GPSdb(GPSnm.get(0) , LatList , LonList, formattedDate));
		}
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {


		// Map View Types Option Menu
		case R.id.action_DB:
			
			// brings in the database, which then goes through the database displaying all the entries 
			// that are currently stored within the database 
			MySQLiteHelper locdb = new MySQLiteHelper(this);
			for (int i = 0; i < locdb.getAllLocs().size(); i++) {

				int id1 = locdb.getAllLocs().get(i).getId();
				String nam1 = locdb.getAllLocs().get(i).getName();
				String lat1 = locdb.getAllLocs().get(i).getLat();
				String lon1 = locdb.getAllLocs().get(i).getLon();
				String dat1 = locdb.getAllLocs().get(i).getDate();

				Toast.makeText(this, "Current Database: \n Items In Database: " + locdb.getAllLocs().size() + " \n ID: " + id1 + " \n Item: " + nam1 + " \n Lat: " + lat1 + " \n Lon: " + lon1 + " \n Date: " + dat1, Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}
}
