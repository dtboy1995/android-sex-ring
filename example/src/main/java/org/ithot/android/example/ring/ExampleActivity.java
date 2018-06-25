package org.ithot.android.example.ring;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.ithot.android.view.RingView;

public class ExampleActivity extends Activity {

    TextView tv1;
    TextView tv2;
    RingView ring1;
    RingView ring2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        ring1 = findViewById(R.id.ring_view);
        ring2 = findViewById(R.id.ring_view2);
    }

    public void go2(View v) {
        ring1.go(30, true);
        ring2.go(30, true);
    }

    public void step1(int progress) {
        tv1.setText(String.valueOf(progress));
    }

    public void step2(int progress) {
        tv2.setText(String.valueOf(progress));
    }

    public void go1(View v) {
        ring1.go(80, true);
        ring2.go(80, true);
    }
}
