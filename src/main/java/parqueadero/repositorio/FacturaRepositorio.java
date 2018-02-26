package parqueadero.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import parqueadero.entidad.FacturaEntity;

@Repository
public interface FacturaRepositorio extends JpaRepository<FacturaEntity, Long>{

}
