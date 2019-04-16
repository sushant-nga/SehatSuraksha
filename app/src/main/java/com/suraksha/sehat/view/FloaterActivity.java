package com.suraksha.sehat.view;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.LinearLayout;

        import com.suraksha.sehat.R;

public class FloaterActivity extends AppCompatActivity {

    //LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floater);

        //rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        //rootLayout.getBackground().setAlpha(150);
    }
}
