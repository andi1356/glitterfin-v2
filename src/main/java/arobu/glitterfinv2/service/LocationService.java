package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.LocationData;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.repository.LocationRepository;
import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;
import arobu.glitterfinv2.service.mapper.LocationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    Logger LOGGER = LogManager.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getOrSaveLocationEntity(final LocationData locationData) {
        GeoCodeResponse geoCodeResponse = reverseGeocode(locationData);
        LOGGER.info("Geocode Service returned response: {}", geoCodeResponse);

        Location entity = LocationMapper.toEntity(geoCodeResponse);

        locationRepository.findLocationByGeocodePlaceId(entity.getGeocodePlaceId())
                .ifPresent(location -> {
                    if (!location.getDisplayName().equals(entity.getDisplayName())) {
                        LOGGER.warn("GeoCode service returned a new display name for existing " +
                                "Location entity with id: {}", location.getId());
                        LOGGER.warn("Updating location entity from: {} to {}", location, entity);
                        entity.setId(location.getId());
                    }
                });
        LOGGER.info("Persisting location entity: {}", entity);
        return locationRepository.save(entity);
    }

    public GeoCodeResponse reverseGeocode(LocationData locationData) {
        return GeolocationExternalService.reverseGeocode(locationData.getLatitude(), locationData.getLongitude());
    }
}
