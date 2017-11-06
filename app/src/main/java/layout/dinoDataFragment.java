package layout;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import s1080488.ikpmd_app.Databases.DbHelperDinoData;
import s1080488.ikpmd_app.Databases.DbHelperDinoNames;
import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.R;
import s1080488.ikpmd_app.Threads.fetchDinoData;

public class dinoDataFragment extends Fragment implements View.OnClickListener, fetchDinoData.AsyncResponse {
    GridView dinoGridView;
    Button btnBackToAllDinos, btnSaveDinoDataLocally;
    public static int columns = 0;

    ArrayList<String> allDinos = new ArrayList<>();
    ArrayList<String> checkArray = new ArrayList<>();
    ArrayAdapter<String> allDinoAdapter;

    //ArrayList that will hold all dino data to be displayed in the tableLayout.
    public static ArrayList<String> dinoData;
    TableLayout dinoTableLayout;

    String chosenDino = "";
    String dataString = "";
    public static String strSeparator = "__,__";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dinoGridView = (GridView) getView().findViewById(R.id.gridDinoData);

        btnBackToAllDinos = (Button) getView().findViewById(R.id.btnBackToAllDinos);
        btnSaveDinoDataLocally = (Button) getView().findViewById(R.id.btnSaveDinoDataLocally);

        btnBackToAllDinos.setOnClickListener(this);
        btnSaveDinoDataLocally.setOnClickListener(this);

        dinoTableLayout = (TableLayout) getView().findViewById(R.id.tableDinoData);

        dinoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenDino = allDinos.get(position);
                MainActivity.toastMessage(getContext(), chosenDino);

                if (checkDinoDataExistsLocally(chosenDino)) {
                    dinoData = convertStringToArray(dataString);
                    showDinoDataOnTableLayout();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dino Data (Offline data)");
                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dino Data (Live data)");
                    loadDinoDataFromJson(chosenDino);
                }

            }
        });


        //Loads all dino's into dataGrid.
        reloadDinoData();
    }

    //Start thread to load chosen dino data.
    public void loadDinoDataFromJson(final String dinoName) {
        fetchDinoData process = new fetchDinoData(dinoName, this);
        process.execute();
    }

    //This overrides the implemented method from AsyncResponse.
    @Override
    public void processFinished() {
        //This code is run when the asyncTask completes.
        showDinoDataOnTableLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToAllDinos:
                MainActivity.toastMessage(getContext(), "Displaying all available dino's.");

                dinoTableLayout.setVisibility(View.INVISIBLE);
                dinoGridView.setVisibility(View.VISIBLE);
                btnSaveDinoDataLocally.setVisibility(View.INVISIBLE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_database_info);

                reloadDinoData();
                break;

            case R.id.btnSaveDinoDataLocally:
                MainActivity.toastMessage(getContext(), "Attempting to save data locally.");

                if (!checkDinoDataExistsLocally(chosenDino)) {
                    saveDinoDataLocally(chosenDino, columns);
                } else
                    MainActivity.toastMessage(this.getContext(), chosenDino + " data already exists.");
                break;

            default:
                break;
        }
    }

    public void saveDinoDataLocally(String dinoName, int columns) {
        final DbHelperDinoData dbHelper = new DbHelperDinoData(this.getContext());
        String dataString = convertArrayToString(allDinos);
        dbHelper.openDatabaseConnection();

        dbHelper.insertToDatabase(dinoName, dataString, columns);

        dbHelper.closeDatabaseConnection();
    }

    public boolean checkDinoDataExistsLocally(String dinoType) {
        boolean exists = false;
        final DbHelperDinoData dbHelper = new DbHelperDinoData(this.getContext());

        dbHelper.openDatabaseConnection();
        Cursor dinoCursor = dbHelper.retrieveFromDatabase("dd_dino_data");

        dinoCursor.moveToFirst();
        while (dinoCursor.moveToNext()) {
            if (dinoCursor.getString(dinoCursor.getColumnIndex("name")).equals(dinoType)) {
                dataString = dinoCursor.getString(dinoCursor.getColumnIndex("data_string"));
                columns = dinoCursor.getInt(dinoCursor.getColumnIndex("columns_required"));
                exists = true;
            } else {
                exists = false;
            }
        }

        dbHelper.closeDatabaseConnection();

        return exists;
    }

    public static String convertArrayToString(ArrayList<String> array) {
        String str = "";
        for (int i = 0; i < array.size(); i++) {
            str = str + array.get(i);
            // Do not append comma at the end of last element
            if (i < array.size() - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static ArrayList<String> convertStringToArray(String str) {
        ArrayList<String> savedDataList = new ArrayList<>(Arrays.asList(str.split(strSeparator)));

        return savedDataList;
    }


    public void showDinoDataOnTableLayout() {
        //Show save button
        btnSaveDinoDataLocally.setVisibility(View.VISIBLE);

        int columnCounter = 0;
        TableRow row = new TableRow(this.getContext());
        allDinos = new ArrayList<>();
        allDinos = dinoData;

        showNewDataOnGrid();

        for (int i = 0; i < allDinos.size(); i++) {
            if (columnCounter == columns) {
                dinoTableLayout.addView(row);
                row = new TableRow(this.getContext());
                columnCounter = 0;
            }

            String dino = allDinos.get(i);
            TextView dinoStat = new TextView(this.getContext());
            dinoStat.setText(dino);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(15, 10, 10, 15);

            row.addView(dinoStat, params);
            columnCounter++;
        }

        dinoTableLayout.setVisibility(View.VISIBLE);
        dinoGridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dino_data, container, false);
    }

    public void reloadDinoData() {
        allDinos = new ArrayList<>();
        columns = 3;
        final DbHelperDinoNames dbHelper = new DbHelperDinoNames(this.getContext());

        //Open Database Connection
        dbHelper.openDatabaseConnection();

        Cursor dinoCursor = dbHelper.retrieveFromDatabase("dd_dinos_wild");
        while (dinoCursor.moveToNext()) {
            //String id = dinoCursor.getString(0);
            String name = dinoCursor.getString(1);
            //allDinos.add(id);
            allDinos.add(name);
            //MainActivity.toastMessage(this.getContext(), "Found dino: " + name);
        }

        //Close Database Connection
        dbHelper.closeDatabaseConnection();

        showNewDataOnGrid();
    }

    public void showNewDataOnGrid() {
        //Set proper amount of columns.
        dinoGridView.setNumColumns(columns);

        //Create a new ArrayAdapter.
        allDinoAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, allDinos);

        //Data bind GridView with ArrayAdapter.
        dinoGridView.setAdapter(allDinoAdapter);

        //Clear all tableData before showing new data.
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dinoTableLayout.removeAllViews();
            }
        });
    }
}
