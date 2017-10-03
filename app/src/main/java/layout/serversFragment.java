package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.R;
import s1080488.ikpmd_app.Threads.fetchServerData;

public class serversFragment extends Fragment {
    Button btnFreshServers;
    public static TextView tvServerData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnFreshServers = (Button) getView().findViewById(R.id.btnRefreshServers);
        tvServerData = (TextView) getView().findViewById(R.id.tvServerData);


        btnFreshServers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadServerData();
                MainActivity.toastMessage(getContext(), "Retrieved server data..");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servers, container, false);
    }

    public void loadServerData(){
        fetchServerData process = new fetchServerData();
        process.execute();
    }
}
