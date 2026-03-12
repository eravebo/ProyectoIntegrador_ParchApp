package com.parchapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Usuario
 * Mapea a la tabla "usuario" en MySQL.
 *
 * Campos del diagrama ER: ID, NOMBRE, APELLIDO, CORREO,
 * EDAD, PAIS, CIUDAD, IDIOMA, TIPO_ID
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    // ─────────────────────────────────────────
    // ATRIBUTOS (variables de la clase)
    // ─────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de ID es obligatorio")
    @Column(name = "tipo_id", nullable = false, length = 20)
    private String tipoId;

    @NotBlank(message = "El número de ID es obligatorio")
    @Column(name = "numero_id", nullable = false, unique = true, length = 30)
    private String numeroId;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellido;

    @Email(message = "El correo debe tener formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column
    private Integer edad;

    @Column(length = 100)
    private String pais;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 50)
    private String idioma;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;


    // ─────────────────────────────────────────
    // CONSTRUCTORES
    // ─────────────────────────────────────────

    /**
     * Constructor vacío.
     * JPA lo requiere obligatoriamente para poder crear
     * instancias de la entidad cuando lee datos de la BD.
     */
    public Usuario() {
    }

    /**
     * Constructor con los campos obligatorios del registro.
     *
     * @param tipoId     Tipo de documento (CC, CE, PASAPORTE)
     * @param numeroId   Número del documento
     * @param nombre     Nombre del usuario
     * @param apellido   Apellido del usuario
     * @param correo     Correo electrónico (usado como login)
     * @param contrasena Contraseña (debe llegar ya encriptada)
     */
    public Usuario(String tipoId, String numeroId, String nombre,
                   String apellido, String correo, String contrasena) {
        this.tipoId     = tipoId;
        this.numeroId   = numeroId;
        this.nombre     = nombre;
        this.apellido   = apellido;
        this.correo     = correo;
        this.contrasena = contrasena;
    }

    /**
     * Constructor completo con todos los campos.
     */
    public Usuario(Long id, String tipoId, String numeroId, String nombre,
                   String apellido, String correo, String contrasena,
                   Integer edad, String pais, String ciudad, String idioma,
                   LocalDateTime fechaRegistro) {
        this.id            = id;
        this.tipoId        = tipoId;
        this.numeroId      = numeroId;
        this.nombre        = nombre;
        this.apellido      = apellido;
        this.correo        = correo;
        this.contrasena    = contrasena;
        this.edad          = edad;
        this.pais          = pais;
        this.ciudad        = ciudad;
        this.idioma        = idioma;
        this.fechaRegistro = fechaRegistro;
    }


    // ─────────────────────────────────────────
    // GETTERS  (leer el valor de cada campo)
    // ─────────────────────────────────────────

    public Long getId()                      { return id; }
    public String getTipoId()                { return tipoId; }
    public String getNumeroId()              { return numeroId; }
    public String getNombre()                { return nombre; }
    public String getApellido()              { return apellido; }
    public String getCorreo()                { return correo; }
    public String getContrasena()            { return contrasena; }
    public Integer getEdad()                 { return edad; }
    public String getPais()                  { return pais; }
    public String getCiudad()               { return ciudad; }
    public String getIdioma()               { return idioma; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<Reserva> getReservas()      { return reservas; }


    // ─────────────────────────────────────────
    // SETTERS  (modificar el valor de cada campo)
    // ─────────────────────────────────────────

    public void setId(Long id)                             { this.id = id; }
    public void setTipoId(String tipoId)                   { this.tipoId = tipoId; }
    public void setNumeroId(String numeroId)               { this.numeroId = numeroId; }
    public void setNombre(String nombre)                   { this.nombre = nombre; }
    public void setApellido(String apellido)               { this.apellido = apellido; }
    public void setCorreo(String correo)                   { this.correo = correo; }
    public void setContrasena(String contrasena)           { this.contrasena = contrasena; }
    public void setEdad(Integer edad)                      { this.edad = edad; }
    public void setPais(String pais)                       { this.pais = pais; }
    public void setCiudad(String ciudad)                   { this.ciudad = ciudad; }
    public void setIdioma(String idioma)                   { this.idioma = idioma; }
    public void setFechaRegistro(LocalDateTime f)          { this.fechaRegistro = f; }
    public void setReservas(List<Reserva> reservas)        { this.reservas = reservas; }


    // ─────────────────────────────────────────
    // MÉTODOS PROPIOS DE NEGOCIO
    // ─────────────────────────────────────────

    /**
     * Retorna nombre y apellido juntos.
     * Ejemplo: "Juan Pérez"
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * Verifica si el usuario tiene al menos una reserva no cancelada.
     */
    public boolean tieneReservasActivas() {
        if (this.reservas == null || this.reservas.isEmpty()) return false;
        return this.reservas.stream()
                .anyMatch(r -> !"CANCELADA".equals(r.getEstado()));
    }

    /**
     * Indica si el usuario es mayor de edad (18+).
     */
    public boolean esMayorDeEdad() {
        if (this.edad == null) return false;
        return this.edad >= 18;
    }


    // ─────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────

    /**
     * NO incluir contrasena (seguridad) ni reservas (evita bucle infinito).
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", tipoId='" + tipoId + '\'' +
                ", numeroId='" + numeroId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", edad=" + edad +
                ", pais='" + pais + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", idioma='" + idioma + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }


    // ─────────────────────────────────────────
    // CICLO DE VIDA JPA
    // ─────────────────────────────────────────

    /** Se ejecuta automáticamente antes del INSERT en la BD. */
    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.idioma == null) this.idioma = "Español";
        if (this.pais == null)   this.pais   = "Colombia";
    }
}
