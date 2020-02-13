package com.app.exceptions;

import java.time.LocalDate;
import org.eclipse.jetty.http.HttpStatus;

public class CampsiteNotAvailableOnDateException extends CampSiteServiceException{
  private static final int HTTP_STATUS = HttpStatus.CONFLICT_409;

  public CampsiteNotAvailableOnDateException(LocalDate date) {
    super("The campsite is not available on date '" + date.toString() + "'.", HTTP_STATUS);
  }
}