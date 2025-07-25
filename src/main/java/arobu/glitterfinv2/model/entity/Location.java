package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Location {
    @Id
    Integer id;
    @Column(unique = true)
    Integer geocodePlaceId;
    String countryCode;
    String postcode;
    String displayName;
    @Column(nullable = false)
    Double latitude;
    @Column(nullable = false)
    Double longitude;

    public Integer getId() {
        return id;
    }

    public Location setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getGeocodePlaceId() {
        return geocodePlaceId;
    }

    public Location setGeocodePlaceId(Integer geocodePlaceId) {
        this.geocodePlaceId = geocodePlaceId;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Location setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public Location setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Location setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Location setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
}

