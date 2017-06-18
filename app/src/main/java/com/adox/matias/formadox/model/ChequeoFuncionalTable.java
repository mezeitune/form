package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;

public class ChequeoFuncionalTable {

    public static final String COL_ID = "ID";
    public static final String COL_DESCRIPCION = "DESCRIPCION";
    public static final String COL_TIPO_EQUIPO = "TIPO_EQUIPO";
    public static final String[] ALL_COLUMNS = {COL_ID, COL_DESCRIPCION,
            COL_TIPO_EQUIPO};

    public static ArrayList<TableDataChangeListener> listeners = new ArrayList<>();

    public static final String TABLE_NAME = "TIPO_EQUIPO";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_DESCRIPCION
            + " TEXT NOT NULL, " + COL_TIPO_EQUIPO + " TEXT NOT NULL)";

    private static ChequeoFuncionalTable instance = null;

    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.
     */
    public static ChequeoFuncionalTable getInstance(Context context) {
        if (instance == null) {
            instance = new ChequeoFuncionalTable(context);
        }
        return instance;
    }

    private ChequeoFuncionalTable(Context context) {
        Log.i("WTrack log - ChequeoFuncionalTable", "constructor");
    }

    public static ArrayList<ChequeoFuncional> getMatches(String query, DatabaseOpenHelper db) {

        return getMatches(query, ALL_COLUMNS, db);
    }

    public static ArrayList<ChequeoFuncional> getMatches(String query, String[] columns,
                                                         DatabaseOpenHelper db) {
        String selection = COL_DESCRIPCION + " LIKE ? ";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        ArrayList<ChequeoFuncional> chequeo_funcionales = new ArrayList<ChequeoFuncional>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            ChequeoFuncional chequeo_funcional = new ChequeoFuncional(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_DESCRIPCION)), cursor.getString(cursor
                    .getColumnIndex(COL_TIPO_EQUIPO)));
            chequeo_funcionales.add(chequeo_funcional);
        }

        return chequeo_funcionales;
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

    public static long addChequeo_funcional(ChequeoFuncional chequeo_funcional, DatabaseOpenHelper db) {
        return addChequeo_funcional(chequeo_funcional, db, true);
    }


    public static long addChequeo_funcional(ChequeoFuncional chequeo_funcional,
                                            DatabaseOpenHelper db, boolean notify) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, chequeo_funcional.getId());
        initialValues.put(COL_DESCRIPCION, chequeo_funcional.getDescripcion());
        initialValues.put(COL_TIPO_EQUIPO, chequeo_funcional.getTipo_equipo());
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


    public static void actualizarTabla(ArrayList<ChequeoFuncional> chequeo_funcionales, DatabaseOpenHelper db) {
        ChequeoFuncionalTable.deleteChequeo_funcional(db);//Borrar para poder actualizar bien
        for (int i = 0; i < chequeo_funcionales.size(); i++) {//guardo en la bd todo lo que traigo
            ChequeoFuncional inst = chequeo_funcionales.get(i);
            ChequeoFuncionalTable.addChequeo_funcional(inst, db, false);
        }
        notifyListeners();
    }

}