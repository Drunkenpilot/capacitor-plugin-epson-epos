package be.betalife.plugin.capacitor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.FilterOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortDiscovery {
    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isDiscovering = false;
    private final List<HashMap<String, String>> discoveredPrinters = new ArrayList<>();

    public interface DiscoveryCallback {
        void onDiscoveryStart();

        void onDiscoveryStop(List<HashMap<String, String>> printerList);

        void onError(String message);
    }

    public PortDiscovery(Context context) {
        this.context = context;
    }

    public void startDiscovery(FilterOption filterOption, long timeoutMillis, DiscoveryCallback callback) {
        if (isDiscovering) {
            callback.onError("Discovery already in progress");
            return;
        }

        discoveredPrinters.clear();

        try {
            Discovery.start(context, filterOption, deviceInfo -> {
                HashMap<String, String> printer = new HashMap<>();
                printer.put("PrinterName", deviceInfo.getDeviceName());
                printer.put("Target", deviceInfo.getTarget());
                discoveredPrinters.add(printer);
            });

            isDiscovering = true;
            callback.onDiscoveryStart();

            // Set timeout to automatically stop discovery
            handler.postDelayed(() -> stopDiscovery(callback), timeoutMillis);
        } catch (Epos2Exception e) {
            callback.onError("Failed to start discovery: " + e.getErrorStatus());
        }
    }

    public void stopDiscovery(DiscoveryCallback callback) {
        if (!isDiscovering) {
            callback.onError("Discovery is not running");
            return;
        }

        try {
            Discovery.stop();
            isDiscovering = false;
            callback.onDiscoveryStop(discoveredPrinters);
        } catch (Epos2Exception e) {
            callback.onError("Failed to stop discovery: " + e.getErrorStatus());
        }
    }


}
