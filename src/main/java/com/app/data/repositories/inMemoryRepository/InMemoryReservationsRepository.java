package com.app.data.repositories.inMemoryRepository;

import com.app.data.repositories.ReservationsRepository;
import com.app.models.ReservationModel;
import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InMemoryReservationsRepository implements ReservationsRepository {

    Map<String, ReservationModel> reservations;
    Set<LocalDate> reservedDates;

    public InMemoryReservationsRepository() {
        reservations = new ConcurrentHashMap<>();
        reservedDates = Sets.newConcurrentHashSet();
    }

    public Set<LocalDate> getReservedDates(){
        return reservedDates;
    }

    public ReservationModel getReservation(String id) {
        return reservations.get(id);
    }


    public ReservationModel createReservation(ReservationModel reservation){
        String id = UUID.randomUUID().toString();
        reservation.setId(id);
        reservedDates.addAll(reservation.getReservationDates());
        reservations.put(id,reservation);
        return reservation;
    }

    public ReservationModel updateReservation(String id, ReservationModel reservation){
        reservedDates.removeAll(reservations.get(id).getReservationDates());
        reservation.setId(id);
        reservations.put(id,reservation);
        reservedDates.addAll(reservation.getReservationDates());
        return reservation;
    }

    public void deleteReservation(String id){
        ReservationModel reservation = reservations.get(id);
        reservedDates.removeAll(reservation.getReservationDates());
        reservations.remove(id);
    }

    public boolean reservationExists(String id){
        return reservations.containsKey(id);
    }

    public boolean isDateAvailable(LocalDate date){
        return !reservedDates.contains(date);
    }
}
