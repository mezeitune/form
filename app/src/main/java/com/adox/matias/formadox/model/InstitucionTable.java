package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.GpsStatus;
import android.util.Log;


import java.util.ArrayList;

public class InstitucionTable {

    public static final String COL_ID = "ID";
    public static final String COL_NOMBRE = "NOMBRE";
    public static final String COL_DESCRIPCION = "DESCRIPCION";
    public static final String COL_TELEFONO = "TELEFONO";
    public static final String COL_PROVINCIA = "PROVINCIA";
    public static final String COL_LOCALIDAD = "LOCALIDAD";
    public static final String[] ALL_COLUMNS = {COL_ID, COL_NOMBRE,
            COL_DESCRIPCION, COL_TELEFONO, COL_PROVINCIA, COL_LOCALIDAD};

    public static ArrayList<TableDataChangeListener> listeners = new ArrayList<>();

    public static final String TABLE_NAME = "instituciones";

    public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_NOMBRE
            + " TEXT NOT NULL, " + COL_DESCRIPCION + " TEXT NOT NULL," + COL_TELEFONO + " TEXT NOT NULL, " + COL_PROVINCIA + " TEXT NOT NULL," + COL_LOCALIDAD + " TEXT NOT NULL)";

    private static InstitucionTable instance = null;

    /**
     * Obtiene la instancia unica de la base de datos.
     * Si no existe la crea.
     */
    public static InstitucionTable getInstance(Context context) {
        if (instance == null) {
            instance = new InstitucionTable(context);
        }
        return instance;
    }

    private InstitucionTable(Context context) {
        Log.i("WTrack log - InstitucionTable", "constructor");
    }

    public static ArrayList<Institucion> getMatches(String query, DatabaseOpenHelper db) {

        return getMatches(query, ALL_COLUMNS, db);
    }

    public static ArrayList<Institucion> getMatches(String query, String[] columns,
                                                    DatabaseOpenHelper db) {
        String selection = COL_NOMBRE + " LIKE ? ";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        ArrayList<Institucion> instituciones = new ArrayList<Institucion>();

        Cursor cursor = db.query(TABLE_NAME, selection, selectionArgs, columns);

        while (cursor != null && cursor.moveToNext()) {
            Institucion Institucion = new Institucion(cursor.getString(cursor
                    .getColumnIndex(COL_ID)), cursor.getString(cursor
                    .getColumnIndex(COL_NOMBRE)), cursor.getString(cursor
                    .getColumnIndex(COL_DESCRIPCION)), cursor.getString(cursor
                    .getColumnIndex(COL_TELEFONO)), cursor.getString(cursor
                    .getColumnIndex(COL_PROVINCIA)), cursor.getString(cursor
                    .getColumnIndex(COL_LOCALIDAD)));
            instituciones.add(Institucion);
        }

        return instituciones;
    }

    /**
     * Funcion que ejecuta cargarinstituciones en un thread aparte.
     */
    public void cargarInstitucionesThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // cargarinstituciones();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static long addInstitucion(Institucion Institucion, DatabaseOpenHelper db) {
        return addInstitucion(Institucion, db, true);
    }

    /**
     * Metodo para agregar institucion a la tabla
     * @param Institucion la institucion a agregar
     * @param db conexion con la BD
     * @param notify tiene que notificar a los listeners o no.
     * @return RowID del registro insertado
     */
    public static long addInstitucion(Institucion Institucion, DatabaseOpenHelper db, boolean notify) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, Institucion.getId());
        initialValues.put(COL_NOMBRE, Institucion.getNombre());
        initialValues.put(COL_DESCRIPCION, Institucion.getDescripcion());
        initialValues.put(COL_TELEFONO, Institucion.getTelefono());
        initialValues.put(COL_PROVINCIA, Institucion.getProvincia());
        initialValues.put(COL_LOCALIDAD, Institucion.getLocalidad());
        /*
		 * Cursor c = db.query(TABLE_NAME, COL_ID + " = ? ", new String[] {
		 * Institucion.getId() }, ALL_COLUMNS);
		 * 
		 * if (c != null && c.moveToFirst()) return db.update(TABLE_NAME, COL_ID
		 * + " = ? ", new String[] { Institucion.getId() }, initialValues); else
		 */
        long resultado = db.insert(TABLE_NAME, initialValues);
        if(notify)
            notifyListeners();
        return resultado;

    }

    public static void deleteInstituciones(DatabaseOpenHelper db) {
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

    public static void actualizarTabla(ArrayList<Institucion> instituciones, DatabaseOpenHelper db) {
        InstitucionTable.deleteInstituciones(db);//Borrar para poder actualizar bien
        for (int i = 0; i < instituciones.size(); i++) {//guardo en la bd todo lo que traigo
            Institucion inst = instituciones.get(i);
            InstitucionTable.addInstitucion(inst, db, false);
        }
        notifyListeners();
    }
}