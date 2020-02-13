package com.app.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class CampSiteServiceException extends Exception{
  private int httpStatus;

  public CampSiteServiceException(String message,  int httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public int getHttpStatus() {
    return httpStatus;
  }
}
