package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;

public class ServiceRemotoTable {

    public static final String COL_ID = "ID";
    public static final String COL_ID_EQUIPO = "ID_EQUIPO";
    public static final String COL_EMAIL_CLIENTE = "EMAIL_CLIENTE";
    public static final String COL_AREA = "AREA";
    public static final String COL_CONTRATO = "CONTRATO";
    public static final String COL_FECHA = "FECHA";
    public static final String COL_OBSERVACIONES = "OBSERVACIONES";
    public static final String[] ALL_COLUMNS = { COL_ID, COL_ID_EQUIPO,
            COL_EMAIL_CLIENTE,COL_AREA ,COL_CONTRATO,COL_FECHA ,COL_OBSERVACIONES};

    public static ArrayList<TableDataChangeListener> listeners= new ArrayList<>();

    public static final String TABLE_NAME = "service_remotos";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_ID_EQUIPO
            + " INTEGER, " + COL_EMAIL_CLIENTE + " TEXT NOT NULL, " + COL_AREA + " TEXT NOT NULL, "  + COL_CONTRATO + " TEXT NOT NULL, "
            + COL_FECHA + " TEXT NOT NULL, " + COL_OBSERVACIONES + " TEXT NOT NULL)";

    private static ServiceRemotoTable instance = null;
    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.*/
    public static ServiceRemotoTable getInstance(Context context){
        if(instance == null){
            instance = new ServiceRemotoTable(context);
        }
        return instance;
    }

    private ServiceRemotoTable(Context context) {
        Log.i("WTrack log - ServiceRemotoTable", "constructor");
    }

    public static ArrayList<ServiceRemoto> getMatches(String query, DatabaseOpenHelper db) {

        return getMatches(query, ALL_COLUMNS, db);
    }

    public static ArrayList<ServiceRemoto> getMatches(String query, String[] columns,
                                             DatabaseOpenHelper db) {
        String selection = COL_ID + " LIKE ? ";
        String[] selectionArgs = new String[] { "%" + query + "%" };

        ArrayList<ServiceRemoto> service_remotos = new ArrayList<ServiceRemoto>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            ServiceRemoto service_remoto = new ServiceRemoto(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_ID_EQUIPO)), cursor.getString(cursor
                    .getColumnIndex(COL_EMAIL_CLIENTE)), cursor.getString(cursor
                    .getColumnIndex(COL_AREA)), cursor.getString(cursor
                    .getColumnIndex(COL_CONTRATO)), cursor.getString(cursor
                    .getColumnIndex(COL_FECHA)), cursor.getString(cursor
                    .getColumnIndex(COL_OBSERVACIONES)));
            service_remotos.add(service_remoto);
        }

        return service_remotos;
    }

    /**
     * Funcion que ejecuta cargarservice_remotos en un thread aparte.
     * */
    public void cargarService_remotosThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // cargarservice_remotos();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static long addService_remoto(ServiceRemoto service_remoto, DatabaseOpenHelper db) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, service_remoto.getId());
        initialValues.put(COL_ID_EQUIPO, service_remoto.getId_equipo());
        initialValues.put(COL_EMAIL_CLIENTE, service_remoto.getEmail_cliente());
        initialValues.put(COL_AREA, service_remoto.getArea());
        initialValues.put(COL_CONTRATO, service_remoto.getContrato());
        initialValues.put(COL_FECHA, service_remoto.getFecha());
        initialValues.put(COL_OBSERVACIONES, service_remoto.getObservaciones());
		/*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * ServiceRemoto.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { ServiceRemoto.getId() }, initialValues); else
		 */
        long resultado = db.insert(TABLE_NAME, initialValues);
        notifyListeners();
        return resultado;

    }

    public static void deleteService_remotos(DatabaseOpenHelper db) {
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

}