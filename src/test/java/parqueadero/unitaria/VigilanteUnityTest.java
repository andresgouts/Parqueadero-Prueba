package parqueadero.unitaria;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import parqueadero.dominio.Vigilante;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

public class VigilanteUnityTest {
	
	@Autowired
	ServicioRepositorio servicioRepositorio;
	@Autowired
	TarifaRepositorio tarifaRepositorio;

	@Test
	public void test() {
		//Assert
		Vigilante vigilante = new Vigilante(servicioRepositorio, tarifaRepositorio);
		
		//Act
		Boolean cupo = vigilante.hayCupo("m");
		
		//assert
		assertTrue(cupo);
	}

}
