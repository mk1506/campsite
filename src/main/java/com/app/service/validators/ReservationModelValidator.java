package com.app.service.validators;

import com.app.exceptions.InvalidReservationException;
import com.app.models.ReservationModel;
import com.google.common.base.Strings;
import java.time.LocalDate;

public class ReservationModelValidator {
  public static void validateReservationModel(ReservationModel reservation) throws InvalidReservationException{
    if(Strings.isNullOrEmpty(reservation.getEmail())){
      throw new InvalidReservationException("email must be filled out");
    }
    if(Strings.isNullOrEmpty(reservation.getName())){
      throw new InvalidReservationException("name must be filled out");
    }
    if(reservation.getArrivalDate() == null){
      throw new InvalidReservationException("arrival date must be filled out");
    }
    if(reservation.getDepartureDate() == null){
      throw new InvalidReservationException("departure date must be filled out");
    }
    if(reservation.getDepartureDate().isBefore(reservation.getArrivalDate())){
      throw new InvalidReservationException("departure date cannot be before arrival date");
    }
    if(!LocalDate.now().isBefore(reservation.getArrivalDate())){
      throw new InvalidReservationException("Reservation must be at least 1 day in advance of arrival.");
    }
    if(reservation.getArrivalDate().isAfter(LocalDate.now().plusMonths(1))){
      throw new InvalidReservationException("Reservation can be at most 1 month in advance.");
    }
    if(reservation.getReservationDates().size() > 3){
      throw new InvalidReservationException("Reservation can be at most 3 days.");
    }
  }
}
