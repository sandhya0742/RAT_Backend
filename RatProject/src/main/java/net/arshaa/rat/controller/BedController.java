package net.arshaa.rat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Models.BedSummary;
import Models.BedsInfo;
import Models.BuildingInfo;
import Models.BuildingModel;
import Models.FloorsInfo;
import Models.NewBuildModel;
import Models.RoomsInfo;
import net.arshaa.rat.entity.Bed;
import net.arshaa.rat.entity.Building;
import net.arshaa.rat.entity.Floor;
import net.arshaa.rat.entity.Room;
import net.arshaa.rat.repository.BedRepository;
import net.arshaa.rat.repository.BedSummaryRepo;
import net.arshaa.rat.repository.FloorRepository;
import net.arshaa.rat.repository.BuildingRepository;
import net.arshaa.rat.repository.RoomRepository;

@RestController
@RequestMapping("/bed")
@CrossOrigin("http://localhost:3000")
public class BedController {

	@Autowired
	private BedRepository bedrepo;

	@Autowired
	private BuildingRepository buildingRepo;

	@Autowired
	private FloorRepository floorRepo;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private BedSummaryRepo bedsumRepo;


//Api for test 

	@RequestMapping(path = "/test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>("hello", HttpStatus.OK);
	}

	
// GET ALL BUILDINGS

