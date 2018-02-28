package parqueadero.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parqueadero.dominio.Vigilante;
import parqueadero.entidad.ServicioEntity;
import parqueadero.repositorio.FacturaRepositorio;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

@RestController
@RequestMapping("/vigilante")
public class VigilanteController {
	
	
	@Autowired
    ServicioRepositorio servicioRepositoro;	
	@Autowired
    TarifaRepositorio tarifaRepositorio;
	@Autowired
	FacturaRepositorio facturaRepositorio;
	
	private static final String INGRESO_SIN_PLACA = "Debe digitar la placa del vehiculo que desea ingresar";
	private static final String SALIDA_SIN_PLACA = "Debe digitar la placa del vehiculo que desea sacar";
	private static final String INGRESO_SIN_TIPO_VEHICULO = "Debe seleccionar un tiop de vehiculo";
	private static final String INGRESO_SIN_CILINDRAJE = "Debe ingresar el cilindraje del vehiculo";
	private static final String TIPO_VEHICULO_MOTO = "m";
	
	//Sacar vehiculo
	@GetMapping("/sacar/{placa}")
	public String consultarTarifa(@PathVariable(value = "placa") String placa) {
		if (placa == null || placa.isEmpty()) {
			return SALIDA_SIN_PLACA;
		}
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio, this.facturaRepositorio);
		return vigilante.facturarServicio(placa);
	}
	
	//Ingresar Vehiculo
	@PostMapping("/ingresar")
	public String ingresarVehiculo(@Valid @RequestBody ServicioEntity servicio) {
		if(servicio.getPlaca() == null || servicio.getPlaca().isEmpty()) {
			return INGRESO_SIN_PLACA;								
		}
		if(servicio.getTipoVehiculo() == null || servicio.getTipoVehiculo().isEmpty()) {
			return INGRESO_SIN_TIPO_VEHICULO;
		}
		if(servicio.getTipoVehiculo().equals(TIPO_VEHICULO_MOTO) && servicio.getCilindraje() ==null) {
			return INGRESO_SIN_CILINDRAJE;
		}
		
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio, this.facturaRepositorio);
		return vigilante.ingresarVehiculo(servicio);
	}	

}
