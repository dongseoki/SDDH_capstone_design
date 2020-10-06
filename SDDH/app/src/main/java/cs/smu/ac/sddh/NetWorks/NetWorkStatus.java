package cs.smu.ac.sddh.NetWorks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetWorkStatus {
    public static final int WIFI=1;
    public static final int MOBILE=2;
    public static final int NOT_CONNECTED=3;
    public static final int Capable=4;
    public static int getConnectivityStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());

        if(networkCapabilities != null){
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                return MOBILE;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                return WIFI;
            }

        }
        return NOT_CONNECTED;

    }
}
