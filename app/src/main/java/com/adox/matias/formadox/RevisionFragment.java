package com.adox.matias.formadox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import com.adox.matias.formadox.http.HttpCallCallbackHandler;
import com.adox.matias.formadox.model.Cliente;
import com.adox.matias.formadox.model.ClienteTable;
import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.RepuestosCliente;
import com.adox.matias.formadox.model.RepuestosClienteTable;
import com.adox.matias.formadox.model.TableDataChangeListener;
import com.adox.matias.formadox.util.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matias on 07/08/15.
 */
public class RevisionFragment extends Fragment implements TableDataChangeListener, HttpCallCallbackHandler {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseOpenHelper db;
    private FormularioActivity parentView;
    private Button sigBUT;
    private Button firmarBUT;
    private Button terminarBUT;
    private TextView nomYApe;
    private TextView emailCliente;
    private TextView tipoEquipo;
    private TextView area;
    private TextView nombreInstitucion;
    private TextView contrato;
    private TextView numeroSerie;
    private TextView numeroSerieAbsorbedor;
    private TextView numeroSerieRespirador;
    ;
    private TextView clientesGuardadosYEnviados;
    ProgressDialog progress;
    SharedPreferences prefe;


    public RevisionFragment() {
    }

    public void setParent(FormularioActivity parent) {
        parentView = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab_revision, container, false);


        prefe = getActivity().getSharedPreferences(Util.SHAPREF,
                Context.MODE_PRIVATE);

        ClienteTable.addListener(this);

        nomYApe = (TextView) rootView.findViewById(R.id.nomYApe2);
        emailCliente = (TextView) rootView.findViewById(R.id.emailCliente);
        tipoEquipo = (TextView) rootView.findViewById(R.id.tipoEquipo);
        area = (TextView) rootView.findViewById(R.id.area);
        nombreInstitucion = (TextView) rootView.findViewById(R.id.nombreInstitucion);
        contrato = (TextView) rootView.findViewById(R.id.contrato);
        numeroSerie = (TextView) rootView.findViewById(R.id.numeroSerie);
        numeroSerieAbsorbedor = (TextView) rootView.findViewById(R.id.numeroSerieAbsorvedor);
        numeroSerieRespirador = (TextView) rootView.findViewById(R.id.numeroSerieRespirador);
        db = DatabaseOpenHelper.getInstance(getActivity());
        //clientesGuardadosYEnviados = (TextView) rootView.findViewById(R.id.textView9);
        firmarBUT = (Button) rootView.findViewById(R.id.firmar);
        terminarBUT = (Button) rootView.findViewById(R.id.terminar);

        String sourceString = "<b><u>Representante:</u></b> " + Util.clienteActual.getNombre_completo();
        nomYApe.setText(Html.fromHtml(sourceString));

        String sourceString2 = "<b><u>Email: </u></b>" + Util.clienteActual.getEmail_cliente();
        emailCliente.setText(Html.fromHtml(sourceString2));

        String sourceString3 = "<b><u>Tipo de equipo:</u></b> " + Util.clienteActual.getTipo();
        tipoEquipo.setText(Html.fromHtml(sourceString3));

        String sourceString4 = "<b><u>Area:</u></b> " + Util.clienteActual.getArea();
        area.setText(Html.fromHtml(sourceString4));

        String sourceString5 = "<b><u>Institucion: </u></b>" + Util.clienteActual.getNombre_institucion();
        nombreInstitucion.setText(Html.fromHtml(sourceString5));

        String sourceString6 = "<b><u>Contrato:</u></b> " + Util.clienteActual.getContrato();
        contrato.setText(Html.fromHtml(sourceString6));

        String sourceString7 = "<b><u>Numero serie: </u></b>" + Util.clienteActual.getNumero_serie();
        numeroSerie.setText(Html.fromHtml(sourceString7));

        if(Util.completoRespirador){
            numeroSerieRespirador.setVisibility(View.VISIBLE);

            String sourceString8 = "<b><u>Numero serie Respirador: </u></b>" + Util.clienteActual.getNumeroRespirador();
            numeroSerieRespirador.setText(Html.fromHtml(sourceString8));
        }
        if(Util.completoAbsorbedor){
            numeroSerieAbsorbedor.setVisibility(View.VISIBLE);

            String sourceString9 = "<b><u>Numero serie Absorbedor: </u></b>" + Util.clienteActual.getNumeroAbsorvedor();
            numeroSerieAbsorbedor.setText(Html.fromHtml(sourceString9));
        }


        terminarBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cliente cli=new Cliente(null,Util.clienteActual.getNombre_completo(), Util.clienteActual.getEmail_cliente(), Util.clienteActual.getTipo(), Util.clienteActual.getNumero_serie(), Util.clienteActual.getArea(), Util.clienteActual.getContrato(), Util.clienteActual.getFecha() , Util.clienteActual.getObservaciones() , Util.clienteActual.getNombre_institucion(), Util.clienteActual.getVinculo_firma(), Util.clienteActual.getEnviado());
                if ("-".equals(Util.clienteActual.getVinculo_firma())) {
                    Util.showToast("Por favor ingrese la firma", getActivity());
                } else {
                    terminar();
                }

            }
        });

        firmarBUT.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent alertIntent = new Intent(getActivity(), FirmarActivity.class);

                startActivity(alertIntent);
            }
        });




        /*List listTitle = new ArrayList();
        for (int i=0;i<Util.repuestos.size();i++) {//Los repuestos marcados  y su lote (no deharse llevar por los nombres de las funciones q le psue adentro del for)

            listTitle.add(i, Util.repuestos.get(i).getTipo_id() + " " +  Util.repuestos.get(i).getEnviado());

        }

        clientesGuardadosYEnviados.setText(listTitle.toString());*/


        return rootView;
    }

    public void terminar() {
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getResources().getString(R.string.loading_tit));
        progress.setMessage(getResources().getString(R.string.loading_txt));
        progress.setCancelable(true);
        progress.show();

        ClienteTable.addCliente(Util.clienteActual, db);

        Builders.Any.M ionCall = Ion.with(this)
                .load(getResources().getString(R.string.services_url))
                        // .uploadProgressBar(uploadProgressBar)
                .setMultipartParameter("acc", Util.ACC_ADDSERVICE)
                .setMultipartParameter("idService", Util.clienteActual.getId())
                .setMultipartParameter("id", prefe.getString(Util.ID_SP, ""))
                .setMultipartParameter("hash", prefe.getString(Util.HASH_SP, ""))
                .setMultipartParameter("numeroSerie", Util.clienteActual.getNumero_serie())
                .setMultipartParameter("tipo", Util.clienteActual.getTipo().getDescripcion())
                .setMultipartParameter("nombreCompleto", Util.clienteActual.getNombre_completo())
                .setMultipartParameter("emailCliente", Util.clienteActual.getEmail_cliente())
                .setMultipartParameter("area", Util.clienteActual.getArea())
                .setMultipartParameter("idContrato", Util.clienteActual.getContrato())
                .setMultipartParameter("fecha", Util.clienteActual.getFecha())
                .setMultipartParameter("observaciones", Util.clienteActual.getObservaciones())
                .setMultipartParameter("nombreInstitucion", Util.clienteActual.getNombre_institucion())
                .setMultipartParameter("vinculoFirma", Util.clienteActual.getVinculo_firma())
                .setMultipartFile("firma", "application/image",
                        new File(Util.clienteActual.getVinculo_firma()))
                .setMultipartParameter("absorbedor", Util.clienteActual.getNumeroAbsorvedor())
                .setMultipartParameter("respirador", Util.clienteActual.getNumeroRespirador());

        for (int i = 0; i < Util.repuestos.size(); i++) {
            ionCall.setMultipartParameter("r" + Util.repuestos.get(i).getTipo_id(), Util.repuestos.get(i).getEnviado());

            RepuestosCliente repuestosCliente=new RepuestosCliente(null,Util.clienteActual.getId() ,Util.repuestos.get(i).getTipo_id(), Util.repuestos.get(i).getEnviado());
            RepuestosClienteTable.addRepuesto_Cliente(repuestosCliente, db, false);
        }


            if (Util.isNetworkAvailable(parentView.getApplicationContext())) {
                ionCall.asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                try {
                                    RevisionFragment.this.onResponse(Util.ACC_ADDSERVICE,
                                            new JSONObject(result), null);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });


            } else {
                Util.showToast("En este momento no tiene internet, luego se guardara la informacion requerida",
                        getActivity());
                progress.dismiss();
            }

        Util.repuestos.clear();
    }


    @Override
    public void onResponse(String action, HashMap<String, String> response,
                           HashMap<String, String> paramsMap) {
        // No implementado
        progress.dismiss();
    }

    @Override
    public void onResponse(String action, JSONObject response,
                           HashMap<String, String> paramsMap) {

        if (Util.ACC_ADDSERVICE.equals(action)) {
            this.processService(response, paramsMap);
        }

        progress.dismiss();

    }


    public void processService(JSONObject response,
                               HashMap<String, String> paramsMap) {

        try {

            if (response.has("state") && response.has("idService")) {

                if ("OK".equals(response.getString("state"))) {
                    //clientesGuardadosYEnviados.setText("hola");
                    ClienteTable.editClienteMarkSynced(response.getString("idService"), db);
                    /*Util.clienteActual.setId(response.getString("idService"));
                    Util.clienteActual.setEnviado("si");
                    ClienteTable.addCliente(Util.clienteActual, db);*/

                }
            } else {
                Util.showToast("lalalaallalaa",
                        getActivity());
            }
        } catch (Exception e) {
            Util.showToast(getResources().getString(R.string.error_net),
                    getActivity());
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChange() {

        /*this.getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {

                        List list = ClienteTable.getMatches("", db);
                        List listTitle = new ArrayList();
                        for (int i = 0; i < list.size(); i++) {
                            Cliente dada = (Cliente) list.get(i);
                            listTitle.add(i, dada.getId() + " " + dada.getEnviado());

                        }
                        clientesGuardadosYEnviados.setText(listTitle.toString());
                    }
                }
        );*/
    }
}
