package arobu.glitterfinv2.model.dto;

public class LocationData {
    private String latitude;
    private String longitude;

    public LocationData(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationData() {
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
