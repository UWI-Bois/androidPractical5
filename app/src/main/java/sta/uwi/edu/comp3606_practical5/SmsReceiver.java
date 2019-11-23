package sta.uwi.edu.comp3606_practical5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import static sta.uwi.edu.comp3606_practical5.MainActivity.SentRiddles;

public class SmsReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        System.out.println("Received SMS");
        Toast.makeText(context, "SMS Message Received", Toast.LENGTH_SHORT).show();
        SmsMessage[] recMsg = null;
        String str = "";
        if (bundle != null)
        {
            //---Access the received SMS message ---
            Object[] pdus = (Object[]) bundle.get("pdus");
            recMsg = new SmsMessage[pdus.length];
            for (int i=0; i<recMsg.length; i++){
                String format = bundle.getString("format");
                recMsg[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                str += "SMS Message received from: " + recMsg[i].getOriginatingAddress();
                str += "=>";
                str += recMsg[i].getMessageBody().toString();
                str += "\n";
            }
            String Ans = recMsg[recMsg.length-1].getMessageBody().toString();
            String phoneNo = recMsg[recMsg.length-1].getOriginatingAddress();
            HandleRiddles(phoneNo,Ans);
            //---display the SMS message received---
            Toast.makeText(context, recMsg[recMsg.length-1].getOriginatingAddress(), Toast.LENGTH_LONG).show();
        }
    }

    private void HandleRiddles(String phoneNo, String Ans)
    {
        for (int i = 0; i< SentRiddles.size(); i++)
        {
            if (SentRiddles.get(i).getPhoneNo().equals(phoneNo))
            {
                String msg;
                if(SentRiddles.get(i).checkAnswer(Ans) == 1)
                {
                    msg = "Riddle: " + SentRiddles.get(i).getRiddle() + "\nCorrect answer! You won!!!";
                    SentRiddles.remove(i);
                }
                else if(SentRiddles.get(i).checkAnswer(Ans) == 0) msg = "Riddle: " + SentRiddles.get(i).getRiddle() + "\nSorry, close but not quite.";
                else msg = "Riddle: " + SentRiddles.get(i).getRiddle() + "\nWrong answer try again!";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                break;
            }
        }
    }
}
