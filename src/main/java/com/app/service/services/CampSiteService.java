package com.app.service.services;

import com.app.data.repositories.ReservationsRepository;
import com.app.exceptions.CampSiteServiceException;
import com.app.exceptions.CampsiteNotAvailableOnDateException;
import com.app.exceptions.ReservationDoesNotExistException;
import com.app.models.ReservationModel;
import com.app.service.validators.ReservationModelValidator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class CampSiteService {

  private ReservationsRepository campSiteReservationsRepository;

  @Inject
  public CampSiteService(ReservationsRepository campSiteReservationsRepository) {
    this.campSiteReservationsRepository = campSiteReservationsRepository;
  }

  public List<String> getReservedDates(LocalDate from, LocalDate to) {
    return campSiteReservationsRepository.getReservedDates()
        .stream().sorted()
        .filter(date -> date.isEqual(from) || date.isAfter(from))
        .filter(date -> date.isEqual(to) || date.isBefore(to)).map(LocalDate::toString)
        .collect(Collectors.toList());
  }

  public synchronized ReservationModel createReservation(ReservationModel reservation)
      throws CampSiteServiceException {
    ReservationModelValidator.validateReservationModel(reservation);
    for (LocalDate date : reservation.getReservationDates()) {
      if (!campSiteReservationsRepository.isDateAvailable(date)) {
        throw new CampsiteNotAvailableOnDateException(date);
      }
    }

    return campSiteReservationsRepository.createReservation(reservation);
  }

  public ReservationModel getReservation(String id) throws CampSiteServiceException {
    ReservationModel reservation = campSiteReservationsRepository.getReservation(id);
    if (!campSiteReservationsRepository.reservationExists(id)) {
      throw new ReservationDoesNotExistException(id);
    }

    return reservation;
  }

  public synchronized ReservationModel updateReservation(String id, ReservationModel reservation)
      throws CampSiteServiceException {
    if (!campSiteReservationsRepository.reservationExists(id)) {
      throw new ReservationDoesNotExistException(id);
    }

    ReservationModel oldReservation = campSiteReservationsRepository.getReservation(id);
    ReservationModelValidator.validateReservationModel(reservation);

    for (LocalDate date : reservation.getReservationDates()) {
      if (!campSiteReservationsRepository.isDateAvailable(date) && !oldReservation
          .getReservationDates().contains(date)) {
        throw new CampsiteNotAvailableOnDateException(date);
      }
    }

    return campSiteReservationsRepository.updateReservation(id, reservation);
  }

  public void deleteReservation(String id) throws CampSiteServiceException {
    if (!campSiteReservationsRepository.reservationExists(id)) {
      throw new ReservationDoesNotExistException(id);
    }
    campSiteReservationsRepository.deleteReservation(id);
  }
}