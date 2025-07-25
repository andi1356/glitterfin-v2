package arobu.glitterfinv2.service.external.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeoCodeResponse {
    @JsonProperty("place_id")
    private int placeId;

    private String lat;
    private String lon;

    @JsonProperty("display_name")
    private String displayName;

    private Map<String, Object> address;

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
}