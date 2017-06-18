package com.adox.matias.formadox.service;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;

import com.adox.matias.formadox.FirmarActivity;
import com.adox.matias.formadox.FormularioActivity;
import com.adox.matias.formadox.R;
import com.adox.matias.formadox.RevisionFragment;
import com.adox.matias.formadox.http.HttpCall;
import com.adox.matias.formadox.http.HttpCallCallbackHandler;
import com.adox.matias.formadox.model.Cliente;
import com.adox.matias.formadox.model.ClienteTable;
import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.Institucion;
import com.adox.matias.formadox.model.InstitucionTable;
import com.adox.matias.formadox.model.RepuestosCliente;
import com.adox.matias.formadox.model.RepuestosClienteTable;
import com.adox.matias.formadox.util.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SyncService extends IntentService implements HttpCallCallbackHandler {

    private DatabaseOpenHelper db;
    SharedPreferences prefe;

    public SyncService() {
        super("SyncService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("SYNCSERVICE", "onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SYNCSERVICE", "onCreate");

        prefe = getSharedPreferences(Util.SHAPREF,
                Context.MODE_PRIVATE);
        db = DatabaseOpenHelper.getInstance(this);//Por que es un singleton

        List list = ClienteTable.getMatches("", db);
        List listTitle = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Cliente cliente = (Cliente) list.get(i);
            if ("no".equals(cliente.getEnviado())) {
                terminar(cliente);
                Util.notify(getApplicationContext(), android.R.drawable.arrow_up_float,
                        getString(R.string.app_name), "Guardando datos no guardados",
                        FormularioActivity.class, Util.SYNC_NOTIFY);
            }
        }


    }


    public void terminar(Cliente cliente) {
        if (Util.isNetworkAvailable(getApplicationContext()) && Ion.getDefault(this) != null) {
            String serviceUrl = getResources().getString(R.string.services_url);


            try {
                Builders.Any.M ionCall = Ion.with(this)
                        .load(getResources().getString(R.string.services_url))
                                // .uploadProgressBar(uploadProgressBar)
                        .setMultipartParameter("acc", Util.ACC_ADDSERVICE)
                        .setMultipartParameter("idService", cliente.getId())
                        .setMultipartParameter("id", prefe.getString(Util.ID_SP, ""))
                        .setMultipartParameter("hash", prefe.getString(Util.HASH_SP, ""))
                        .setMultipartParameter("numeroSerie", cliente.getNumero_serie())
                        .setMultipartParameter("tipo", cliente.getTipo().getDescripcion())
                        .setMultipartParameter("nombreCompleto", cliente.getNombre_completo())
                        .setMultipartParameter("emailCliente", cliente.getEmail_cliente())
                        .setMultipartParameter("area", cliente.getArea())
                        .setMultipartParameter("idContrato", cliente.getContrato())
                        .setMultipartParameter("fecha", cliente.getFecha())
                        .setMultipartParameter("observaciones", cliente.getObservaciones())
                        .setMultipartParameter("nombreInstitucion", cliente.getNombre_institucion())
                        .setMultipartParameter("vinculoFirma", cliente.getVinculo_firma())
                        .setMultipartFile("firma", "application/image",
                                new File(cliente.getVinculo_firma()))
                        .setMultipartParameter("absorbedor", Util.clienteActual.getNumeroAbsorvedor())
                        .setMultipartParameter("respirador", Util.clienteActual.getNumeroRespirador());;

                ArrayList<RepuestosCliente> list = RepuestosClienteTable.findByIdCliente(cliente.getId(), RepuestosClienteTable.ALL_COLUMNS, db);
                List listTitle = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    RepuestosCliente repuestosCliente = list.get(i);

                    ionCall.setMultipartParameter("r" + repuestosCliente.getRepuesto(), repuestosCliente.getLote());
                }
                ionCall.asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                try {
                                    SyncService.this.onResponse(Util.ACC_ADDSERVICE,
                                            new JSONObject(result), null);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                int seconds = 0;

                while (seconds < 15) {
                    SystemClock.sleep(1000);
                    seconds++;
                }

                Util.repuestos.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResponse(String action, HashMap<String, String> response,
                           HashMap<String, String> paramsMap) {
        // No implementado

    }

    @Override
    public void onResponse(String action, JSONObject response,
                           HashMap<String, String> paramsMap) {

        if (Util.ACC_ADDSERVICE.equals(action)) {
            this.processService(response, paramsMap);
        }


    }

    public void processService(JSONObject response,
                               HashMap<String, String> paramsMap) {

        try {

            if (response.has("state") && response.has("idService")) {

                if ("OK".equals(response.getString("state"))) {
                    //lalilala.setText("hola");
                    ClienteTable.editClienteMarkSynced(response.getString("idService"), db);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SYNCSERVICE", "onHandleIntent");
    }

    @Override
    public void onDestroy() {
        Log.i("SYNCSERVICE", "onDestroy");
        super.onDestroy();
    }
}
