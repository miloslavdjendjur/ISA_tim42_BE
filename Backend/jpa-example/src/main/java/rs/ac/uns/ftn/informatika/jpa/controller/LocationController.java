package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.LocationDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.service.LocationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestParam String latitude,
                                                   @RequestParam String longitude,
                                                   @RequestParam String address) {
        Location location = locationService.createOrRetrieveLocation(latitude, longitude, address);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    // Add a GET mapping to fetch all locations
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
}
