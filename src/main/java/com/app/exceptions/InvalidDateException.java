package com.app.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class InvalidDateException extends CampSiteServiceException{
  private static final int HTTP_STATUS = HttpStatus.BAD_REQUEST_400;

  public InvalidDateException() {
    super("The provided date does not follow the format 'yyyy-MM-dd'.", HTTP_STATUS);
  }
}