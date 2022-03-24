package net.arshaa.rat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Models.BedSummary;
import Models.BedsInfo;
import Models.BuildingInfo;
import Models.Buildings;
import Models.FloorsInfo;
import Models.RoomsInfo;
import net.arshaa.rat.entity.Bed;
import net.arshaa.rat.entity.Building;
import net.arshaa.rat.entity.Floor;
import net.arshaa.rat.entity.Room;
import net.arshaa.rat.repository.AllBuildingRepository;
import net.arshaa.rat.repository.BedRepository;
import net.arshaa.rat.repository.BedSummaryRepo;
import net.arshaa.rat.repository.BuildingRepository;
import net.arshaa.rat.repository.FloorRepository;
import net.arshaa.rat.repository.RoomRepository;

@RestController
@RequestMapping("/room")
@CrossOrigin("http://localhost:3000")

public class RoomController {

// Dependency Injection-Injecting those objects into controller layer

	@Autowired
	private BedRepository bedrepo;

	@Autowired
	private BuildingRepository buildingRepo;

	@Autowired
	private FloorRepository floorRepo;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private AllBuildingRepository buildingsRepo;
	
	@Autowired
	private BedSummaryRepo bedsumRepo;

	// sample API to test

	@RequestMapping(path = "/test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>("hello", HttpStatus.OK);
	}

	// GET ALL BUILDINGS
	@RequestMapping(path = "/getBedsByBuildings")
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
											newBed.setBedId(bed.getBedId());
											newBed.setBedId(bed.getBedId());
											newBed.setBedName(bed.getBedName());
											newBed.setBedStatus(bed.isBedStatus());
											newBed.setFloorId(bed.getFloorId());
											newBed.setRoomId(bed.getRoomId());

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

	// GET MAPPING API FOR AVAILABLE BEDS BY BUILDING ID

