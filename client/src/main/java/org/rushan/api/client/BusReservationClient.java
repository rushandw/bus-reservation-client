package org.rushan.api.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BusReservationClient
{

  private static final String BASE_URL = "http://localhost:8080/api";

  /**
   * Sends a POST request to the check availability endpoint.
   *
   * @param origin         Starting point of the journey.
   * @param destination    Ending point of the journey.
   * @param passengerCount Number of passengers.
   * @return The server's JSON response as a String.
   * @throws IOException If an I/O error occurs.
   */
  public String checkAvailability(String origin, String destination, int passengerCount) throws IOException
  {
    String endpointUrl = BASE_URL + "/check-availability";
    String jsonRequest = String.format("{\"origin\":\"%s\", \"destination\":\"%s\", \"passengerCount\":%d}",
                                       origin,
                                       destination,
                                       passengerCount);

    return sendPostRequest(endpointUrl, jsonRequest);
  }

  /**
   * Sends a POST request to the reserve ticket endpoint.
   *
   * @param origin         Starting point of the journey.
   * @param destination    Ending point of the journey.
   * @param passengerCount Number of passengers.
   * @param amount         Payment amount.
   * @return The server's JSON response as a String.
   * @throws IOException If an I/O error occurs.
   */
  public String reserveTicket(String origin, String destination, int passengerCount, int amount) throws IOException
  {
    String endpointUrl = BASE_URL + "/reserve-ticket";
    String jsonRequest = String.format(
      "{\"origin\":\"%s\", \"destination\":\"%s\", \"passengerCount\":%d, \"amount\":%d}",
      origin,
      destination,
      passengerCount,
      amount);

    return sendPostRequest(endpointUrl, jsonRequest);
  }

  /**
   * Sends a POST request to the specified endpoint with a JSON payload.
   *
   * @param targetUrl       The endpoint URL.
   * @param jsonInputString The JSON payload.
   * @return The server's JSON response as a String.
   * @throws IOException If an I/O error occurs.
   */
  public String sendPostRequest(String targetUrl, String jsonInputString) throws IOException
  {
    URL url = new URL(targetUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json; utf-8");
    connection.setDoOutput(true);

    try (OutputStream os = connection.getOutputStream())
    {
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);
    }

    StringBuilder response = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")))
    {
      String responseLine;
      while ((responseLine = br.readLine()) != null)
      {
        response.append(responseLine.trim());
      }
    }

    return response.toString();
  }

  public static void main(String[] args)
  {
    BusReservationClient client = new BusReservationClient();

    try
    {
      String availabilityResponse = client.checkAvailability("A", "C", 2);
      System.out.println("Availability Response: " + availabilityResponse);

      String reservationResponse = client.reserveTicket("A", "C", 2, 200);
      System.out.println("Reservation Response: " + reservationResponse);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
