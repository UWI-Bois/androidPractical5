package sta.uwi.edu.comp3606_practical5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText phoneno_et, riddle_et, solution_et;
    Button send_b;
    public static ArrayList<Riddle_plus_Target> SentRiddles;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_b = findViewById(R.id.send);
        phoneno_et = findViewById(R.id.phoneno_et);
        riddle_et = findViewById(R.id.riddle_et);
        solution_et = findViewById(R.id.solution_et);

        SentRiddles = new ArrayList<>();
        SmsReceiver smsr = new SmsReceiver();

        send_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phoneNo = phoneno_et.getText().toString();
                String riddle = riddle_et.getText().toString();
                String solution = solution_et.getText().toString();
                try {
                    boolean alreadyPlaying = false;
                    for (int i = 0; i< SentRiddles.size(); i++)
                    {
                        if (SentRiddles.get(i).getPhoneNo().equals(phoneNo)) {
                            alreadyPlaying = true;
                            break;
                        }
                    }
                    if (!alreadyPlaying)
                    {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null,
                                "Hello fellow champion! Think you can crack today's riddle?\n" + riddle, null, null);
                        SentRiddles.add(new Riddle_plus_Target(phoneNo,riddle,solution));
                        Toast.makeText(getApplicationContext(), "SMS Riddle Successfully Sent!",
                                Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getApplicationContext(), "Error " + phoneNo + " is already working on a riddle",
                            Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS Send Failed, error occurred!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

}
