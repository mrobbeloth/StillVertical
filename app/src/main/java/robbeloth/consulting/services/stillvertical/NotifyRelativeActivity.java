package robbeloth.consulting.services.stillvertical;

import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class NotifyRelativeActivity extends AppCompatActivity {

    private Button iAmOkayButton;
    private TextView lastTimeRelativeWasOkay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_relative);

        iAmOkayButton = (Button) findViewById(R.id.aokay_button);
        lastTimeRelativeWasOkay = (TextView) findViewById(R.id.last_okay_time);

        // Provide callback ref as anonymous inner class
        iAmOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display time on last notification
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                String prefix = getResources().getString(R.string.last_time_prefix);
                lastTimeRelativeWasOkay.setText(prefix + d.toString());

                // let relative know they informed loved one
                Toast t = Toast.makeText(NotifyRelativeActivity.this,
                                         R.string.action_completed,
                                         Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }
}
