package com.parchapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Plan
 * Mapea a la tabla "plan" en MySQL.
 *
 * Campos del diagrama ER: ID_PLAN, VALOR, DISPONIBILIDAD,
 * NOMBRE (agencia), NOMBRE (plan), descripcion, destino
 */
@Entity
@Table(name = "plan")
public class Plan {

    // ─────────────────────────────────────────
    // ATRIBUTOS
    // ─────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Agencia que publica el plan (FK agencia_id) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia_id", nullable = false)
    private Agencia agencia;

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 150)
    private String destino;

    @NotNull(message = "El valor del plan es obligatorio")
    @DecimalMin(value = "0.0", message = "El valor no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Min(value = 0, message = "Los cupos no pueden ser negativos")
    @Column(nullable = false)
    private Integer disponibilidad;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;


    // ─────────────────────────────────────────
    // CONSTRUCTORES
    // ─────────────────────────────────────────

    /** Constructor vacío requerido por JPA. */
    public Plan() {
    }

    /**
     * Constructor con campos mínimos para publicar un plan.
     *
     * @param agencia       Agencia propietaria
     * @param nombre        Nombre del tour
     * @param valor         Precio en COP
     * @param disponibilidad Cupos disponibles
     */
    public Plan(Agencia agencia, String nombre, BigDecimal valor, Integer disponibilidad) {
        this.agencia       = agencia;
        this.nombre        = nombre;
        this.valor         = valor;
        this.disponibilidad = disponibilidad;
    }

    /**
     * Constructor completo.
     */
    public Plan(Long id, Agencia agencia, String nombre, String descripcion,
                String destino, BigDecimal valor, Integer disponibilidad,
                String imagenUrl, Boolean activo, LocalDateTime fechaCreacion) {
        this.id            = id;
        this.agencia       = agencia;
        this.nombre        = nombre;
        this.descripcion   = descripcion;
        this.destino       = destino;
        this.valor         = valor;
        this.disponibilidad = disponibilidad;
        this.imagenUrl     = imagenUrl;
        this.activo        = activo;
        this.fechaCreacion = fechaCreacion;
    }


    // ─────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────

    public Long getId()                      { return id; }
    public Agencia getAgencia()              { return agencia; }
    public String getNombre()                { return nombre; }
    public String getDescripcion()           { return descripcion; }
    public String getDestino()               { return destino; }
    public BigDecimal getValor()             { return valor; }
    public Integer getDisponibilidad()       { return disponibilidad; }
    public String getImagenUrl()             { return imagenUrl; }
    public Boolean getActivo()               { return activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<Reserva> getReservas()       { return reservas; }


    // ─────────────────────────────────────────
    // SETTERS
    // ─────────────────────────────────────────

    public void setId(Long id)                          { this.id = id; }
    public void setAgencia(Agencia agencia)             { this.agencia = agencia; }
    public void setNombre(String nombre)                { this.nombre = nombre; }
    public void setDescripcion(String descripcion)      { this.descripcion = descripcion; }
    public void setDestino(String destino)              { this.destino = destino; }
    public void setValor(BigDecimal valor)              { this.valor = valor; }
    public void setDisponibilidad(Integer d)            { this.disponibilidad = d; }
    public void setImagenUrl(String imagenUrl)          { this.imagenUrl = imagenUrl; }
    public void setActivo(Boolean activo)               { this.activo = activo; }
    public void setFechaCreacion(LocalDateTime f)       { this.fechaCreacion = f; }
    public void setReservas(List<Reserva> reservas)     { this.reservas = reservas; }


    // ─────────────────────────────────────────
    // MÉTODOS PROPIOS DE NEGOCIO
    // ─────────────────────────────────────────

    /**
     * Calcula el total a pagar para una cantidad de personas.
     * Ejemplo: valor=120000, personas=3 → retorna 360000
     *
     * @param cantidadPersonas número de personas
     * @return total en COP como BigDecimal
     */
    public BigDecimal calcularTotal(int cantidadPersonas) {
        return this.valor.multiply(BigDecimal.valueOf(cantidadPersonas));
    }

    /**
     * Indica si aún hay cupos disponibles para reservar.
     */
    public boolean tieneCuposDisponibles() {
        return this.disponibilidad != null && this.disponibilidad > 0;
    }

    /**
     * Indica si el plan está disponible para reservar:
     * debe estar activo y tener cupos.
     */
    public boolean estaDisponible() {
        return Boolean.TRUE.equals(this.activo) && tieneCuposDisponibles();
    }

    /**
     * Reduce los cupos disponibles en la cantidad indicada.
     * Lanza excepción si no hay suficientes cupos.
     *
     * @param cantidad personas a descontar
     */
    public void reducirCupos(int cantidad) {
        if (this.disponibilidad < cantidad) {
            throw new IllegalStateException(
                "No hay suficientes cupos. Disponibles: " + this.disponibilidad);
        }
        this.disponibilidad -= cantidad;
    }


    // ─────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", destino='" + destino + '\'' +
                ", valor=" + valor +
                ", disponibilidad=" + disponibilidad +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }


    // ─────────────────────────────────────────
    // CICLO DE VIDA JPA
    // ─────────────────────────────────────────

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
    }
}
