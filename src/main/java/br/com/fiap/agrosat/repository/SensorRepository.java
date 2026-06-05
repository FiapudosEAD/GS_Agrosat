package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
