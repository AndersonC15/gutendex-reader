package AndersonC15.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumoAPI {

    private static final String BASE_URL = "https://gutendex.com/books";
    private final RestTemplate restTemplate = new RestTemplate();

    public String obtenerDatos(String busqueda) {
        String url = BASE_URL + "?search=" + busqueda.replace(" ", "%20");
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.out.println("❌ Error al consumir la API: " + e.getMessage());
            return null;
        }
    }
}