package be.betalife.plugin.capacitor;


import android.content.Context;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.HashMap;
import java.util.List;


public class PrinterManager {
    private Printer mPrinter = null;

    public interface PrinterCallback {
        void onSuccess();
        void onError(String message);
    }

    // 初始化打印机
    public boolean initializePrinter(int printerSeries, int lang, Context context) {
        try {
            if (mPrinter == null) {
                mPrinter = new Printer(printerSeries, lang, context);
            }
            return true;
        } catch (Epos2Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 连接打印机
    public boolean connectPrinter(String target) {
        if (mPrinter == null) {
            return false;
        }
        try {
            mPrinter.connect(target, Printer.PARAM_DEFAULT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                        mPrinter.addFeedLine((int) value);
                        break;
                    case "addTextAlign":
                        mPrinter.addTextAlign((int) value);
                        break;
                    case "addText":
                        mPrinter.addText((String) value);
                        break;
                    case "addBarcode":
                        mPrinter.addBarcode((String) value, Printer.BARCODE_CODE39, Printer.HRI_BELOW,
                                Printer.FONT_A, 2, 100);
                        break;
                    case "addTextSize":
                        int[] size = (int[]) value;
                        mPrinter.addTextSize(size[0], size[1]);
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
            return false;
        }
    }

    // 打印数据
    public boolean printData(PrinterCallback callback) {
        if (mPrinter == null) {
            if (callback != null) {
                callback.onError("Printer not initialized");
            }
            return false;
        }
        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
            if (callback != null) {
                callback.onSuccess();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
            return false;
        }
    }

    // 释放资源
    public void finalizePrinter() {
        if (mPrinter == null) {
            return;
        }
        try {
            mPrinter.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
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
}
