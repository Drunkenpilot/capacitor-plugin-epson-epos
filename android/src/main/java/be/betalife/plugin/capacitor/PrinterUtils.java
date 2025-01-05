package be.betalife.plugin.capacitor;

import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.printer.Printer;
import com.getcapacitor.JSArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrinterUtils {


    public static int parsePrinterPortType(String portType) {
        if (portType == null) {
            return Discovery.PORTTYPE_ALL;
        }
        return switch (portType.toUpperCase()) {
            case "TCP" -> Discovery.PORTTYPE_TCP;
            case "BLUETOOTH" -> Discovery.PORTTYPE_BLUETOOTH;
            case "USB" -> Discovery.PORTTYPE_USB;
            default -> Discovery.PORTTYPE_ALL;
        };
    }

    public static int parsePrinterLang(String langCode) {
        if (langCode == null) {
            return Printer.LANG_EN;
        }
        return switch (langCode.toUpperCase()) {
            case "JAPANESE" -> Printer.LANG_JA;
            case "CHINESE_SC" -> Printer.LANG_ZH_CN;
            case "CHINESE_TC" -> Printer.LANG_ZH_TW;
            case "KOREAN" -> Printer.LANG_KO;
            case "THAI" -> Printer.LANG_TH;
            case "SOUTHASIA" -> Printer.LANG_VI;
            default -> Printer.LANG_EN;
        };
    }

    public static int parsePrinterSeries(String printerSeriesCode) {
        if (printerSeriesCode == null) {
            return Printer.TM_M10;
        }
        return switch (printerSeriesCode.toUpperCase()) {
            case "TM_M10" -> Printer.TM_M10;
            case "TM_M30" -> Printer.TM_M30;
            case "TM_P20" -> Printer.TM_P20;
            case "TM_P60" -> Printer.TM_P60;
            case "TM_P60II" -> Printer.TM_P60II;
            case "TM_P80" -> Printer.TM_P80;
            case "TM_T20" -> Printer.TM_T20;
            case "TM_T60" -> Printer.TM_T60;
            case "TM_T70" -> Printer.TM_T70;
            case "TM_T81" -> Printer.TM_T81;
            case "TM_T82" -> Printer.TM_T82;
            case "TM_T83" -> Printer.TM_T83;
            case "TM_T83III" -> Printer.TM_T83III;
            case "TM_T88" -> Printer.TM_T88;
            case "TM_T90" -> Printer.TM_T90;
            case "TM_T90KP" -> Printer.TM_T90KP;
            case "TM_T100" -> Printer.TM_T100;
            case "TM_U220" -> Printer.TM_U220;
            case "TM_U330" -> Printer.TM_U330;
            case "TM_L90" -> Printer.TM_L90;
            case "TM_H6000" -> Printer.TM_H6000;
            case "TM_M30II" -> Printer.TM_M30II;
            case "TS_100" -> Printer.TS_100;
            case "TM_M50" -> Printer.TM_M50;
            case "TM_T88VII" -> Printer.TM_T88VII;
            case "TM_L90LFC" -> Printer.TM_L90LFC;
            case "EU_M30" -> Printer.EU_M30;
            case "TM_L100" -> Printer.TM_L100;
            case "TM_P20II" -> Printer.TM_P20II;
            case "TM_P80II" -> Printer.TM_P80II;
            case "TM_M30III" -> Printer.TM_M30III;
            case "TM_M50II" -> Printer.TM_M50II;
            case "TM_M55" -> Printer.TM_M55;
            case "TM_U220II" -> Printer.TM_U220II;
            default ->
                // 如果输入型号不匹配，返回默认型号
                    Printer.TM_M10;
        };
    }

    public static int parseTextAlign(String code) {
        if (code == null) {
            return Printer.ALIGN_LEFT;
        }
        return switch (code.toLowerCase()) {
            case "center" -> Printer.ALIGN_CENTER;
            case "right" -> Printer.ALIGN_RIGHT;
            default -> Printer.ALIGN_LEFT;
        };
    }

    public static int parseCutType(String cut) {
        if (cut == null) {
            return Printer.CUT_FEED;
        }
        return switch (cut.toLowerCase()) {
            case "cut_no_feed" -> Printer.CUT_NO_FEED;
            case "cut_reserve" -> Printer.CUT_RESERVE;
            case "full_cut_feed" -> Printer.FULL_CUT_FEED;
            case "full_cut_no_feed" -> Printer.FULL_CUT_NO_FEED;
            case "full_cut_reserve" -> Printer.FULL_CUT_RESERVE;
            default -> Printer.CUT_FEED;
        };
    }

    public static int parseDrawerPin(String pin) {
        return "5pin".equals(pin) ? Printer.DRAWER_5PIN : Printer.DRAWER_2PIN;
    }

    public static int parsePulseTime(String time) {
        if (time == null) {
            return Printer.PULSE_100;
        }
        return switch (time.toLowerCase()) {
            case "pulse_200" -> Printer.PULSE_200;
            case "pulse_300" -> Printer.PULSE_300;
            case "pulse_400" -> Printer.PULSE_400;
            case "pulse_500" -> Printer.PULSE_500;
            default -> Printer.PULSE_100;
        };
    }

    public static int parseFeedPosition(String position) {
        if (position == null) {
            return Printer.FEED_PEELING;
        }
        return switch (position.toLowerCase()) {
            case "cutting" -> Printer.FEED_CUTTING;
            case "current_tof" -> Printer.FEED_CURRENT_TOF;
            case "next_tof" -> Printer.FEED_NEXT_TOF;
            default -> Printer.FEED_PEELING;
        };
    }

    public static int parseLayoutType(String type) {
        if (type == null) {
            return Printer.LAYOUT_RECEIPT;
        }
        return switch (type.toLowerCase()) {
            case "receipt_bm" -> Printer.LAYOUT_RECEIPT_BM;
            case "label" -> Printer.LAYOUT_LABEL;
            case "label_bm" -> Printer.LAYOUT_LABEL_BM;
            default -> Printer.LAYOUT_RECEIPT;
        };
    }

    public static int parseLineStyle(String lineStyle) {
        if (lineStyle == null) {
            return Printer.LINE_THIN;
        }
        return switch (lineStyle.toLowerCase()) {
            case "medium" -> Printer.LINE_MEDIUM;
            case "thick" -> Printer.LINE_THICK;
            case "thin_double" -> Printer.LINE_THIN_DOUBLE;
            case "medium_double" -> Printer.LINE_MEDIUM_DOUBLE;
            case "thick_double" -> Printer.LINE_THICK_DOUBLE;
            default -> Printer.LINE_THIN;
        };
    }

    public static int parseBarcodeType(String type) {
        if (type == null) {
            return Printer.BARCODE_CODE39;
        }
        return switch (type.toUpperCase()) {
            case "UPC_A" -> Printer.BARCODE_UPC_A;
            case "UPC_E" -> Printer.BARCODE_UPC_E;
            case "EAN13" -> Printer.BARCODE_EAN13;
            case "JAN13" -> Printer.BARCODE_JAN13;
            case "EAN8" -> Printer.BARCODE_EAN8;
            case "JAN8" -> Printer.BARCODE_JAN8;
            case "ITF" -> Printer.BARCODE_ITF;
            case "CODA_BAR" -> Printer.BARCODE_CODABAR;
            case "CODE_93" -> Printer.BARCODE_CODE93;
            case "CODE_128" -> Printer.BARCODE_CODE128;
            case "CODE_128_AUTO" -> Printer.BARCODE_CODE128_AUTO;
            case "GS1_128" -> Printer.BARCODE_GS1_128;
            case "GS1_DATA_BAR_OMNIDIRECTIONAL" -> Printer.BARCODE_GS1_DATABAR_OMNIDIRECTIONAL;
            case "GS1_DATA_BAR_TRUNCATED" -> Printer.BARCODE_GS1_DATABAR_TRUNCATED;
            case "GS1_DATA_BAR_LIMITED" -> Printer.BARCODE_GS1_DATABAR_LIMITED;
            case "GS1_DATA_BAR_EXPANDED" -> Printer.BARCODE_GS1_DATABAR_EXPANDED;
            default -> Printer.BARCODE_CODE39;
        };
    }

    public static int parseBarcodeFont(String font) {
        if (font == null) {
            return Printer.FONT_A;
        }
        return switch (font.toUpperCase()) {
            case "FONT_B" -> Printer.FONT_B;
            case "FONT_C" -> Printer.FONT_C;
            case "FONT_D" -> Printer.FONT_D;
            case "FONT_E" -> Printer.FONT_E;
            default -> Printer.FONT_A;
        };
    }

    public static int parseBarcodeHri(String hri) {
        if (hri == null) {
            return Printer.HRI_BELOW;
        }
        return switch (hri.toUpperCase()) {
            case "HRI_NONE" -> Printer.HRI_NONE;
            case "HRI_ABOVE" -> Printer.HRI_ABOVE;
            case "HRI_BOTH" -> Printer.HRI_BOTH;
            default -> Printer.HRI_BELOW;
        };
    }

    public static int parseSymbolType(String type) {
        return switch (type.toUpperCase()) {
            case "PDF417_TRUNCATED" -> Printer.SYMBOL_PDF417_TRUNCATED;
            case "QRCODE_MODEL_1" -> Printer.SYMBOL_QRCODE_MODEL_1;
            case "QRCODE_MODEL_2" -> Printer.SYMBOL_QRCODE_MODEL_2;
            case "QRCODE_MICRO" -> Printer.SYMBOL_QRCODE_MICRO;
            case "MAXICODE_MODE_2" -> Printer.SYMBOL_MAXICODE_MODE_2;
            case "MAXICODE_MODE_3" -> Printer.SYMBOL_MAXICODE_MODE_3;
            case "MAXICODE_MODE_4" -> Printer.SYMBOL_MAXICODE_MODE_4;
            case "MAXICODE_MODE_5" -> Printer.SYMBOL_MAXICODE_MODE_5;
            case "MAXICODE_MODE_6" -> Printer.SYMBOL_MAXICODE_MODE_6;
            case "GS1_DATABAR_STACKED" -> Printer.SYMBOL_GS1_DATABAR_STACKED;
            case "GS1_DATABAR_STACKED_OMNIDIRECTIONAL" ->
                    Printer.SYMBOL_GS1_DATABAR_STACKED_OMNIDIRECTIONAL;
            case "GS1_DATABAR_EXPANDED_STACKED" -> Printer.SYMBOL_GS1_DATABAR_EXPANDED_STACKED;
            case "AZTECCODE_FULLRANGE" -> Printer.SYMBOL_AZTECCODE_FULLRANGE;
            case "AZTECCODE_COMPACT" -> Printer.SYMBOL_AZTECCODE_COMPACT;
            case "DATAMATRIX_SQUARE" -> Printer.SYMBOL_DATAMATRIX_SQUARE;
            case "DATAMATRIX_RECTANGLE_8" -> Printer.SYMBOL_DATAMATRIX_RECTANGLE_8;
            case "DATAMATRIX_RECTANGLE_12" -> Printer.SYMBOL_DATAMATRIX_RECTANGLE_12;
            case "DATAMATRIX_RECTANGLE_16" -> Printer.SYMBOL_DATAMATRIX_RECTANGLE_16;
            default -> Printer.SYMBOL_PDF417_STANDARD;
        };
    }

    public static int parseSymbolLevel(String level) {
        if (level == null) {
            return Printer.PARAM_DEFAULT;
        }
        return switch (level.toUpperCase()) {
            case "LEVEL_0" -> Printer.LEVEL_0;
            case "LEVEL_1" -> Printer.LEVEL_1;
            case "LEVEL_2" -> Printer.LEVEL_2;
            case "LEVEL_3" -> Printer.LEVEL_3;
            case "LEVEL_4" -> Printer.LEVEL_4;
            case "LEVEL_5" -> Printer.LEVEL_5;
            case "LEVEL_6" -> Printer.LEVEL_6;
            case "LEVEL_7" -> Printer.LEVEL_7;
            case "LEVEL_8" -> Printer.LEVEL_8;
            case "LEVEL_L" -> Printer.LEVEL_L;
            case "LEVEL_M" -> Printer.LEVEL_M;
            case "LEVEL_Q" -> Printer.LEVEL_Q;
            case "LEVEL_H" -> Printer.LEVEL_H;
            default -> Printer.PARAM_DEFAULT;
        };
    }


    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> castToMap(Object obj) {
        if (obj instanceof HashMap) {
            return (HashMap<K, V>) obj;
        }
        throw new IllegalArgumentException("Object is not a HashMap");
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
                Object value = textObject.get("value");
                if (value instanceof String) {
                    command.put("addText", value);
                } else if(value instanceof JSONArray textArray) {
                    StringBuilder combinedText = new StringBuilder();

                    for (int j = 0; j < textArray.length(); j++) {
                        combinedText.append(textArray.getString(j));
                        combinedText.append("\n"); // Add a newline between texts
                    }

                    command.put("addText", combinedText.toString());
                }else {
                    throw new IllegalArgumentException("Unsupported value type for addText: " + value.getClass().getName());
                }

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
                // 正则表达式匹配所有图片类型的 base64 数据
                String regex = "data:image/(jpeg|png|gif|bmp|webp|svg\\+xml);base64[^\\s]*";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(base64Value);
                // 替换匹配到的字符串
                base64Value = matcher.replaceAll("");

                imageCommand.put("value", base64Value);

                imageCommand.put("x", imageObject.optInt("x", 0));
                imageCommand.put("y", imageObject.optInt("y", 0));
                imageCommand.put("width", imageObject.optInt("width", 0));
                imageCommand.put("height", imageObject.optInt("height", 0));
                command.put("addBase64Image", imageCommand);
            }
            if (jsonObject.has("addHLine")) {
                command.put("addHLine", jsonObject.getJSONObject("addHLine")); // Store as JSONObject for further parsing
            }
            if (jsonObject.has("addVLineBegin") && jsonObject.has("addVLineEnd")) {
                command.put("addVLineBegin", jsonObject.getJSONObject("addVLineBegin")); // Store as JSONObject for further parsing
                command.put("addVLineEnd", jsonObject.getJSONObject("addVLineEnd")); // Store as JSONObject for further parsing
            }

            if (jsonObject.has("addBarcode")) {
                JSONObject barcodeObject = jsonObject.getJSONObject("addBarcode");
                HashMap<String, Object> barcodeCommand = new HashMap<>();
                barcodeCommand.put("value", barcodeObject.getString("value"));
                if (barcodeObject.has("type")) {
                    barcodeCommand.put("type", barcodeObject.getString("type"));
                }

                if (barcodeObject.has("hri")) {
                    barcodeCommand.put("hri", barcodeObject.getString("hri"));
                }

                if (barcodeObject.has("font")) {
                    barcodeCommand.put("font", barcodeObject.getString("font"));
                }

                barcodeCommand.put("width", barcodeObject.optInt("width", 2));
                barcodeCommand.put("height", barcodeObject.optInt("height", 100));
                command.put("addBarcode", barcodeCommand);
            }

            if (jsonObject.has("addSymbol")) {
                JSONObject symbolObject = jsonObject.getJSONObject("addSymbol");
                HashMap<String, Object> symbolCommand = new HashMap<>();
                symbolCommand.put("value", symbolObject.getString("value"));
                String symbolType = "";
                if (symbolObject.has("type")) {
                    symbolType = symbolObject.getString("type");
                    symbolCommand.put("type", symbolType);
                }

                if(symbolType.startsWith("AZTECCODE")) {
                    if (symbolObject.has("level")) {
                        symbolCommand.put("level", symbolObject.optInt("level", 23));
                    }
                } else {
                    if (symbolObject.has("level")) {
                        symbolCommand.put("level", symbolObject.getString("level"));
                    }
                }

                symbolCommand.put("width", symbolObject.optInt("width", 2));
                symbolCommand.put("height", symbolObject.optInt("height", 3));
                symbolCommand.put("size", symbolObject.optInt("size", 0));

                command.put("addSymbol", symbolCommand);
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
