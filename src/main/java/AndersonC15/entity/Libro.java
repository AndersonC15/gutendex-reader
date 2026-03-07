package AndersonC15.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;  // ← CORRECTO


@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @NotBlank(message = "Este campo no debe de ir vacio")
    @Column(nullable = false, unique = true, length = 255)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull(message = "Ingresar la palabra (Ninguno) en el campo autor si el libro no tiene autor")
    private Autor autor;

    @Size(min = 1, max = 5, message = "Escriba por favor la abreviacion")
    @Column(length = 5, nullable = false)
    private String idioma;

    @Column(name = "numero_descargas")
    private Double numeroDescargas;
}