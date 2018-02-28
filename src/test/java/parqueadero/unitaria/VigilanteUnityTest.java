package parqueadero.unitaria;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import parqueadero.databuilder.FacturaDataBuilder;
import parqueadero.databuilder.ServicioDataBuilder;
import parqueadero.databuilder.TarifaDataBuilder;
import parqueadero.dominio.Vigilante;
import parqueadero.entidad.FacturaEntity;
import parqueadero.entidad.ServicioEntity;
import parqueadero.entidad.TarifaEntity;
import parqueadero.repositorio.FacturaRepositorio;
import parqueadero.repositorio.ServicioRepositorio;
import parqueadero.repositorio.TarifaRepositorio;

public class VigilanteUnityTest {
	
	ServicioRepositorio servicioRepositorio;
	TarifaRepositorio tarifaRepositorio;
	FacturaRepositorio facturaRepositorio;
	ServicioDataBuilder servicioDataBuilder;
	TarifaDataBuilder tarifaDataBuilder;
	FacturaDataBuilder facturaDataBuilder;
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
		facturaRepositorio = mock(FacturaRepositorio.class);
		servicioDataBuilder = new ServicioDataBuilder();
		tarifaDataBuilder = new  TarifaDataBuilder();
		facturaDataBuilder = new FacturaDataBuilder();
		vigilante = new Vigilante(servicioRepositorio, tarifaRepositorio, facturaRepositorio);
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
		LocalDateTime ldtFechaInicio = LocalDateTime.parse("2018-02-01T08:00");
		LocalDateTime ldtfechaFin = LocalDateTime.parse("2018-02-01T08:15");
		//Act
		List<Long> tiempoServicio = vigilante.calcularTiempoServicio(Date.from(ldtFechaInicio.atZone(ZoneId.systemDefault()).toInstant()), 
				Date.from(ldtfechaFin.atZone(ZoneId.systemDefault()).toInstant()));
		
		//assert
		assertNotNull(tiempoServicio);
	}
	
	@Test
	public void calcularTiempoDiasHorasTest()  {
		//Arrange
		LocalDateTime ldtFechaInicio = LocalDateTime.parse("2018-02-01T00:00");
		LocalDateTime ldtfechaFin = LocalDateTime.parse("2018-02-12T12:15");
		
		//Act
		List<Long> tiempoServicio = vigilante.calcularTiempoServicio(Date.from(ldtFechaInicio.atZone(ZoneId.systemDefault()).toInstant()), 
				Date.from(ldtfechaFin.atZone(ZoneId.systemDefault()).toInstant()));
		
		//assert
		assertNotNull(tiempoServicio);
	}
	
	@Test
	public void calcularTiempoHorasTest()  {
		//Arrange
		LocalDateTime ldtFechaInicio = LocalDateTime.parse("2018-02-01T00:00");
		LocalDateTime ldtfechaFin = LocalDateTime.parse("2018-02-02T07:15");
		
		//Act
		List<Long> tiempoServicio = vigilante.calcularTiempoServicio(Date.from(ldtFechaInicio.atZone(ZoneId.systemDefault()).toInstant()), 
				Date.from(ldtfechaFin.atZone(ZoneId.systemDefault()).toInstant()));
		
		//assert
		assertNotNull(tiempoServicio);
	}
	
	@Test
	public void buscarServicioActivoTest()  {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.build();
		when(servicioRepositorio.findByPlacaAndFechaSalida(servicio.getPlaca(), null)).thenReturn(servicio);
		
		//Act
		ServicioEntity servicioConsultado = vigilante.buscarServicioActivo(servicio.getPlaca());
		
		//Assert
		assertEquals(servicioConsultado.getPlaca(), servicio.getPlaca());
		
	}
	
	@Test
	public void asignarTarifaCarroTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("c").build();
		TarifaEntity tarifa = tarifaDataBuilder.conTipoVehiculo("c").build();
		when(tarifaRepositorio.findByTipoVehiculo(servicio.getTipoVehiculo())).thenReturn(tarifa);
		
		//Act
		TarifaEntity tarifaAsignada = vigilante.asignarTarifa(servicio.getTipoVehiculo());
		
		//Assert
		assertEquals(tarifaAsignada.getTipoVehiculo(), servicio.getTipoVehiculo());
		
	}
	
	@Test
	public void asignarTarifaMotoTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("m").build();
		TarifaEntity tarifa = tarifaDataBuilder.conTipoVehiculo("m").build();
		when(tarifaRepositorio.findByTipoVehiculo(servicio.getTipoVehiculo())).thenReturn(tarifa);
		
		//Act
		TarifaEntity tarifaAsignada = vigilante.asignarTarifa(servicio.getTipoVehiculo());
		
		//Assert
		assertEquals(tarifaAsignada.getTipoVehiculo(), servicio.getTipoVehiculo());
		
	}
	
	@Test
	public void calcularSubtotalTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conTipoVehiculo("m").build();
		TarifaEntity tarifa = tarifaDataBuilder.conTipoVehiculo("m").build();
		when(tarifaRepositorio.findByTipoVehiculo(servicio.getTipoVehiculo())).thenReturn(tarifa);
		List<Long> diasHoras = new ArrayList<>();
		diasHoras.add(1L);
		diasHoras.add(2L);
		Double ValorEsperado = (diasHoras.get(0)* tarifa.getValorDia()) + (diasHoras.get(1)* tarifa.getValorHora()); 
		
		//Act
		Double valor = vigilante.calcularSubtotalFactura(diasHoras, servicio.getTipoVehiculo());
		
		//Assert
		assertEquals(valor, ValorEsperado);
		
	}
	
	@Test
	public void guardarFacturaTest() {
		//Arrange
		ServicioEntity servicio = servicioDataBuilder.conFechaEgreso(new Date()).build();
		FacturaEntity factura = facturaDataBuilder.conServicio(servicio).build();		
		when(facturaRepositorio.save(factura)).thenReturn(factura);
		
		//Act
		FacturaEntity facturaGuardada = vigilante.guuardarFactura(factura);
		
		//Assert
		assertEquals(factura.getTotalFactura(), facturaGuardada.getTotalFactura());
		
	}
	
}
