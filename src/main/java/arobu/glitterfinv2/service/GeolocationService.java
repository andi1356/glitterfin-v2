package arobu.glitterfinv2.service;

import arobu.glitterfinv2.service.external.geocode.GeoCodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

@Service
public class GeolocationService {

    static Logger LOGGER = LogManager.getLogger(GeolocationService.class);

    @Value("${external.geocode.api-uri}")
    private String GEOCODING_API_URI;

    public GeoCodeResponse reverseGeocode(String lat, String lon) {
        try (HttpClient httpClient = HttpClient.newHttpClient()){
            String requestUri = GEOCODING_API_URI + "&lat=" + lat + "&lon=" + lon;
            HttpRequest geocodingRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(requestUri))
                    .timeout(Duration.ofMillis(2000)).build();

            HttpResponse<String> geocodingResponseString = httpClient.send(geocodingRequest, BodyHandlers.ofString());
            if(geocodingResponseString.statusCode() != 200 || geocodingResponseString.body().contains("error")) {
                LOGGER.error("GeoCode Service returned an error; Response Status: {} Body: {} ",
                        geocodingResponseString.statusCode(), geocodingResponseString.body());
                LOGGER.info("Building custom response containing only latitude and longitude");
                return new GeoCodeResponse(lat, lon);
            } else {
                return new ObjectMapper().readValue(geocodingResponseString.body(), GeoCodeResponse.class);
            }
        } catch (IOException e) {
            LOGGER.error("Httpclient request to GeoCode service returned an error. Cause: {}", e.getCause());
            return new GeoCodeResponse(lat, lon);
        } catch (InterruptedException e) {
            LOGGER.error("Mapping the GeoCode response to internal GeoCodeResponse model failed. Cause: {}",
                    e.getCause());
            return new GeoCodeResponse(lat, lon);
        }
    }
}