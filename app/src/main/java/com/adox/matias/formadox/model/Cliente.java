package com.adox.matias.formadox.model;

/**
 * Created by matias on 27/08/15.
 */
public class Cliente {

    private String id;
    private String nombre_completo;
    private String email_cliente;
    private TipoEquipo tipo;
    private String numero_serie;
    private String area;
    private String contrato;
    private String fecha;
    private String observaciones;
    private String nombre_institucion;
    private String vinculo_firma;
    private String enviado;
    private String numero_absorvedor;
    private String numero_respirador;


    public Cliente(String id, String nombre_completo, String email_cliente, TipoEquipo tipo, String numero_serie, String area, String contrato, String fecha, String observaciones, String nombre_institucion, String vinculo_firma, String enviado,String numero_absorvedor,String numero_respirador) {
        this.id=id;
        this.nombre_completo=nombre_completo;
        this.email_cliente=email_cliente;
        this.tipo=tipo;
        this.numero_serie=numero_serie;
        this.area=area;
        this.contrato=contrato;
        this.fecha=fecha;
        this.observaciones=observaciones;
        this.nombre_institucion=nombre_institucion;
        this.vinculo_firma=vinculo_firma;
        this.enviado=enviado;
        this.numero_absorvedor=numero_absorvedor;
        this.numero_respirador=numero_respirador;
    }

    public Cliente(){
        this.id=null;
        this.enviado="no";
        this.observaciones="-";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getEmail_cliente() {
        return email_cliente;
    }

    public void setEmail_cliente(String email_cliente) {
        this.email_cliente = email_cliente;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public String getNumeroAbsorvedor() {
        return numero_absorvedor;
    }

    public void setNumeroAbsorvedor(String numero_absorvedor) {
        this.numero_absorvedor = numero_absorvedor;
    }

    public String getNumeroRespirador() {
        return numero_respirador;
    }

    public void setNumeroRespirador(String numero_respirador) {
        this.numero_respirador= numero_respirador;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombre_institucion() {
        return nombre_institucion;
    }

    public void setNombre_institucion(String nombre_institucion) {
        this.nombre_institucion = nombre_institucion;
    }

    public String getVinculo_firma() {
        return vinculo_firma;
    }

    public void setVinculo_firma(String vinculo_firma) {
        this.vinculo_firma = vinculo_firma;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }
}