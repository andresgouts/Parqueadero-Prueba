package parqueadero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parqueadero.dominio.Vigilante;
import parqueadero.entidad.ServicioEntity;
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
	@GetMapping("/notes/{placa}")
    public String consultarTarifa(@PathVariable(value = "placa") String placa) {
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio);
		return vigilante.facturarServicio(placa);
    }

}
