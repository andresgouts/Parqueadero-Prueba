package parqueadero.dominio;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import parqueadero.entidad.ServicioEntity;
import parqueadero.entidad.TarifaEntity;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

public class Vigilante {
	
	private static final Integer CAPACIDAD_CARROS = 20;
	private static final Integer CAPACIDAD_MOTOS = 10;
	private static final char LETRA_RESTRICCION ='A';
	private static final String MEMSAJE_SIN_TARIFA = "No existe una tarifa para el tipo de vehiculo";
	private static final String MEMSAJE_SIN_CUPO = "No hay cupo disponible para el tipo de vehiculo";
	private static final String MEMSAJE_RESTRINGIDO = "Este vehiculo tiene restriccion de ingreso";
	private static final String MEMSAJE_GUARDADO_EXITOSO = "Se ha registrado el ingreso exitosamente";
	private static final String MEMSAJE_GUARDADO_ERROR = "Se ha presentado un error durante el guardado";
	private static final String tipoCarro = "c";
	private static final String tipoMoto = "m";
	
	@Autowired
	ServicioRepositorio servicioRepositorio;
	@Autowired
	TarifaRepositorio tarifaRepositorio;	
	
	
	public Vigilante(ServicioRepositorio servicioRepositorio, TarifaRepositorio tarifaRepositorio) {
		this.servicioRepositorio = servicioRepositorio;
		this.tarifaRepositorio = tarifaRepositorio;
	}

	public String ingresarVehiculo(ServicioEntity servicio) {
		LocalDateTime fechaIngreso = LocalDateTime.now();
		servicio.setFechaIngreso(Date.from(fechaIngreso.atZone(ZoneId.systemDefault()).toInstant()));
		servicio.setPlaca(servicio.getPlaca().toUpperCase());
		if(!hayCupo(servicio.getTipoVehiculo())) {
			return MEMSAJE_SIN_CUPO;
		}
		if(tieneRestriccion(servicio)) {
			return MEMSAJE_RESTRINGIDO;
		}
		
		TarifaEntity tarifa = asignarTarifa(servicio.getTipoVehiculo());
		if(tarifa == null) {
			return MEMSAJE_SIN_TARIFA; 
		}
		servicio.setTarifa(tarifa);
		ServicioEntity servicioGuardado = servicioRepositorio.save(servicio);
		if(servicioGuardado==null) {
			return MEMSAJE_GUARDADO_ERROR;
		}
		
		return MEMSAJE_GUARDADO_EXITOSO;
		
	}
	
	public Boolean hayCupo(String tipoVehiculo) {
		Long camposOcupados = servicioRepositorio.countByFechaSalidaAndTipoVehiculo(null, tipoVehiculo);
		if (tipoVehiculo.equals(tipoCarro)) {
			if (camposOcupados < CAPACIDAD_CARROS) {
				return true;
			}
		} else {
			if (camposOcupados < CAPACIDAD_MOTOS) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean tieneRestriccion (ServicioEntity servicio) {
		if (servicio.getPlaca().charAt(0) == LETRA_RESTRICCION && servicio.getTipoVehiculo().equals(tipoCarro)) {
			Calendar calendario = new GregorianCalendar();
			calendario.setTime(servicio.getFechaIngreso());
			if(calendario.get(Calendar.DAY_OF_WEEK) > 2) {
				return true;
			}			
		}
		return false;
	}
	
	public TarifaEntity asignarTarifa(String tipoVehiculo) {
		TarifaEntity tarifa = null;
		tarifa = tarifaRepositorio.findByTipoVehiculo(tipoVehiculo);
		return tarifa;
	}
	

}
