package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;

public class ClienteTable {

    public static final String COL_ID = "ID";
    public static final String COL_NOMBRE_COMPLETO="NOMBRE";
    public static final String COL_EMAIL_CLIENTE="EMAIL";
    public static final String COL_ID_TIPO ="TIPO";
    public static final String COL_NUMERO_SERIE="NUMERO";
    public static final String COL_AREA="AREA";
    public static final String COL_CONTRATO="CONTRATO";
    public static final String COL_FECHA="FECHA";
    public static final String COL_OBSERVACIONES="OBSERVACIONES";
    public static final String COL_NOMBRE_INSTITUCION="INSTITUCION";
    public static final String COL_VINCULO_FIRMA="FIRMA";
    public static final String COL_ENVIADO="ENVIADO";
    public static final String COL_ABS="ABSORVEDOR";
    public static final String COL_RES="RESPIRADOR";
    public static final String[] ALL_COLUMNS = { COL_ID, COL_NOMBRE_COMPLETO,COL_EMAIL_CLIENTE, COL_ID_TIPO,
            COL_NUMERO_SERIE , COL_AREA, COL_CONTRATO, COL_FECHA, COL_OBSERVACIONES, COL_NOMBRE_INSTITUCION, COL_VINCULO_FIRMA, COL_ENVIADO, COL_ABS, COL_RES};

    public static ArrayList<TableDataChangeListener> listeners= new ArrayList<>();

    public static final String TABLE_NAME = "CLIENTES";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOMBRE_COMPLETO + " TEXT NOT NULL, "
            + COL_EMAIL_CLIENTE + " TEXT NOT NULL, "
            + COL_ID_TIPO + " TEXT NOT NULL, "
            + COL_NUMERO_SERIE + " TEXT NOT NULL, "
            + COL_AREA + " TEXT NOT NULL, "
            + COL_CONTRATO + " TEXT NOT NULL, "
            + COL_FECHA + " TEXT NOT NULL,"
            + COL_OBSERVACIONES + " TEXT NULL, "
            + COL_NOMBRE_INSTITUCION + " TEXT NOT NULL, "
            + COL_VINCULO_FIRMA + " TEXT NOT NULL, "
            + COL_ENVIADO + " TEXT NOT NULL,"
            + COL_ABS + " TEXT NOT NULL, "
            + COL_RES + " TEXT NOT NULL)";

    private static ClienteTable instance = null;
    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.*/
    public static ClienteTable getInstance(Context context){
        if(instance == null){
            instance = new ClienteTable(context);
        }
        return instance;
    }

    private ClienteTable(Context context) {
        Log.i("WTrack log - ClienteTable", "constructor");
    }

    public static ArrayList<Cliente> getMatches(String query, DatabaseOpenHelper db) {

        return getMatches(query, ALL_COLUMNS, db);
    }

