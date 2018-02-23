package parqueadero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parqueadero.dominio.Vigilante;
import parqueadero.entidad.TarifaEntity;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

@RestController
@RequestMapping("/api")
public class ServicioController {
	
	
	@Autowired
    ServicioRepositorio servicioRepositoro;	
	@Autowired
    TarifaRepositorio tarifaRepositorio;
	
    //Get All
	@GetMapping("/notes")
    public TarifaEntity consultarTarifa() {	
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio);
		return vigilante.asignarTarifa("m");
    }

}
