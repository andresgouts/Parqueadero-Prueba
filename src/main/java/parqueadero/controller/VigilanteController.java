package parqueadero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parqueadero.dominio.Vigilante;
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
	@GetMapping("/vehiculo")
    public String consultarTarifa() {
		String placa = "ZJV11C"; 
		Vigilante vigilante = new Vigilante(this.servicioRepositoro, this.tarifaRepositorio, this.facturaRepositorio);
		return vigilante.facturarServicio(placa);
    }

}
