//http://examples.javacodegeeks.com/android/core/database/android-database-example/
package com.adox.matias.formadox;


import com.adox.matias.formadox.http.HttpCall;
import com.adox.matias.formadox.http.HttpCallCallbackHandler;
import com.adox.matias.formadox.model.Cliente;
import com.adox.matias.formadox.model.ClienteTable;
import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.Repuesto;
import com.adox.matias.formadox.model.RepuestoTable;
import com.adox.matias.formadox.model.Institucion;
import com.adox.matias.formadox.model.InstitucionTable;
import com.adox.matias.formadox.model.RepuestosCliente;
import com.adox.matias.formadox.model.RepuestosClienteTable;
import com.adox.matias.formadox.model.TipoEquipo;
import com.adox.matias.formadox.model.TipoEquipoTable;
import com.adox.matias.formadox.model.ChequeoFuncional;
import com.adox.matias.formadox.model.ChequeoFuncionalTable;
import com.adox.matias.formadox.service.SyncService;
import com.adox.matias.formadox.ui.BrushView;
import com.adox.matias.formadox.util.SecureActivity;
import com.adox.matias.formadox.util.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FormularioActivity extends SecureActivity
        implements ActionBar.TabListener, HttpCallCallbackHandler {

    private DatabaseOpenHelper db;
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    IngresoFragment tab1;
    RepuestosFragment tab2;
    ChequeoFragment tab3;
    RevisionFragment tab4;
    ProgressDialog progress;
    public static BrushView brushView = null;
    SharedPreferences prefe;
    private ArrayList<Institucion> instituciones;
    private ArrayList<Repuesto> equipos;
    private ArrayList<TipoEquipo> tipo_equipos;
    private ArrayList<ChequeoFuncional> chequeo_funcionales;
    PendingIntent pintent;
    AlarmManager alarmManager;

    // Contador de respuestas
    int respuestas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.classType = FormularioActivity.class;
        super.onCreate(savedInstanceState);//Aca llama al Secure activity para que se fije que si no esta logeado vaya a login activity
        setContentView(R.layout.activity_formulario);

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para el service, si ve que es por primera vez , le dice que se ejecute cada tanto
        //sino va a ir al manifest

        if (prefs.getBoolean(Util.PRIMERAVEZ_SP, true)) {

            Log.i("WORKTRACK", "Primera vez.");

            Intent alarmIntent = new Intent(getApplicationContext(),
                    SyncService.class);
            pintent = PendingIntent.getService(getApplicationContext(), 0,
                    alarmIntent, 0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP, Util.SEGS_POS,
                    Util.SEGS_POS, pintent);

            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(Util.PRIMERAVEZ_SP, false);
            editor.commit();

        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        db = DatabaseOpenHelper.getInstance(this);//Por que es un singleton

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        tab1 = new IngresoFragment();
        tab2 = new RepuestosFragment();
        tab3 = new ChequeoFragment();
        tab4 = new RevisionFragment();


        tab1.setParent(this);
        tab2.setParent(this);
        tab3.setParent(this);
        tab4.setParent(this);


        prefe = getSharedPreferences(Util.SHAPREF,
                Context.MODE_PRIVATE);

        String caller = getIntent().getStringExtra("deDondeViene");
        if ("LoginActivity".equals(caller)) {//si viene del logeo, que actualize la bd
            actualizarBD();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //System.exit(0);
        //finish();
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.item1:
                Util.logout(this);
                break;

            case R.id.item2:
                actualizarBD();
                break;

            case R.id.item3:
                subirServices();
                break;

            case R.id.item4:
                finish();
                break;

        }
        return true;


    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void goToPage(int idx) {
        mViewPager.setCurrentItem(idx);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return tab1;
                case 1:
                    return tab2;
                case 2:
                    return tab3;
                case 3:
                    return tab4;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);

            }
            return null;
        }


    }

    public void subirServices() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Util.isNetworkAvailable(getApplicationContext())) {

                        Util.showToast("En este momento no tiene internet",
                                FormularioActivity.this);


                }else{


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress = new ProgressDialog(FormularioActivity.this);
                            progress.setTitle(getResources().getString(R.string.loading_tit));
                            progress.setMessage(getResources().getString(R.string.loading_subiendo));
                            progress.setCancelable(true);
                            progress.show();

                        }
                    });


                    prefe = getSharedPreferences(Util.SHAPREF,
                            Context.MODE_PRIVATE);
                    db = DatabaseOpenHelper.getInstance(FormularioActivity.this);//Por que es un singleton


                    List list = ClienteTable.getMatches("", db);
                    List listTitle = new ArrayList();
                    int contadorServicesSubidos = 0;

                    for (int i = 0; i < list.size(); i++) {
                        Cliente cliente = (Cliente) list.get(i);
                        if ("no".equals(cliente.getEnviado())) {
                            contadorServicesSubidos++;
                            terminar(cliente);
                            Util.notify(getApplicationContext(), android.R.drawable.arrow_up_float,
                                    getString(R.string.app_name), "Guardando datos no guardados",
                                    FormularioActivity.class, Util.SYNC_NOTIFY);
                        }
                    }

                    progress.dismiss();
                    if (contadorServicesSubidos == 0) {
                        Util.showToast("No hay Services pendientes",
                                FormularioActivity.this);
                    } else {
                        Util.showToast("Se subieron " + contadorServicesSubidos + " Services pendientes",
                                FormularioActivity.this);
                    }
                }
            }
        }
        ).start();


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
                                new File(cliente.getVinculo_firma()));

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
                                    responseSubirServices(Util.ACC_ADDSERVICE,
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


    public void responseSubirServices(String action, JSONObject response,
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

        }
    }

    public void actualizarBD() {
        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading_tit));
        progress.setMessage(getResources().getString(R.string.loading_txt));
        progress.setCancelable(true);
        progress.show();

        respuestas = 0;

        HttpCall call = new HttpCall(getResources().getString(
                R.string.services_url), Util.ACC_DIN, this, HttpCall.CallType.JSON);


        call.addParam("id", prefe.getString(Util.ID_SP, ""));
        call.addParam("hash", prefe.getString(Util.HASH_SP, ""));

        call.start();

        HttpCall call2 = new HttpCall(getResources().getString(
                R.string.services_url), Util.ACC_STATIC, this, HttpCall.CallType.JSON);
        call2.addParam("id", prefe.getString(Util.ID_SP, ""));
        call2.addParam("hash", prefe.getString(Util.HASH_SP, ""));
        call2.start();


    }


    @Override
    public void onResponse(String action, HashMap<String, String> response, HashMap<String, String> map) {

    }

    @Override
    public void onResponse(String action, JSONObject response, HashMap<String, String> map) {
        final FormularioActivity instance = this;
        final JSONObject localResponse = response;
        final String actionResponse = action;
        // Cargo los datos en los objetos de la sesion
        try {

            if (Util.ACC_STATIC.equals(actionResponse)) {
                // Proceso respuesta para static
                tipo_equipos = TipoEquipo.fromArray(response.getJSONArray("tipos"));
                chequeo_funcionales = ChequeoFuncional.fromArray(localResponse.getJSONArray("chequeos"));
                equipos = Repuesto.fromArray(localResponse.getJSONArray("repuestos"));

                ChequeoFuncionalTable.actualizarTabla(chequeo_funcionales, db);
                TipoEquipoTable.actualizarTabla(tipo_equipos, db);
                RepuestoTable.actualizarTabla(equipos, db);
                respuestas++;
            }

            if (Util.ACC_DIN.equals(actionResponse)) {
                // Proceso respuesta para dinamica
                instituciones = Institucion.fromArray(localResponse.getJSONArray("instituciones"));

                InstitucionTable.actualizarTabla(instituciones, db);
                respuestas++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Ya respondieron ambos CALL entonces escondo el progress.
        if (respuestas >= 2) {
            runOnUiThread(new Runnable() {//por que no puedo modificiar la vista , a menos que este en el hilo que modifique primero
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
        }

    }

}


