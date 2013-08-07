package com.example.asteroides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;

@SuppressWarnings("deprecation")
public class ReceptorSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		/*
		 * Se configura un PendingIntent con la Actividad que queremos lanzar
		 * cuando se reciba un SMS (en este caso "AcercaDe").
		 */
		PendingIntent intencionAcercaDe = PendingIntent.getActivity(context, 0,
				new Intent(context, AcercaDe.class), 0);

		/*
		 * Se realiza la operación especificada en el PendingIntent, en este
		 * caso lanzar la actividad "AcercaDe". El método "send()" arroja una
		 * excepción que ha de ser controlada.
		 */
		try {
			intencionAcercaDe.send();
		} catch (CanceledException e) {
			/*
			 * Auto-generated catch block
			 */
			e.printStackTrace();
		}
	}

	public void lanzarAcercaDe(View view) {

		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}

	private void startActivity(Intent i) {
		// TODO Auto-generated method stub

	}

}