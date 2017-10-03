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
import s1080488.ikpmd_app.MainNavigation;
import s1080488.ikpmd_app.R;

public class dinoDataFragment extends Fragment {
    GridView dinoGridView;
    Button btnEditSelected;

    ArrayList<String> allDinos = new ArrayList<>();
    ArrayAdapter<String> allDinoAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dinoGridView = (GridView) getView().findViewById(R.id.gridDinoData);
        btnEditSelected = (Button) getView().findViewById(R.id.btnEditSelectedData);

        //Loads all dino's into dataGrid.
        reloadDinoData();

        dinoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.toastMessage(getContext(), allDinos.get(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dino_data, container, false);
    }

    public void reloadDinoData() {
        allDinos.clear();
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

        // Create a new ArrayAdapter
        allDinoAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, allDinos);

        // Data bind GridView with ArrayAdapter
        dinoGridView.setAdapter(allDinoAdapter);

    }
}
