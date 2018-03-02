package parqueadero.databuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import parqueadero.entidad.ServicioEntity;
import parqueadero.entidad.TarifaEntity;

public class ServicioDataBuilder {
	
	private Long idServicio;
	private String placa;
	private String tipoVehiculo;
	private Integer cilindraje;
	private Date fechaIngreso;
	private Date fechaSalida;
	private TarifaEntity tarifa;
	
	
	public ServicioDataBuilder() {
		
		LocalDateTime fechaActual = LocalDateTime.now();
		this.idServicio = null;
		this.placa = "zjv11c";
		this.tipoVehiculo = "moto";
		this.cilindraje = 800;
		this.fechaIngreso = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());
		this.fechaSalida = null;
		this.tarifa = null;
	}
	
	public ServicioDataBuilder conPlaca(String placa) {
		this.placa = placa;
		return this;
	}
	
	public ServicioDataBuilder conTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
		return this;
	}
	
	public ServicioDataBuilder conCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
		return this;
	}
	
	public ServicioDataBuilder conFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		return this;
	}
	
	public ServicioDataBuilder conFechaEgreso(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
		return this;
	}
	
	public ServicioDataBuilder conTarifa (TarifaEntity tarifa) {
		this.tarifa = tarifa;
		return this;
	}
	
	public ServicioEntity build() {
		return new ServicioEntity(this.idServicio, this.placa, this.tipoVehiculo, this.cilindraje, 
				this.fechaIngreso, this.fechaSalida, this.tarifa);
	}
	
	

}
