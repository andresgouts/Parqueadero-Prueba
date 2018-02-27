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
	
	//Sacar vehiculo
	@GetMapping("/sacar/{placa}")
	public String consultarTarifa(@PathVariable(value = "placa") String placa) {
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio, this.facturaRepositorio);
		return vigilante.facturarServicio(placa);
	}
	
	//Ingresar Vehiculo
	@PostMapping("/ingresar")
	public String ingresarVehiculo(@Valid @RequestBody ServicioEntity servicio) {
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio, this.facturaRepositorio);
		return vigilante.ingresarVehiculo(servicio);
	}	

}
