package com.app.models;

import com.app.util.LocalDateDeserializer;
import com.app.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ReservationModel {

  private String id;
  private String name;
  private String email;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate arrivalDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate departureDate;
  @JsonIgnore
  //for internal processing
  private Set<LocalDate> reservationDates;

  public ReservationModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(LocalDate arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public Set<LocalDate> getReservationDates() {
    if(reservationDates == null){
      reservationDates = new HashSet<>();
      LocalDate date = getArrivalDate();
      while (!date.isAfter(getDepartureDate())) {
        this.reservationDates.add(date);
        date = date.plusDays(1);
      }
    }
    return reservationDates;
  }

  public void setReservationDates(Set<LocalDate> reservationDates) {
    this.reservationDates = reservationDates;
  }

  public static ReservationModel fromReservationRequestModel(ReservationRequestModel reservationRequest){
    ReservationModel reservation = new ReservationModel();
    reservation.setName(reservationRequest.getName());
    reservation.setEmail(reservationRequest.getEmail());
    reservation.setArrivalDate(reservationRequest.getArrivalDate());
    reservation.setDepartureDate(reservationRequest.getDepartureDate());

    return reservation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReservationModel)) {
      return false;
    }
    ReservationModel that = (ReservationModel) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getName(), that.getName()) &&
        Objects.equals(getEmail(), that.getEmail()) &&
        Objects.equals(getArrivalDate(), that.getArrivalDate()) &&
        Objects.equals(getDepartureDate(), that.getDepartureDate()) &&
        Objects.equals(getReservationDates(), that.getReservationDates());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getEmail(), getArrivalDate(), getDepartureDate(),
        getReservationDates());
  }
}
