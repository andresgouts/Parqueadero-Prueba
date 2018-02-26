package parqueadero.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="factura")
public class FacturaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idFactura;
	
	private Long diasfacturados;
	
	private Long horasFacturadas;
	
	@Column(nullable = false)
	private Double subtotal;
	
	private Double valorOtrosConceptos;
	
	@Column(nullable = false)
	private Double totalFactura;
	
	@OneToOne
	@JoinColumn(name="idServicio",referencedColumnName="idServicio")
	private ServicioEntity servicio;

	public FacturaEntity(Long idFactura, Long diasfacturados, Long horasFacturadas, Double subtotal,
			Double valorOtrosConceptos, Double totalFactura, ServicioEntity servicio) {
		this.idFactura = idFactura;
		this.diasfacturados = diasfacturados;
		this.horasFacturadas = horasFacturadas;
		this.subtotal = subtotal;
		this.valorOtrosConceptos = valorOtrosConceptos;
		this.totalFactura = totalFactura;
		this.servicio = servicio;
	}

	public FacturaEntity() {

	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public Long getDiasfacturados() {
		return diasfacturados;
	}

	public void setDiasfacturados(Long diasfacturados) {
		this.diasfacturados = diasfacturados;
	}

	public Long getHorasFacturadas() {
		return horasFacturadas;
	}

	public void setHorasFacturadas(Long horasFacturadas) {
		this.horasFacturadas = horasFacturadas;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getValorOtrosConceptos() {
		return valorOtrosConceptos;
	}

	public void setValorOtrosConceptos(Double valorOtrosConceptos) {
		this.valorOtrosConceptos = valorOtrosConceptos;
	}

	public Double getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(Double totalFactura) {
		this.totalFactura = totalFactura;
	}

	public ServicioEntity getServicio() {
		return servicio;
	}

	public void setServicio(ServicioEntity servicio) {
		this.servicio = servicio;
	}
	
	
	
	
	
	

}
