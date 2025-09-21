package com.mycompany.travelmanagementsystem.service;

import com.mycompany.travelmanagementsystem.entity.Hotel;
import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import com.mycompany.travelmanagementsystem.repository.HotelBookingRepository;
import com.mycompany.travelmanagementsystem.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        existingHotel.setHotelName(hotelDetails.getHotelName());
        existingHotel.setCity(hotelDetails.getCity());
        existingHotel.setCostPerPerson(hotelDetails.getCostPerPerson());
        existingHotel.setAcRoomCost(hotelDetails.getAcRoomCost());
        existingHotel.setFoodIncludedCost(hotelDetails.getFoodIncludedCost());
        existingHotel.setImageUrl(hotelDetails.getImageUrl());
        
        return hotelRepository.save(existingHotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
    
    public HotelBooking createHotelBooking(HotelBooking booking) {
        return hotelBookingRepository.save(booking);
    }

    public List<HotelBooking> getBookingsByUserId(Long userId) {
        return hotelBookingRepository.findByUserId(userId);
    }

    // This is the new search method that was missing
    public List<Hotel> searchHotels(String query) {
        return hotelRepository.findByHotelNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query);
    }
}

