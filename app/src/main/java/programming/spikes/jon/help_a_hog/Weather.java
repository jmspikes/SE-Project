package programming.spikes.jon.help_a_hog;

import programming.spikes.jon.help_a_hog.WeatherLocation;


public class Weather
{
    public WeatherLocation location;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Clouds clouds = new Clouds();

    public byte[] iconData;


    public class CurrentCondition
    {
        private int weatherID;
        private String Condition;
        private String description;
        private String icon;
        private float humidity;

        public int getWeatherId() {
            return weatherID;
        }

        public void setWeatherId(int weatherID) {
            this.weatherID = weatherID;
        }

        public String getCondition() {
            return Condition;
        }

        public void setCondition(String condition) {
            Condition = condition;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getHumidity() {
            return humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }
    }


    public class Temperature
    {
        private float temp;
        private float minTemp;
        private float maxTemp;


        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }
    }


    public class Wind
    {
        private float speed;
        private float deg;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDeg() {
            return deg;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }
    }


    public class Clouds
    {
        private int percent;

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }



}
