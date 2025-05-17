package co.edu.uniquindio.prasegured.dto;

public class LocationDTO {
    private double lat;
    private double lng;

    public LocationDTO(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    // Getters y setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}