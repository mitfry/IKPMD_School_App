package layout;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import s1080488.ikpmd_app.Databases.DatabaseHelper;
import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.R;
import s1080488.ikpmd_app.Threads.fetchDinoData;

public class dinoDataFragment extends Fragment implements View.OnClickListener, fetchDinoData.AsyncResponse {
    GridView dinoGridView;
    Button btnBackToAllDinos, btnSaveDinoDataLocally;
    public static int columns = 0;

    ArrayList<String> allDinos = new ArrayList<>();
    ArrayAdapter<String> allDinoAdapter;

    public static ArrayList<String> dinoData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dinoGridView = (GridView) getView().findViewById(R.id.gridDinoData);

        btnBackToAllDinos = (Button) getView().findViewById(R.id.btnBackToAllDinos);
        btnSaveDinoDataLocally = (Button) getView().findViewById(R.id.btnSaveDinoDataLocally);

        btnBackToAllDinos.setOnClickListener(this);
        btnSaveDinoDataLocally.setOnClickListener(this);


        dinoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.toastMessage(getContext(), allDinos.get(position));

                //showDinoDataOnGrid(allDinos.get(position));
                loadDinoDataFromJson(allDinos.get(position));


            }
        });


        //Loads all dino's into dataGrid.
        reloadDinoData();
    }

    //Start thread to load chosen dino data
    public void loadDinoDataFromJson(String dinoName){
        fetchDinoData process = new fetchDinoData(dinoName, this);
        process.execute();
    }

    //This overrides the implemented method from AsyncResponse
    @Override
    public void processFinished() {
        //This code is run when the asyncTask completes
        showDinoDataOnGrid();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToAllDinos:
                MainActivity.toastMessage(getContext(), "Back button");

                reloadDinoData();
                break;

            case R.id.btnSaveDinoDataLocally:
                MainActivity.toastMessage(getContext(), "Save button");
                break;

            default:
                break;
        }
    }


    public void showDinoDataOnGrid() {
        allDinos.clear();
        allDinos = dinoData;


        showNewDataOnGrid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dino_data, container, false);
    }

    public void reloadDinoData() {
        allDinos.clear();
        columns = 2;
        final DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());

        //Open Database Connection
        dbHelper.openDatabaseConnection();

        Cursor dinoCursor = dbHelper.retrieveFromDatabase("dd_dinos_wild");
        while (dinoCursor.moveToNext()) {
            String id = dinoCursor.getString(0);
            String name = dinoCursor.getString(1);
            allDinos.add(id);
            allDinos.add(name);
            //MainActivity.toastMessage(this.getContext(), "Found dino: " + name);
        }

        //Close Database Connection
        dbHelper.closeDatabaseConnection();

        showNewDataOnGrid();
    }

    public void showNewDataOnGrid() {
        //set proper amount of columns
        dinoGridView.setNumColumns(columns);

        // Create a new ArrayAdapter
        allDinoAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, allDinos);

        // Data bind GridView with ArrayAdapter
        dinoGridView.setAdapter(allDinoAdapter);
    }
}
