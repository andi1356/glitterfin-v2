package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.LocationData;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.repository.LocationRepository;
import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;
import arobu.glitterfinv2.model.mapper.LocationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    Logger LOGGER = LogManager.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final GeolocationService geolocationService;

    public LocationService(LocationRepository locationRepository, GeolocationService geolocationService) {
        this.locationRepository = locationRepository;
        this.geolocationService = geolocationService;
    }

    public Location getOrSaveLocationEntity(final LocationData locationData) {
        GeoCodeResponse geoCodeResponse = geolocationService.reverseGeocode(locationData.getLatitude(), locationData.getLongitude());
        LOGGER.info("GeoLocationService returned response: {}", geoCodeResponse);

        Location newEntity = LocationMapper.toEntity(geoCodeResponse);

        return locationRepository.findLocationByGeocodePlaceId(newEntity.getGeocodePlaceId())
                .map(location -> {
                    if (!location.getDisplayName().equals(newEntity.getDisplayName())) {
                        LOGGER.warn("GeoCode service returned a new display name for existing " +
                                "Location entity with id: {}", location.getId());
                        LOGGER.warn("Updating location's display name from: {} to {}",
                                location.getDisplayName(), newEntity.getDisplayName());
                        location.setDisplayName(newEntity.getDisplayName());
                        return locationRepository.save(location);
                    } else {
                        return location;
                    }
                }).orElseGet(() -> {
                    LOGGER.info("Persisting location entity: {}", newEntity);
                    return locationRepository.save(newEntity);
                });
    }
}
