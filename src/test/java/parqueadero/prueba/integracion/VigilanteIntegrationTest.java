package parqueadero.prueba.integracion;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;



@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class VigilanteIntegrationTest {
	
	@Autowired
	private MockMvc mocMvc;
	
	private static final String MEMSAJE_NO_ERROR_FACTURA = "Ocurrio un problema al generar la factura";
	
	
	@Test
	@Sql({"/borrarServicios.sql", "/ingresarMotoAltoCilindraje.sql"})
	public void ingresarMotoAltoCilindraje() throws Exception {
		
		MockHttpServletRequestBuilder solicitud = get("/vigilante/sacar/ZJV11C").accept(MediaType.TEXT_HTML_VALUE);
		MvcResult resultado = this.mocMvc.perform(solicitud).andExpect(status().isOk()).andReturn();		
		
		String respuesta = resultado.getResponse().getContentAsString(); 
		
		assertNotEquals(MEMSAJE_NO_ERROR_FACTURA, respuesta);
		
	}
	
	@Test
	@Sql({"/borrarServicios.sql", "/ingresarMotoBajoCilindraje.sql"})
	public void ingresarMotoBajoCilindraje() throws Exception {
		
		MockHttpServletRequestBuilder solicitud = get("/vigilante/sacar/ZJV11C").accept(MediaType.TEXT_HTML_VALUE);
		MvcResult resultado = this.mocMvc.perform(solicitud).andExpect(status().isOk()).andReturn();		
		
		String respuesta = resultado.getResponse().getContentAsString(); 
		
		assertNotEquals(MEMSAJE_NO_ERROR_FACTURA, respuesta);
		
	}
	
	@Test
	@Sql({"/borrarServicios.sql", "/ingresarCarro.sql"})
	public void ingresarCarro() throws Exception {
		
		MockHttpServletRequestBuilder solicitud = get("/vigilante/sacar/HHP105").accept(MediaType.TEXT_HTML_VALUE);
		MvcResult resultado = this.mocMvc.perform(solicitud).andExpect(status().isOk()).andReturn();		
		
		String respuesta = resultado.getResponse().getContentAsString(); 
		
		assertNotEquals(MEMSAJE_NO_ERROR_FACTURA, respuesta);
		
	}

}
