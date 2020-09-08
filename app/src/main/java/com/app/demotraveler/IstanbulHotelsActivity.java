package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class IstanbulHotelsActivity extends AppCompatActivity {
    private Toolbar mToolbat;
    private Button Book1,Book2,Book3,Book4,Book5,Book6,Book7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul_hotels);

        mToolbat= (Toolbar) findViewById(R.id.ist_hotels_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Istanbul Hotels");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Book1= (Button) findViewById(R.id.book_four_seasons);
        Book2= (Button) findViewById(R.id.book_cirgan_palace);
        Book3= (Button) findViewById(R.id.book_swissotel);
        Book4= (Button) findViewById(R.id.book_azada);
        Book5= (Button) findViewById(R.id.book_ibrahem);
        Book6= (Button) findViewById(R.id.book_marmara);
        Book7= (Button) findViewById(R.id.book_ottoman);




        Book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/four-seasons-istanbul-at-the-bosphorus.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&hpos=1&no_rooms=1&soh=1&sr_order=popularity&srepoch=1585852188&srpvid=3892820d36db009f&ucfs=1&from=searchresults;highlight_room=#no_availability_msg"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/ciragan-palace-kempinski-istanbul.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&all_sr_blocks=4939601_246025845_0_2_0&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&highlighted_blocks=4939601_246025845_0_2_0&hpos=1&no_rooms=1&sr_order=popularity&sr_pri_blocks=4939601_246025845_0_2_0__24840&srepoch=1585852303&srpvid=6b1c824721cc008a&ucfs=1&from=searchresults;highlight_room=#hotelTmpl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Book3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/swissotelistanbul.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&all_sr_blocks=8605120_112243768_2_1_0&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&highlighted_blocks=8605120_112243768_2_1_0&hpos=1&no_rooms=1&sr_order=popularity&sr_pri_blocks=8605120_112243768_2_1_0__19374&srepoch=1585852334&srpvid=ac9a8256de400145&ucfs=1&from=searchresults;highlight_room=#hotelTmpl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        Book4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/azade-hotel.en-gb.html?aid=346426;label=istanbul-best;sid=b1d8985952ace3acd4e9eeb2f6f3be80"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        Book5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/ibrahim-pasha.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&all_sr_blocks=17313402_195518593_0_1_0&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&highlighted_blocks=17313402_195518593_0_1_0&hpos=1&no_rooms=1&sr_order=popularity&sr_pri_blocks=17313402_195518593_0_1_0__11000&srepoch=1585852420&srpvid=97f38282f79200b2&ucfs=1&from=searchresults;highlight_room=#hotelTmpl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        Book6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/marmara-taksim.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&all_sr_blocks=8669418_203701042_0_42_0&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&highlighted_blocks=8669418_203701042_0_42_0&hpos=1&no_rooms=1&sr_order=popularity&sr_pri_blocks=8669418_203701042_0_42_0__11610&srepoch=1585852446&srpvid=ef07828e9fbb0039&ucfs=1&from=searchresults;highlight_room=#hotelTmpl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        Book7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.booking.com/hotel/tr/ottoman-imperial.en-gb.html?aid=346426&label=istanbul-best&sid=b1d8985952ace3acd4e9eeb2f6f3be80&all_sr_blocks=8647921_232434514_0_1_0&checkin=2020-04-12&checkout=2020-04-13&dest_id=-755070&dest_type=city&group_adults=2&group_children=0&hapos=1&highlighted_blocks=8647921_232434514_0_1_0&hpos=1&no_rooms=1&sr_order=popularity&sr_pri_blocks=8647921_232434514_0_1_0__70000&srepoch=1585852470&srpvid=3909829abe6b0006&ucfs=1&from=searchresults;highlight_room=#hotelTmpl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });




    }
}
