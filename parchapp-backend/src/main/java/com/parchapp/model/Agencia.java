package com.parchapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Agencia
 * Mapea a la tabla "agencia" en MySQL.
 *
 * Campos del diagrama ER: NOMBRE, CORREO, DIRECCIÓN, TELEFONO
 */
@Entity
@Table(name = "agencia")
public class Agencia {

    // ─────────────────────────────────────────
    // ATRIBUTOS
    // ─────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la agencia es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Email(message = "El correo debe tener formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "agencia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plan> planes;

    @OneToMany(mappedBy = "agencia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;


    // ─────────────────────────────────────────
    // CONSTRUCTORES
    // ─────────────────────────────────────────

    /** Constructor vacío requerido por JPA. */
    public Agencia() {
    }

    /**
     * Constructor con campos obligatorios.
     *
     * @param nombre Nombre comercial de la agencia
     * @param correo Correo de contacto (único en el sistema)
     */
    public Agencia(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    /**
     * Constructor completo.
     */
    public Agencia(Long id, String nombre, String correo,
                   String telefono, String direccion, LocalDateTime fechaRegistro) {
        this.id            = id;
        this.nombre        = nombre;
        this.correo        = correo;
        this.telefono      = telefono;
        this.direccion     = direccion;
        this.fechaRegistro = fechaRegistro;
    }


    // ─────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────

    public Long getId()                      { return id; }
    public String getNombre()                { return nombre; }
    public String getCorreo()                { return correo; }
    public String getTelefono()              { return telefono; }
    public String getDireccion()             { return direccion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<Plan> getPlanes()            { return planes; }
    public List<Reserva> getReservas()       { return reservas; }


    // ─────────────────────────────────────────
    // SETTERS
    // ─────────────────────────────────────────

    public void setId(Long id)                          { this.id = id; }
    public void setNombre(String nombre)                { this.nombre = nombre; }
    public void setCorreo(String correo)                { this.correo = correo; }
    public void setTelefono(String telefono)            { this.telefono = telefono; }
    public void setDireccion(String direccion)          { this.direccion = direccion; }
    public void setFechaRegistro(LocalDateTime f)       { this.fechaRegistro = f; }
    public void setPlanes(List<Plan> planes)            { this.planes = planes; }
    public void setReservas(List<Reserva> reservas)     { this.reservas = reservas; }


    // ─────────────────────────────────────────
    // MÉTODOS PROPIOS DE NEGOCIO
    // ─────────────────────────────────────────

    /**
     * Cuenta cuántos planes activos tiene la agencia.
     */
    public int contarPlanesActivos() {
        if (this.planes == null) return 0;
        return (int) this.planes.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .count();
    }

    /**
     * Indica si la agencia tiene información de contacto completa
     * (teléfono y dirección registrados).
     */
    public boolean tieneContactoCompleto() {
        return this.telefono != null && !this.telefono.isBlank()
            && this.direccion != null && !this.direccion.isBlank();
    }


    // ─────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────

    @Override
    public String toString() {
        return "Agencia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }


    // ─────────────────────────────────────────
    // CICLO DE VIDA JPA
    // ─────────────────────────────────────────

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
