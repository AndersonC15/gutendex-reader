package AndersonC15.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de el autor es obligatorio")
    @NotBlank(message = "Este campo no debe de ir vacio")
    @Column(nullable = false, unique = true, length = 255)
    private String nombre;

    @Min(value = 1000, message = "Año de nacimiento invalido")
    @Max(value = 2026, message = "Año de nacimiento no puede ser futuro")
    @Column(name = "fecha_nacimiento")
    private Integer fechaNacimiento;


    @Min(value = 1000, message = "Año de fallecimiento invalido")
    @Max(value = 2026, message = "Año de fallecimiento no puede ser futuro")
    @Column(name = "fecha_muerte")
    private Integer fechaMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Libro> libros;
}
