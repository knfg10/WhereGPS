package uni.brighton.gpstracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context; 
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.pm.ActivityInfo; 

import android.location.Location;  
import android.location.LocationListener;  
import android.os.Bundle;  


/*---------- Listener class to get coordinates ------------- */
class MyLocationListener implements LocationListener   
//implements OnClickListener
//implements LocationListener 
{
 
	   
	//private Button btnGetLocation = null;  
	public static double latitude;
	public static double longitude;
	

	
	@Override
    public void onLocationChanged(Location loc) {
        
    	loc.getLatitude();  
        loc.getLongitude();  
        latitude=loc.getLatitude();  
        longitude=loc.getLongitude(); 
    	
    }
	@Override
    public void onProviderDisabled(String provider) {}
	@Override
    public void onProviderEnabled(String provider) {}
	@Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}


}
