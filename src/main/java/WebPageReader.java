package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPageReader {
    private String urlString;

    public WebPageReader(String urlString) {
        // initialize the urlString instance variable with the input urlString
        this.urlString = urlString;
    }

    // method to get the HTML content of the web page at the urlString
    public String getHtmlContent() {
        try {
            // create a new URL object with the urlString
            URL url = new URL(urlString);
            // open a connection to the URL and cast the resulting of http
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // read the HTML content from the connection input
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder html = new StringBuilder();
            // loop through each line of the HTML content and append it
            while ((line = reader.readLine()) != null) {
                html.append(line).append("\n");
            }
            reader.close();

            // return the HTML content as a String
            return html.toString();
        } catch (IOException e) {
            // print an error message if there is a problem with the URL connection and return null
            System.err.println("URL connection error");
            return null;
        }
    }
}
