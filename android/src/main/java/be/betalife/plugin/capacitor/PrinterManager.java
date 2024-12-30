package be.betalife.plugin.capacitor;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.HashMap;
import java.util.List;

public class PrinterManager implements ReceiveListener {
    private Activity activity; // 保存 Activity 实例
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private Context context;

    public PrinterManager(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
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

        String methodName = "";

        try {
            for (HashMap<String, Object> command : commands) {
                methodName = (String) command.get("method");
                Object value = command.get("value");

                switch (methodName) {
                    case "addFeedLine":
                        if (value instanceof Integer) {
                            mPrinter.addFeedLine((int) value);
                        } else {
                            throw new IllegalArgumentException("Invalid value for addFeedLine");
                        }
                        break;
                    case "addTextAlign":
                        if (value instanceof Integer) {
                            mPrinter.addTextAlign((int) value);
                        } else {
                            throw new IllegalArgumentException("Invalid value for addTextAlign");
                        }
                        break;
                    case "addText":
                        if (value instanceof String) {
                            mPrinter.addText((String) value);
                        } else {
                            throw new IllegalArgumentException("Invalid value for addText");
                        }
                        break;
                    case "addBarcode":
                        mPrinter.addBarcode((String) value, Printer.BARCODE_CODE39, Printer.HRI_BELOW,
                                Printer.FONT_A, 2, 100);
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
                        mPrinter.addCut(Printer.CUT_FEED);
                        break;
                    default:
                        ShowMsg.showMsg("Unsupported method: " + methodName, context);
                        return false;
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
