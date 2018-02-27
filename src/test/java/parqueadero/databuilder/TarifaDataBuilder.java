package parqueadero.databuilder;

import parqueadero.entidad.TarifaEntity;

public class TarifaDataBuilder {
	
	private Long idTarifa;
	private String tipoVehiculo;
	private Double valorHora;
	private Double valorDia;
	
	public TarifaDataBuilder(){
		
	}
	
	public TarifaDataBuilder(Long idTarifa, String tipoVehiculo, Double valorHora, Double valorDia) {
		this.idTarifa = 1L;
		this.tipoVehiculo = "m";
		this.valorHora = 500D;
		this.valorDia = 4000D;
	}
	
	public TarifaDataBuilder conIdTarifa(Long idTarifa) {
		this.idTarifa = idTarifa;
		return this;
	}
	
	public TarifaDataBuilder conTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
		return this;
	}
	
	public TarifaDataBuilder conValorHora(Double valorHora) {
		this.valorHora = valorHora;
		return this;
	}
	
	public TarifaDataBuilder conValorDia(Double valorDia) {
		this.valorDia = valorDia;
		return this;
	}
	
	public TarifaEntity build() {
		return new TarifaEntity(this.idTarifa, this.tipoVehiculo, this.valorHora, this.valorDia);
	}
	
	
}
