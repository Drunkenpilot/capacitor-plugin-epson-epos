package be.betalife.plugin.capacitor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

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

    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private PrinterUtils printerUtils;

    public PrinterManager(Activity activity) {
        this.context = activity.getApplicationContext();
        this.printerUtils = new PrinterUtils();
    }

    private Printer mPrinter = null;
    private static final int DISCONNECT_INTERVAL = 500;// millseconds

    public interface PrinterCallback {
        void onSuccess();

        void onError(String message);
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
            e.printStackTrace();
            return false;
        }
    }

    // 连接打印机
    public boolean connectPrinter(String target, Context context) {
        if (!isPrinterInitialized()) {
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
                                int alignment = printerUtils.parseTextAlign((String) value);
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
                                HashMap<String, Boolean> styleParams = (HashMap<String, Boolean>) value;
                                boolean reverse = printerUtils.getOrDefault(styleParams, "reverse", false);
                                boolean ul = printerUtils.getOrDefault(styleParams, "ul", false);
                                boolean em = printerUtils.getOrDefault(styleParams, "em", false);
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
                                HashMap<String, Object> imageParams = (HashMap<String, Object>) value;
                                String base64 = (String) imageParams.get("value");
                                int x = (int) printerUtils.getOrDefault(imageParams, "x", 0);
                                int y = (int) printerUtils.getOrDefault(imageParams, "y", 0);
                                int width = (int) printerUtils.getOrDefault(imageParams, "width", 0);
                                int height = (int) printerUtils.getOrDefault(imageParams, "height", 0);
                                byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                if(width> 0 &&  bitmap.getWidth() > width) {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth(), false);
                                    log("Scaled Image Width: "+bitmap.getWidth());
                                    log("Scaled Image Height: "+bitmap.getHeight());
                                }

                                mPrinter.addImage(
                                        bitmap,
                                        x,
                                        y,
                                        width > 0 ? width : bitmap.getWidth(),
                                        height > 0 ? height : bitmap.getHeight(),
                                        Printer.COLOR_1,
                                        Printer.MODE_MONO,
                                        Printer.HALFTONE_DITHER,
                                        Printer.PARAM_DEFAULT,
                                        Printer.COMPRESS_AUTO);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addBase64Image");
                            }
                            break;

                        case "addHLine":
                            if (value instanceof JSONObject) {
                                JSONObject hLine = (JSONObject) value;
                                JSONArray position = hLine.getJSONArray("position");

                                int x1 = position.getInt(0);
                                int x2 = position.getInt(1);
                                String lineStyle = hLine.getString("lineStyle");
                                mPrinter.addHLine(x1, x2, printerUtils.parseLineStyle(lineStyle));
                            } else {
                                throw new IllegalArgumentException("Invalid value for addHLine");
                            }
                            break;

                        case "addVLineBegin":
                            if (value instanceof JSONObject) {
                                JSONObject vLine = (JSONObject) value;
                                int position = vLine.getInt("position");
                                String lineStyle = vLine.getString("lineStyle");
                                JSONArray lineIds = vLine.getJSONArray("lineId");

                                int[] ids = new int[lineIds.length()];
                                for (int i = 0; i < lineIds.length(); i++) {
                                    int lineId = lineIds.getInt(i);
                                    ids[i]=lineId;
                                }
                                mPrinter.addVLineBegin(position, printerUtils.parseLineStyle(lineStyle), ids);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addVLineBegin");
                            }
                            break;

                        case "addVLineEnd":
                            if (value instanceof JSONObject) {
                                JSONObject vLineEnd = (JSONObject) value;
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
                                HashMap<String, Object> barcodeParams = (HashMap<String, Object>) value;
                                String barcodeValue = (String) barcodeParams.get("value");
                                mPrinter.addBarcode(
                                        barcodeValue,
                                        Printer.BARCODE_CODE39,
                                        Printer.HRI_BELOW,
                                        Printer.FONT_A,
                                        2,
                                        100);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addBarcode");
                            }
                            break;

                        case "addTextSize":
                            if (value instanceof int[] && ((int[]) value).length == 2) {
                                int[] size = (int[]) value;
                                mPrinter.addTextSize(size[0], size[1]);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addTextSize");
                            }
                            break;

                        case "addCut":
                            if (value instanceof String) {
                                int cutType = printerUtils.parseCutType((String) value);
                                mPrinter.addCut(cutType);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addCut");
                            }
                            break;

                        case "addPulse":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> pulseParams = (HashMap<String, Object>) value;
                                String drawer = (String) pulseParams.get("drawer");
                                String time = (String) pulseParams.get("time");
                                mPrinter.addPulse(
                                        printerUtils.parseDrawerPin(drawer),
                                        printerUtils.parsePulseTime(time));
                            } else {
                                throw new IllegalArgumentException("Invalid value for addPulse");
                            }
                            break;

                        case "addFeedPosition":
                            if (value instanceof String) {
                                int feedPosition = printerUtils.parseFeedPosition((String) value);
                                mPrinter.addFeedPosition(feedPosition);
                            } else {
                                throw new IllegalArgumentException("Invalid value for addFeedPosition");
                            }
                            break;

                        case "addLayout":
                            if (value instanceof HashMap) {
                                HashMap<String, Object> layoutParams = (HashMap<String, Object>) value;
                                int layoutType = printerUtils.parseLayoutType((String) layoutParams.get("type"));
                                int width = (int) layoutParams.get("width");
                                int height = (int) layoutParams.get("height");
                                int marginTop = (int) layoutParams.get("marginTop");
                                int marginBottom = (int) layoutParams.get("marginBottom");
                                int offsetCut = (int) layoutParams.get("offsetCut");
                                int offsetLabel = (int) layoutParams.get("offsetLabel");
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
            e.printStackTrace();
            ShowMsg.showException(e, "executePrintCommands", context);
            return false;
        }
    }

    // 打印数据
    public boolean printData(PrinterCallback callback) {
        if (!isPrinterInitialized()) {
            if (callback != null) {
                callback.onError("Printer not initialized");
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
            log("Data sent to printer successfully.");

            if (callback != null) {
                callback.onSuccess();
            }
            return true;
        } catch (Exception e) {
            logError("Error sending print data", e);
            if (callback != null) {
                callback.onError(e.getMessage());
            }
            return false;
        } finally {
            // log("Finalizing printer after printData.");
            // finalizePrinter();
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
            disconnectPrinter(context); // 确保打印机被断开连接
        } catch (Exception e) {
            logError("Error during printer finalization", e);
        } finally {
            mPrinter = null;
        }
    }

    // 获取打印机状态
    public PrinterStatusInfo getPrinterStatus() {
        if (mPrinter == null) {
            return null;
        }
        return mPrinter.getStatus();
    }

    // 设置事件监听器
    public void setReceiveEventListener(ReceiveListener listener) {
        if (mPrinter != null) {
            mPrinter.setReceiveEventListener(listener);
        }
    }

    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status,
            final String printJobId) {

        log("Received print event with code: " + code);
        uiHandler.post(() -> {
            ShowMsg.showResult(code, makeErrorMessage(status), context);
            disconnectPrinter(context); // 直接调用，无需额外线程
        });
    }

    private void disconnectPrinter(Context context) {
        log("Attempting to disconnect printer...");
        if (mPrinter == null) {
            log("No printer to disconnect.");
            return;
        }

        while (true) {
            try {
                mPrinter.disconnect();
                log("Printer disconnected successfully.");
                break;
            } catch (Epos2Exception e) {
                if (e.getErrorStatus() == Epos2Exception.ERR_PROCESSING) {
                    log("Printer is still processing. Retrying...");
                    try {
                        Thread.sleep(DISCONNECT_INTERVAL);
                    } catch (InterruptedException ex) {
                        logError("Thread interrupted during disconnect retry", ex);
                    }
                } else {
                    logError("Error during printer disconnect", e);
                    break;
                }
            } catch (Exception e) {
                logError("Unknown error during disconnectPrinter", e);
                break;
            }
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

    public boolean checkPrinterStatus(Context context) {
        if (!isPrinterInitialized()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();
        if (!isPrinterReady(status, context)) {
            log("Printer is not ready: " + makeErrorMessage(status));
            return false;
        }

        return true;
    }

    private boolean reconnectPrinter(String target, Context context) {
        try {
            log("Reconnecting to printer: " + target);
            mPrinter.connect(target, Printer.PARAM_DEFAULT);
            return true;
        } catch (Epos2Exception e) {
            logError("Failed to reconnect to printer", e);
            return false;
        }
    }

    private boolean isPrinterReady(PrinterStatusInfo status, Context context) {
        if (status.getOnline() == Printer.FALSE) {
            ShowMsg.showMsg("Printer is offline", context);
            return false;
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            ShowMsg.showMsg("Printer is out of paper", context);
            return false;
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            ShowMsg.showMsg("Printer cover is open", context);
            return false;
        }
        return true;
    }

    private boolean isPrinterInitialized() {
        if (mPrinter == null) {
            log("Printer is not initialized");
            return false;
        }
        return true;
    }

    private void log(String message) {
        Log.d("PrinterManager", message);
    }

    private void logError(String message, Exception e) {
        Log.e("PrinterManager", message, e);
    }
}
