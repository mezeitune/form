package com.adox.matias.formadox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.Repuesto;
import com.adox.matias.formadox.model.RepuestoTable;
import com.adox.matias.formadox.model.TableDataChangeListener;
import com.adox.matias.formadox.util.Util;

import java.util.List;

/**
 * Created by matias on 07/08/15.
 */
public class RepuestosFragment extends Fragment implements TableDataChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseOpenHelper db;
    private FormularioActivity parentView;
    private Button sigBUT;
    private EditText observaciones;
    private TableLayout t1;
    private LinearLayout llRepuestos;


    public RepuestosFragment() {
    }

    public void setParent(FormularioActivity parent) {
        parentView = parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_repuestos, container, false);
        RepuestoTable.addListener(this);
        //Como agregar filas dinamicamente a la tabla
        llRepuestos = (LinearLayout) rootView.findViewById(R.id.llRepuestos);
        db = DatabaseOpenHelper.getInstance(getActivity());//Por que es un singleton
        sigBUT = (Button) rootView.findViewById(R.id.sigBUT2);
        observaciones = (EditText) rootView.findViewById(R.id.observaciones);

        // Esta tabla deberia llamarse Respuesto
        List list = RepuestoTable.getMatches("", db);
        for (int i = 0; i < list.size(); i++) {
            final Repuesto repuesto = (Repuesto) list.get(i);
            // Create a new row to be added.
            LinearLayout llContenedor = new LinearLayout(getActivity());
            llContenedor.setOrientation(LinearLayout.HORIZONTAL);
            llContenedor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            llContenedor.setWeightSum(9);

            CheckBox cb = (CheckBox) getActivity().getLayoutInflater().inflate(R.layout.cbtemplate, null);
            cb.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 4f));

            TextView tv = new TextView(getActivity());
            tv.setText(repuesto.getNumero_serie());
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            final EditText et = (EditText) getActivity().getLayoutInflater().inflate(R.layout.ettemplate, null);
            et.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 3f));

            et.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    for (int i = 0; i < Util.repuestos.size(); i++) {
                        if (repuesto.getTipo_id().equals(Util.repuestos.get(i).getTipo_id())) {
                            Util.repuestos.get(i).setEnviado(repuesto.getEditText().getText().toString());
                        }
                    }
                }
            });


// Add Button to row.
            llContenedor.addView(cb);
            llContenedor.addView(tv);
            llContenedor.addView(et);
//Add row to TableLayout.
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
            llRepuestos.addView(llContenedor);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Repuesto eq = new Repuesto("", "", repuesto.getTipo_id(), "", repuesto.getEditText().getText().toString());
                        Util.repuestos.add(eq);
                    }
                }
            });
            repuesto.setEditText(et);
        }


        sigBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                parentView.goToPage(2);
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


                Util.clienteActual.setObservaciones(observaciones.getText().toString());
            }
        }
    }

    @Override
    public void onDataChange() {
        getActivity().runOnUiThread(new Runnable() {//por que no puedo modificiar la vista , a menos que este en el hilo que modifique primero
            @Override
            public void run() {
                llRepuestos = (TableLayout) getActivity().findViewById(R.id.tabla_cuerpo);


                llRepuestos.removeAllViewsInLayout();

                // Esta tabla deberia llamarse Respuesto
                List list = RepuestoTable.getMatches("", db);

                for (int i = 0; i < list.size(); i++) {
                    final Repuesto repuesto = (Repuesto) list.get(i);
                    // Create a new row to be added.
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
// Create a Button to be the row-content.

                    CheckBox cb = new CheckBox(getActivity());
                    cb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                    TextView tv = new TextView(getActivity());
                    tv.setText(repuesto.getNumero_serie());
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                    final EditText et = new EditText(getActivity());
                    et.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    et.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        public void afterTextChanged(Editable s) {
                            for (int i = 0; i < Util.repuestos.size(); i++) {
                                if (repuesto.getTipo_id().equals(Util.repuestos.get(i).getTipo_id())) {
                                    Util.repuestos.get(i).setEnviado(repuesto.getEditText().getText().toString());
                                }
                            }
                        }
                    });
// Add Button to row.
                    tr.addView(cb);
                    tr.addView(tv);
                    tr.addView(et);
//Add row to TableLayout.
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
                    llRepuestos.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                Repuesto eq = new Repuesto("", "", repuesto.getTipo_id(), "", repuesto.getEditText().getText().toString());
                                Util.repuestos.add(eq);
                            }
                        }
                    });
                    repuesto.setEditText(et);

                }
            }


        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RepuestoTable.removeListener(this);
    }
}
