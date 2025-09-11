package arobu.glitterfinv2.model.mapper;

import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;

import java.util.Map;

public class LocationMapper {

    public static Location toEntity(GeoCodeResponse geoCodeResponse) {
        Location location = new Location();

        Map<String, Object> addressMap = geoCodeResponse.getAddress();
        location
                .setGeocodePlaceId(geoCodeResponse.getPlaceId())
                .setLatitude(Double.valueOf(geoCodeResponse.getLat()))
                .setLongitude(Double.valueOf(geoCodeResponse.getLon()))
                .setCountryCode(String.valueOf(addressMap.getOrDefault("country_code", "")))
                .setPostcode(String.valueOf(addressMap.getOrDefault("postcode", "")))
                .setDisplayName(geoCodeResponse.getDisplayName())
                .setCountry(String.valueOf(addressMap.getOrDefault("country", "")))
                .setCounty(getStateOrCounty(addressMap))
                .setCity(getCityVillageOrTown(addressMap))
                .setRoad(String.valueOf(addressMap.getOrDefault("road", "")))
                .setHouseNumber(String.valueOf(addressMap.getOrDefault("house_number", "")));

        return location;
    }

    private static String getStateOrCounty(Map<String, Object> addressMap) {
        String county = (String) addressMap.getOrDefault("state", "");
        if (county.isEmpty()) {
            county = (String) addressMap.getOrDefault("county", "");
        }
        return county;
    }

    private static String getCityVillageOrTown(Map<String, Object> addressMap) {
        String city;
        city = (String) addressMap.getOrDefault("city", "");
        if  (city.isEmpty()) {
            city = (String) addressMap.getOrDefault("village", "");
            if (city.isEmpty()) {
                city = (String) addressMap.getOrDefault("town", "");
            }
        }
        return city;
    }
}
