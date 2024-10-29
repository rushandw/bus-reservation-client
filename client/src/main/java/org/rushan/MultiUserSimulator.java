package org.rushan;

import java.io.IOException;

import org.rushan.api.client.BusReservationClient;

public class MultiUserSimulator
{
  public static void main(String[] args)
  {
    BusReservationClient client = new BusReservationClient();

    for (int i = 0; i < 10; i++)
    { // 10 concurrent users
      new Thread(() -> {
        try
        {
          String availabilityResponse = client.sendPostRequest("http://localhost:8080/api/check-availability",
                                                               "{\"origin\":\"A\", \"destination\":\"C\", \"passengerCount\":2}");
          System.out.println("Availability Response: " + availabilityResponse);

          String reservationResponse = client.sendPostRequest("http://localhost:8080/api/reserve-ticket",
                                                              "{\"origin\":\"A\", \"destination\":\"C\", \"passengerCount\":2, \"amount\":200}");
          System.out.println("Reservation Response: " + reservationResponse);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }).start();
    }
  }
}
