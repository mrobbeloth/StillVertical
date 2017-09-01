package robbeloth.consulting.services.stillvertical;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setup);

        mNameTextView = (TextView) findViewById(R.id.name_field);
        mPhoneTextView = (TextView) findViewById(R.id.phone_field);
        mNameTextView = (TextView) findViewById(R.id.email_field);

        mUpdateButton = (Button) findViewById(R.id.update_button);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactSetup = new ContactSetup();
                mContactSetup.setName(mNameTextView.getText().toString());
                mContactSetup.setPhone(mPhoneTextView.getText().toString());
                mContactSetup.setEmail(mEmailTextView.getText().toString());
            }
        });
    }
}
