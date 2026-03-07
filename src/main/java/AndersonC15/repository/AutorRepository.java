package AndersonC15.repository;


import AndersonC15.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Buscar autor por nombre exacto
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    // Listar todos los autores
    List<Autor> findAll();

    // Autores vivos en un año determinado
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :year AND a.fechaMuerte >= :year")
    List<Autor> findAutoresVivosPorAnio(Integer year);
}