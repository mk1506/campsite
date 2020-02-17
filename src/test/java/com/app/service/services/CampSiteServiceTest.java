package com.app.service.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.data.repositories.inMemoryRepository.InMemoryReservationsRepository;
import com.app.exceptions.CampsiteNotAvailableOnDateException;
import com.app.exceptions.InvalidReservationException;
import com.app.exceptions.ReservationDoesNotExistException;
import com.app.models.ReservationModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CampSiteServiceTest {

  private CampSiteService campSiteService;

  @Mock
  InMemoryReservationsRepository reservationsRepositoryMock;

  @Before
  public void setup() {
    this.campSiteService = new CampSiteService(reservationsRepositoryMock);
  }

  @Test
  public void canGetReservedDates() {
    LocalDate now = LocalDate.now();
    List<String> dates = Lists.newArrayList(now.toString());
    when(reservationsRepositoryMock.getReservedDates()).thenReturn(Sets.newHashSet(now));
    List<String> result = campSiteService.getReservedDates(now, now);

    assertThat(result).isEqualTo(dates);
  }

  @Test
  public void canGetReservation() throws Exception {
    String id = "1";
    ReservationModel reservation = new ReservationModel(id);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    when(reservationsRepositoryMock.getReservation(id)).thenReturn(reservation);
    ReservationModel result = campSiteService.getReservation(id);

    assertThat(result).isEqualTo(reservation);
  }

  @Test(expected = ReservationDoesNotExistException.class)
  public void cannotGetReservationForInvalidId() throws Exception {
    String id = "1";
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(false);
    campSiteService.getReservation(id);
  }

  @Test
  public void canDeleteReservation() throws Exception {
    String id = "1";
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.deleteReservation(id);
    verify(reservationsRepositoryMock).deleteReservation(id);
  }

  @Test(expected = ReservationDoesNotExistException.class)
  public void cannotDeleteReservationForInvalidId() throws Exception {
    String id = "1";
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(false);
    campSiteService.deleteReservation(id);
  }

  @Test
  public void canCreateReservation() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay, nextDay);
    when(reservationsRepositoryMock.isDateAvailable(nextDay)).thenReturn(true);
    when(reservationsRepositoryMock.createReservation(reservation)).thenReturn(reservation);
    ReservationModel result = campSiteService.createReservation(reservation);
    assertThat(result).isEqualTo(reservation);
  }

  @Test(expected = CampsiteNotAvailableOnDateException.class)
  public void cannotCreateReservationWithUnavailableDates() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay, nextDay);
    when(reservationsRepositoryMock.isDateAvailable(nextDay)).thenReturn(false);
    ReservationModel result = campSiteService.createReservation(reservation);
    assertThat(result).isEqualTo(reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationWithMissingName() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", null, "test@test", nextDay, nextDay);
    campSiteService.createReservation(reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationWithMissingEmail() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", null, nextDay, nextDay);
    campSiteService.createReservation(reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationWithMissingArrivalDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", null, nextDay);
    campSiteService.createReservation(reservation);
  }


  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationWithMissingDepartureDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay, null);
    campSiteService.createReservation(reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationWithMissingInvalidDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay,
        nextDay.minusDays(1));
    campSiteService.createReservation(reservation);

  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationOverMonthInAdvance() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(45);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay, nextDay);
    campSiteService.createReservation(reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotCreateReservationForOver3Days() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay,
        nextDay.plusDays(4));
    campSiteService.createReservation(reservation);
  }

  @Test
  public void canUpdateReservation() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", nextDay, nextDay);
    when(reservationsRepositoryMock.isDateAvailable(nextDay)).thenReturn(false);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    when(reservationsRepositoryMock.getReservation(id)).thenReturn(reservation);
    when(reservationsRepositoryMock.updateReservation(id, reservation)).thenReturn(reservation);
    ReservationModel result = campSiteService.updateReservation(id, reservation);
    assertThat(result).isEqualTo(reservation);
  }

  @Test(expected = CampsiteNotAvailableOnDateException.class)
  public void cannotUpdateReservationWhenDatesReserved() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    LocalDate newReservationEnd = nextDay.plusDays(1);
    String id = "1";
    ReservationModel reservationOld = new ReservationModel(id, "test", "test@test", nextDay,
        nextDay);
    ReservationModel reservationNew = new ReservationModel(id, "test", "test@test", nextDay,
        newReservationEnd);
    when(reservationsRepositoryMock.isDateAvailable(newReservationEnd)).thenReturn(false);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    when(reservationsRepositoryMock.getReservation(id)).thenReturn(reservationOld);
    campSiteService.updateReservation(id, reservationNew);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationWithMissingName() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, null, "test@test", nextDay, nextDay);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationWithMissingEmail() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", null, nextDay, nextDay);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationWithMissingArrivalDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", null, nextDay);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }


  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationWithMissingDepartureDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", nextDay, null);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationWithInvalidDate() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", nextDay,
        nextDay.minusDays(1));
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationOverMonthInAdvance() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(45);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", nextDay, nextDay);
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test(expected = InvalidReservationException.class)
  public void cannotUpdateReservationForOver3Days() throws Exception {
    LocalDate nextDay = LocalDate.now().plusDays(1);
    String id = "1";
    ReservationModel reservation = new ReservationModel(id, "test", "test@test", nextDay,
        nextDay.plusDays(4));
    when(reservationsRepositoryMock.reservationExists(id)).thenReturn(true);
    campSiteService.updateReservation(id, reservation);
  }

  @Test
  public void canHandleConcurrentReservationRequests() throws Exception {
    //same reservation used for all tests
    LocalDate nextDay = LocalDate.now().plusDays(1);
    ReservationModel reservation = new ReservationModel("1", "test", "test@test", nextDay, nextDay);

    int threads = 2;
    //mock result so that 1st thread gets the date available subsequent threads fail
    when(reservationsRepositoryMock.isDateAvailable(nextDay)).thenReturn(true,false);


    //execute the threads at the same time, use 1 if it succeeds, 0 if it fails
    ExecutorService service =
        Executors.newFixedThreadPool(threads);
    Collection<Future<Integer>> futures =
        new ArrayList<>(threads);
    for (int t = 0; t < threads; ++t) {

      futures.add(service.submit(() -> {
        try {
          campSiteService.createReservation(reservation);
          return 1;
        } catch (CampsiteNotAvailableOnDateException e) {
          return 0;
        }
      }));
    }

    //count the amount of successes in the thread pool
    List<Integer> reservations = new ArrayList<>();
    for (Future<Integer> f : futures) {
      Integer result = f.get();
      if (result != 0) {
        reservations.add(result);
      }
    }

    //only 1 request should succeed as the reservatio nwas identical
    assertThat(reservations.size()).isEqualTo(1);
  }
}
