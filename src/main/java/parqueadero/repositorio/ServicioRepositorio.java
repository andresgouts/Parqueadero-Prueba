package parqueadero.repositorio;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parqueadero.entidad.ServicioEntity;

@Repository
public interface ServicioRepositorio extends JpaRepository<ServicioEntity, Long> {
	
	Long countByFechaSalidaAndTipoVehiculo (Date fechaSalida, String tipoVehiculo);
	
}
