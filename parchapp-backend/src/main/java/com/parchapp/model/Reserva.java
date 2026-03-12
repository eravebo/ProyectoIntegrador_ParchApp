package com.parchapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Reserva
 * Mapea a la tabla "reserva" en MySQL.
 *
 * Campos del diagrama ER: CODIGO_RESERVA, FECHA_IN, FECHA_FIN,
 * FECHA_CAN, ID_PLAN, AGENCIA
 *
 * Estados posibles: PENDIENTE → CONFIRMADA → CANCELADA
 */
@Entity
@Table(name = "reserva")
public class Reserva {

    // ─────────────────────────────────────────
    // ATRIBUTOS
    // ─────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** CODIGO_RESERVA del diagrama. Formato: PARCH-YYYYMMDD-XXXX */
    @NotBlank(message = "El código de reserva es obligatorio")
    @Column(name = "codigo_reserva", nullable = false, unique = true, length = 50)
    private String codigoReserva;

    /** FK → tabla usuario */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** FK → tabla plan (ID_PLAN del diagrama) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    /** FK → tabla agencia (AGENCIA del diagrama) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia_id", nullable = false)
    private Agencia agencia;

    /** FECHA_IN del diagrama */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /** FECHA_FIN del diagrama */
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    /** FECHA_CAN del diagrama. Null si la reserva está activa. */
    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @Min(value = 1, message = "Debe haber al menos 1 persona")
    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;

    /** Calculado en el servicio: valor del plan × cantidadPersonas */
    @NotNull(message = "El total a pagar es obligatorio")
    @Column(name = "total_pagar", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPagar;

    /** PENDIENTE / CONFIRMADA / CANCELADA */
    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;


    // ─────────────────────────────────────────
    // CONSTRUCTORES
    // ─────────────────────────────────────────

    /** Constructor vacío requerido por JPA. */
    public Reserva() {
    }

    /**
     * Constructor con los campos mínimos para crear una reserva.
     *
     * @param codigoReserva   Código único generado por el servicio
     * @param usuario         Usuario que reserva
     * @param plan            Plan reservado
     * @param agencia         Agencia del plan
     * @param fechaInicio     Fecha de inicio del tour (FECHA_IN)
     * @param cantidadPersonas Número de personas
     * @param totalPagar      Total calculado (valor × personas)
     */
    public Reserva(String codigoReserva, Usuario usuario, Plan plan,
                   Agencia agencia, LocalDate fechaInicio,
                   Integer cantidadPersonas, BigDecimal totalPagar) {
        this.codigoReserva    = codigoReserva;
        this.usuario          = usuario;
        this.plan             = plan;
        this.agencia          = agencia;
        this.fechaInicio      = fechaInicio;
        this.cantidadPersonas = cantidadPersonas;
        this.totalPagar       = totalPagar;
        this.estado           = "PENDIENTE";
    }

    /**
     * Constructor completo.
     */
    public Reserva(Long id, String codigoReserva, Usuario usuario, Plan plan,
                   Agencia agencia, LocalDate fechaInicio, LocalDate fechaFin,
                   LocalDateTime fechaCancelacion, Integer cantidadPersonas,
                   BigDecimal totalPagar, String estado, LocalDateTime fechaCreacion) {
        this.id               = id;
        this.codigoReserva    = codigoReserva;
        this.usuario          = usuario;
        this.plan             = plan;
        this.agencia          = agencia;
        this.fechaInicio      = fechaInicio;
        this.fechaFin         = fechaFin;
        this.fechaCancelacion = fechaCancelacion;
        this.cantidadPersonas = cantidadPersonas;
        this.totalPagar       = totalPagar;
        this.estado           = estado;
        this.fechaCreacion    = fechaCreacion;
    }


    // ─────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────

    public Long getId()                          { return id; }
    public String getCodigoReserva()             { return codigoReserva; }
    public Usuario getUsuario()                  { return usuario; }
    public Plan getPlan()                        { return plan; }
    public Agencia getAgencia()                  { return agencia; }
    public LocalDate getFechaInicio()            { return fechaInicio; }
    public LocalDate getFechaFin()               { return fechaFin; }
    public LocalDateTime getFechaCancelacion()   { return fechaCancelacion; }
    public Integer getCantidadPersonas()         { return cantidadPersonas; }
    public BigDecimal getTotalPagar()            { return totalPagar; }
    public String getEstado()                    { return estado; }
    public LocalDateTime getFechaCreacion()      { return fechaCreacion; }


    // ─────────────────────────────────────────
    // SETTERS
    // ─────────────────────────────────────────

    public void setId(Long id)                              { this.id = id; }
    public void setCodigoReserva(String codigoReserva)      { this.codigoReserva = codigoReserva; }
    public void setUsuario(Usuario usuario)                 { this.usuario = usuario; }
    public void setPlan(Plan plan)                          { this.plan = plan; }
    public void setAgencia(Agencia agencia)                 { this.agencia = agencia; }
    public void setFechaInicio(LocalDate fechaInicio)       { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin)             { this.fechaFin = fechaFin; }
    public void setFechaCancelacion(LocalDateTime f)        { this.fechaCancelacion = f; }
    public void setCantidadPersonas(Integer c)              { this.cantidadPersonas = c; }
    public void setTotalPagar(BigDecimal totalPagar)        { this.totalPagar = totalPagar; }
    public void setEstado(String estado)                    { this.estado = estado; }
    public void setFechaCreacion(LocalDateTime f)           { this.fechaCreacion = f; }


    // ─────────────────────────────────────────
    // MÉTODOS PROPIOS DE NEGOCIO
    // ─────────────────────────────────────────

    /**
     * Indica si la reserva está activa (no cancelada).
     */
    public boolean estaActiva() {
        return !"CANCELADA".equals(this.estado);
    }

    /**
     * Indica si la reserva está pendiente de pago.
     */
    public boolean estaPendiente() {
        return "PENDIENTE".equals(this.estado);
    }

    /**
     * Confirma la reserva cambiando su estado a CONFIRMADA.
     * Solo se puede confirmar si está en estado PENDIENTE.
     */
    public void confirmar() {
        if (!"PENDIENTE".equals(this.estado)) {
            throw new IllegalStateException(
                "Solo se puede confirmar una reserva en estado PENDIENTE. Estado actual: " + this.estado);
        }
        this.estado = "CONFIRMADA";
    }

    /**
     * Cancela la reserva registrando la fecha de cancelación (FECHA_CAN).
     * Solo se puede cancelar si no está ya cancelada.
     */
    public void cancelar() {
        if ("CANCELADA".equals(this.estado)) {
            throw new IllegalStateException("La reserva ya está cancelada.");
        }
        this.estado           = "CANCELADA";
        this.fechaCancelacion = LocalDateTime.now();
    }


    // ─────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────

    /** No incluir usuario/plan/agencia para evitar bucles infinitos en logs. */
    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", codigoReserva='" + codigoReserva + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", cantidadPersonas=" + cantidadPersonas +
                ", totalPagar=" + totalPagar +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }


    // ─────────────────────────────────────────
    // CICLO DE VIDA JPA
    // ─────────────────────────────────────────

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null)           this.estado           = "PENDIENTE";
        if (this.cantidadPersonas == null) this.cantidadPersonas = 1;
    }
}
