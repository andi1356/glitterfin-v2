package arobu.glitterfinv2.service;

import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;
import arobu.glitterfinv2.service.external.geocode.GeocodeRequestErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class GeolocationExternalService {

    static Logger LOGGER = LogManager.getLogger(GeolocationExternalService.class);

    @Value("${external.geocode.api-uri}")
    private static String GEOCODING_RESOURCE;

    public static GeoCodeResponse reverseGeocode(String lat, String lon) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            String requestUri = GEOCODING_RESOURCE  + "&lat=" + lat + "&lon=" + lon;
            HttpRequest geocodingRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(requestUri))
                    .timeout(Duration.ofMillis(2000)).build();

            HttpResponse<String> geocodingResponseString = httpClient.send(geocodingRequest, BodyHandlers.ofString());
            if(geocodingResponseString.statusCode() != 200) {
                throw new GeocodeRequestErrorException(geocodingResponseString);
            }
            return new ObjectMapper().readValue(geocodingResponseString.body(), GeoCodeResponse.class);
        } catch (GeocodeRequestErrorException | IOException | InterruptedException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }
}