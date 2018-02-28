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
import parqueadero.repositorio.FacturaRepositorio;
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
	private static final String MEMSAJE_NO_ERROR_FACTURA = "Ocurrio un problema al generar la factura";
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
	@Autowired
	FacturaRepositorio facturaRepositorio;
			

	public Vigilante(ServicioRepositorio servicioRepositorio, TarifaRepositorio tarifaRepositorio,
			FacturaRepositorio facturaRepositorio) {
		this.servicioRepositorio = servicioRepositorio;
		this.tarifaRepositorio = tarifaRepositorio;
		this.facturaRepositorio = facturaRepositorio;
	}

	/**
	 * Metodo para ingresar un vehiculo al parqueadero
	 * @param servicio
	 * @return
	 */
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
	
	/**
	 * Valida si hay cupo en el parqueadero para un tipo de vehiculo
	 * @param tipoVehiculo
	 * @return
	 */
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
	
	/**
	 * Valida si el vehiculo a ingresar al parqueadero tiene restriccion
	 * @param servicio
	 * @return
	 */
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
	
	/**
	 * Consulta la tarifa a aplicar al tipo de vehiculo
	 * @param tipoVehiculo
	 * @return
	 */
	public TarifaEntity asignarTarifa(String tipoVehiculo) {
		TarifaEntity tarifa = null;
		tarifa = tarifaRepositorio.findByTipoVehiculo(tipoVehiculo);
		return tarifa;
	}
	
	/**
	 * genera la salida y factura de vehiculos
	 * @param placa
	 * @return
	 */
	public String facturarServicio(String placa) {
		FacturaEntity factura = new FacturaEntity();
		LocalDateTime fechaSalida = LocalDateTime.now();
		ServicioEntity servicio = buscarServicioActivo(placa);		
		if (servicio == null) {
			return MEMSAJE_NO_EXISTE_VEHICULO;
		}		
		servicio.setFechaSalida(Date.from(fechaSalida.atZone(ZoneId.systemDefault()).toInstant()));
		factura.setServicio(servicio);
		
		List<Long> diasHorasDeServicio = calcularTiempoServicio(servicio.getFechaIngreso(), servicio.getFechaSalida());
		factura.setDiasfacturados(diasHorasDeServicio.get(0));
		factura.setHorasFacturadas(diasHorasDeServicio.get(1));
		factura.setSubtotal(calcularSubtotalFactura(diasHorasDeServicio, servicio.getTipoVehiculo()));
				
		if (servicio.getTipoVehiculo().equals(TIPO_VEHICULO_MOTO) && servicio.getCilindraje()>CILINDRAJE_COBRO_EXTRA) {
			factura.setValorOtrosConceptos(VALOR_COBRO_EXTRA);
		} else {
			factura.setValorOtrosConceptos(0D);
		}
		
		factura.setTotalFactura(factura.getSubtotal() + factura.getValorOtrosConceptos());
		
		FacturaEntity facturaGuardada = guuardarFactura(factura);
		
		if(facturaGuardada != null) {
			return "Esta sacando el vahiculo de placas " + facturaGuardada.getServicio().getPlaca() + ". El valor del subtotal es de " + 
					facturaGuardada.getSubtotal() + " El valor de los Extras es de " + facturaGuardada.getValorOtrosConceptos() 
					+ " Para un total de " + facturaGuardada.getTotalFactura();		
		}
		return MEMSAJE_NO_ERROR_FACTURA;
		
	}
	
	/**
	 * Consulta un servicio activo por placa
	 * @param placa
	 * @return
	 */
	public ServicioEntity buscarServicioActivo(String placa) {
		return servicioRepositorio.findByPlacaAndFechaSalida(placa, null);
	}
	
	/**
	 * calcula los tiempos de estacionamiento segun fecha inicio y fin 
	 * @param inicio
	 * @param fin
	 * @return
	 */
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
		valorTotal = valorTotal + (totalDiasHoras.get(1) * tarifa.getValorHora());
		return valorTotal;
		
	}
	
	public FacturaEntity guuardarFactura (FacturaEntity factura) {				
		return facturaRepositorio.save(factura);
	}
	
	public List<ServicioEntity> consultarVehiculosParqueados() {
		return servicioRepositorio.findByFechaSalida(null);
	}

}
