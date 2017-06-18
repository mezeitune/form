package com.adox.matias.formadox.model;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matias on 10/08/15.
 */
public class Repuesto {

    private String id;
    private String numero_serie;
    private String tipo_id;
    private String institucion_id;
    private String enviado;
    private EditText editText;

    public Repuesto(String id, String numero_serie, String tipo_id, String institucion_id, String enviado) {
        this.id = id;
        this.numero_serie = numero_serie;
        this.tipo_id = tipo_id;
        this.institucion_id = institucion_id;
        this.enviado = enviado;
    }
    public Repuesto(JSONObject data) {
        try {
            this.id= null;
            this.numero_serie = data.getString("descripcion");
            this.tipo_id = data.getString("id");
            this.institucion_id = " ";
            this.enviado=" ";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Repuesto> fromArray(JSONArray equipos) {//guardo en un array todos los objetos que me mandaron por el response

        ArrayList<Repuesto> result = new ArrayList<Repuesto>();

        try {
            for (int i = 0; i < equipos.length(); i++) {
                result.add(new Repuesto(equipos.getJSONObject(i)));//guardo los objetos en el array
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNumero_serie() { return numero_serie;}

    public void setEnviado(String enviado) { this.enviado = enviado; }

    public String getEnviado() { return enviado;}

    public void setNumero_serie(String numero_serie) {this.numero_serie = numero_serie;}

    public String getTipo_id() { return tipo_id;}

    public void setTipo_id(String tipo_id) { this.tipo_id = tipo_id;}

    public String getInstitucion_id() { return institucion_id;}

    public void setInstitucion_id(String institucion_id) { this.institucion_id = institucion_id;}


    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public EditText getEditText() {
        return editText;
    }
}
