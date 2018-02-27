package parqueadero.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="tarifa")
public class TarifaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idTarifa;
	
	@Column(nullable = false)
	private String tipoVehiculo;
	
	@Column(nullable = false)
	private Double valorHora;
	
	@Column(nullable = false)
	private Double valorDia;
	
	
	
	public TarifaEntity() {
	}

	public TarifaEntity(Long idTarifa, String tipoVehiculo, Double valorHora, Double valorDia) {
		super();
		this.idTarifa = idTarifa;
		this.tipoVehiculo = tipoVehiculo;
		this.valorHora = valorHora;
		this.valorDia = valorDia;
	}

	public Long getIdTarifa() {
		return idTarifa;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Double getValorHora() {
		return valorHora;
	}

	public void setValorHora(Double valorHora) {
		this.valorHora = valorHora;
	}

	public Double getValorDia() {
		return valorDia;
	}

	public void setValorDia(Double valorDia) {
		this.valorDia = valorDia;
	}

	public void setIdTarifa(Long idTarifa) {
		this.idTarifa = idTarifa;
	}
	
	

}
