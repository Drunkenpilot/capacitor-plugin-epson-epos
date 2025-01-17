package be.betalife.plugin.capacitor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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


@CapacitorPlugin(name = "EpsonEpos")
public class EpsonEposPlugin extends Plugin {
    private Activity activity; // 保存 Activity 实例
    private static final String TAG = "EpsonEposPlugin";

    private PortDiscovery portDiscovery;
    private PrinterManager printerManager;


    @Override
    public void load() {
        super.load();
        Context context = getContext();
        portDiscovery = new PortDiscovery(context);
        activity = getActivity();
        printerManager = new PrinterManager(activity);
    }

    @PluginMethod
    public void requestPermission(PluginCall call) {
        try {
            printerManager.requestRuntimePermission();
            call.resolve(new JSObject().put("success", true));
        } catch (Exception e) {
            call.reject("Failed to request permissions: " + e.getMessage());
        }
    }

    @PluginMethod
    public void startDiscovery(PluginCall call) {
        Log.d(TAG, "startDiscovery called");

        Long timeoutValue = call.getLong("timeout"); // 获取可能为 null 的值
        long timeout = (timeoutValue != null) ? timeoutValue : 10000L; // 如果为 null，使用默认值, 默认超时时间为10秒

        String portType = call.getString("portType", "ALL"); // 默认端口类型为 "ALL"
        FilterOption filterOption = new FilterOption();

        filterOption.setPortType(PrinterUtils.parsePrinterPortType(portType));
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

    @PluginMethod
    public void print(PluginCall call) {
        String target = call.getString("target");
        String printerModelCode = call.getString("modelCode");
        String langCode = call.getString("langCode", "ANK"); // 默认端口类型为 "ANK"
        JSArray jsArray = call.getArray("instructions"); // 获取 JSArray

        List<HashMap<String, Object>> commands;
        try {
            commands = PrinterUtils.parseInstructions(jsArray);
        } catch (Exception e) {
            call.reject("Failed to parse instructions: " + e.getMessage());
            return;
        }

        int lang = PrinterUtils.parsePrinterLang(langCode);
        int printerSeries = PrinterUtils.parsePrinterSeries(printerModelCode);
        // 初始化打印机
        if (!printerManager.initializePrinter(printerSeries, lang, getContext())) {
            call.reject("Failed to initialize printer");
            return;
        }

        // 连接打印机
        if (!printerManager.connectPrinter(target, getContext())) {
            call.reject("Failed to connect to printer");
            return;
        }

        // 执行打印逻辑
        boolean success = printerManager.executePrintCommands(commands, getContext());
        if (!success) {
            call.reject("Failed to execute print commands");
            return;
        }

        // 打印数据
        printerManager.printData(new PrinterManager.PrinterCallback() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(() -> call.resolve(new JSObject().put("success", true)));
            }

            @Override
            public void onError(String message) {
                printerManager.finalizePrinter();
                call.reject("Printing failed: " + message);
            }
        });

    }

    @PluginMethod
    public void finalizePrinter(PluginCall call) {
        if (printerManager != null) {
            printerManager.finalizePrinter();
            call.resolve(new JSObject().put("message", "Printer finalized successfully"));
        } else {
            call.reject("PrinterManager is not initialized");
        }
    }

}
