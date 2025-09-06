package arobu.glitterfinv2.service.external.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoCodeResponse {
    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("display_name")
    private String displayName;

    private String lat;
    private String lon;

    private String licence;
    private String osm_type;
    private String osm_id;
    private Map<String, Object> address;
    @JsonProperty("boundingbox")
    private List<String> boundingBox;

    public GeoCodeResponse() {
    }

    public GeoCodeResponse(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        this.displayName = "";
        this.address = new HashMap<>();
    }

  public int getPlaceId() {
        return placeId;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<String, Object> getAddress() {
        return address;
    }

    public String getLicence() {
        return licence;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public String getOsm_id() {
        return osm_id;
    }

    public List<String> getBoundingBox() {
        return boundingBox;
    }

    @Override
    public String toString() {
        return "GeoCodeResponse{" +
                "placeId=" + placeId +
                ", displayName='" + displayName + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}