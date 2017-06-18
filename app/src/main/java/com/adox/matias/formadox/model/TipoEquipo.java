package com.adox.matias.formadox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matias on 10/08/15.
 */
public class TipoEquipo {

    private String id;
    private String descripcion;





    public TipoEquipo(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;

    }

    public TipoEquipo(JSONObject data) {
        try {
            this.id= data.getString("id");
            this.descripcion = data.getString("descripcion");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TipoEquipo> fromArray(JSONArray tipo_equipos) {

        ArrayList<TipoEquipo> result = new ArrayList<TipoEquipo>();

        try {
            for (int i = 0; i < tipo_equipos.length(); i++) {
                result.add(new TipoEquipo(tipo_equipos.getJSONObject(i)));
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
    public String getDescripcion() { return descripcion;}

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id;}


}
