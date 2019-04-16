package com.suraksha.sehat.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.suraksha.sehat.R;

public class MainActivity extends AppCompatActivity {

    CardView floaterButton, topupsButton, specialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floaterButton = (CardView) findViewById(R.id.card_view_floater);
        topupsButton = (CardView) findViewById(R.id.card_view_topups);
        specialButton = (CardView) findViewById(R.id.card_view_special);

        floaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch new intent for loading floater category screen.
                startActivity(new Intent(MainActivity.this, FloaterActivity.class));
            }
        });

        topupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch new intent for loading topups category screen.
                startActivity(new Intent(MainActivity.this, TopupsActivity.class));
            }
        });

        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch new intent for loading special params screen.
                startActivity(new Intent(MainActivity.this, SpecialActivity.class));
            }
        });
    }
}
