package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;

public class RepuestosClienteTable {

    public static final String COL_ID = "ID";
    public static final String COL_ID_CLIENTE = "ID_CLIENTE";
    public static final String COL_REPUESTO = "REPUESTO";
    public static final String COL_LOTE = "LOTE";
    public static final String[] ALL_COLUMNS = {COL_ID, COL_ID_CLIENTE ,COL_REPUESTO, COL_LOTE};

    public static ArrayList<TableDataChangeListener> listeners = new ArrayList<>();

    public static final String TABLE_NAME = "REPUESTOS_CLIENTE";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ID_CLIENTE
            + " INTEGER, " + COL_REPUESTO  + " TEXT NOT NULL, " + COL_LOTE + " TEXT NOT NULL )";

    private static RepuestosClienteTable instance = null;

    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.
     */
    public static RepuestosClienteTable getInstance(Context context) {
        if (instance == null) {
            instance = new RepuestosClienteTable(context);
        }
        return instance;
    }

    private RepuestosClienteTable(Context context) {
        Log.i("WTrack log - RepuestosClienteTable", "constructor");
    }

    public static ArrayList<RepuestosCliente> getMatches(String query, DatabaseOpenHelper db) {

        return findByIdCliente(query, ALL_COLUMNS, db);
    }

    public static ArrayList<RepuestosCliente> findByIdCliente(String id, String[] columns,
                                                          DatabaseOpenHelper db){
        String selection = COL_ID_CLIENTE + " = ? ";
        String[] selectionArgs = new String[] { id };

        return getMatches(selection, selectionArgs, columns, db);
    }

    public static ArrayList<RepuestosCliente> getMatches(String selection, String[] selectionArgs, String[] columns,
                                                         DatabaseOpenHelper db) {
        ArrayList<RepuestosCliente> repuestosClientes = new ArrayList<RepuestosCliente>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            RepuestosCliente repuestosCliente = new RepuestosCliente(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_ID_CLIENTE)), cursor.getString(cursor
                    .getColumnIndex(COL_REPUESTO)), cursor.getString(cursor
                    .getColumnIndex(COL_LOTE)));
            repuestosClientes.add(repuestosCliente);
        }

        return repuestosClientes;
    }

    /**
     * Funcion que ejecuta cargarBooks en un thread aparte.
     */
    public void cargarChequeo_funcionalesThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // cargarBooks();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static long addRepuesto_Cliente(RepuestosCliente repuesto_cliente, DatabaseOpenHelper db, boolean b) {
        return addRepuesto_cliente(repuesto_cliente, db, true);
    }


    public static long addRepuesto_cliente(RepuestosCliente repuesto_cliente,
                                            DatabaseOpenHelper db, boolean notify) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, repuesto_cliente.getId());
        initialValues.put(COL_ID_CLIENTE, repuesto_cliente.getId_cliente());
        initialValues.put(COL_REPUESTO, repuesto_cliente.getRepuesto());
        initialValues.put(COL_LOTE, repuesto_cliente.getLote());
        /*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * book.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { book.getId() }, initialValues); else
		 */
        long resultado = db.insert(TABLE_NAME, initialValues);
        notifyListeners();
        return resultado;

    }

    public static void deleteChequeo_funcional(DatabaseOpenHelper db) {
        db.delete(TABLE_NAME);
    }

    public static void addListener(TableDataChangeListener listener) {
        listeners.add(listener);
    }

    public static void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDataChange();
        }
    }


    public static void actualizarTabla(ArrayList<RepuestosCliente> repuestos_clientes, DatabaseOpenHelper db) {
        RepuestosClienteTable.deleteChequeo_funcional(db);//Borrar para poder actualizar bien
        for (int i = 0; i < repuestos_clientes.size(); i++) {//guardo en la bd todo lo que traigo
            RepuestosCliente inst = repuestos_clientes.get(i);
            RepuestosClienteTable.addRepuesto_Cliente(inst, db, false);
        }
        notifyListeners();
    }

}