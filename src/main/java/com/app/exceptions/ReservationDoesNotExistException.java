package com.app.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ReservationDoesNotExistException extends CampSiteServiceException{
  private static final int HTTP_STATUS = HttpStatus.NOT_FOUND_404;

  public ReservationDoesNotExistException(String reservationId) {
    super("The reservation with id '" + reservationId + "' does not exist.", HTTP_STATUS);
  }
}
