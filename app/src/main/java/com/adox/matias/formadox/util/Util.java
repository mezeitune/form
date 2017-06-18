package com.adox.matias.formadox.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.adox.matias.formadox.LoginActivity;
import com.adox.matias.formadox.model.Cliente;
import com.adox.matias.formadox.model.Repuesto;

import java.util.ArrayList;

public class Util {

    public static final String SHAPREF = "perfil";
    public static final String USER_SP = "us_sha";
    public static final String PASS_SP = "pass_sha";
    public static final String ID_SP = "id";
    public static final String HASH_SP = "hash";
    public static final String PRIMERAVEZ_SP = "primera_vez";


    public static Cliente clienteActual;
    public static ArrayList<Repuesto> repuestos= new ArrayList<>();
    public static boolean completoRespirador=false, completoAbsorbedor=false;
;
    //public static final int LOGIN_NOTIFY = 13584246;


    public static final String ACC_LOGIN = "LOGIN";
    public static final String ACC_DIN= "ACTUALIZARDINAMICO";
    public static final String ACC_STATIC= "ACTUALIZARESTATICO";
    public static final String ACC_ADDSERVICE= "AGREGARSERVICE";

    public static final  long SEGS_POS=20000;

    public static final int SYNC_NOTIFY=1358429;


    public static void showToast(final String msj, final Activity act) {

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act.getApplicationContext(), msj,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public static boolean isNetworkAvailable(Context context) {//funcion que uso para saber si hay internet
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void logout(Activity act) {
        SharedPreferences preferencias = act.getSharedPreferences(SHAPREF ,
                Context.MODE_PRIVATE);

        Editor editor = preferencias.edit();

        editor.putString(PASS_SP, "");
        editor.putString(USER_SP, "");
        editor.putString(ID_SP, "");
        editor.putString(HASH_SP, "");
        editor.commit();

        Intent loginIntent = new Intent(act, LoginActivity.class);
        loginIntent.putExtra("class", act.getClass());
        act.finish();
        act.startActivity(loginIntent);

    }


    public static int notify(Context context, int icon, String title,
                             String text, Class<?> activityClass, int mId) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(icon).setContentTitle(title)
                .setContentText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, activityClass);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(activityClass);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
        }

        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Random 13548246
        mNotificationManager.notify(mId, mBuilder.build());

        return mId;
    }

    public static void cancelNotify(Context context, int mId){
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(mId);
    }
}