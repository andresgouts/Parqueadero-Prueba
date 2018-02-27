package parqueadero.databuilder;

import parqueadero.entidad.FacturaEntity;
import parqueadero.entidad.ServicioEntity;

public class FacturaDataBuilder {
	
	private Long idFactura;
	private Long diasfacturados;	
	private Long horasFacturadas;
	private Double subtotal;	
	private Double valorOtrosConceptos;
	private Double totalFactura;
	private ServicioEntity servicio;
	
	public FacturaDataBuilder() {
		this.idFactura = null;
		this.diasfacturados = 2L;
		this.horasFacturadas = 8L;
		this.subtotal = 12000.0;
		this.valorOtrosConceptos = 2000.0;
		this.totalFactura = 14000.0;
		this.servicio = null;
	}
	
	public FacturaDataBuilder conIdFactura(Long idFactura) {
		this.idFactura = idFactura;
		return this;
	}
	
	public FacturaDataBuilder conDiasFacturados(Long diasFacturados) {
		this.diasfacturados = diasFacturados;
		return this;
	}
	
	public FacturaDataBuilder conHorasFacturadas(Long horasFacturadas) {
		this.horasFacturadas = horasFacturadas;
		return this;
	}
	
	public FacturaDataBuilder conSubtotal(Double subtotal) {
		this.subtotal = subtotal;
		return this;
	}
	
	public FacturaDataBuilder conValorOtrosConceptos(Double valorOtrosConceptos) {
		this.valorOtrosConceptos = valorOtrosConceptos;
		return this;
	}
	
	public FacturaDataBuilder conTotalFactura(Double totalFactura) {
		this.totalFactura = totalFactura;
		return this;
	}
	
	public FacturaDataBuilder conServicio(ServicioEntity servicio) {
		this.servicio = servicio;
		return this;
	}
	
	public FacturaEntity build() {
		return new FacturaEntity(this.idFactura, this.diasfacturados, this.horasFacturadas, this.subtotal, 
				this.valorOtrosConceptos, this.totalFactura, this.servicio);
	}
	
}
