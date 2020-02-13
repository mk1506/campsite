package com.app.exceptions.mappers;

import com.app.exceptions.CampSiteServiceException;
import com.app.models.ErrorResponseModel;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.eclipse.jetty.http.HttpStatus;

@Provider
@Singleton
public class UnknownExceptionMapper implements
    ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception e) {
    return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new ErrorResponseModel(HttpStatus.INTERNAL_SERVER_ERROR_500,"An internal server error occured."))
        .build();
  }
}