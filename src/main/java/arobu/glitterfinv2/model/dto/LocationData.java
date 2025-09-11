package arobu.glitterfinv2.model.dto;

public class LocationData {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public LocationData setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationData setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
}
