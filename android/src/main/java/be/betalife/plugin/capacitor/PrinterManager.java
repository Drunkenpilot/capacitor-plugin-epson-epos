package be.betalife.plugin.capacitor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrinterManager implements ReceiveListener {

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final Context context;

    private final Activity activity;

    private static final int REQUEST_PERMISSION = 100;

    public PrinterManager(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    private Printer mPrinter = null;
    private static final int DISCONNECT_INTERVAL = 500;// milliseconds

    public interface PrinterCallback {
        void onSuccess();

        void onError(String message);
    }

    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        List<String> requestPermissions = new ArrayList<>();

        if (Build.VERSION_CODES.S <= Build.VERSION.SDK_INT) {
            // Android 12 及以上需要 BLUETOOTH_SCAN 和 BLUETOOTH_CONNECT 权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else if (Build.VERSION_CODES.Q <= Build.VERSION.SDK_INT) {
            // Android 10-11 需要 ACCESS_FINE_LOCATION 权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            // Android 9 及以下需要 ACCESS_COARSE_LOCATION 权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    requestPermissions.toArray(new String[0]), REQUEST_PERMISSION);
        }
    }

    // 初始化打印机
    public boolean initializePrinter(int printerSeries, int lang, Context context) {
        try {
            if (mPrinter == null) {
                mPrinter = new Printer(printerSeries, lang, context);
                mPrinter.setReceiveEventListener(this); // 设置事件监听器
            }
            return true;
        } catch (Epos2Exception e) {
            logError("Failed to initialize printer", e);
            return false;
        }
    }

    // 连接打印机
    public boolean connectPrinter(String target, Context context) {
        if (isPrinterNotInitialized()) {
            return false;
        }
        try {
            log("Connecting to printer: " + target);
            mPrinter.connect(target, Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            logError("Failed to connect to printer", e);
            ShowMsg.showException(e, "connect", context);
            return false;
        }
        return true;
    }

    public boolean executePrintCommands(List<HashMap<String, Object>> commands, Context context) {
        if (mPrinter == null) {
            ShowMsg.showMsg("Printer is not initialized", context);
            return false;
        }

        try {
            for (HashMap<String, Object> command : commands) {
                for (String key : command.keySet()) {
                    Object value = command.get(key);

                    switch (key) {
                        case "addHPosition":
                            if (value instanceof Integer) {
                                mPrinter.addHPosition((int) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addHPosition");
                            }
                            break;

                        case "addLineSpace":
                            if (value instanceof Integer) {
                                mPrinter.addLineSpace((int) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addLineSpace");
                            }
                            break;

                        case "addFeedLine":
                            if (value instanceof Integer) {
                                mPrinter.addFeedLine((int) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addFeedLine");
                            }
                            break;

                        case "addFeedUnit":
                            if (value instanceof Integer) {
                                mPrinter.addFeedUnit((int) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addFeedUnit");
                            }
                            break;

                        case "addTextAlign":
                            if (value instanceof String) {
                                int alignment = PrinterUtils.parseTextAlign((String) value);
                                mPrinter.addTextAlign(alignment);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addTextAlign");
                            }
                            break;

                        case "addText":
                            if (value instanceof String) {
                                mPrinter.addText((String) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addTextAlign");
                            }
                            break;

                        case "addTextStyle":
                            if (value instanceof HashMap) {
                                HashMap<String, Boolean> styleParams = PrinterUtils.castToMap(value);
                                boolean reverse = PrinterUtils.getOrDefault(styleParams, "reverse", false);
                                boolean ul = PrinterUtils.getOrDefault(styleParams, "ul", false);
                                boolean em = PrinterUtils.getOrDefault(styleParams, "em", false);
                                mPrinter.addTextStyle(
                                        reverse ? Printer.TRUE : Printer.FALSE,
                                        ul ? Printer.TRUE : Printer.FALSE,
                                        em ? Printer.TRUE : Printer.FALSE,
                                        Printer.COLOR_1);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addTextStyle");
                            }
                            break;

                        case "addBase64Image":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> imageParams = PrinterUtils.castToMap(value);
                                String base64 = (String) imageParams.get("value");
                                int x = (int) PrinterUtils.getOrDefault(imageParams, "x", 0);
                                int y = (int) PrinterUtils.getOrDefault(imageParams, "y", 0);
                                int width = (int) PrinterUtils.getOrDefault(imageParams, "width", 0);

                                byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                log("Width: " + width);
                                log("Bitmap Width: " + bitmap.getWidth());
                                log("Bitmap height: " + bitmap.getHeight());

                                if (width > 0 && bitmap.getWidth() > width) {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth(), false);
                                    log("Down Scaled Image Width: " + bitmap.getWidth());
                                    log("Down Scaled Image Height: " + bitmap.getHeight());
                                }

                                if (width > 0 && bitmap.getWidth() < width) {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth(), true);
                                    log("Up Scaled Image Width: " + bitmap.getWidth());
                                    log("Up Scaled Image Height: " + bitmap.getHeight());
                                }

                                int color = PrinterUtils.parseImageColor((String) imageParams.get("color"));
                                int mode = PrinterUtils.parseImageMode((String) imageParams.get("mode"));
                                int halftone = PrinterUtils.parseImageHalftone((String) imageParams.get("halftone"));
                                int compress = PrinterUtils.parseImageCompress((String) imageParams.get("compress"));
                                double brightness = (Double) PrinterUtils.getOrDefault(imageParams, "brightness", 1);

                                mPrinter.addImage(
                                        bitmap,
                                        x,
                                        y,
                                        bitmap.getWidth(),
                                        bitmap.getHeight(),
                                        color,
                                        mode,
                                        halftone,
                                        brightness,
                                        compress);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addBase64Image");
                            }
                            break;

                        case "addHLine":
                            if (value instanceof JSONObject hLine) {
                                JSONArray position = hLine.getJSONArray("position");

                                int x1 = position.getInt(0);
                                int x2 = position.getInt(1);

                                String lineStyle = hLine.getString("lineStyle");

                                mPrinter.addHLine(x1, x2, PrinterUtils.parseLineStyle(lineStyle));
                            } else {
                                throw new IllegalArgumentException("Invalid value for addHLine");
                            }
                            break;

                        case "addVLineBegin":
                            if (value instanceof JSONObject vLine) {
                                int position = vLine.getInt("position");
                                String lineStyle = vLine.getString("lineStyle");
                                JSONArray lineIds = vLine.getJSONArray("lineId");

                                int[] ids = new int[lineIds.length()];
                                for (int i = 0; i < lineIds.length(); i++) {
                                    int lineId = lineIds.getInt(i);
                                    ids[i] = lineId;
                                }
                                mPrinter.addVLineBegin(position, PrinterUtils.parseLineStyle(lineStyle), ids);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addVLineBegin");
                            }
                            break;

                        case "addVLineEnd":
                            if (value instanceof JSONObject vLineEnd) {
                                JSONArray lineIds = vLineEnd.getJSONArray("lineId");
                                for (int i = 0; i < lineIds.length(); i++) {
                                    int lineId = lineIds.getInt(i);
                                    mPrinter.addVLineEnd(lineId);
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid value for addVLineEnd");
                            }
                            break;


                        case "addBarcode":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> barcodeParams = PrinterUtils.castToMap(value);
                                String barcodeValue = (String) barcodeParams.get("value");
                                String barcodeType = (String) barcodeParams.get("type");
                                String barcodeFont = (String) barcodeParams.get("font");
                                String barcodeHri = (String) barcodeParams.get("hri");
                                int width = (int) PrinterUtils.getOrDefault(barcodeParams, "width", 2);
                                int height = (int) PrinterUtils.getOrDefault(barcodeParams, "height", 100);
                                mPrinter.addBarcode(
                                        barcodeValue,
                                        PrinterUtils.parseBarcodeType(barcodeType),
                                        PrinterUtils.parseBarcodeHri(barcodeHri),
                                        PrinterUtils.parseBarcodeFont(barcodeFont),
                                        width,
                                        height);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addBarcode");
                            }
                            break;


                        case "addSymbol":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> symbolParams = PrinterUtils.castToMap(value);
                                String symbolValue = (String) symbolParams.get("value");
                                String symbolType = (String) symbolParams.get("type");
                                int finalLevel;
                                if (symbolType != null && symbolType.startsWith("AZTECCODE")) {
                                    finalLevel = (int) PrinterUtils.getOrDefault(symbolParams, "level", 23);
                                } else {
                                    finalLevel = PrinterUtils.parseSymbolLevel((String) symbolParams.get("level"));
                                }

                                int width = (int) PrinterUtils.getOrDefault(symbolParams, "width", 2);
                                int height = (int) PrinterUtils.getOrDefault(symbolParams, "height", 3);
                                int size = (int) PrinterUtils.getOrDefault(symbolParams, "size", 0);
                                mPrinter.addSymbol(
                                        symbolValue,
                                        PrinterUtils.parseSymbolType(symbolType),
                                        finalLevel,
                                        width,
                                        height, size);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addSymbol");
                            }
                            break;

                        case "addTextSize":
                            if (value instanceof int[] size && size.length == 2) {
                                mPrinter.addTextSize(size[0], size[1]);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addTextSize");
                            }
                            break;

                        case "addCut":
                            if (value instanceof String) {
                                int cutType = PrinterUtils.parseCutType((String) value);
                                mPrinter.addCut(cutType);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addCut");
                            }
                            break;

                        case "addPulse":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> pulseParams = PrinterUtils.castToMap(value);
                                String drawer = (String) pulseParams.get("drawer");
                                String time = (String) pulseParams.get("time");
                                mPrinter.addPulse(
                                        PrinterUtils.parseDrawerPin(drawer),
                                        PrinterUtils.parsePulseTime(time));
                            } else {
                                throw new IllegalArgumentException("Invalid value for addPulse");
                            }
                            break;

                        case "addFeedPosition":
                            if (value instanceof String) {
                                int feedPosition = PrinterUtils.parseFeedPosition((String) value);
                                mPrinter.addFeedPosition(feedPosition);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addFeedPosition");
                            }
                            break;

                        case "addLayout":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> layoutParams = PrinterUtils.castToMap(value);
                                int layoutType = PrinterUtils.parseLayoutType((String) layoutParams.get("type"));
                                int width = (int) PrinterUtils.getOrDefault(layoutParams, "width", 0);
                                int height = (int) PrinterUtils.getOrDefault(layoutParams, "height", 0);
                                int marginTop = (int) PrinterUtils.getOrDefault(layoutParams, "marginTop", 0);
                                int marginBottom = (int) PrinterUtils.getOrDefault(layoutParams, "marginBottom", 0);
                                int offsetCut = (int) PrinterUtils.getOrDefault(layoutParams, "offsetCut", 0);
                                int offsetLabel = (int) PrinterUtils.getOrDefault(layoutParams, "offsetLabel", 0);
                                mPrinter.addLayout(
                                        layoutType,
                                        width,
                                        height,
                                        marginTop,
                                        marginBottom,
                                        offsetCut,
                                        offsetLabel);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addLayout");
                            }
                            break;

                        case "addCommand":
                            if (value instanceof byte[]) {
                                mPrinter.addCommand((byte[]) value);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addCommand");
                            }
                            break;

                        default:
                            throw new IllegalArgumentException("Unsupported method: " + key);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logError("Error execute print commands", e);
            ShowMsg.showException(e, "executePrintCommands", context);
            return false;
        }
    }

    // 打印数据
    public void printData(PrinterCallback callback) {
        if (isPrinterNotInitialized()) {
            if (callback != null) {
                callback.onError("Printer not initialized");
            }
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
            log("Data sent to printer successfully.");

            if (callback != null) {
                callback.onSuccess();
            }

        } catch (Exception e) {
            logError("Error sending print data", e);
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    // 释放资源
    public void finalizePrinter() {
        if (mPrinter == null) {
            log("Printer is already finalized");
            return;
        }
        try {
            log("Finalizing printer resources");
            disconnectPrinter(); // 确保打印机被断开连接
        } catch (Exception e) {
            logError("Error during printer finalization", e);
        } finally {
            mPrinter = null;
        }
    }


    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status,
                             final String printJobId) {

        log("Received print event with code: " + code);
        uiHandler.post(() -> {
            ShowMsg.showResult(code, makeErrorMessage(status), context);
            disconnectPrinter(); // 直接调用，无需额外线程
        });
    }

    private void disconnectPrinter() {
        log("Attempting to disconnect printer...");
        if (mPrinter == null) {
            log("No printer to disconnect.");
            return;
        }

        boolean isDisconnected = false;
        int retryCount = 0;
        int maxRetries = 5; // Limit the number of retries to avoid infinite loops

        while (!isDisconnected && retryCount < maxRetries) {
            try {
                mPrinter.disconnect();
                log("Printer disconnected successfully.");
                isDisconnected = true;
            } catch (Epos2Exception e) {
                if (e.getErrorStatus() == Epos2Exception.ERR_PROCESSING) {
                    retryCount++;
                    log("Printer is still processing. Retrying... Attempt: " + retryCount);
                    try {
                        Thread.sleep(DISCONNECT_INTERVAL); // Small delay before retrying
                    } catch (InterruptedException ie) {
                        logError("Thread interrupted while waiting to retry disconnect", ie);
                    }
                } else {
                    logError("Error during printer disconnect", e);
                    break; // Exit the loop for non-processing errors
                }
            } catch (Exception e) {
                logError("Unexpected error during printer disconnect", e);
                break;
            }
        }

        if (!isDisconnected) {
            log("Failed to disconnect printer after " + retryCount + " attempts.");
        }

        try {
            mPrinter.clearCommandBuffer();
            log("Command buffer cleared.");
        } catch (Exception e) {
            logError("Error clearing command buffer", e);
        } finally {
            mPrinter = null;
        }
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += context.getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += context.getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += context.getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += context.getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += context.getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += context.getString(R.string.handlingmsg_err_autocutter);
            msg += context.getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += context.getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += context.getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += context.getString(R.string.handlingmsg_err_battery_real_end);
        }
        if (status.getRemovalWaiting() == Printer.REMOVAL_WAIT_PAPER) {
            msg += context.getString(R.string.handlingmsg_err_wait_removal);
        }
        if (status.getUnrecoverError() == Printer.HIGH_VOLTAGE_ERR ||
                status.getUnrecoverError() == Printer.LOW_VOLTAGE_ERR) {
            msg += context.getString(R.string.handlingmsg_err_voltage);
        }

        return msg;
    }


    // 获取打印机状态
//    public PrinterStatusInfo getPrinterStatus() {
//        if (mPrinter == null) {
//            return null;
//        }
//        return mPrinter.getStatus();
//    }

// 设置事件监听器
//    public void setReceiveEventListener(ReceiveListener listener) {
//        if (mPrinter != null) {
//            mPrinter.setReceiveEventListener(listener);
//        }
//    }

//    public boolean checkPrinterStatus(Context context) {
//        if (!isPrinterInitialized()) {
//            return false;
//        }
//
//        PrinterStatusInfo status = mPrinter.getStatus();
//        if (!isPrinterReady(status, context)) {
//            log("Printer is not ready: " + makeErrorMessage(status));
//            return false;
//        }
//
//        return true;
//    }

//    private boolean reconnectPrinter(String target) {
//        try {
//            log("Reconnecting to printer: " + target);
//            mPrinter.connect(target, Printer.PARAM_DEFAULT);
//            return true;
//        } catch (Epos2Exception e) {
//            logError("Failed to reconnect to printer", e);
//            return false;
//        }
//    }

//    private boolean isPrinterReady(PrinterStatusInfo status, Context context) {
//        if (status.getOnline() == Printer.FALSE) {
//            ShowMsg.showMsg("Printer is offline", context);
//            return false;
//        }
//        if (status.getPaper() == Printer.PAPER_EMPTY) {
//            ShowMsg.showMsg("Printer is out of paper", context);
//            return false;
//        }
//        if (status.getCoverOpen() == Printer.TRUE) {
//            ShowMsg.showMsg("Printer cover is open", context);
//            return false;
//        }
//        return true;
//    }

    private boolean isPrinterNotInitialized() {
        if (mPrinter == null) {
            log("Printer is not initialized.");
            return true;
        }
        return false;
    }

    private void log(String message) {
        Log.d("PrinterManager", message);
    }

    private void logError(String message, Exception e) {
        Log.e("PrinterManager", message, e);
    }
}
