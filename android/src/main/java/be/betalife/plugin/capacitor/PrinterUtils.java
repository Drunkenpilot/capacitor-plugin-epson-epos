package be.betalife.plugin.capacitor;

import com.epson.epos2.printer.Printer;
import com.getcapacitor.JSArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PrinterUtils {
    public static int parsePrinterLang(String langCode) {
        switch (langCode.toUpperCase()) {
            case "JAPANESE":
                return Printer.LANG_JA;
            case "CHINESE_SC":
                return Printer.LANG_ZH_CN;
            case "CHINESE_TC":
                return Printer.LANG_ZH_TW;
            case "KOREAN":
                return Printer.LANG_KO;
            case "THAI":
                return Printer.LANG_TH;
            case "SOUTHASIA":
                return Printer.LANG_VI;
            default:
                return Printer.LANG_EN;

        }
    }

    public static int parsePrinterSeries(String printerSeriesCode) {
        switch (printerSeriesCode.toUpperCase()) {
            case "TM_M10":
                return Printer.TM_M10;
            case "TM_M30":
                return Printer.TM_M30;
            case "TM_P20":
                return Printer.TM_P20;
            case "TM_P60":
                return Printer.TM_P60;
            case "TM_P60II":
                return Printer.TM_P60II;
            case "TM_P80":
                return Printer.TM_P80;
            case "TM_T20":
                return Printer.TM_T20;
            case "TM_T60":
                return Printer.TM_T60;
            case "TM_T70":
                return Printer.TM_T70;
            case "TM_T81":
                return Printer.TM_T81;
            case "TM_T82":
                return Printer.TM_T82;
            case "TM_T83":
                return Printer.TM_T83;
            case "TM_T83III":
                return Printer.TM_T83III;
            case "TM_T88":
                return Printer.TM_T88;
            case "TM_T90":
                return Printer.TM_T90;
            case "TM_T90KP":
                return Printer.TM_T90KP;
            case "TM_T100":
                return Printer.TM_T100;
            case "TM_U220":
                return Printer.TM_U220;
            case "TM_U330":
                return Printer.TM_U330;
            case "TM_L90":
                return Printer.TM_L90;
            case "TM_H6000":
                return Printer.TM_H6000;
            case "TM_M30II":
                return Printer.TM_M30II;
            case "TS_100":
                return Printer.TS_100;
            case "TM_M50":
                return Printer.TM_M50;
            case "TM_T88VII":
                return Printer.TM_T88VII;
            case "TM_L90LFC":
                return Printer.TM_L90LFC;
            case "EU_M30":
                return Printer.EU_M30;
            case "TM_L100":
                return Printer.TM_L100;
            case "TM_P20II":
                return Printer.TM_P20II;
            case "TM_P80II":
                return Printer.TM_P80II;
            case "TM_M30III":
                return Printer.TM_M30III;
            case "TM_M50II":
                return Printer.TM_M50II;
            case "TM_M55":
                return Printer.TM_M55;
            case "TM_U220II":
                return Printer.TM_U220II;
            default:
                // 如果输入型号不匹配，返回默认型号
                return Printer.TM_M10;
        }
    }

    public List<HashMap<String, Object>> parseInstructions(JSArray jsArray) throws Exception {
        List<HashMap<String, Object>> commands = new ArrayList<>();
        for (int i = 0; i < jsArray.length(); i++) {
            JSONObject jsonObject = jsArray.getJSONObject(i);
            HashMap<String, Object> command = new HashMap<>();

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                command.put(key, jsonObject.get(key));
            }

            commands.add(command);
        }
        return commands;
    }
}
