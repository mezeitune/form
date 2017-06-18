package com.adox.matias.formadox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matias on 10/08/15.
 */
public class ChequeoFuncional {

    private String id;
    private String descripcion;
    private String tipo_equipo;

    public ChequeoFuncional(String id, String descripcion, String tipo_equipo) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipo_equipo = tipo_equipo;
    }



    public ChequeoFuncional(JSONObject data) {
        try {
            this.id= data.getString("id");
            this.descripcion = data.getString("descripcion");
            this.tipo_equipo =data.getString("tipoEquipo");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ChequeoFuncional> fromArray(JSONArray chequeo_funcionales) {

        ArrayList<ChequeoFuncional> result = new ArrayList<ChequeoFuncional>();

        try {
            for (int i = 0; i < chequeo_funcionales.length(); i++) {
                result.add(new ChequeoFuncional(chequeo_funcionales.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public String toString() {
        return descripcion;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo_equipo() {
        return tipo_equipo;
    }

    public void setTipo_equipo(String tipo_equipo) {
        this.tipo_equipo= tipo_equipo;
    }
}
