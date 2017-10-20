package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import s1080488.ikpmd_app.MainActivity;
import s1080488.ikpmd_app.MainNavigation;
import s1080488.ikpmd_app.R;


public class serversFragment extends Fragment {
    MainNavigation mainNavigation = new MainNavigation();
    public static TextView tvServerData1, tvServerData2, tvServerData3, tvServerData4;
    Button btnFreshServers;

    public static String[] json_urls = {
        "https://ark-servers.net/api/?object=servers&element=detail&key=KGj7i6Jy3iXCtHyb9SdF3fMPRCWZfPnIigG",
        "https://ark-servers.net/api/?object=servers&element=detail&key=jRWCjGTTrqOz4rpwOs6oCyVEwiuInK1E7k",
        "https://ark-servers.net/api/?object=servers&element=detail&key=4UR0m2e6nfhcoIivIRggr62aU7ZivGu2De",
        "https://ark-servers.net/api/?object=servers&element=detail&key=nby8sdVJIp7dbC92sG3ewImp2dX0AbFmJ8C"
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnFreshServers = (Button) getView().findViewById(R.id.btnRefreshServers);
        tvServerData1 = (TextView) getView().findViewById(R.id.tvServerData1);
        tvServerData2 = (TextView) getView().findViewById(R.id.tvServerData2);
        tvServerData3 = (TextView) getView().findViewById(R.id.tvServerData3);
        tvServerData4 = (TextView) getView().findViewById(R.id.tvServerData4);

        if (MainNavigation.serversData.size() != 0) {
            mainNavigation.setServerData();
        }

        btnFreshServers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainNavigation.loadServerData(json_urls);
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
}