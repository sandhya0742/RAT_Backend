package net.arshaa.rat.entity;

import javax.persistence.*;

@Entity
	public class Building {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int building_id;
	@Column
	private String building_name;
	public int getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}
	public String getBuilding_name() {
		return building_name;
	}
	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}
	public Building(int building_id, String building_name) {
		super();
		this.building_id = building_id;
		this.building_name = building_name;

	}
	public Building() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
