package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;

public class RepuestoTable {

    public static final String COL_ID = "ID";
    public static final String COL_NUMERO_SERIE = "NUMERO_SERIE";
    public static final String COL_TIPO_ID = "TIPO_ID";
    public static final String COL_INSTITUCION_ID = "INSTITUCION_ID";
    public static final String COL_ENVIADO = "ENVIADO";
    public static final String[] ALL_COLUMNS = {COL_ID, COL_NUMERO_SERIE,
            COL_TIPO_ID, COL_INSTITUCION_ID, COL_ENVIADO};

    public static ArrayList<TableDataChangeListener> listeners = new ArrayList<>();

    public static final String TABLE_NAME = "Equipos";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_NUMERO_SERIE + " TEXT NOT NULL, "
            + COL_TIPO_ID + " INTEGER , " + COL_INSTITUCION_ID + " INTEGER, " + COL_ENVIADO + " TEXT NOT NULL )";

    private static RepuestoTable instance = null;

    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.
     */
    public static RepuestoTable getInstance(Context context) {
        if (instance == null) {
            instance = new RepuestoTable(context);
        }
        return instance;
    }

    private RepuestoTable(Context context) {
        Log.i("WTrack log - RepuestoTable", "constructor");
    }

    public static ArrayList<Repuesto> getMatches(String query, DatabaseOpenHelper db) {

        return getMatches(query, ALL_COLUMNS, db);
    }

    public static ArrayList<Repuesto> getMatches(String query, String[] columns,
                                               DatabaseOpenHelper db) {
        String selection = COL_NUMERO_SERIE + " LIKE ? ";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        ArrayList<Repuesto> equipos = new ArrayList<Repuesto>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            Repuesto Equipo = new Repuesto(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_NUMERO_SERIE)), cursor.getString(cursor
                    .getColumnIndex(COL_TIPO_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_INSTITUCION_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_ENVIADO)));
            equipos.add(Equipo);
        }

        return equipos;
    }

    /**
     * Funcion que ejecuta cargarEquipos en un thread aparte.
     */
    public void cargarEquiposThread() {
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

    private static long addEquipo(Repuesto eq, DatabaseOpenHelper db) {
        return addEquipo(eq, db, true);
    }

    public static long addEquipo(Repuesto equipo, DatabaseOpenHelper db, boolean notify) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, equipo.getId());
        initialValues.put(COL_NUMERO_SERIE, equipo.getNumero_serie());
        initialValues.put(COL_TIPO_ID, equipo.getTipo_id());
        initialValues.put(COL_INSTITUCION_ID, equipo.getInstitucion_id());
        initialValues.put(COL_ENVIADO, equipo.getEnviado());
        /*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * Repuesto.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { Repuesto.getId() }, initialValues); else
		 */
        long resultado = db.insert(TABLE_NAME, initialValues);
        if (notify) {
            notifyListeners();
        }
        return resultado;

    }

    public static void deleteEquipos(DatabaseOpenHelper db) {
        db.delete(TABLE_NAME);
    }

    public static void addListener(TableDataChangeListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(TableDataChangeListener listener) {
        listeners.remove(listener);
    }

    public static void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDataChange();
        }
    }

    public static void actualizarTabla(ArrayList<Repuesto> equipos, DatabaseOpenHelper db) {
        RepuestoTable.deleteEquipos(db);//Borrar para poder actualizar bien
        for (int i = 0; i < equipos.size(); i++) {//guardo en la bd todo lo que traigo
            Repuesto eq = equipos.get(i);
            RepuestoTable.addEquipo(eq, db, false);
        }
        notifyListeners();
    }


}