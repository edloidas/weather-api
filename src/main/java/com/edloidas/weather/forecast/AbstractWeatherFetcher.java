package com.edloidas.weather.forecast;

import com.edloidas.weather.WeatherFetcher;
import com.edloidas.weather.forecast.elements.Coordinates;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mtaukachou.
 */
public abstract class AbstractWeatherFetcher implements WeatherFetcher {

    private static Logger LOGGER = Logger.getLogger(AbstractWeatherFetcher.class);

    private String url = null;

    public AbstractWeatherFetcher(String city) {
        this.url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric";
    }

    public AbstractWeatherFetcher(Coordinates coord) {
        this.url = "http://api.openweathermap.org/data/2.5/weather?lat=" +
                   coord.getLat() + "&lon=" + coord.getLon() + "&units=metric";
    }

    private void setLocation(String city) {
        this.url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric";
    }

    private void setLocation(Coordinates coord) {
        this.url = "http://api.openweathermap.org/data/2.5/weather?lat=" +
                   coord.getLat() + "&lon=" + coord.getLon() + "&units=metric";
    }

    /**
     * Method fetches json data.
     *
     * @return json as {@code String}.
     */
    public String fetchJson() {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL weather = new URL(url);
            URLConnection conn = weather.openConnection();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (MalformedURLException muEx) {
            LOGGER.error(muEx);
        } catch (IOException ioEx) {
            LOGGER.error(ioEx);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

    /**
     * Method fetches json data and creates forecast.
     *
     * @return weather forecast for the current moment.
     */
    public abstract WeatherForecast fetch();
}
