package parqueadero.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import parqueadero.entidad.TarifaEntity;

public interface TarifaRepositorio extends JpaRepository<TarifaEntity, Long> {
	
	TarifaEntity findByTipoVehiculo(String tipoVehiculo);
	
}
