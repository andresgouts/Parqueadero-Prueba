package parqueadero.unitaria;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import parqueadero.databuilder.ServicioDataBuilder;
import parqueadero.dominio.Vigilante;
import parqueadero.entidad.ServicioEntity;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

public class VigilanteUnityTest {
	
	ServicioRepositorio servicioRepositorio;
	TarifaRepositorio tarifaRepositorio;
	ServicioDataBuilder servicioDataBuilder;
	Vigilante vigilante;
	private static final Long CAPACIDAD_CARROS =  20L;
	private static final Long CAPACIDAD_MOTOS = 10L;
	private static final Date DIA_SIN_RESTRICCION = new GregorianCalendar(2018, Calendar.FEBRUARY, 26).getTime();
	private static final Date DIA_CON_RESTRICCION = new GregorianCalendar(2018, Calendar.FEBRUARY, 24).getTime();
	@Value("${application.tipovehiculo.carro}")
	private String tipoCarro;
	@Value("${application.tipovehiculo.moto}")
	private String tipoMoto;
	
	@Before
	public void setUp() {
		servicioRepositorio = mock(ServicioRepositorio.class);
		tarifaRepositorio = mock(TarifaRepositorio.class);
		servicioDataBuilder = new ServicioDataBuilder();
		vigilante = new Vigilante(servicioRepositorio, tarifaRepositorio);
	}
	
	
	@Test
	public void sinCupoDisponibleMotoTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.build();
		when(servicioRepositorio.countByFechaSalidaAndTipoVehiculo(null,servicio.getTipoVehiculo())).thenReturn(CAPACIDAD_MOTOS);
		
		
		//Act
		Boolean cupo = vigilante.hayCupo(servicio.getTipoVehiculo());
		
		//assert
		assertFalse(cupo);
	}
	
	@Test
	public void conCupoDisponibleMotoTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.build();
		when(servicioRepositorio.countByFechaSalidaAndTipoVehiculo(null,servicio.getTipoVehiculo())).thenReturn(CAPACIDAD_MOTOS - 3L);
		
		//Act
		Boolean cupo = vigilante.hayCupo(servicio.getTipoVehiculo());
		
		//assert
		assertTrue(cupo);
	}
	
	@Test
	public void sinCupoDisponibleCarroTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").build();
		when(servicioRepositorio.countByFechaSalidaAndTipoVehiculo(null,servicio.getTipoVehiculo())).thenReturn(CAPACIDAD_CARROS);
		
		//Act
		Boolean cupo = vigilante.hayCupo(servicio.getTipoVehiculo());
		
		//assert
		assertFalse(cupo);
	}
	
	@Test
	public void conCupoDisponibleCarroTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").build();
		when(servicioRepositorio.countByFechaSalidaAndTipoVehiculo(null,servicio.getTipoVehiculo())).thenReturn(CAPACIDAD_CARROS - 3L);
		
		//Act
		Boolean cupo = vigilante.hayCupo(servicio.getTipoVehiculo());
		
		//assert
		assertTrue(cupo);
	}
	
	@Test
	public void carroSinRestriccionPorDiaTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").conPlaca("AHP105").
				conFechaIngreso(DIA_SIN_RESTRICCION).build();
		
		//Act
		Boolean restriccion = vigilante.tieneRestriccion(servicio);
		
		//assert
		assertFalse(restriccion);
	}
	
	@Test
	public void carroConRestriccionPorPlacaTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").conPlaca("AHP105").
				conFechaIngreso(DIA_CON_RESTRICCION).build();
		
		//Act
		Boolean restriccion = vigilante.tieneRestriccion(servicio);
		
		//assert
		assertTrue(restriccion);
	}
	
	@Test
	public void carroSinRestriccionPorPlacaTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").conFechaIngreso(DIA_CON_RESTRICCION).build();
		
		//Act
		Boolean restriccion = vigilante.tieneRestriccion(servicio);
		
		//assert
		assertFalse(restriccion);
	}
	
	@Test
	public void motoSinRestriccionPorPlacaTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("m").conFechaIngreso(DIA_CON_RESTRICCION).conPlaca("AAA12C").build();
		
		//Act
		Boolean restriccion = vigilante.tieneRestriccion(servicio);
		
		//assert
		assertFalse(restriccion);
	}
	
	@Test
	public void calcularTiempoMinutosTest()  {
		//Arrange
		Date fechaInicio = new Date(2018, 2, 01, 0, 0);
		Date fechaFin = new Date(2018, 2, 02, 8, 15);
		
		//Act
		List<Long> tiempoServicio = vigilante.calcularTiempoServicio(fechaInicio, fechaFin);
		
		//assert
		assertNotNull(tiempoServicio);
	}

}
