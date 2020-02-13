package com.app.resource.resources;

import com.app.exceptions.CampSiteServiceException;
import com.app.exceptions.InvalidDateException;
import com.app.models.ErrorResponseModel;
import com.app.models.ReservationModel;
import com.app.models.ReservationRequestModel;
import com.app.service.services.CampSiteService;
import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/campsite")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = "Campsite API Version 1", protocols = "http")
public class CampSiteResource {

  private CampSiteService campSiteService;

  @Inject
  public CampSiteResource(CampSiteService campSiteService) {
    this.campSiteService = campSiteService;
  }

  @GET
  @Path("/availability")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Returns a list of days for which the campsite is not available in a time period, defaults to upcoming month.", response = ReservationModel.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.OK_200, message = "Successful Retrieval of a Reservation", response = ReservationModel.class)})
  public Response getAvailability(
      @ApiParam(value = "the data from which to see availability", format = "yyyy-MM-dd") @QueryParam(value = "from") String from,
      @ApiParam(value = "the data until which to see availability", format = "yyyy-MM-dd") @QueryParam("to") String to)
      throws CampSiteServiceException {
    LocalDate fromDate;
    LocalDate toDate;
    try {
      fromDate = from == null ? LocalDate.now().plusDays(1) : LocalDate.parse(from);
      toDate = to == null ? LocalDate.now().plusMonths(1) : LocalDate.parse(to);
    } catch (DateTimeParseException e) {
      throw new InvalidDateException();
    }
    return Response.status(HttpStatus.OK_200).entity(campSiteService.getReservedDates(fromDate, toDate))
        .build();
  }

  @POST
  @Path("/reservations")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new reservation.", response = ReservationModel.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.OK_200, message = "Successful creation of a reservation", response = ReservationModel.class)})
  public Response printMessage(@ApiParam ReservationRequestModel reservationRequest)
      throws CampSiteServiceException {
    return Response.status(HttpStatus.OK_200).entity(campSiteService.createReservation(ReservationModel.fromReservationRequestModel(reservationRequest)))
        .build();
  }

  @GET
  @Path("/reservations/{reservationId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Returns the reservation corresponding to the ID.", response = ReservationModel.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.OK_200, message = "Successful retrieval reservation with the given id", response = ReservationModel.class)})
  public Response getReservation(@PathParam("reservationId") String reservationId)
      throws CampSiteServiceException {
    return Response.status(HttpStatus.OK_200).entity(campSiteService.getReservation(reservationId))
        .build();
  }

  @PUT
  @Path("/reservations/{reservationId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Updates the reservation corresponding to the ID.", response = ReservationModel.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.OK_200, message = "Successful update of a reservation.", response = ReservationModel.class)})
  public Response updateReservation(@ApiParam ReservationRequestModel reservationRequest,
      @PathParam("reservationId") String reservationId)
      throws CampSiteServiceException {

    return Response.status(HttpStatus.OK_200)
        .entity(campSiteService.updateReservation(reservationId, ReservationModel.fromReservationRequestModel(reservationRequest)))
        .build();
  }

  @DELETE
  @Path("/reservations/{reservationId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Deletes the reservation corresponding to the ID.")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.NO_CONTENT_204, message = "Successful deletion of a reservation")})
  public Response deleteReservation(@PathParam("reservationId") String reservationId)
      throws CampSiteServiceException {
    campSiteService.deleteReservation(reservationId);
    return Response.status(HttpStatus.NO_CONTENT_204).build();
  }
}