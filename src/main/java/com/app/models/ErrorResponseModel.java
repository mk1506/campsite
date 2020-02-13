package com.app.models;

import org.eclipse.jetty.http.HttpStatus;

public class ErrorResponseModel {

  private int errorCode;
  private String message;

  public ErrorResponseModel() {
  }

  public ErrorResponseModel(int errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
