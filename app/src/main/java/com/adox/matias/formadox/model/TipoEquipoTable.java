package com.adox.matias.formadox.model;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ProgressBar;


import java.util.ArrayList;

public class TipoEquipoTable {

    public static final String COL_ID = "ID";
    public static final String COL_DESCRIPCION = "DESCRIPCION";

    public static final String[] ALL_COLUMNS = { COL_ID, COL_DESCRIPCION};

    public static ArrayList<TableDataChangeListener> listeners= new ArrayList<>();

    public static final String TABLE_NAME = "tipo_equipoo";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_DESCRIPCION
            + " TEXT NOT NULL)";

    private static TipoEquipoTable instance = null;
    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.*/
    public static TipoEquipoTable getInstance(Context context){
        if(instance == null){
            instance = new TipoEquipoTable(context);
        }
        return instance;
    }

    private TipoEquipoTable(Context context) {
        Log.i("WTrack log - TipoEquipoTable", "constructor");
    }

    public static ArrayList<TipoEquipo> getMatches(String query, DatabaseOpenHelper db) {

        return findByDescription(query, ALL_COLUMNS, db);
    }

    public static ArrayList<TipoEquipo> findByDescription(String description, String[] columns,
                                                          DatabaseOpenHelper db){
        String selection = COL_DESCRIPCION + " LIKE ? ";
        String[] selectionArgs = new String[] { "%" + description + "%" };

        return getMatches(selection, selectionArgs, columns, db);
    }

    public static ArrayList<TipoEquipo> getMatches(String selection, String[] selectionArgs, String[] columns,
                                             DatabaseOpenHelper db) {


        ArrayList<TipoEquipo> tipo_equipos = new ArrayList<TipoEquipo>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            TipoEquipo tipo_equipo = new TipoEquipo(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_DESCRIPCION)));
            tipo_equipos.add(tipo_equipo);
        }

        return tipo_equipos;
    }
    public static TipoEquipo getTipoEquipo(String id,DatabaseOpenHelper db){
        String selection = COL_ID + " = ? ";
        String[] selectionArgs = new String[] {id};

        ArrayList<TipoEquipo> tipo_equipos = getMatches(selection, selectionArgs, ALL_COLUMNS, db);
        if(tipo_equipos.size()==0){
            return null;
        }else {
            TipoEquipo tipoEquipo = tipo_equipos.get(0);
            return tipoEquipo;
        }


    }

    /**
     * Funcion que ejecuta cargartipo_equipos en un thread aparte.
     * */
    public void cargarTipo_equiposThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // cargartipo_equipos();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static long addTipo_equipo(TipoEquipo tipo_e, DatabaseOpenHelper db) {
        return addTipo_equipo(tipo_e, db, true);

    }

    public static long addTipo_equipo(TipoEquipo tipo_e, DatabaseOpenHelper db, boolean notify) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_DESCRIPCION, tipo_e.getDescripcion());
        initialValues.put(COL_ID, tipo_e.getId());


		/*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * TipoEquipo.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { TipoEquipo.getId() }, initialValues); else
		 */
        long resultado = db.insert(TABLE_NAME, initialValues);
        if(notify) {
            notifyListeners();
        }
        return resultado;

    }

    public static void deleteTipo_equipos(DatabaseOpenHelper db) {
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

    public static void actualizarTabla(ArrayList<TipoEquipo> tipo_equipos,DatabaseOpenHelper db){
        TipoEquipoTable.deleteTipo_equipos(db);//Borrar para poder actualizar bien
        for (int i = 0; i < tipo_equipos.size(); i++) {//guardo en la bd todo lo que traigo
            TipoEquipo inst = tipo_equipos.get(i);
            TipoEquipoTable.addTipo_equipo(inst, db, false);
        }
        notifyListeners();

    }

}