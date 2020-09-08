package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class IstanbulWeatherActivity extends AppCompatActivity {

    private String saveCurrentDate;
    private Toolbar mToolbat;

    public static final String TAG = IstanbulWeatherActivity.class.getSimpleName();
    private TextView temp1,temp2,temp3,temp4,desc1,desc2,desc3,desc4
            ,cloud1,cloud2,cloud3,cloud4,dayM1,dayM2,dayM3,dayM4,dayW1,dayW2,dayW3,dayW4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul_weather);

        mToolbat= (Toolbar) findViewById(R.id.ist_weth_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Weather");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        temp1=(TextView) findViewById(R.id.weather_temp1);
        temp2=(TextView) findViewById(R.id.weather_temp2);
        temp3=(TextView) findViewById(R.id.weather_temp3);
        temp4=(TextView) findViewById(R.id.weather_temp4);

        desc1=(TextView) findViewById(R.id.weather_desc1);
        desc2=(TextView) findViewById(R.id.weather_desc2);
        desc3=(TextView) findViewById(R.id.weather_desc3);
        desc4=(TextView) findViewById(R.id.weather_desc4);

        cloud1=(TextView) findViewById(R.id.weather_cloud1);
        cloud2=(TextView) findViewById(R.id.weather_cloud2);
        cloud3=(TextView) findViewById(R.id.weather_cloud3);
        cloud4=(TextView) findViewById(R.id.weather_cloud4);

        dayM1=(TextView) findViewById(R.id.weather_daymonth1);
        dayM2=(TextView) findViewById(R.id.weather_daymonth2);
        dayM3=(TextView) findViewById(R.id.weather_daymonth3);
        dayM4=(TextView) findViewById(R.id.weather_daymonth4);

        dayW1=(TextView) findViewById(R.id.weather_dayweek1);
        dayW2=(TextView) findViewById(R.id.weather_dayweek2);
        dayW3=(TextView) findViewById(R.id.weather_dayweek3);
        dayW4=(TextView) findViewById(R.id.weather_dayweek4);





        /*
        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

*/

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int day = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (String.valueOf(day).length()==1){
            dayM1.setText("0"+String.valueOf(day));

        }else{
            dayM1.setText(String.valueOf(day));
        }

        switch(dayOfWeek) {
            case 1:
                dayW1.setText("SUN");
                break;
            case 2:
                dayW1.setText("MON");
                break;
            case 3:
                dayW1.setText("TUE");
                break;
            case 4:
                dayW1.setText("WED");
                break;
            case 5:
                dayW1.setText("THU");
                break;
            case 6:
                dayW1.setText("FRI");
                break;
            case 7:
                dayW1.setText("SAT");
                break;

        }


        calendar.add(Calendar.DATE,1);
        int day2 = calendar.get(Calendar.DATE);
        int dayOfWeek2 = calendar.get(Calendar.DAY_OF_WEEK);
        if (String.valueOf(day2).length()==1){
            dayM2.setText("0"+String.valueOf(day2));

        }else{
            dayM2.setText(String.valueOf(day2));
        }

        switch(dayOfWeek2) {
            case 1:
                dayW2.setText("SUN");
                break;
            case 2:
                dayW2.setText("MON");
                break;
            case 3:
                dayW2.setText("TUE");
                break;
            case 4:
                dayW2.setText("WED");
                break;
            case 5:
                dayW2.setText("THU");
                break;
            case 6:
                dayW2.setText("FRI");
                break;
            case 7:
                dayW2.setText("SAT");
                break;

        }



        calendar.add(Calendar.DATE,1);
        int day3 = calendar.get(Calendar.DATE);
        int dayOfWeek3 = calendar.get(Calendar.DAY_OF_WEEK);
        if (String.valueOf(day3).length()==1){
            dayM3.setText("0"+String.valueOf(day3));

        }else{
            dayM3.setText(String.valueOf(day3));
        }

        switch(dayOfWeek3) {
            case 1:
                dayW3.setText("SUN");
                break;
            case 2:
                dayW3.setText("MON");
                break;
            case 3:
                dayW3.setText("TUE");
                break;
            case 4:
                dayW3.setText("WED");
                break;
            case 5:
                dayW3.setText("THU");
                break;
            case 6:
                dayW3.setText("FRI");
                break;
            case 7:
                dayW3.setText("SAT");
                break;

        }

        calendar.add(Calendar.DATE,1);
        int day4 = calendar.get(Calendar.DATE);
        int dayOfWeek4 = calendar.get(Calendar.DAY_OF_WEEK);
        if (String.valueOf(day4).length()==1){
            dayM4.setText("0"+String.valueOf(day4));

        }else{
            dayM4.setText(String.valueOf(day4));
        }

        switch(dayOfWeek4) {
            case 1:
                dayW4.setText("SUN");
                break;
            case 2:
                dayW4.setText("MON");
                break;
            case 3:
                dayW4.setText("TUE");
                break;
            case 4:
                dayW4.setText("WED");
                break;
            case 5:
                dayW4.setText("THU");
                break;
            case 6:
                dayW4.setText("FRI");
                break;
            case 7:
                dayW4.setText("SAT");
                break;

        }



        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);




        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();



        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC);
        helper.setLang(Lang.ENGLISH);






        helper.getThreeHourForecastByCityName("Istanbul", new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                Log.v(TAG, "City/Country: "+ threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() +"\n"
                        +"Forecast Array Count: " + threeHourForecast.getCnt() +"\n"
                        //For this example, we are logging details of only the first forecast object in the forecasts array
                        +"First Forecast Date Timestamp: " + threeHourForecast.getList().get(0).getDt() +"\n"
                        +"First Forecast Weather Description: " + threeHourForecast.getList().get(0).getWeatherArray().get(0).getDescription()+ "\n"
                        +"First Forecast Max Temperature: " + threeHourForecast.getList().get(0).getMain().getTempMax()+"\n"
                        +"First Forecast Wind Speed: " + threeHourForecast.getList().get(0).getWind().getSpeed() + "\n"
                );

                /*
                weath0.setText("Description: " + threeHourForecast.getList().get(0).getWeatherArray().get(0).getDescription()+ "\n"
                        +"Max Temperature: " + threeHourForecast.getList().get(0).getMain().getTempMax()+"\n"
                        +"Wind Speed: " + threeHourForecast.getList().get(0).getWind().getSpeed() + "\n");


                 */


                 //0/8/16/24/32


                temp1.setText(threeHourForecast.getList().get(0).getMain().getTempMax()+"°");
                temp2.setText(threeHourForecast.getList().get(8).getMain().getTempMax()+"°");
                temp3.setText(threeHourForecast.getList().get(16).getMain().getTempMax()+"°");
                temp4.setText(threeHourForecast.getList().get(24).getMain().getTempMax()+"°");


                desc1 .setText(""+threeHourForecast.getList().get(0).getWeatherArray().get(0).getDescription());
                desc2.setText(""+threeHourForecast.getList().get(8).getWeatherArray().get(0).getDescription());
                desc3.setText(""+threeHourForecast.getList().get(16).getWeatherArray().get(0).getDescription());
                desc4.setText(""+threeHourForecast.getList().get(24).getWeatherArray().get(0).getDescription());



                cloud1.setText("wind: " +threeHourForecast.getList().get(0).getWind().getSpeed());
                cloud2.setText("wind: " +threeHourForecast.getList().get(8).getWind().getSpeed());
                cloud3.setText("wind: " +threeHourForecast.getList().get(16).getWind().getSpeed());
                cloud4.setText("wind: " +threeHourForecast.getList().get(24).getWind().getSpeed());

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v(TAG, throwable.getMessage());
            }
        });

    }
}
