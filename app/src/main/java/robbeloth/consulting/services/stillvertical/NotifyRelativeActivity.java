package robbeloth.consulting.services.stillvertical;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PersistableBundle;
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

    private static final String TAG = "NRActivity";
    private static final String CHECKIN_KEY = "CHECKIN";
    private static final int MODIFY_CONTACT_SETUP = 100;

    private Button mIAmOkayButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Button setupButton;
    private Chronometer mLastTimeRelativeWasOkay;
    private TextView mPrevCheckins;
    private ArrayList<Notification> mNotificationArrayList = new ArrayList<>();
    private int mCurIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_relative);

        if (savedInstanceState != null) {
            for (int i = 0; i < savedInstanceState.size(); i++) {
                if (savedInstanceState.get(CHECKIN_KEY+i) != null) {
                    Object o = savedInstanceState.get(CHECKIN_KEY+i);
                    if (o instanceof Long) {
                        Notification n = new Notification(new Date((long)o));
                        mNotificationArrayList.add(n);
                    }
                }
            }
        }

        // wire up the widgets for use, don't let prev next buttons get used with no checkins
        mIAmOkayButton = (Button) findViewById(R.id.aokay_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevCheckins = (TextView) findViewById(R.id.notication_time);
        mLastTimeRelativeWasOkay = (Chronometer) findViewById(R.id.time_since_last_notification);
        if (mNotificationArrayList.size() > 0) {
            // Show most recent checkin
            Notification n = mNotificationArrayList.get(mNotificationArrayList.size()-1);
            Date d = n.getEntry();
            mPrevCheckins.setText(d.toString());

            /* Show time since most recent checkin
            *  currentTimeMillis is ms since Jan 1 1970
            *  elapsedRealtime is ms since boot
            *
            *  1. Subtract two points in time from Jan 1 1970 to get
            *  ms between those two points in time
            *  2. Subtract the difference from the current number of
            *  ms since boot to get a basis that is greater than zero
            *  or the last key press
            *
            *  yep, it's messy and not intuitive for a chronometer */
            long curTime = System.currentTimeMillis();
            long msSinceBoot = SystemClock.elapsedRealtime();
            long mostRecentTime = d.getTime();
            long basis = msSinceBoot - (curTime - mostRecentTime);
            mLastTimeRelativeWasOkay.stop();
            mLastTimeRelativeWasOkay.setBase(basis);
            mLastTimeRelativeWasOkay.start();
        }
        else {
            mPrevButton.setEnabled(false);
            mNextButton.setEnabled(false);
        }


        // Provide callback ref as anonymous inner class
        mIAmOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Relative clicked I am okay");

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

        setupButton = (Button) findViewById(R.id.setup_button);
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources rs = getResources();
                /* Restore contact info from previous app session if available; if not,
                *  use some default data */
                SharedPreferences sp =  getSharedPreferences(
                        NotificationSetupActivity.CONTACT_INFO_IN_SP, 0);
                String name = sp.getString(NotificationSetupActivity.NAME_KEY,
                              rs.getString(R.string.default_name));
                String phone = sp.getString(NotificationSetupActivity.PHONE_KEY,
                               rs.getString(R.string.default_phone));
                String email = sp.getString(NotificationSetupActivity.EMAIL_KEY,
                               rs.getString(R.string.default_email));
                Intent intent = NotificationSetupActivity.newIntent(NotifyRelativeActivity.this,
                        name, phone, email);
                startActivityForResult(intent, MODIFY_CONTACT_SETUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MODIFY_CONTACT_SETUP) {
            if (resultCode == Activity.RESULT_OK) {
                Toast t  = Toast.makeText(
                        NotifyRelativeActivity.this, R.string.contact_updated,  Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState() -- Saving data");

        // Convert the dates to longs (millisecond rep)
        int cnt = 0;
        for (Notification checkin : mNotificationArrayList) {
            Date d = checkin.getEntry();
            outState.putLong(CHECKIN_KEY+cnt,d.getTime());
            cnt++;
        }

    }
}
