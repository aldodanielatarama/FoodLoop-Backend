package foodloop_api.models;
import jakarta.persistence.*;



import java.time.LocalDateTime;

@Entity @Table(name="lotes_excedentes") 
public class LoteExcedente {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Version private Long version;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="producto_id",nullable=false) private Producto producto;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="establecimiento_id",nullable=false) private Establecimiento establecimiento;
 @Column(nullable=false) private Long propietarioId;
 @Column(nullable=false) private String modalidad; // VENTA, DONACION
 @Column(nullable=false) private Integer stockInicial;
 @Column(nullable=false) private Integer stockDisponible;
 @Column(nullable=false) private Integer stockReservado=0;
 private Double precioOriginal;
 private Double precioLiquidacion;
 private Double pesoPorUnidadKg;
 private LocalDateTime fechaPreparacion;
 @Column(nullable=false) private LocalDateTime fechaVencimiento;
 @Column(nullable=false) private LocalDateTime limiteReserva;
 @Column(nullable=false) private LocalDateTime limiteRecojo;
 private String conservacion;
 private String estadoInocuidad="DECLARADO_APTO";
 private String politicaAsignacion="MANUAL";
 private String estado="ACTIVO";
 private LocalDateTime publicadoEn=LocalDateTime.now();

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getVersion() { return this.version; }
    public void setVersion(Long value) { this.version = value; }
    public Producto getProducto() { return this.producto; }
    public void setProducto(Producto value) { this.producto = value; }
    public Establecimiento getEstablecimiento() { return this.establecimiento; }
    public void setEstablecimiento(Establecimiento value) { this.establecimiento = value; }
    public Long getPropietarioId() { return this.propietarioId; }
    public void setPropietarioId(Long value) { this.propietarioId = value; }
    public String getModalidad() { return this.modalidad; }
    public void setModalidad(String value) { this.modalidad = value; }
    public Integer getStockInicial() { return this.stockInicial; }
    public void setStockInicial(Integer value) { this.stockInicial = value; }
    public Integer getStockDisponible() { return this.stockDisponible; }
    public void setStockDisponible(Integer value) { this.stockDisponible = value; }
    public Integer getStockReservado() { return this.stockReservado; }
    public void setStockReservado(Integer value) { this.stockReservado = value; }
    public Double getPrecioOriginal() { return this.precioOriginal; }
    public void setPrecioOriginal(Double value) { this.precioOriginal = value; }
    public Double getPrecioLiquidacion() { return this.precioLiquidacion; }
    public void setPrecioLiquidacion(Double value) { this.precioLiquidacion = value; }
    public Double getPesoPorUnidadKg() { return this.pesoPorUnidadKg; }
    public void setPesoPorUnidadKg(Double value) { this.pesoPorUnidadKg = value; }
    public LocalDateTime getFechaPreparacion() { return this.fechaPreparacion; }
    public void setFechaPreparacion(LocalDateTime value) { this.fechaPreparacion = value; }
    public LocalDateTime getFechaVencimiento() { return this.fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime value) { this.fechaVencimiento = value; }
    public LocalDateTime getLimiteReserva() { return this.limiteReserva; }
    public void setLimiteReserva(LocalDateTime value) { this.limiteReserva = value; }
    public LocalDateTime getLimiteRecojo() { return this.limiteRecojo; }
    public void setLimiteRecojo(LocalDateTime value) { this.limiteRecojo = value; }
    public String getConservacion() { return this.conservacion; }
    public void setConservacion(String value) { this.conservacion = value; }
    public String getEstadoInocuidad() { return this.estadoInocuidad; }
    public void setEstadoInocuidad(String value) { this.estadoInocuidad = value; }
    public String getPoliticaAsignacion() { return this.politicaAsignacion; }
    public void setPoliticaAsignacion(String value) { this.politicaAsignacion = value; }
    public String getEstado() { return this.estado; }
    public void setEstado(String value) { this.estado = value; }
    public LocalDateTime getPublicadoEn() { return this.publicadoEn; }
    public void setPublicadoEn(LocalDateTime value) { this.publicadoEn = value; }
}
