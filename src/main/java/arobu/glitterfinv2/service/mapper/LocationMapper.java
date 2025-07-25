package arobu.glitterfinv2.service.mapper;

import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;

public class LocationMapper {

    public static Location toEntity(GeoCodeResponse geoCodeResponse) {
        Location location = new Location();

        location
                .setGeocodePlaceId(geoCodeResponse.getPlaceId())
                .setLatitude(Double.valueOf(geoCodeResponse.getLat()))
                .setLongitude(Double.valueOf(geoCodeResponse.getLon()))
                .setCountryCode(String.valueOf(geoCodeResponse.getAddress().get("country_code")))
                .setPostcode(String.valueOf(geoCodeResponse.getAddress().get("postcode")))
                .setDisplayName(geoCodeResponse.getDisplayName());

        return location;
    }
}
