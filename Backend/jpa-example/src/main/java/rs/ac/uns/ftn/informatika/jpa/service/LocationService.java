package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    private static final String DEFAULT_LATITUDE = "45.2671";
    private static final String DEFAULT_LONGITUDE = "19.8335";
    private static final String DEFAULT_ADDRESS = "Novi Sad, Serbia";

    public Location createOrRetrieveLocation(String latitude, String longitude, String address) {
        if (latitude == null || latitude.isEmpty()) {
            latitude = DEFAULT_LATITUDE;
        }
        if (longitude == null || longitude.isEmpty()) {
            longitude = DEFAULT_LONGITUDE;
        }
        if (address == null || address.isEmpty()) {
            address = DEFAULT_ADDRESS;
        }

        Location location = new Location();
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));
        location.setAddress(address);

        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
