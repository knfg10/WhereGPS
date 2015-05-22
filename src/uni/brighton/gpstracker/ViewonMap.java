package uni.brighton.gpstracker;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ViewonMap extends ActionBarActivity {

	private GoogleMap ViewMap;
	MapView mMapView;

	ArrayList<GPSdb> gpsLoc;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewmap_main); 	// Use pre-made Layout xml file

		// Creates and loads map
		ViewMap = ((SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.viewmap)).getMap(); 	

		// Enabling the use of Location
		ViewMap.setMyLocationEnabled(true);
		ViewMap.getUiSettings().setCompassEnabled(true);
		ViewMap.getUiSettings().setAllGesturesEnabled(true);
		ViewMap.getUiSettings().setZoomControlsEnabled(true);

		//Tests If Map Loads
		if (ViewMap == null) {
			Toast.makeText(this, "Google Maps not available", 
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu3) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main2, menu3);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item2) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		// This implements all the accessable menus
		switch (item2.getItemId()) {


		// Map View Types Option Menu
		case R.id.action_normal:
			ViewMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.action_terrain:
			ViewMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case R.id.action_hybrid:
			ViewMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.action_satellite:
			ViewMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;

			// Option Menu for Finding items 
		case R.id.menu_findItem:
			// Retrieving Current database entries
			GPSdb TrackGps = new GPSdb();
			MainActivity TrackGPS = new MainActivity(); 
			MySQLiteHelper locdb = new MySQLiteHelper(this);
			
			// goes through database retrieving all the values from each entry 
			for (int i = 0; i < locdb.getAllLocs().size(); i++) {

				int id1 = locdb.getAllLocs().get(i).getId();
				String nam1 = locdb.getAllLocs().get(i).getName();
				String lat1 = locdb.getAllLocs().get(i).getLat();
				String lon1 = locdb.getAllLocs().get(i).getLon();
				String dat1 = locdb.getAllLocs().get(i).getDate();
				int pos1 = locdb.getAllLocs().get(i).getId();

				double latt = Double.parseDouble(lat1);
				double lonn = Double.parseDouble(lon1);

				LatLng LocPoss2 = new LatLng(latt,lonn);

				// Sets the camera position near current location an items 
				CameraPosition myPosition11 = new CameraPosition.Builder()
				.target(LocPoss2).zoom(14).bearing(5).tilt(15).build();
				ViewMap.animateCamera(
						CameraUpdateFactory.newCameraPosition(myPosition11));

				// creates a marker on the map of the entry with its details 
				ViewMap.addMarker(new MarkerOptions()
				.position(LocPoss2)
				.title("Item: " + nam1 + " - ID: " + id1 )
				.snippet("DateStamp: " + dat1)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
			}
			
//			// Resets the whole table 
//			if (locdb.getAllLocs().size() >= 2)
//			{
//				locdb.onUpgrade(locdb.getWritableDatabase(), 1, 1);
//			}
			break;
			
			// displays the current entries stored within the database
		case R.id.menu_CurrentDB:
			MySQLiteHelper locdb2 = new MySQLiteHelper(this);
			for (int i = 0; i < locdb2.getAllLocs().size(); i++) {

				int id1 = locdb2.getAllLocs().get(i).getId();
				String nam1 = locdb2.getAllLocs().get(i).getName();
				String lat1 = locdb2.getAllLocs().get(i).getLat();
				String lon1 = locdb2.getAllLocs().get(i).getLon();
				String dat1 = locdb2.getAllLocs().get(i).getDate();

				Toast.makeText(this, "Current Database: \n Items In Database: " + locdb2.getAllLocs().size() + " \n ID: " + id1 + " \n Item: " + nam1 + " \n Lat: " + lat1 + " \n Lon: " + lon1 + " \n Date: " + dat1, Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}
}