	@RequestMapping(path = "/getBedsByAllBuildings")
	public ResponseEntity<List<BuildingInfo>> getBedsByBuildings() {
		List<BuildingInfo> info = new ArrayList<>();
		List<Building> getBuildings = buildingRepo.findAll();
		if (!getBuildings.isEmpty()) {
			getBuildings.forEach(building -> {
				BuildingInfo newBuild = new BuildingInfo();
				Optional<Building> getBuilding = buildingRepo.findById(building.getBuilding_id());
				if (getBuilding.isPresent()) {
					newBuild.setBuildingName(getBuilding.get().getBuilding_name());
					List<FloorsInfo> floorsList = new ArrayList<>();
					Optional<List<Floor>> getFloors = floorRepo.findByBuildingId(getBuilding.get().getBuilding_id());
					if (getFloors.isPresent()) {
						getFloors.get().forEach(floor -> {
							FloorsInfo newFloor = new FloorsInfo();
							newFloor.setFloorName(floor.getFloorName());
							List<RoomsInfo> roomList = new ArrayList<>();
							Optional<List<Room>> getRooms = roomRepo.findByFloorId(floor.getFloorId());
							if (getRooms.isPresent()) {
								getRooms.get().forEach(room -> {
									RoomsInfo newRoom = new RoomsInfo();
									newRoom.setRoomNumber(room.getRoomName());
									List<BedsInfo> bedsList = new ArrayList<>();
									Optional<List<Bed>> getBeds = bedrepo.findByroomId(room.getRoomId());
									if (getBeds.isPresent()) {
										getBeds.get().forEach(bed -> {
											BedsInfo newBed = new BedsInfo();
											newBed.setBedStatus(bed.isBedStatus());
											newBed.setBuildingId(bed.getBuildingId());
											newBed.setBuildingName(bed.getBuildingName());
											newBed.setFloorId(bed.getFloorId());
											newBed.setDefaultRent(bed.getDefaultRent());
											newBed.setRoomId(bed.getRoomId());
											newBed.setAc(bed.isAc());
											newBed.setBedId(bed.getBedId());
											newBed.setBedName(bed.getBedName());
											newBed.setBedNum(bed.getBedNum());
											newBed.setGuestId(bed.getGuestId());
											bedsList.add(newBed);
										});
									}
									newRoom.setBeds(bedsList);
									roomList.add(newRoom);
								});
							}
							newFloor.setRooms(roomList);
							floorsList.add(newFloor);
						});
					}
					newBuild.setFloors(floorsList);
				}
				info.add(newBuild);
			});
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	
// Get beds by building Id

	@RequestMapping(path = "/getBedsByBuildingId/{id}", method = RequestMethod.GET)
	public ResponseEntity<BuildingInfo> getByBuildingId(@PathVariable Integer id) {

		BuildingInfo info = new BuildingInfo();
		Optional<Building> getBuilding = buildingRepo.findById(id);
		if (getBuilding.isPresent()) {
			info.setBuildingName(getBuilding.get().getBuilding_name());
			List<FloorsInfo> floorsList = new ArrayList<>();
			Optional<List<Floor>> getFloors = floorRepo.findByBuildingId(getBuilding.get().getBuilding_id());
			if (getFloors.isPresent()) {
				getFloors.get().forEach(floor -> {
					FloorsInfo newFloor = new FloorsInfo();
					newFloor.setFloorName(floor.getFloorName());
					List<RoomsInfo> roomList = new ArrayList<>();
					Optional<List<Room>> getRooms = roomRepo.findByFloorId(floor.getFloorId());
					if (getRooms.isPresent()) {
						getRooms.get().forEach(room -> {
							RoomsInfo newRoom = new RoomsInfo();
							newRoom.setRoomNumber(room.getRoomName());
							List<BedsInfo> bedsList = new ArrayList<>();
							Optional<List<Bed>> getBeds = bedrepo.findByroomId(room.getRoomId());
							if (getBeds.isPresent()) {
								getBeds.get().forEach(bed -> {
									BedsInfo newBed = new BedsInfo();
									newBed.setBuildingId(bed.getBuildingId());
									newBed.setBuildingName(bed.getBuildingName());
									newBed.setRoomId(bed.getRoomId());
									newBed.setFloorId(bed.getFloorId());
									newBed.setBedId(bed.getBedId());
									newBed.setBedName(bed.getBedName());
									newBed.setBedStatus(bed.isBedStatus());
									newBed.setDefaultRent(bed.getDefaultRent());
									newBed.setAc(bed.isAc());
									newBed.setGuestId(bed.getGuestId());
									newBed.setBedNum(bed.getBedNum());
									bedsList.add(newBed);
								});
							}
							newRoom.setBeds(bedsList);
							roomList.add(newRoom);
						});
					}
					newFloor.setRooms(roomList);
					floorsList.add(newFloor);
				});
			}
			info.setFloors(floorsList);
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	
	// GET MAPPING API FOR AVAILABLE BEDS BY BUILDING ID

	@RequestMapping(path = "/getAvailableBedsByBuildingId/{id}", method = RequestMethod.GET)
	public ResponseEntity<java.util.List<Bed>> buildingId(@PathVariable Integer id) {
		List<Bed> listbed = new ArrayList<>();
		Optional<Building> getBuilding = buildingRepo.findById(id);
		if (getBuilding.isPresent()) {
			List<FloorsInfo> floorsList = new ArrayList<>();
			Optional<List<Floor>> getFloors = floorRepo.findByBuildingId(getBuilding.get().getBuilding_id());
			if (getFloors.isPresent()) {
				getFloors.get().forEach(floor -> {
					Optional<List<Room>> getRooms = roomRepo.findByFloorId(floor.getFloorId());
					if (getRooms.isPresent()) {
						getRooms.get().forEach(room -> {
							Optional<List<Bed>> getBeds = bedrepo.findByroomIdAndBedStatus(room.getRoomId(), true);
							if (getBeds.isPresent()) {
								getBeds.get().forEach(bed -> {
									Bed newBed = new Bed();
									listbed.add(newBed);
								});
							}
						});
					}
				});
			}
		}
		return new ResponseEntity<List<Bed>>(listbed, HttpStatus.OK);
	}

	
// GET MAPPING API FOR NOT AVAILABLE BEDS BY BUILDING ID

	@RequestMapping(path = "/getNotAvailableBedsByBuildingId/{id}", method = RequestMethod.GET)
	public ResponseEntity<java.util.List<Bed>> getNotAvailableBedsByBuildingId(@PathVariable Integer id) {
		List<Bed> listbed = new ArrayList<>();
		Optional<Building> getBuilding = buildingRepo.findById(id);
		if (getBuilding.isPresent()) {
			List<FloorsInfo> floorsList = new ArrayList<>();
			Optional<List<Floor>> getFloors = floorRepo.findByBuildingId(getBuilding.get().getBuilding_id());
			if (getFloors.isPresent()) {
				getFloors.get().forEach(floor -> {
					Optional<List<Room>> getRooms = roomRepo.findByFloorId(floor.getFloorId());
					if (getRooms.isPresent()) {
						getRooms.get().forEach(room -> {
							Optional<List<Bed>> getBeds = bedrepo.findByroomIdAndBedStatus(room.getRoomId(), false);
							if (getBeds.isPresent()) {
								getBeds.get().forEach(bed -> {
									Bed newBed = new Bed();
									newBed.setBedId(bed.getBedId());
									newBed.setBedId(bed.getBedId());
									newBed.setBedName(bed.getBedName());
									newBed.setBedStatus(bed.isBedStatus());
									newBed.setFloorId(bed.getFloorId());
									newBed.setRoomId(bed.getRoomId());
									newBed.setBedNum(bed.getBedNum());
									newBed.setDefaultRent(bed.getDefaultRent());
									newBed.setBuildingId(bed.getBuildingId());
									newBed.setBuildingName(bed.getBuildingName());
									listbed.add(newBed);
								});
							}
						});
					}
				});
			}
		}
		return new ResponseEntity<List<Bed>>(listbed, HttpStatus.OK);
	}

	
//    getApi for all buldings available beds

	@RequestMapping(path = "/getAvailableBedsByBuildings")
	public ResponseEntity<List<BuildingModel>> getAvailableBedsByBuildings() {
		List<BuildingModel> info = new ArrayList<>();
		List<Building> getBuildings = buildingRepo.findAll();
		if (!getBuildings.isEmpty()) {
			getBuildings.forEach(building -> {
				BuildingModel newBuild = new BuildingModel();
				Optional<Building> getBuilding = buildingRepo.findById(building.getBuilding_id());
				if (getBuilding.isPresent()) {
					newBuild.setBuildingId(getBuilding.get().getBuilding_id());
					newBuild.setBuildingName(getBuilding.get().getBuilding_name());
					List<BedsInfo> bedsList = new ArrayList<>();
					Optional<List<Bed>> getBeds = bedrepo
							.findBybuildingIdAndBedStatus(getBuilding.get().getBuilding_id(), true);
					if (getBeds.isPresent()) {
						getBeds.get().forEach(bed -> {
							BedsInfo newBed = new BedsInfo();
							newBed.setBedId(bed.getBedId());
							newBed.setBedId(bed.getBedId());
							newBed.setBedName(bed.getBedName());
							newBed.setBedStatus(bed.isBedStatus());
							newBed.setFloorId(bed.getFloorId());
							newBed.setRoomId(bed.getRoomId());
							newBed.setBedNum(bed.getBedNum());
							newBed.setBuildingId(bed.getBuildingId());
							newBed.setBuildingName(bed.getBuildingName());
							bedsList.add(newBed);

						});
					}
					newBuild.setBeds(bedsList);
					info.add(newBuild);
				}
			});
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	
//UPDATE API FOR BED STATUS AND GUEST ID BY BEDID

	@PutMapping("/updateBedStatusBydBedId")
	public ResponseEntity<String> updateBedStatusByBedNumber(@RequestBody Bed bed) {
		Bed getBed = bedrepo.findByBedId("110-A-NonAC");
		getBed.setGuestId(bed.getGuestId());
		getBed.setBedStatus(!getBed.isBedStatus());
		bedrepo.save(getBed);
		return new ResponseEntity<String>("Updated Successfully", HttpStatus.OK);
	}

	
//GET API FOR GETTING THE COUNT OF TOTAL BEDS AND OCCUPIED BEDS FOR RAT PIE CHART FOR ALL BUILDINGS
	
	@RequestMapping(path = "/getBedSummaryForPieChartByAllBuildings", method = RequestMethod.GET)
	public ResponseEntity<List<NewBuildModel>> getAvailableBedsByBuilding() {
		List<NewBuildModel> info = new ArrayList<NewBuildModel>();
		List<Building> getBuildings = buildingRepo.findAll();
		if (!getBuildings.isEmpty()) {
			getBuildings.forEach(building -> {
				NewBuildModel newBuild = new NewBuildModel();
				Optional<Building> getBuilding = buildingRepo.findById(building.getBuilding_id());
				if (getBuilding.isPresent()) {
					newBuild.setBuildingId(getBuilding.get().getBuilding_id());
					newBuild.setBuildingName(getBuilding.get().getBuilding_name());
					List<Bed> bedsList = bedrepo.findAllByBuildingId(building.getBuilding_id());
					List<Bed> listbeds = bedsumRepo.findByBuildingIdAndBedStatus(building.getBuilding_id(), false);
					List<Bed> listOfAvailablebeds = bedsumRepo.findByBuildingIdAndBedStatus(building.getBuilding_id(),
							true);
					int bedsCount = bedsList.size();
					int occupiedBedsCount = listbeds.size();
					int totalAvailableBeds = listOfAvailablebeds.size();
					newBuild.setTotalBeds(bedsCount);
					newBuild.setOccupiedBeds(occupiedBedsCount);
					newBuild.setAvailableBeds(totalAvailableBeds);
					info.add(newBuild);
				}
			});
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

//GET API FOR GETTING THE COUNT OF TOTAL BEDS AND OCCUPIED BEDS FOR RAT PIE CHART BY BUILDING ID
	
	@RequestMapping(path = "/getBedSummaryForPieChartByBuildingId/{buildingId}", method = RequestMethod.GET)
	public ResponseEntity<List<NewBuildModel>> getAvailableBedsByBuildingId(@PathVariable int buildingId) {
		List<NewBuildModel> info = new ArrayList<>();
		NewBuildModel newBuild = new NewBuildModel();
		Optional<Building> getBuilding = buildingRepo.findById(buildingId);
		if (getBuilding.isPresent()) {
			newBuild.setBuildingId(getBuilding.get().getBuilding_id());
			newBuild.setBuildingName(getBuilding.get().getBuilding_name());
			List<Bed> bedsList = bedrepo.findAllByBuildingId(buildingId);
			List<Bed> listbeds = bedsumRepo.findByBuildingIdAndBedStatus(buildingId, false);
			List<Bed> listOfAvailablebeds = bedsumRepo.findByBuildingIdAndBedStatus(buildingId, true);
			int bedsCount = bedsList.size();
			int occupiedBedsCount = listbeds.size();
			int totalAvailableBeds = listOfAvailablebeds.size();
			newBuild.setTotalBeds(bedsCount);
			newBuild.setOccupiedBeds(occupiedBedsCount);
			newBuild.setAvailableBeds(totalAvailableBeds);
			info.add(newBuild);
		}
		return new ResponseEntity<>(info, HttpStatus.OK);

	}
	

}
