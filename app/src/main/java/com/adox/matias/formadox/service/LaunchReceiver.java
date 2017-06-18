package com.adox.matias.formadox.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.adox.matias.formadox.util.Util;

public class LaunchReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

			try {

				Log.i("LAUNCHER", "ALARMA SETEADA");

				Intent alarmIntent = new Intent(context, SyncService.class);
				PendingIntent pintent = PendingIntent.getService(context, 0,
						alarmIntent, 0);
				AlarmManager alarm = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				 Util.SEGS_POS,
				 Util.SEGS_POS, pintent);

			} catch (Exception e) {
				Toast.makeText(context,
						"Launcher no iniciado. Error: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}
}