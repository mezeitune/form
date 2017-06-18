package com.adox.matias.formadox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.model.TableDataChangeListener;
import com.adox.matias.formadox.model.ChequeoFuncional;
import com.adox.matias.formadox.model.ChequeoFuncionalTable;
import com.adox.matias.formadox.util.Util;

import java.util.List;

/**
 * Created by matias on 07/08/15.
 */
public class ChequeoFragment extends Fragment implements TableDataChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private DatabaseOpenHelper db;
    private FormularioActivity parentView;
    private Button sigBUT;
    private TableLayout t1;
    private TableLayout tl;

    public ChequeoFragment() {
    }

    public void setParent(FormularioActivity parent) {
        parentView = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_chequeos, container, false);

        //Como agregar filas dinamicamente a la tabla
        tl = (TableLayout) rootView.findViewById(R.id.tabla_cuerpo);
        db = DatabaseOpenHelper.getInstance(getActivity());//Por que es un singleton
        sigBUT = (Button) rootView.findViewById(R.id.sigBUT3);

        List list = ChequeoFuncionalTable.getMatches("", db);
        for (int i = 0; i < list.size(); i++) {
            ChequeoFuncional chequeoFuncional = (ChequeoFuncional) list.get(i);
            if (Util.clienteActual.getTipo().getId().equals(chequeoFuncional.getTipo_equipo())
                    || (Util.clienteActual.getTipo().getDescripcion().equals("Sistema de Anestesia AS2000")
                        && Util.completoAbsorbedor
                        && chequeoFuncional.getTipo_equipo().equals("7"))/* el tipo es as2000 y el chequeo es de respirador y completo id respirador */
                    || (Util.clienteActual.getTipo().getDescripcion().equals("Sistema de Anestesia AS2000")
                        && Util.completoRespirador
                        && chequeoFuncional.getTipo_equipo().equals("8"))/* el tipo es as2000 y el chequeo es de respirador y completo id respirador */) {

        /* Create a new row to be added. */
                TableRow tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
/* Create a Button to be the row-content. */
                TextView tv = new TextView(getActivity());
                tv.setText(chequeoFuncional.getDescripcion());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                CheckBox cb = new CheckBox(getActivity());
                cb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                CheckBox cb2 = new CheckBox(getActivity());
                cb2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


/* Add Button to row. */
                tr.addView(tv);
                tr.addView(cb);
                tr.addView(cb2);

/* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }



        sigBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView.goToPage(3);
            }
        });

        return rootView;
    }

    @Override
    public void onDataChange() {
        getActivity().runOnUiThread(new Runnable() {//por que no puedo modificiar la vista , a menos que este en el hilo que modifique primero
            @Override
            public void run() {
                List list = ChequeoFuncionalTable.getMatches("", db);

                for (int i = 0; i < list.size(); i++) {
                    ChequeoFuncional chequeoFuncional = (ChequeoFuncional) list.get(i);
                    if (!(Util.clienteActual.getTipo().getId()).equals(null) && Util.clienteActual.getTipo().getId().equals(chequeoFuncional.getTipo_equipo())) {

        /* Create a new row to be added. */
                        TableRow tr = new TableRow(getActivity());
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
/* Create a Button to be the row-content. */
                        TextView tv = new TextView(getActivity());
                        tv.setText(chequeoFuncional.getDescripcion());
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        CheckBox cb = new CheckBox(getActivity());
                        cb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        CheckBox cb2 = new CheckBox(getActivity());
                        cb2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


/* Add Button to row. */
                        tr.addView(tv);
                        tr.addView(cb);
                        tr.addView(cb2);

/* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
                        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }


        });

    }
}

