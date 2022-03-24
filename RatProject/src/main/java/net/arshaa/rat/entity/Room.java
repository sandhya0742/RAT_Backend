package net.arshaa.rat.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roomId;
	@Column
	private String roomName;
	
	
	@Column
	private int floorId;

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public Room(int roomId, String roomName, Bed[] bed) {
		super();
		this.roomId = roomId;
		this.roomName = roomName;
	}

	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

