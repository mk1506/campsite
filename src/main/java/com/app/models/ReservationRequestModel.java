package com.app.models;

import com.app.util.LocalDateDeserializer;
import com.app.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationRequestModel {

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

  public ReservationRequestModel() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReservationRequestModel)) {
      return false;
    }
    ReservationRequestModel that = (ReservationRequestModel) o;
    return Objects.equals(getName(), that.getName()) &&
        Objects.equals(getEmail(), that.getEmail()) &&
        Objects.equals(getArrivalDate(), that.getArrivalDate()) &&
        Objects.equals(getDepartureDate(), that.getDepartureDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getEmail(), getArrivalDate(), getDepartureDate());
  }
}
