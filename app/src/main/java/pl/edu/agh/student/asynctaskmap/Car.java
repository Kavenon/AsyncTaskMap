package pl.edu.agh.student.asynctaskmap;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Car {

    private final double lat;
    private final double lon;
    private final String name;

    public Car(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public void addToMap(GoogleMap map){
        LatLng position = new LatLng(this.getLat(), this.getLon());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(this.getName())
                .snippet("Car: "+this.getName()));

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(position, 15.0f);
        map.animateCamera(yourLocation);
    }
}
