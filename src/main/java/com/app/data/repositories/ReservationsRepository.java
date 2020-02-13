package com.app.data.repositories;

import com.app.models.ReservationModel;
import com.google.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReservationsRepository {

    Set<LocalDate> getReservedDates();

    ReservationModel createReservation(ReservationModel reservationModel);

    ReservationModel getReservation(String id);

    ReservationModel updateReservation(String id,ReservationModel reservationModel);

    void deleteReservation(String id);

    boolean reservationExists(String id);

    boolean isDateAvailable(LocalDate date);

}
