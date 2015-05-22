package uni.brighton.gpstracker;

public class GPSdb {

	private int id;
	private String name;
	private String lat;
	private String lon;
	private String date;

	public GPSdb(){}

	public GPSdb(String name, String lat, String lon, String date) {
		super();
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.date = date;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	@Override
	public String toString() {
		return "GPS [id=" + id + ", name=" + name + ", lat=" + lat + ", lon=" + lon + ", date=" + date
				+ "]";
	}

}
