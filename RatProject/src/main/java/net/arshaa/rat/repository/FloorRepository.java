package net.arshaa.rat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import net.arshaa.rat.entity.Floor;

public interface FloorRepository extends JpaRepository<Floor, Integer>{

	public Optional<List<Floor>> findByBuildingId(Integer id);

}
