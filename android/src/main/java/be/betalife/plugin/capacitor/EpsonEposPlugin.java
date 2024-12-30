package be.betalife.plugin.capacitor;

import android.content.Context;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.HashMap;
import java.util.List;

import com.epson.epos2.discovery.FilterOption;
import com.epson.epos2.discovery.Discovery;

import be.betalife.plugin.capacitor.PortDiscovery;

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
        long timeout = call.getLong("timeout", 10000L); // 默认超时时间为10秒
        String portType = call.getString("portType", "ALL"); // 默认端口类型为 "ALL"
        FilterOption filterOption = new FilterOption();

        // 根据传入的 portType 设置端口类型
        switch (portType.toUpperCase()) {
            case "TCP":
                filterOption.setPortType(Discovery.PORTTYPE_TCP);
                break;
            case "BLUETOOTH":
                filterOption.setPortType(Discovery.PORTTYPE_BLUETOOTH);
                break;
            case "USB":
                filterOption.setPortType(Discovery.PORTTYPE_USB);
                break;
            default:
                filterOption.setPortType(Discovery.PORTTYPE_ALL); // 默认设置为 ALL
                break;
        }

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
