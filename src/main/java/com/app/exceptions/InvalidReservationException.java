package com.app.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class InvalidReservationException extends CampSiteServiceException{
  private static final int HTTP_STATUS = HttpStatus.BAD_REQUEST_400;

  public InvalidReservationException(String error) {
    super("The reservation is invalid: " + error + ".", HTTP_STATUS);
  }
}