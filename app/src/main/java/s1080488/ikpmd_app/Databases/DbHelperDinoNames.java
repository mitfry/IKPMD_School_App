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

//TO-DO:
//Create one dataBase helper that can handle different requests.
//Move database properties below to a separate databaseInfo file for better scalability.

public class DbHelperDinoNames extends SQLiteOpenHelper {
    SQLiteDatabase ddDatabase;

    //Database properties
    private static final String dbName = "DinoDeluxe.db";
    private static int dbVersion = 15;

    //Table / column properties
    private static final String dbTable = "dd_dinos_wild";
    private static final String dbUID = "_id";
    private static final String dbColName = "name";
    private static final String dbColString = "data_string";
    private static final String dbColColumns = "columns_required";

    //SQL queries
    private static final String dbCreateTableDinoData = "CREATE TABLE 'dd_dino_data' (" + dbUID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + dbColName + " VARCHAR(255), " + dbColString + " VARCHAR(255), " + dbColColumns + " INT(5));";
    private static final String dbCreateTableNames = "CREATE TABLE 'dd_dinos_wild' (" + dbUID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + dbColName + " VARCHAR(255));";
    private static final String dbDropTable = "DROP TABLE IF EXISTS 'dd_dino_data';";
    private static final String dbDropTable2 = "DROP TABLE IF EXISTS 'dd_dinos_wild';";

    private Context context;

    public DbHelperDinoNames(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        try {
            db.execSQL(dbCreateTableNames);
            db.execSQL(dbCreateTableDinoData);
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
            db.execSQL(dbDropTable2);
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
        } catch (Exception e) {
            MainActivity.toastMessage(context, "Database connection failed.");
        }
    }

    public void closeDatabaseConnection(){
        //Close database connection
        this.close();
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
