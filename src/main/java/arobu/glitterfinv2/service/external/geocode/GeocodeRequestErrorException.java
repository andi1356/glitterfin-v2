package arobu.glitterfinv2.service.external.geocode;

import java.net.http.HttpResponse;

public class GeocodeRequestErrorException extends Throwable {
    public GeocodeRequestErrorException(HttpResponse<String> geocodingResponseString) {
        super(geocodingResponseString.toString());
    }
}
