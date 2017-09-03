package robbeloth.consulting.services.stillvertical;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NotificationSetupActivity extends AppCompatActivity {
    private ContactSetup mContactSetup;
    private Button mUpdateButton;
    private TextView mNameTextView;
    private TextView mPhoneTextView;
    private TextView mEmailTextView;

    /* Keys for intent */
    public static final String NAME_KEY = "EXTRA_NAME_KEY";
    public static final String PHONE_KEY = "EXTRA_PHONE_KEY";
    public static final String EMAIL_KEY = "EXTRA_EMAIL_KEY";
    public static final String CONTACT_INFO_IN_SP = "CONTACT_INFO_IN_SP";

    public static Intent newIntent(Context packageContext, String name, String phone, String email){
        Intent intent = new Intent(packageContext, NotificationSetupActivity.class);
        intent.putExtra(NAME_KEY, (name != null) ? name : "Captain Kirk");
        intent.putExtra(PHONE_KEY, (phone != null) ? phone : "(740) 555-1212");
        intent.putExtra(EMAIL_KEY, (email != null) ? email : "jtkirk_enterprise@ufp.org");
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setup);

        mNameTextView = (TextView) findViewById(R.id.name_field);
        mPhoneTextView = (TextView) findViewById(R.id.phone_field);
        mEmailTextView = (TextView) findViewById(R.id.email_field);
        mContactSetup = new ContactSetup();

        /* Restore data from fields in there is a bundle; otherwise, it is the
        *  first time the activity is starting, so use the passed data */
        if (savedInstanceState != null) {
            mContactSetup.setName(savedInstanceState.getString(NAME_KEY));
            mContactSetup.setPhone(savedInstanceState.getString(PHONE_KEY));
            mContactSetup.setEmail(savedInstanceState.getString(EMAIL_KEY));
        }
        else {
            mContactSetup.setName(getIntent().getStringExtra(NAME_KEY));
            mContactSetup.setPhone(getIntent().getStringExtra(PHONE_KEY));
            mContactSetup.setEmail(getIntent().getStringExtra(EMAIL_KEY));
        }

        /* set the textfields w/ data*/
        mNameTextView.setText(mContactSetup.getName());
        mPhoneTextView.setText(mContactSetup.getPhone());
        mEmailTextView.setText(mContactSetup.getEmail());

        mUpdateButton = (Button) findViewById(R.id.update_button);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactSetup = new ContactSetup();
                /* Save changes to model object */
                mContactSetup.setName(mNameTextView.getText().toString());
                mContactSetup.setPhone(mPhoneTextView.getText().toString());
                mContactSetup.setEmail(mEmailTextView.getText().toString());

                /* Save model data to shared preferences file to keep between
                * uses of the app */
                SharedPreferences sp = getSharedPreferences(CONTACT_INFO_IN_SP, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(NAME_KEY, mContactSetup.getName());
                editor.putString(PHONE_KEY, mContactSetup.getEmail());
                editor.putString(EMAIL_KEY, mContactSetup.getPhone());
                editor.commit();
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        /* Save possible intermediate updates to text fields */
        outState.putString(NAME_KEY, mNameTextView.getText().toString());
        outState.putString(PHONE_KEY, mPhoneTextView.getText().toString());
        outState.putString(EMAIL_KEY, mEmailTextView.getText().toString());
    }
}
