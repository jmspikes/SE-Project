package programming.spikes.jon.help_a_hog;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherHttpClient
{
    //   API Code: 558c6877d9f6c41473faf8f2f92abd40

    // Links to the OpenWeatherMap API
    private String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?zip=72701,us&APPID=558c6877d9f6c41473faf8f2f92abd40";
    private String IMG_URL = "http://openweathermap.org/img/w/";

    // attempt to pull the data from the API for the location
    public String getWeatherData(String location)
    {
        HttpURLConnection con = null;
        InputStream is = null;

        try
        {
            con = (HttpURLConnection) (new URL(BASE_URL + location)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null)
            {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }


    //Attempt to download the weather image
    public byte[] getImage(String code)
    {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }


}
