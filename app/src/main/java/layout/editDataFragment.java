package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import s1080488.ikpmd_app.Databases.DbHelperDinoNames;
import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.R;

public class editDataFragment extends Fragment {
    Button btnSaveDinoData;
    public static ArrayList<String> availableDinoTypes = new ArrayList<>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final AutoCompleteTextView actvDinoNames = (AutoCompleteTextView) getView().findViewById(R.id.txtNewDinoName);
        btnSaveDinoData = (Button) getView().findViewById(R.id.btnSaveNewDinoData);

        //SQLite is Thread safe by default
        final DbHelperDinoNames dbHelper = new DbHelperDinoNames(this.getContext());


        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this.getContext(), android.R.layout.select_dialog_item, availableDinoTypes);

        //Getting the instance of AutoCompleteTextView
        actvDinoNames.setThreshold(1);//will start working from first character
        actvDinoNames.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        btnSaveDinoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove spaces and check if not empty before insert
                if (!(actvDinoNames.getText().toString().trim().equals(""))) {
                    try {
                        dbHelper.openDatabaseConnection();
                        dbHelper.insertToDatabase(actvDinoNames.getText().toString().trim());
                        dbHelper.closeDatabaseConnection();
                        MainActivity.toastMessage(getView().getContext(), actvDinoNames.getText() + " successfully added.");

                        //Clear text field
                        actvDinoNames.setText("");
                    } catch (Exception e) {
                        MainActivity.toastMessage(getView().getContext(), "Data could not be added.");
                    }
                } else {
                    //Clear text field
                    actvDinoNames.setText("");
                    MainActivity.toastMessage(getView().getContext(), "Name field is empty.");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_data, container, false);
    }
}
