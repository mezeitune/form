package com.adox.matias.formadox.model;

/**
 * Created by matias on 10/08/15.
 */
public class ServiceRemoto {

    private String id;
    private String id_equipo;
    private String email_cliente;
    private String area;
    private String contrato;
    private String fecha;
    private String observaciones;

    public ServiceRemoto(String id, String id_equipo, String email_cliente, String area, String contrato, String fecha, String observaciones) {
        this.id = id;
        this.id_equipo = id_equipo;
        this.email_cliente = email_cliente;
        this.area = area;
        this.contrato = contrato;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getId_equipo() {return id_equipo;}
    public void setId_equipo(String id_equipo) {this.id_equipo = id_equipo;}
    public String getEmail_cliente() {return email_cliente;}
    public void setEmail_cliente(String email_cliente) {this.email_cliente = email_cliente;}
    public String getArea() {return area;}
    public void setArea(String area) {this.area = area;}
    public String getContrato() {return contrato;}
    public void setContrato(String contrato) {this.contrato = contrato;}
    public String getFecha() {return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    public String getObservaciones() {return observaciones;}
    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}
}
