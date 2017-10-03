package s1080488.ikpmd_app.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import s1080488.ikpmd_app.MainActivity;

/**
 * Created by Mitchell on 22-9-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase ddDatabase;

    //Database properties
    private static final String dbName = "DinoDeluxe.db";
    private static int dbVersion = 1;

    //Table / column properties
    private static final String dbTable = "dd_dinos_wild";
    private static final String dbUID = "_id";
    private static final String dbColName = "name";

    //SQL queries
    private static final String dbCreateTable = "CREATE TABLE " + dbTable + " (" + dbUID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + dbColName + " VARCHAR(255));";
    private static final String dbDropTable = "DROP TABLE IF EXIST" + dbTable + ";";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        MainActivity.toastMessage(context, "Constructor called.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        try {
            db.execSQL(dbCreateTable);
            MainActivity.toastMessage(context, "dbCreateTable successfully executed.");
        } catch (SQLException e) {
            MainActivity.toastMessage(context, "dbCreateTable executed, but failed." + e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables
        try {
            db.execSQL(dbDropTable);
            onCreate(db);
            MainActivity.toastMessage(context, "dbDropTable successfully executed.");
        } catch (SQLException e) {
            MainActivity.toastMessage(context, "dbDropTable executed, but failed." + e);
        }
    }

    public void openDatabaseConnection() {
        //Open Database connection
        try {
            ddDatabase = this.getWritableDatabase();
            MainActivity.toastMessage(context, "Database connection succeeded.");
        } catch (Exception e) {
            MainActivity.toastMessage(context, "Database connection failed.");
        }
    }

    public void closeDatabaseConnection(){
        //Close database connection
        this.close();
        MainActivity.toastMessage(context, "Database connection closed.");
    }

    public void insertToDatabase(String name) {
        //Execute insert query
        try {
            ContentValues cV = new ContentValues();
            cV.put(dbColName, name);

            ddDatabase.insert(dbTable, dbUID, cV);
            MainActivity.toastMessage(context, "Inserting data succeeded.");
        } catch (Exception e) {
            MainActivity.toastMessage(context, "Inserting data failed.");
        }
    }

    public Cursor retrieveFromDatabase(String table) {
        String[] columns = {dbUID, dbColName};
        return ddDatabase.query(table, columns, null, null, null, null, null);
    }
}