    public static ArrayList<Cliente> getMatches(String query, String[] columns,
                                               DatabaseOpenHelper db) {
        String selection = COL_NUMERO_SERIE + " LIKE ? ";
        String[] selectionArgs = new String[] { "%" + query + "%" };

        ArrayList<Cliente> clientes = new ArrayList<Cliente>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            TipoEquipo tipoEquipo=TipoEquipoTable.getTipoEquipo(cursor.getString(cursor
                    .getColumnIndex(COL_ID_TIPO)), db);
            Cliente Cliente = new Cliente(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_NOMBRE_COMPLETO)), cursor.getString(cursor
                    .getColumnIndex(COL_EMAIL_CLIENTE)),
                    tipoEquipo
                    , cursor.getString(cursor
                    .getColumnIndex(COL_NUMERO_SERIE)), cursor.getString(cursor
                    .getColumnIndex(COL_AREA)), cursor.getString(cursor
                    .getColumnIndex(COL_CONTRATO)), cursor.getString(cursor
                    .getColumnIndex(COL_FECHA)), cursor.getString(cursor
                    .getColumnIndex(COL_OBSERVACIONES)), cursor.getString(cursor
                    .getColumnIndex(COL_NOMBRE_INSTITUCION)), cursor.getString(cursor
                    .getColumnIndex(COL_VINCULO_FIRMA)),  cursor.getString(cursor
                    .getColumnIndex(COL_ENVIADO)), cursor.getString(cursor
                    .getColumnIndex(COL_ABS)), cursor.getString(cursor
                    .getColumnIndex(COL_RES)));
            clientes.add(Cliente);
        }

        return clientes;
    }

    /**
     * Funcion que ejecuta cargarEquipos en un thread aparte.
     * */
    public void cargarClientesThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // cargarEquipos();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static long addCliente(Cliente cliente, DatabaseOpenHelper db) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, cliente.getId());
        initialValues.put(COL_NOMBRE_COMPLETO, cliente.getNombre_completo());
        initialValues.put(COL_EMAIL_CLIENTE, cliente.getEmail_cliente());
        initialValues.put(COL_ID_TIPO, cliente.getTipo().getId());
        initialValues.put(COL_NUMERO_SERIE, cliente.getNumero_serie());
        initialValues.put(COL_AREA, cliente.getArea());
        initialValues.put(COL_CONTRATO, cliente.getContrato());
        initialValues.put(COL_FECHA, cliente.getFecha());
        initialValues.put(COL_OBSERVACIONES, cliente.getObservaciones());
        initialValues.put(COL_NOMBRE_INSTITUCION, cliente.getNombre_institucion());
        initialValues.put(COL_VINCULO_FIRMA, cliente.getVinculo_firma());
        initialValues.put(COL_ENVIADO, cliente.getEnviado());
        initialValues.put(COL_ABS, cliente.getNumeroAbsorvedor());
        initialValues.put(COL_RES, cliente.getNumeroRespirador());
		/*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * Repuesto.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { Repuesto.getId() }, initialValues); else
		 */
        long idCliente= db.insert(TABLE_NAME, initialValues);
        cliente.setId(String.valueOf(idCliente));
        notifyListeners();
        return idCliente;

    }

    public static long editCliente(Cliente cliente, DatabaseOpenHelper db) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, cliente.getId());
        initialValues.put(COL_NOMBRE_COMPLETO, cliente.getNombre_completo());
        initialValues.put(COL_EMAIL_CLIENTE, cliente.getEmail_cliente());
        initialValues.put(COL_ID_TIPO, cliente.getTipo().getDescripcion());
        initialValues.put(COL_NUMERO_SERIE, cliente.getNumero_serie());
        initialValues.put(COL_AREA, cliente.getArea());
        initialValues.put(COL_CONTRATO, cliente.getContrato());
        initialValues.put(COL_FECHA, cliente.getFecha());
        initialValues.put(COL_OBSERVACIONES, cliente.getObservaciones());
        initialValues.put(COL_NOMBRE_INSTITUCION, cliente.getNombre_institucion());
        initialValues.put(COL_VINCULO_FIRMA, cliente.getVinculo_firma());
        initialValues.put(COL_ENVIADO, cliente.getEnviado());
        initialValues.put(COL_ABS, cliente.getNumeroAbsorvedor());
        initialValues.put(COL_RES, cliente.getNumeroRespirador());
		/*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * Repuesto.getId() }, ALL_COLUMNS);
		 *
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { Repuesto.getId() }, initialValues); else
		 */

        String selection = COL_ID+ " = ? ";
        String[] selectionArgs = new String[] { cliente.getId()};

        long resultado = db.update(TABLE_NAME, selection, selectionArgs, initialValues);
        notifyListeners();
        return resultado;

    }

    public static long editClienteMarkSynced(String id, DatabaseOpenHelper db) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ENVIADO, "si");


        String selection = COL_ID + " = ? ";
        String[] selectionArgs = new String[] { id };

        long resultado = db.update(TABLE_NAME,selection,selectionArgs,initialValues);
        notifyListeners();
        return resultado;
        //String consulta = "UPDATE " + TABLE_NAME +
          //      " SET ENVIADO= 'si'  WHERE ID='" + id + "'"  ;
       /* String consulta="DELETE FROM CLIENTES" +
                " WHERE ID = '" + id + "'";
        db.execUpdate(consulta);
*/

        //db.updateContact(id,"si");


    }

    public static void deleteClientes(DatabaseOpenHelper db) {
        db.delete(TABLE_NAME);
    }

    public static void addListener(TableDataChangeListener listener) {
        listeners.add(listener);
    }

    public static void notifyListeners(){
        for(int i=0;i<listeners.size();i++){
            listeners.get(i).onDataChange();
        }
    }

    public static void actualizarTabla(ArrayList<Cliente> clientes,DatabaseOpenHelper db){
        ClienteTable.deleteClientes(db);//Borrar para poder actualizar bien
        for (int i = 0; i < clientes.size(); i++) {//guardo en la bd todo lo que traigo
            Cliente eq = clientes.get(i);
            ClienteTable.addCliente(eq, db);

        }
    }

}