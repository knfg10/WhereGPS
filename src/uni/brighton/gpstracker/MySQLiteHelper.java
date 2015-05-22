package uni.brighton.gpstracker;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {


	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "GPSDB";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}

	@Override // Creates Database
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create gps table
		db.execSQL("DROP TABLE IF EXISTS GPS");
		String CREATE_GPS_TABLE = "CREATE TABLE GPS ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"name TEXT, "+
				"lat TEXT, "+
				"lon TEXT, "+
				"date TEXT )";

		// create table
		db.execSQL(CREATE_GPS_TABLE);
	}

	@Override // will delete old table and make a new one
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS GPS");

		// create fresh table
		this.onCreate(db);
	}

 
    // table name
    private static final String TABLE_GPS = "GPS";
 
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_DATE = "date";
 
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_LAT,KEY_LON,KEY_DATE};
 
    // function to add entry to the database
    public void addgps(GPSdb gps){
        Log.d("addgps", gps.toString());
        
        // reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, gps.getName()); // get name 
        values.put(KEY_LAT, gps.getLat()); // get lat
        values.put(KEY_LON, gps.getLon()); // get lon 
        values.put(KEY_DATE, gps.getDate()); // get date
 
        // insert to table
        db.insert(TABLE_GPS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // close table 
        db.close(); 
    }
 
    // retrieves a selected entry
    public GPSdb getGPS(int id){
 
        // reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // build query
        Cursor cursor = 
                db.query(TABLE_GPS, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // build gps object
        GPSdb gps = new GPSdb();
        gps.setId(Integer.parseInt(cursor.getString(0)));
        gps.setName(cursor.getString(1));
        gps.setLat(cursor.getString(2));
        gps.setLon(cursor.getString(3));
        gps.setDate(cursor.getString(4));
 
        Log.d("getLoc("+id+")", gps.toString());
 
        // return gps
        return gps;
    }
    
    // puts all entries into a array 
    public List<GPSdb> getAllLocs() {
        List<GPSdb> locs = new LinkedList<GPSdb>();
  
        // build the query
        String query = "SELECT  * FROM " + TABLE_GPS;
  
        // reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        // go over each row, build and add it to list
        GPSdb gps = null;
        if (cursor.moveToFirst()) {
            do {
                gps = new GPSdb();
                gps.setId(Integer.parseInt(cursor.getString(0)));
                gps.setName(cursor.getString(1));
                gps.setLat(cursor.getString(2));
                gps.setLon(cursor.getString(3));
                gps.setDate(cursor.getString(4));
  
                // Add GPS to table
                locs.add(gps);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllLocs()", locs.toString());
  
        // return gps
        return locs;
    }
    
    
 // Updating single gps entry
    public int updateLoc(GPSdb gps) {
 
        // reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", gps.getName()); // get name 
        values.put("lat", gps.getLat()); // get lat
        values.put("lon", gps.getLon()); // get lon 
        values.put("date", gps.getDate()); // get date
 
        // updating row
        int i = db.update(TABLE_GPS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(gps.getId()) }); //selection args
 
        // close
        db.close();
        return i;
    }
 
    // Deleting single entry
    public void deleteLoc(GPSdb gps) {
 
        // reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // delete entry from ID
        db.delete(TABLE_GPS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(gps.getId()) });
 
        // close
        db.close();
 
        Log.d("deleteLoc", gps.toString());
    }
}
