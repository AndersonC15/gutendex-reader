package AndersonC15.service;


import AndersonC15.entity.Autor;
import AndersonC15.entity.Libro;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ConversionDatos {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Libro convertirALibro(JsonNode nodoLibro) {
        Libro libro = new Libro();

        libro.setTitulo(nodoLibro.get("title").asText());
        libro.setNumeroDescargas(nodoLibro.get("download_count").asDouble());

        // Obtener idioma (es un array, tomamos el primero)
        if (nodoLibro.has("languages") && nodoLibro.get("languages").size() > 0) {
            libro.setIdioma(nodoLibro.get("languages").get(0).asText());
        }

        // Obtener autor (es un array, tomamos el primero)
        if (nodoLibro.has("authors") && nodoLibro.get("authors").size() > 0) {
            JsonNode nodoAutor = nodoLibro.get("authors").get(0);
            Autor autor = new Autor();
            autor.setNombre(nodoAutor.get("name").asText());
            autor.setFechaNacimiento(nodoAutor.has("birth_year") ?
                    nodoAutor.get("birth_year").asInt() : null);
            autor.setFechaMuerte(nodoAutor.has("death_year") ?
                    nodoAutor.get("death_year").asInt() : null);

            libro.setAutor(autor);
        }

        return libro;
    }
}