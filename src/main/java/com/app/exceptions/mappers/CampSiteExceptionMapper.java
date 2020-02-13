package com.app.exceptions.mappers;

import com.app.exceptions.CampSiteServiceException;
import com.app.models.ErrorResponseModel;
import javax.inject.Singleton;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

@Provider
@Singleton
public class CampSiteExceptionMapper implements
    ExceptionMapper<CampSiteServiceException> {

  @Override
  public Response toResponse(CampSiteServiceException e) {
    return Response.status(e.getHttpStatus()).entity(new ErrorResponseModel(e.getHttpStatus(),e.getMessage()))
        .build();
  }
}