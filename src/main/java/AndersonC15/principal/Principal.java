package AndersonC15.principal;

import AndersonC15.entity.Autor;
import AndersonC15.entity.Libro;
import AndersonC15.repository.AutorRepository;
import AndersonC15.repository.LibroRepository;
import AndersonC15.service.ConsumoAPI;
import AndersonC15.service.ConversionDatos;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private final ConsumoAPI consumoAPI;
    private final ConversionDatos  conversionDatos;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Scanner scanner = new Scanner(System.in);

    public Principal(ConsumoAPI consumoAPI, ConversionDatos conversionDatos,
                     LibroRepository libroRepository, AutorRepository autorRepository) {
        this.consumoAPI = consumoAPI;
        this.conversionDatos = conversionDatos;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    //Menu para interactuar por consola, hace llamado a todos los otros metodos
    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n========================================");
            System.out.println("       🌟 LITERALURA - CATÁLOGO 🌟      ");
            System.out.println("========================================");
            System.out.println("Elija la opción a través de su número:");
            System.out.println("1- buscar libro por título");
            System.out.println("2- listar libros registrados");
            System.out.println("3- listar autores registrados");
            System.out.println("4- listar autores vivos en un determinado año");
            System.out.println("5- listar libros por idioma");
            System.out.println("0- salir");
            System.out.println("========================================");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("\n✓ ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("\n❌ Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: Ingrese un número válido");
                scanner.nextLine();
            }
        }
    }

    // ========== OPCIÓN 1: BUSCAR LIBRO POR TÍTULO ==========
    private void buscarLibroPorTitulo() {
        System.out.println("\n✓ Ingrese el nombre del libro que desea buscar:");
        String nombreLibro = scanner.nextLine();

        // Consumir API
        String json = consumoAPI.obtenerDatos(nombreLibro);
        if (json == null) return;

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode resultados = root.get("results");

            if (resultados.isEmpty()) {
                System.out.println("\n❌ Libro no encontrado en la API");
                return;
            }

            JsonNode primerResultado = resultados.get(0);
            Libro libro = conversionDatos.convertirALibro(primerResultado);

            // Validar si ya existe
            Optional<Libro> libroExistente = libroRepository
                    .findByTituloIgnoreCase(libro.getTitulo());

            if (libroExistente.isPresent()) {
                System.out.println("\n❌ Este libro ya existe en la base de datos");
                return;
            }

            // Validar si el autor ya existe
            Optional<Autor> autorExistente = autorRepository
                    .findByNombreIgnoreCase(libro.getAutor().getNombre());

            if (autorExistente.isPresent()) {
                libro.setAutor(autorExistente.get());
            } else {
                autorRepository.save(libro.getAutor());
            }

            // Guardar libro
            libroRepository.save(libro);

            // Mostrar resultado
            mostrarLibro(libro);

        } catch (Exception e) {
            System.out.println("❌ Error al procesar la respuesta: " + e.getMessage());
        }
    }

    // ========== OPCIÓN 2: LISTAR LIBROS REGISTRADOS ==========
    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("\n❌ No hay libros registrados");
            return;
        }

        System.out.println("\n========== LIBROS REGISTRADOS ==========");
        libros.forEach(this::mostrarLibro);
    }

    // ========== OPCIÓN 3: LISTAR AUTORES REGISTRADOS ==========
    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("\n❌ No hay autores registrados");
            return;
        }

        System.out.println("\n========== AUTORES REGISTRADOS ==========");
        autores.forEach(autor -> {
            System.out.println("\n----- AUTOR -----");
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Nacimiento: " + (autor.getFechaNacimiento() != null ?
                    autor.getFechaNacimiento() : "No disponible"));
            System.out.println("Fallecimiento: " + (autor.getFechaMuerte() != null ?
                    autor.getFechaMuerte() : "No disponible"));
            System.out.println("Libros: " + autor.getLibros().size());
        });
    }

    // ========== OPCIÓN 4: LISTAR AUTORES VIVOS EN UN AÑO ==========
    private void listarAutoresVivosPorAnio() {
        System.out.println("\n✓ Ingrese el año:");
        int anio = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = autorRepository.findAutoresVivosPorAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("\n❌ No hay autores vivos en el año " + anio);
            return;
        }

        System.out.println("\n========== AUTORES VIVOS EN " + anio + " ==========");
        autores.forEach(autor -> {
            System.out.println("\n----- AUTOR -----");
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fallecimiento: " + autor.getFechaMuerte());
        });
    }

    // ========== OPCIÓN 5: LISTAR LIBROS POR IDIOMA ==========
    private void listarLibrosPorIdioma() {
        System.out.println("\n✓ Ingrese el código de idioma (ES, EN, FR, PT, etc):");
        String idioma = scanner.nextLine().toLowerCase();

        List<Libro> libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("\n❌ No hay libros en idioma: " + idioma);
            return;
        }

        System.out.println("\n========== LIBROS EN IDIOMA: " + idioma.toUpperCase() + " ==========");
        libros.forEach(this::mostrarLibro);
    }

    // Método auxiliar para mostrar un libro
    private void mostrarLibro(Libro libro) {
        System.out.println("\n----- LIBRO -----");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor().getNombre());
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Número de descargas: " + libro.getNumeroDescargas());
    }
}