package com.adox.matias.formadox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matias on 10/08/15.
 */
public class Institucion {


    private String id;
    private String nombre;
    private String descripcion;
    private String telefono;
    private String provincia;
    private String localidad;


    public Institucion(String id, String nombre, String descripcion, String telefono, String provincia, String localidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.provincia = provincia;
        this.localidad = localidad;
    }


    public Institucion(JSONObject data) {
        try {
            this.id= null;
            this.nombre = data.getString("nombre");
            this.localidad = " ";
            this.descripcion = " ";
            this.telefono = " ";
            this.provincia = " ";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Institucion> fromArray(JSONArray instituciones) {

        ArrayList<Institucion> result = new ArrayList<Institucion>();

        try {
            for (int i = 0; i < instituciones.length(); i++) {
                result.add(new Institucion(instituciones.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public String getProvincia() {return provincia;}
    public void setProvincia(String provincia) {this.provincia = provincia;}
    public String getLocalidad() {return localidad;}
    public void setLocalidad(String localidad) {this.localidad = localidad;}


}
