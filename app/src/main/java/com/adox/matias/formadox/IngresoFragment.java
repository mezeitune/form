package com.adox.matias.formadox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adox.matias.formadox.model.Cliente;
import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.Institucion;
import com.adox.matias.formadox.model.InstitucionTable;
import com.adox.matias.formadox.model.TableDataChangeListener;
import com.adox.matias.formadox.model.TipoEquipo;
import com.adox.matias.formadox.model.TipoEquipoTable;
import com.adox.matias.formadox.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class IngresoFragment extends Fragment implements TableDataChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseOpenHelper db;
    private Button sigBUT;
    private FragmentManager fManager;
    private FormularioActivity parentView;

    private EditText nomYApe;
    private AutoCompleteTextView nombreInstitucion;
    private EditText area;
    private EditText numeroSerie;
    private EditText numeroSerieAbsorvedor;
    private EditText numeroSerieRespirador;
    private TextView numeroSerieAbsorvedortv;
    private TextView numeroSerieRespiradortv;
    private EditText emailCliente;
    private Spinner spinnerTipoEquipo;
    private Spinner spinnerTipoContrato;
    private ArrayAdapter<TipoEquipo> adapterTipoEquipo;
    private AutoCompleteTextView textView;
    private TextView tvDebug;


    public IngresoFragment() {
    }

    public void setParent(FormularioActivity parent) {
        parentView = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_ingreso, container, false);

        InstitucionTable.addListener(this);

        sigBUT = (Button) rootView.findViewById(R.id.sigBUT);
        textView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        tvDebug = (TextView) rootView.findViewById(R.id.textView);
        spinnerTipoEquipo = (Spinner) rootView.findViewById(R.id.spnTipoEquipo);
        spinnerTipoContrato = (Spinner) rootView.findViewById(R.id.spnTipoContrato);
        nomYApe = (EditText) rootView.findViewById(R.id.nomYApe);
        numeroSerie = (EditText) rootView.findViewById(R.id.numeroSerie);
        numeroSerieAbsorvedor = (EditText) rootView.findViewById(R.id.numeroSerieAbsorvedor);
        numeroSerieRespirador = (EditText) rootView.findViewById(R.id.numeroSerieRespirador);
        numeroSerieAbsorvedortv = (TextView) rootView.findViewById(R.id.textViewnsa);
        numeroSerieRespiradortv = (TextView) rootView.findViewById(R.id.textViewnsr);
        nombreInstitucion = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        emailCliente = (EditText) rootView.findViewById(R.id.emailCliente);
        area = (EditText) rootView.findViewById(R.id.area);
        db = DatabaseOpenHelper.getInstance(getActivity());//Por que es un singleton

        Util.clienteActual = new Cliente();

        // get all instituciones
        List list = InstitucionTable.getMatches("", db);
        List listTitle = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Institucion institucion = (Institucion) list.get(i);
            listTitle.add(i, institucion.getNombre());

        }
        // tvDebug.setText(listTitle.toString());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, listTitle);



        // Numero de caracteres necesarios para que se empiece
        // a mostrar la lista
        textView.setThreshold(2);

        // Se establece el Adapter
        textView.setAdapter(adapter);


        List listaTipoEquipo = TipoEquipoTable.getMatches("", db);
        List listTitle2 = new ArrayList();
        for (int i = 0; i < listaTipoEquipo.size(); i++) {
            TipoEquipo tipoEquipo = (TipoEquipo) listaTipoEquipo.get(i);
            listTitle2.add(i, tipoEquipo.getDescripcion()+" "+tipoEquipo.getId());
        }

        adapterTipoEquipo = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, listaTipoEquipo);

        spinnerTipoEquipo.setAdapter(adapterTipoEquipo);

        spinnerTipoEquipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Util.clienteActual.setTipo(
                        adapterTipoEquipo.getItem(spinnerTipoEquipo.getSelectedItemPosition()));
                if(Util.clienteActual.getTipo().getDescripcion().equals("Sistema de Anestesia AS2000")){
                    numeroSerieRespirador.setVisibility(View.VISIBLE);
                    numeroSerieAbsorvedor.setVisibility(View.VISIBLE);
                    numeroSerieRespiradortv.setVisibility(View.VISIBLE);
                    numeroSerieAbsorvedortv.setVisibility(View.VISIBLE);
                }
                if(!Util.clienteActual.getTipo().getDescripcion().equals("Sistema de Anestesia AS2000")){
                    numeroSerieRespirador.setVisibility(View.GONE);
                    numeroSerieAbsorvedor.setVisibility(View.GONE);
                    numeroSerieRespiradortv.setVisibility(View.GONE);
                    numeroSerieAbsorvedortv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        sigBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentView.goToPage(1);

            }
        });


        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//detectar cuando cambio de fragment , asi guardo todo lo de la tab
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                //Log.d("MyFragment", "Not visible anymore.  Stopping audio.");

                boolean incompleto = false;


                if ("".equals(nomYApe.getText().toString())) {
                    Util.clienteActual.setNombre_completo("NoCompletado");
                    incompleto = true;
                }

                if ("".equals(numeroSerie.getText().toString())) {
                    Util.clienteActual.setNumero_serie("NoCompletado");
                    incompleto = true;
                }
                if ("".equals(nombreInstitucion.getText().toString())) {
                    Util.clienteActual.setNombre_institucion("NoCompletado");
                    incompleto = true;
                }
                if ("".equals(emailCliente.getText().toString())) {
                    Util.clienteActual.setEmail_cliente("NoCompletado");
                    incompleto = true;
                }
                if ("".equals(area.getText().toString())) {
                    Util.clienteActual.setArea("NoCompletado");
                    incompleto = true;
                }



                Util.clienteActual.setNombre_completo(nomYApe.getText().toString());
                Util.clienteActual.setNumero_serie(numeroSerie.getText().toString());
                Util.clienteActual.setNombre_institucion(nombreInstitucion.getText().toString());
                Util.clienteActual.setEmail_cliente(emailCliente.getText().toString());
                Util.clienteActual.setTipo(
                        this.adapterTipoEquipo.getItem(spinnerTipoEquipo.getSelectedItemPosition()));
                Util.clienteActual.setContrato(spinnerTipoContrato.getSelectedItem().toString());
                Util.clienteActual.setVinculo_firma("-");
                Util.clienteActual.setEnviado("no");
                Util.clienteActual.setArea(area.getText().toString());
                Util.clienteActual.setNumeroAbsorvedor(numeroSerieAbsorvedor.getText().toString());
                Util.clienteActual.setNumeroRespirador(numeroSerieRespirador.getText().toString());

                if(!numeroSerieAbsorvedor.getText().toString().equals("")){
                    Util.completoAbsorbedor=true;
                }else{
                    Util.completoAbsorbedor=false;
                }

                if(!numeroSerieRespirador.getText().toString().equals("")){
                    Util.completoRespirador=true;
                }else{
                    Util.completoRespirador=false;
                }

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());


                Util.clienteActual.setFecha(formattedDate);

                if (incompleto) {
                    Util.showToast("No completo todos los datos",
                            getActivity());
                    //parentView.goToPage(0);
                    return;
                }
            }
        }

    }
    @Override
    public void onDataChange() {
        getActivity().runOnUiThread(new Runnable() {//por que no puedo modificiar la vista , a menos que este en el hilo que modifique primero
            @Override
            public void run() {
                List resultTipoEquipos = TipoEquipoTable.getMatches("", db);
                List listTipoEquipos = new ArrayList();
                for (int i = 0; i < resultTipoEquipos.size(); i++) {
                    TipoEquipo tipoEquipo = (TipoEquipo) resultTipoEquipos.get(i);
                    listTipoEquipos.add(i, tipoEquipo.getDescripcion());
                }

                adapterTipoEquipo = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, resultTipoEquipos);
                spinnerTipoEquipo = (Spinner) getActivity().findViewById(R.id.spnTipoEquipo);
                spinnerTipoEquipo.setAdapter(adapterTipoEquipo);
                adapterTipoEquipo.notifyDataSetChanged();

                List list = InstitucionTable.getMatches("", db);
                List listTitle = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Institucion dada = (Institucion) list.get(i);
                    listTitle.add(i, dada.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, listTitle);

                textView = (AutoCompleteTextView) getActivity().findViewById(R.id.autoCompleteTextView);

                // Numero de caracteres necesarios para que se empiece
                // a mostrar la lista
                textView.setThreshold(2);

                // Se establece el Adapter
                textView.setAdapter(adapter);
            }


        });
    }
}
