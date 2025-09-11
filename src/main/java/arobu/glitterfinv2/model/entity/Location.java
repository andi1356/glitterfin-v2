package arobu.glitterfinv2.model.entity;

import jakarta.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    String country;
    String county;
    String city;
    String road;
    String houseNumber;

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

    public String getCountry() {
        return country;
    }

    public Location setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public Location setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Location setCity(String city) {
        this.city = city;
        return this;
    }

    public String getRoad() {
        return road;
    }

    public Location setRoad(String road) {
        this.road = road;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public Location setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", geocodePlaceId=" + geocodePlaceId +
                ", postcode='" + postcode + '\'' +
                ", displayName='" + displayName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", road='" + road + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
