package be.betalife.plugin.capacitor;

import com.epson.epos2.printer.Printer;
import com.getcapacitor.JSArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public static int parseTextAlign(String code) {
        switch (code.toUpperCase()) {
            case "LEFT":
                return Printer.ALIGN_LEFT;
            case "CENTER":
                return Printer.ALIGN_CENTER;
            case "RIGHT":
                return Printer.ALIGN_RIGHT;
            default:
                return Printer.ALIGN_LEFT;
        }
    }

    public static int parseCutType(String cut) {
        switch (cut.toLowerCase()) {
            case "cut_feed":
                return Printer.CUT_FEED;
            case "cut_no_feed":
                return Printer.CUT_NO_FEED;
            case "cut_reserve":
                return Printer.CUT_RESERVE;
            case "full_cut_feed":
                return Printer.FULL_CUT_FEED;
            case "full_cut_no_feed":
                return Printer.FULL_CUT_NO_FEED;
            case "full_cut_reserve":
                return Printer.FULL_CUT_RESERVE;
            default:
                return Printer.CUT_FEED;
        }
    }

    public static int parseDrawerPin(String pin) {
        return "5pin".equals(pin) ? Printer.DRAWER_5PIN : Printer.DRAWER_2PIN;
    }

    public static int parsePulseTime(String time) {
        switch (time.toLowerCase()) {
            case "pulse_100":
                return Printer.PULSE_100;
            case "pulse_200":
                return Printer.PULSE_200;
            case "pulse_300":
                return Printer.PULSE_300;
            case "pulse_400":
                return Printer.PULSE_400;
            case "pulse_500":
                return Printer.PULSE_500;
            default:
                return Printer.PULSE_100;
        }
    }

    public static int parseFeedPosition(String position) {
        switch (position.toLowerCase()) {
            case "peeling":
                return Printer.FEED_PEELING;
            case "cutting":
                return Printer.FEED_CUTTING;
            case "current_tof":
                return Printer.FEED_CURRENT_TOF;
            case "next_tof":
                return Printer.FEED_NEXT_TOF;
            default:
                return Printer.FEED_PEELING;
        }
    }

    public static int parseLayoutType(String type) {
        switch (type.toLowerCase()) {
            case "receipt":
                return Printer.LAYOUT_RECEIPT;
            case "receipt_bm":
                return Printer.LAYOUT_RECEIPT_BM;
            case "label":
                return Printer.LAYOUT_LABEL;
            case "label_bm":
                return Printer.LAYOUT_LABEL_BM;
            default:
                return Printer.LAYOUT_RECEIPT;
        }
    }

    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public static List<HashMap<String, Object>> parseInstructions(JSArray jsArray) throws Exception {
        List<HashMap<String, Object>> commands = new ArrayList<>();

        for (int i = 0; i < jsArray.length(); i++) {
            JSONObject jsonObject = jsArray.getJSONObject(i);
            HashMap<String, Object> command = new HashMap<>();

            // Parse each instruction based on its defined keys
            if (jsonObject.has("addHPosition")) {
                command.put("addHPosition", jsonObject.getInt("addHPosition"));
            }
            if (jsonObject.has("addLineSpace")) {
                command.put("addLineSpace", jsonObject.getInt("addLineSpace"));
            }
            if (jsonObject.has("addFeedLine")) {
                command.put("addFeedLine", jsonObject.getInt("addFeedLine"));
            }
            if (jsonObject.has("addFeedUnit")) {
                command.put("addFeedUnit", jsonObject.getInt("addFeedUnit"));
            }
            if (jsonObject.has("addTextAlign")) {
                command.put("addTextAlign", jsonObject.getString("addTextAlign"));
            }
            if (jsonObject.has("addText")) {
                JSONObject textObject = jsonObject.getJSONObject("addText");
                command.put("addText", textObject.getString("value"));

                if (textObject.has("size")) {
                    command.put("addTextSize", parseSizeArray(textObject.getJSONArray("size")));
                }
                if (textObject.has("align")) {
                    command.put("addTextAlign", textObject.getString("align"));
                }
                if (textObject.has("style")) {
                    JSONObject styleObject = textObject.getJSONObject("style");
                    HashMap<String, Boolean> styleCommand = new HashMap<>();
                    styleCommand.put("reverse", styleObject.optBoolean("reverse", false));
                    styleCommand.put("ul", styleObject.optBoolean("ul", false));
                    styleCommand.put("em", styleObject.optBoolean("em", false));
                    command.put("addTextStyle", styleCommand);
                }
            }
            if (jsonObject.has("addTextStyle")) {
                JSONObject styleObject = jsonObject.getJSONObject("addTextStyle");
                HashMap<String, Boolean> styleCommand = new HashMap<>();
                styleCommand.put("reverse", styleObject.optBoolean("reverse", false));
                styleCommand.put("ul", styleObject.optBoolean("ul", false));
                styleCommand.put("em", styleObject.optBoolean("em", false));
                command.put("addTextStyle", styleCommand);
            }
            if (jsonObject.has("addBase64Image")) {
                JSONObject imageObject = jsonObject.getJSONObject("addBase64Image");
                HashMap<String, Object> imageCommand = new HashMap<>();

                String base64Value = imageObject.getString("value");
                // Remove prefix if present
                if (base64Value.startsWith("data:image/")) {
                    int commaIndex = base64Value.indexOf(",");
                    if (commaIndex != -1) {
                        base64Value = base64Value.substring(commaIndex + 1);
                    }
                }

                imageCommand.put("value", base64Value);

                imageCommand.put("x", imageObject.optInt("x", 0));
                imageCommand.put("y", imageObject.optInt("y", 0));
                imageCommand.put("width", imageObject.optInt("width", 0));
                imageCommand.put("height", imageObject.optInt("height", 0));
                command.put("addBase64Image", imageCommand);
            }
            if (jsonObject.has("addBarcode")) {
                JSONObject barcodeObject = jsonObject.getJSONObject("addBarcode");
                HashMap<String, Object> barcodeCommand = new HashMap<>();
                barcodeCommand.put("value", barcodeObject.getString("value"));
                command.put("addBarcode", barcodeCommand);
            }
            if (jsonObject.has("addTextSize")) {
                command.put("addTextSize", parseSizeArray(jsonObject.getJSONArray("addTextSize")));
            }
            if (jsonObject.has("addCut")) {
                command.put("addCut", jsonObject.getString("addCut"));
            }
            if (jsonObject.has("addPulse")) {
                JSONObject pulseObject = jsonObject.getJSONObject("addPulse");
                HashMap<String, Object> pulseCommand = new HashMap<>();
                pulseCommand.put("drawer", pulseObject.optString("drawer", "2pin"));
                pulseCommand.put("time", pulseObject.optString("time", "pulse_100"));
                command.put("addPulse", pulseCommand);
            }
            if (jsonObject.has("addFeedPosition")) {
                command.put("addFeedPosition", jsonObject.getString("addFeedPosition"));
            }
            if (jsonObject.has("addLayout")) {
                JSONObject layoutObject = jsonObject.getJSONObject("addLayout");
                HashMap<String, Object> layoutCommand = new HashMap<>();
                layoutCommand.put("type", layoutObject.getString("type"));
                layoutCommand.put("width", layoutObject.getInt("width"));
                layoutCommand.put("height", layoutObject.getInt("height"));
                layoutCommand.put("marginTop", layoutObject.getInt("marginTop"));
                layoutCommand.put("marginBottom", layoutObject.getInt("marginBottom"));
                layoutCommand.put("offsetCut", layoutObject.getInt("offsetCut"));
                layoutCommand.put("offsetLabel", layoutObject.getInt("offsetLabel"));
                command.put("addLayout", layoutCommand);
            }
            if (jsonObject.has("addCommand")) {
                command.put("addCommand", jsonObject.get("addCommand"));
            }

            // Add the command to the list
            commands.add(command);
        }

        return commands;
    }

    // Helper method to parse size arrays
    private static int[] parseSizeArray(JSONArray jsonArray) throws Exception {
        int[] size = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            size[i] = jsonArray.getInt(i);
        }
        return size;
    }
}
