package arobu.glitterfinv2.service.external.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodeResponse {
    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("display_name")
    private String displayName;

    private String lat;
    private String lon;

    private Map<String, Object> address;

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

    @Override
    public String toString() {
        return "GeoCodeResponse{" +
                "placeId=" + placeId +
                ", displayName='" + displayName + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", address=" + address +
                '}';
    }
}