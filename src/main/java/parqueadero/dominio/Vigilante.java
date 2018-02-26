package parqueadero.dominio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import parqueadero.entidad.FacturaEntity;
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
	private static final String MEMSAJE_NO_EXISTE_VEHICULO = "No se ha encontrado un vehiculo estacionado con esa placa";
	private static final Integer CILINDRAJE_COBRO_EXTRA = 500;
	private static final Double VALOR_COBRO_EXTRA = 2000D;
	private static final Long MINUTOS_DE_UNA_HORA = 60L;
	private static final Long MINUTOS_DEL_DIA = 1440L;
	private static final String TIPO_VEHIUCLO_CARRO = "c";
	private static final String TIPO_VEHICULO_MOTO = "m";
	
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
		if (tipoVehiculo.equals(TIPO_VEHIUCLO_CARRO)) {
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
		if (servicio.getPlaca().charAt(0) == LETRA_RESTRICCION && servicio.getTipoVehiculo().equals(TIPO_VEHIUCLO_CARRO)) {
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
	
	public String facturarServicio(String placa) {
		FacturaEntity factura = new FacturaEntity();
		LocalDateTime fechaSalida = LocalDateTime.now();
		ServicioEntity servicio = buscarServicioActivo(placa);
		Double valorExtras = 0D;
		if (servicio == null) {
			return MEMSAJE_NO_EXISTE_VEHICULO;
		}
		servicio.setFechaSalida(Date.from(fechaSalida.atZone(ZoneId.systemDefault()).toInstant()));
		
		List<Long> diasHorasDeServicio = calcularTiempoServicio(servicio.getFechaIngreso(), servicio.getFechaSalida());
		Double valorSubtotal = calcularSubtotalFactura(diasHorasDeServicio, servicio.getTipoVehiculo());
		
		
		if (servicio.getTipoVehiculo().equals(TIPO_VEHICULO_MOTO) && servicio.getCilindraje()>CILINDRAJE_COBRO_EXTRA) {
			valorExtras = VALOR_COBRO_EXTRA;			
		}
		
		Double total = valorSubtotal + valorExtras;
		
		factura.setServicio(servicio);
		factura.setDiasfacturados(diasHorasDeServicio.get(0));
		factura.setHorasFacturadas(diasHorasDeServicio.get(1));
		factura.setSubtotal(valorSubtotal);
		factura.setValorOtrosConceptos(valorExtras);
		factura.setTotalFactura(total);
		
		
		return "El valor del subtotal es de " + valorSubtotal.toString() + " El valor de los Extras es de " + valorExtras.toString() + 
				"Para un total de " + total;		
	}
	
	public ServicioEntity buscarServicioActivo(String placa) {
		return servicioRepositorio.findByPlacaAndFechaSalida(placa, null);
	}
	
	public List<Long> calcularTiempoServicio(Date inicio, Date fin) {
		List<Long> diasHoras = new ArrayList<>();
		Long cantidadDias;
		Long cantidadHoras;
		LocalDateTime fechaIinicio = LocalDateTime.ofInstant(inicio.toInstant(), ZoneId.systemDefault());
		LocalDateTime fechaFin = LocalDateTime.ofInstant(fin.toInstant(), ZoneId.systemDefault());		
		Duration diferencia = Duration.between(fechaIinicio, fechaFin);
		Long diferenciaEnMinutos = diferencia.getSeconds()/MINUTOS_DE_UNA_HORA;
		cantidadDias = diferenciaEnMinutos/MINUTOS_DEL_DIA;
		if (cantidadDias > 0) {
			diferenciaEnMinutos = diferenciaEnMinutos - (cantidadDias * MINUTOS_DEL_DIA);
		}
		cantidadHoras = diferenciaEnMinutos/MINUTOS_DE_UNA_HORA;
		if(cantidadHoras > 0) {
			if (cantidadHoras >= 9 && cantidadHoras <= 24) {
				cantidadDias = cantidadDias + 1L;
				diferenciaEnMinutos = diferenciaEnMinutos - (cantidadHoras * MINUTOS_DE_UNA_HORA);
				cantidadHoras = 0L;
				
			}else {
				diferenciaEnMinutos = diferenciaEnMinutos - (cantidadHoras * MINUTOS_DE_UNA_HORA);
				if(diferenciaEnMinutos > 0 && diferenciaEnMinutos <= MINUTOS_DE_UNA_HORA) {
					cantidadHoras = cantidadHoras + 1L;
				}
			}
			
		}
		
		diasHoras.add(cantidadDias);
		diasHoras.add(cantidadHoras);
		
		return diasHoras;
		
	}
	
	public Double calcularSubtotalFactura(List<Long> totalDiasHoras, String tipoVehiculo) {
		TarifaEntity tarifa = tarifaRepositorio.findByTipoVehiculo(tipoVehiculo);
		double valorTotal = totalDiasHoras.get(0) * tarifa.getValorDia();
		valorTotal = valorTotal + (totalDiasHoras.get(1) * tarifa.getValorDia());
		return valorTotal;
		
	}
	
	
	
	

}
