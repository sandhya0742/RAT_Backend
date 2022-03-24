package net.arshaa.rat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class AllBuildings {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int buildingId;
	

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	
	
	
}
