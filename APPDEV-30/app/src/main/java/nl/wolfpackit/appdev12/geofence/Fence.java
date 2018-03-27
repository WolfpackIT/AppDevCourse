package nl.wolfpackit.appdev12.geofence;

import com.google.android.gms.location.GeofencingClient;

/**
 * Created by Wolfpack on 3/20/2018.
 */

public class Fence {
    private static double earthRadius = 6378100;
    private static double earthCircumference = earthRadius*Math.PI*2;
    private static double degCircumference = earthCircumference/360d;

    protected String name;
    protected double latitude;
    protected double longitude;
    protected double range = 10; //expressed in meters

    public Fence(String data){
        name = "invalid";
        latitude = 0;
        longitude = 0;

        String[] parts = data.split("=");
        try{
            name = parts[0];
            latitude = Double.parseDouble(parts[1]);
            longitude = Double.parseDouble(parts[2]);
        }catch(Exception e){}
    }
    public Fence(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getName(){return this.name.replace("=", ":");}
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
    public double getRange(){ return range; }

    public boolean isInRange(double lat, double lon){
        double deltaLat = (latitude-lat)*degCircumference; //expressed in meters
        double deltaLon = (longitude-lon)*degCircumference;
        double distSq = deltaLat*deltaLat + deltaLon*deltaLon;

        return range*range > distSq;
    }
    public String export(){
        return name+"="+latitude+"="+longitude;
    }
}
