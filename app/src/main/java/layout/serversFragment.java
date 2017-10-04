package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.MainNavigation;
import s1080488.ikpmd_app.R;


public class serversFragment extends Fragment {
    MainNavigation mainNavigation = new MainNavigation();
    String[] json_urls = {
            "https://ark-servers.net/api/?object=servers&element=detail&key=KGj7i6Jy3iXCtHyb9SdF3fMPRCWZfPnIigG",
            "https://ark-servers.net/api/?object=servers&element=detail&key=jRWCjGTTrqOz4rpwOs6oCyVEwiuInK1E7k",
            "https://ark-servers.net/api/?object=servers&element=detail&key=4UR0m2e6nfhcoIivIRggr62aU7ZivGu2De",
            "https://ark-servers.net/api/?object=servers&element=detail&key=nby8sdVJIp7dbC92sG3ewImp2dX0AbFmJ8C"
    };
    public static TextView tvServerData1, tvServerData2, tvServerData3, tvServerData4;
    Button btnFreshServers;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnFreshServers = (Button) getView().findViewById(R.id.btnRefreshServers);
        tvServerData1 = (TextView) getView().findViewById(R.id.tvServerData1);
        tvServerData2 = (TextView) getView().findViewById(R.id.tvServerData2);
        tvServerData3 = (TextView) getView().findViewById(R.id.tvServerData3);
        tvServerData4 = (TextView) getView().findViewById(R.id.tvServerData4);

        btnFreshServers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainNavigation.loadServerData(json_urls);
                MainActivity.toastMessage(getContext(), "Retrieved server data..");
            }
        });
        MainActivity.toastMessage(getContext(), "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servers, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainNavigation.loadServerData(json_urls);
    }
}
