package foodloop_api.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service
public class RutasService {
 private final String key;
 private final RestClient client = RestClient.create();
 public RutasService(@Value("${foodloop.maps.api-key:}") String key){this.key=key;}
 public Map<String,Object> calcular(double latOrigen,double lonOrigen,double latDestino,double lonDestino){
     if(key==null||key.isBlank())return Map.of("configurado",false,"mensaje","Google Maps Routes API sin configurar. No se muestra ETA inventada.");
     if(Math.abs(latOrigen)>90||Math.abs(latDestino)>90||Math.abs(lonOrigen)>180||Math.abs(lonDestino)>180)
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Coordenadas inválidas");
     Map<String,Object> origen=Map.of("location",Map.of("latLng",Map.of("latitude",latOrigen,"longitude",lonOrigen)));
     Map<String,Object> destino=Map.of("location",Map.of("latLng",Map.of("latitude",latDestino,"longitude",lonDestino)));
     Map<String,Object> request=Map.of("origin",origen,"destination",destino,"travelMode","DRIVE","routingPreference","TRAFFIC_AWARE");
     try {
         @SuppressWarnings("unchecked") Map<String,Object> response=client.post().uri("https://routes.googleapis.com/directions/v2:computeRoutes")
           .header("X-Goog-Api-Key",key).header("X-Goog-FieldMask","routes.duration,routes.distanceMeters")
           .contentType(MediaType.APPLICATION_JSON).body(request).retrieve().body(Map.class);
         return Map.of("configurado",true,"resultado",response==null?Map.of():response);
     }catch(Exception ex){throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Servicio externo de rutas no disponible");}
 }
}
