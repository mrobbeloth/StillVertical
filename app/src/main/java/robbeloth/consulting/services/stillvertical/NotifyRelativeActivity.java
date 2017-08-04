package robbeloth.consulting.services.stillvertical;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class NotifyRelativeActivity extends AppCompatActivity {

    private Button mIAmOkayButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Chronometer mLastTimeRelativeWasOkay;
    private TextView mPrevCheckins;
    private ArrayList<Notification> mNotificationArrayList = new ArrayList<>();
    private int mCurIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_relative);

        // wire up the widgets for use, don't let prev next buttons get used with no checkins
        mIAmOkayButton = (Button) findViewById(R.id.aokay_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setEnabled(false);
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setEnabled(false);
        mPrevCheckins = (TextView) findViewById(R.id.notication_time);

        mLastTimeRelativeWasOkay = (Chronometer) findViewById(R.id.time_since_last_notification);

        // Provide callback ref as anonymous inner class
        mIAmOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("NRActivity", "Relative clicked I am okay");

                // Store time of checkin by elderly relative
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                mNotificationArrayList.add(new Notification(d));

                // reset Chronometer showing elapsed time since last checkin
                mLastTimeRelativeWasOkay.stop();
                mLastTimeRelativeWasOkay.setBase(SystemClock.elapsedRealtime());
                mLastTimeRelativeWasOkay.start();

                // set previous notifications to when elderly relative clicked I'm okay
                mCurIndex = mNotificationArrayList.size()-1;
                Notification n = mNotificationArrayList.get(mCurIndex);
                Date recordedDate = n.getEntry();
                mPrevCheckins.setText(recordedDate.toString());

                // let relative know they informed loved one
                Toast t = Toast.makeText(NotifyRelativeActivity.this,
                                         R.string.action_completed,
                                         Toast.LENGTH_SHORT);
                t.show();

                // allow elderly relative to cycle throw all check-ins
                mPrevButton.setEnabled(true);
                mNextButton.setEnabled(true);
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reaching beginning of list by going to end
                if (mCurIndex == 0) {
                    mCurIndex = mNotificationArrayList.size()-1;
                }
                else {
                    mCurIndex--;
                }

                // show the last checkin
                Notification n = mNotificationArrayList.get(mCurIndex);
                Date d = n.getEntry();
                mPrevCheckins.setText(d.toString());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reaching end of list by going to beginning
                if (mCurIndex == mNotificationArrayList.size()-1) {
                    mCurIndex = 0;
                }
                else {
                    mCurIndex++;
                }

                // show the next checkin
                Notification n = mNotificationArrayList.get(mCurIndex);
                Date d = n.getEntry();
                mPrevCheckins.setText(d.toString());
            }
        });
    }
}
