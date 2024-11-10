package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Location;

public class LocationDTO {

    private String latitude;
    private String longitude;
    private String address;

    public LocationDTO(Location location) {
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
        this.address = location.getAddress();
    }

    // Getters
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}

