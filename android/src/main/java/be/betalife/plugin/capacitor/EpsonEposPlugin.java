package be.betalife.plugin.capacitor;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "EpsonEpos")
public class EpsonEposPlugin extends Plugin {

    private PortDiscovery portDiscovery;

    @Override
    public void load() {
        super.load();
        Context context = getContext();
        portDiscovery = new PortDiscovery(context);
    }

    @PluginMethod
    public void startDiscovery(PluginCall call) {
        long timeout = call.getLong("timeout", 10000); // 默认超时时间为10秒

        FilterOption filterOption = new FilterOption();
        filterOption.setPortType(Discovery.PORTTYPE_ALL);
        filterOption.setBroadcast(call.getString("broadcast", "255.255.255.255")); // 默认广播地址
        filterOption.setDeviceModel(Discovery.MODEL_ALL);
        filterOption.setEpsonFilter(Discovery.FILTER_NONE);

        portDiscovery.startDiscovery(filterOption, timeout, new PortDiscovery.DiscoveryCallback() {
            @Override
            public void onDiscoveryStart() {
                // 通知前端扫描开始
                JSObject result = new JSObject();
                result.put("message", "Discovery started");
                notifyListeners("discoveryStart", result);
            }

            @Override
            public void onDiscoveryStop(List<HashMap<String, String>> printerList) {
                // 通知前端扫描结束并返回发现的打印机列表
                JSObject result = new JSObject();
                JSArray printers = new JSArray();
                for (HashMap<String, String> printer : printerList) {
                    JSObject printerObj = new JSObject();
                    printerObj.put("PrinterName", printer.get("PrinterName"));
                    printerObj.put("Target", printer.get("Target"));
                    printers.put(printerObj);
                }
                result.put("printers", printers);
                call.resolve(result);
            }

            @Override
            public void onError(String message) {
                call.reject("Discovery error: " + message);
            }
        });
    }

    @PluginMethod
    public void stopDiscovery(PluginCall call) {
        portDiscovery.stopDiscovery(new PortDiscovery.DiscoveryCallback() {
            @Override
            public void onDiscoveryStart() {
                // 不需要实现
            }

            @Override
            public void onDiscoveryStop(List<HashMap<String, String>> printerList) {
                JSObject result = new JSObject();
                result.put("message", "Discovery stopped");
                call.resolve(result);
            }

            @Override
            public void onError(String message) {
                call.reject("Error stopping discovery: " + message);
            }
        });
    }
}
