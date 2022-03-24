package net.arshaa.rat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.arshaa.rat.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer>{

	public Optional<List<Room>> findByFloorId(Integer id);



}