	@RequestMapping(path = "/getAvailableBedsByBuildingId/{id}", method = RequestMethod.GET)
	public ResponseEntity<java.util.List<Bed>> getAvailableBedsByBuildingId(@PathVariable Integer id) {
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
									newBed.setBedId(bed.getBedId());
									newBed.setBedId(bed.getBedId());
									newBed.setBedName(bed.getBedName());
									newBed.setBedStatus(bed.isBedStatus());
									newBed.setFloorId(bed.getFloorId());
									newBed.setRoomId(bed.getRoomId());
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

	/*
	 * @RequestMapping(path ="/bedStatus/{status}") public
	 * ResponseEntity<java.util.List<Bed>> getByBuildingId(@PathVariable Boolean
	 * status) { List<Bed> listbeds = bedrepo.findByBedStatus(status); return new
	 * ResponseEntity<>(listbeds, HttpStatus.OK); }
	 */

	@RequestMapping(path = "/getBedSummaryForPieChartByBuildingId/{buildingId}", method = RequestMethod.GET)
	public ResponseEntity <List<BedSummary>> getBedsByBuldingId(@PathVariable int buildingId) {
		List<Bed> bedsList = bedrepo.findAllByBuildingId(buildingId);
		
		List<Bed> listbeds = bedsumRepo.findByBuildingIdAndBedStatus(buildingId, false);
		List<Bed> listOfAvailablebeds = bedsumRepo.findByBuildingIdAndBedStatus(buildingId, true);
		Bed bed = bedrepo.getBedByBuildingId(buildingId);


		List <BedSummary> bedSummary=new ArrayList<>();
		BedSummary bs = new BedSummary();
		int bedCount = bedsList.size();
		int count=listbeds.size();
		int availabedsCount=listOfAvailablebeds.size();
		bs.setTotalBeds(bedCount);
		bs.setOccupiedBeds(count);
		bs.setAvailableBeds(availabedsCount);
		bs.setBedNum(bs.getBedNum());
		bs.setBuildingName(bed.getBedName());
		bedSummary.add(bs);
		return new ResponseEntity<>(bedSummary, HttpStatus.OK);
	}

	// Get mapping

	/*
	 * @RequestMapping(path ="/getRoomsByFloorId/{id}") public
	 * ResponseEntity<java.util.List<Room>> getById(@PathVariable Integer id) {
	 * 
	 * Optional<List<Room>> listRooms = bedrepo.findById(id); return new
	 * ResponseEntity<>(listRooms.get(), HttpStatus.OK); }
	 */

	/*
	 * @PostMapping(path ="/add") public ResponseEntity<Room> addBed(@RequestBody
	 * Room newRoom) {
	 * 
	 * Room room = bedrepo.save(newRoom); return new ResponseEntity<>(room,
	 * HttpStatus.OK); }
	 */

	/*
	 * @RequestMapping(path ="/getBedsByRoomId/{id}") public
	 * ResponseEntity<java.util.List<Bed>> getById(@PathVariable Integer id) {
	 * 
	 * Optional<List<Bed>> listbed = bedrepo.findByroomId(id); return new
	 * ResponseEntity<>(listbed.get(), HttpStatus.OK); }
	 */

	// Api to add room
	@PostMapping(path = "/addRoom")
	public ResponseEntity<Room> addRoom(@RequestBody Room newRoom) {

		Room room = roomRepo.save(newRoom);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	// Api to add Floor
	@PostMapping(path = "/addFloor")
	public ResponseEntity<Floor> addFloor(@RequestBody Floor newFloor) {

		Floor floor = floorRepo.save(newFloor);
		return new ResponseEntity<>(floor, HttpStatus.OK);
	}

	// Api to add Building
	@PostMapping(path = "/addBuilding")
	public ResponseEntity<Building> addBuilding(@RequestBody Building newBuilding) {

		Building building = buildingRepo.save(newBuilding);
		return new ResponseEntity<>(building, HttpStatus.OK);
	}

	/*
	 * @RequestMapping(path ="/bedStatus/{id}/{status}") public
	 * ResponseEntity<java.util.List<Bed>> getByBuildingId(@PathVariable int
	 * id,@PathVariable Boolean status) {
	 * 
	 * List<Bed> listbeds = bedrepo.findByBedStatusandId(id,status); return new
	 * ResponseEntity<>(listbeds, HttpStatus.OK); }
	 */

	@PostMapping(path = "/addBeds")
	public ResponseEntity<Bed> addBed(@RequestBody Bed newBed) {

		Bed bed = bedrepo.save(newBed);
		return new ResponseEntity<>(bed, HttpStatus.OK);
	}

	// Get beds by roomid

	@RequestMapping(path = "/getBedsByRoomId/{id}")
	public ResponseEntity<java.util.List<Bed>> getById(@PathVariable Integer id) {

		Optional<List<Bed>> listbed = bedrepo.findByroomId(id);
		return new ResponseEntity<>(listbed.get(), HttpStatus.OK);
	}
	/*
	 * @RequestMapping(path ="/getBedsByRoomId/{id}") public
	 * ResponseEntity<java.util.List<Bed>> getByRoomId(@PathVariable Integer id) {
	 * 
	 * List<Bed> listFloor = bedrepo.findByroomId(id); return new
	 * ResponseEntity<>(listFloor, HttpStatus.OK); }
	 */

	/*
	@RequestMapping(path = "/bedSummaryForPieChart", method = RequestMethod.GET)
	public ResponseEntity<List<NewBuildModel>> getAvailableBedsByBuilding() {
		List<NewBuildModel> info = new ArrayList<NewBuildModel>();
		List<Building> getBuildings = buildingRepo.findAll();
		if (!getBuildings.isEmpty()) {
			getBuildings.forEach(building -> {
				NewBuildModel newBuild = new NewBuildModel();
				Optional<Building> getBuilding = buildingRepo.findById(building.getBuilding_id());
				if (getBuilding.isPresent()) {
					newBuild.setBuilding_id(getBuilding.get().getBuilding_id());
					newBuild.setBuilding_name(getBuilding.get().getBuilding_name());
					List<BedSummary> bedSum = new ArrayList<>();

					List<Bed> bedsList = bedrepo.findAllByBuildingId(building.getBuilding_id());
					List<Bed> listbeds = bedsumRepo.findByBuildingIdAndBedStatus(building.getBuilding_id(), false);
					List<Bed> listOfAvailablebeds = bedsumRepo.findByBuildingIdAndBedStatus(building.getBuilding_id(),
							true);

					BedSummary bs = new BedSummary();
					int bedsCount = bedsList.size();
					int occupiedBedsCount = listbeds.size();
					int totalAvailableBeds = listOfAvailablebeds.size();
					bs.setTotalBeds(bedsCount);
					bs.setOccupiedBeds(occupiedBedsCount);
					bs.setAvailableBeds(totalAvailableBeds);
					bedSum.add(bs);

					newBuild.setBedSummary(bedSum);
					info.add(newBuild);
				}
			});
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}
*/

	@RequestMapping(path = "/bedStatus/{status}")
	public ResponseEntity<java.util.List<Bed>> getByBuildingId(@PathVariable Boolean status) {

		List<Bed> listbeds = bedrepo.findByBedStatus(status);
		return new ResponseEntity<>(listbeds, HttpStatus.OK);
	}

}
