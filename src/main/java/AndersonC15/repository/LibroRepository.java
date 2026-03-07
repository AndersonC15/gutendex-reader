package AndersonC15.repository;

import AndersonC15.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Buscar libro por título (ignora mayúsculas/minúsculas)
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    // Listar todos los libros
    List<Libro> findAll();

    // Buscar libros por idioma
    List<Libro> findByIdioma(String idioma);
}