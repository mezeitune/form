package com.adox.matias.formadox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RepuestosCliente {

    private String id;
    private String id_cliente;
    private String repuesto;
    private String lote;

    public RepuestosCliente(String id, String id_cliente, String repuesto, String lote) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.repuesto = repuesto;
        this.lote=lote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(String repuesto) {
        this.repuesto = repuesto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}