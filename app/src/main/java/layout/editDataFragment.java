package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import s1080488.ikpmd_app.Databases.DatabaseHelper;
import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.R;

public class editDataFragment extends Fragment {
    EditText txtDinoName;
    Button btnSaveDinoData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtDinoName = (EditText) getView().findViewById(R.id.txtNewDinoName);
        btnSaveDinoData = (Button) getView().findViewById(R.id.btnSaveNewDinoData);

        //SQLite is Thread safe by default
        final DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());

        btnSaveDinoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove spaces and check if not empty before insert
                if (!(txtDinoName.getText().toString().trim().equals(""))) {
                    try {
                        dbHelper.openDatabaseConnection();
                        dbHelper.insertToDatabase(txtDinoName.getText().toString().trim());
                        dbHelper.closeDatabaseConnection();
                        MainActivity.toastMessage(getView().getContext(), txtDinoName.getText() + " successfully added.");

                        //Clear text field
                        txtDinoName.setText("");
                    } catch (Exception e) {
                        MainActivity.toastMessage(getView().getContext(), "Data could not be added.");
                    }
                }
                else {
                    //Clear text field
                    txtDinoName.setText("");
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
