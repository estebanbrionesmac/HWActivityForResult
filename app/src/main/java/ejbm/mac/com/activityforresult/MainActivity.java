package ejbm.mac.com.activityforresult;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST = 1 ;

    TextView  textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMessage=(TextView)findViewById(R.id.textViewMessage);
    }

    // Method to handle the Click Event on GetMessage Button
    public void getMessage(View V) {
        // Create The  Intent and Start The Activity to get The message
        Intent intentGetMessage=new Intent(this,SecondActivity.class);
        startActivityForResult( intentGetMessage,  2 /*PICK_CONTACT_REQUEST*/ );// Activity is started with requestCode 2
    }

    // Method to handle the Click Event on GetMessage Button
    public void getContact(View V) {
        // Create The  Intent and Start The Activity to get The message
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2) {
            if(null!=data)
            {
                // fetch the message String
                String message = data.getStringExtra("MESSAGE");
                // Set the message string in textView
                textViewMessage.setText("Message from second Activity: " + message);
            }
        }

        if(requestCode==PICK_CONTACT_REQUEST) {
            if(null!=data) {
                // fetch the message String
                String message = data.getStringExtra("Contact ");

                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                // Set the message string in textView
                textViewMessage.setText("Contact # : " + cursor.getString(column) ) ;
            }
        }
    }
}
