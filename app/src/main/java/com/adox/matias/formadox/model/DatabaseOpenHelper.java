package com.adox.matias.formadox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Booooooooooooks.db";
	private static final int DATABASE_VERSION = 2;
	
//	private final Context mHelperContext;
	private SQLiteDatabase database;

	private static DatabaseOpenHelper instance = null;
	
	/**
	 * Obtiene la instancia unica de la base de datos.
	 * Si no existe la crea.*/
	public static DatabaseOpenHelper getInstance(Context context){
		if(instance == null){
			instance = new DatabaseOpenHelper(context);
		}
		return instance;
	}

	/**
	 * Constructor privado.*/
	private DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		Log.i("WTrack log - db", "constructor");
//		mHelperContext = context;
		database = getReadableDatabase();
	}

	/**
	 * Solamente se ejecuta la primera vez, o cuando 
	 * se actualiza la base de datos .*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("WTrack log - db", "oncreate");
		database = db;
		database.execSQL(ChequeoFuncionalTable.CREATE_SCRIPT);
		database.execSQL(RepuestoTable.CREATE_SCRIPT);
		database.execSQL(InstitucionTable.CREATE_SCRIPT);
		database.execSQL(ServiceRemotoTable.CREATE_SCRIPT);
		database.execSQL(TipoEquipoTable.CREATE_SCRIPT);
		database.execSQL(ClienteTable.CREATE_SCRIPT);
		database.execSQL(RepuestosClienteTable.CREATE_SCRIPT);
		/*database.execSQL(ContactosTable.CREATE_SCRIPT);
		database.execSQL(ContactosPuntoItinerarioTable.CREATE_SCRIPT);*/
	}


	/**
	 * Solamente se ejecuta cuando se actualiza 
	 * la base de datos .*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("WTrack log - db", "onUpgrade");
		// db.execSQL("DROP TABLE IF EXISTS " + BookTable.TABLE_NAME + "," + ChequeoFuncionalTable.TABLE_NAME + "," + RepuestoTable.TABLE_NAME + "," + InstitucionTable.TABLE_NAME + "," + RepuestosBasicosTable.TABLE_NAME + "," + RepuestosBasicosUsadosTable.TABLE_NAME + "," + ServiceRemotoTable.TABLE_NAME + "," + TipoEquipoTable.TABLE_NAME+ "," + ClienteTable.TABLE_NAME);
		/*db.execSQL("DROP TABLE IF EXISTS " + ContactosTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ContactosPuntoItinerarioTable.TABLE_NAME);*/
		onCreate(db);
	}

	/**
	 * Borra todas las filas de una tabla .*/
	public void delete(String tableName) {
		Log.i("WTrack log - db", "delete");
		this.database.delete(tableName, null, null);
	}

	/**
	 * Inserta un registro en una tabla .*/
	public long insert(String tableName,
			ContentValues initialValues) {
		Log.i("WTrack log - db", "insert " + tableName);
		long result = this.database.insert(tableName, null, initialValues);
		return result;
	}

	/**
	 * Inserta un registro en una tabla .*/
	public long update(String tableName,
			String selection, String[] selectionArgs,
			ContentValues initialValues) {
		Log.i("WTrack log - db", "update " + tableName + " - " + selection);
		this.database.update(tableName, initialValues, selection, selectionArgs);
		return 0;
	}

	/**
	 * Realiza una consulta y devuelve un cursor con los resultados.*/
	public Cursor query(String tables, String selection, String[] selectionArgs,
			String[] columns) {
		Log.i("WTrack log - db", "query " + tables + " - " + selection);
		Log.i("WTrack log - db", "args " + selectionArgs[0]);
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(tables);
		database = getReadableDatabase();

		Cursor cursor = builder.query(database, columns, selection,
				selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		}

		Log.i("WTrack log - db", "count " + cursor.getCount());
//		 else if (!cursor.moveToFirst()) {
//			cursor.close();
//			return null;
//		}
		return cursor;
	}

	public void execUpdate(String consulta){
		database.execSQL(consulta);
		Log.i("WTrack log - db", "updateando todoooooooooooooooooooooooooooooooooooooooooooooooooooooo");
	}

	public void updateContact(String id, String enviado) {
		ContentValues args = new ContentValues();
		args.put("ENVIADO", enviado);

		database.update("CLIENTES", args, "ID" + "=" + id, null) ;
	}


}