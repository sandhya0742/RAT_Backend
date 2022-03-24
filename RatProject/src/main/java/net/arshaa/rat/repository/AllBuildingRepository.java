package net.arshaa.rat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.arshaa.rat.entity.AllBuildings;

public interface AllBuildingRepository<Buildings> extends JpaRepository<AllBuildings, Integer> {

	//public Optional<Buildings> findByName(String name);

}
