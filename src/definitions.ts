export interface EpsonEposPlugin {
  requestPermission(): Promise<{ success: boolean }>;
  startDiscovery(options: DiscoveryOptions): Promise<DiscoveryResult>;
  stopDiscovery(): Promise<{ message: string }>;
  // Print with flexible instructions
  print(options: PrintOptions): Promise<{ success: boolean }>;
  // Finalize the printer to clean up resources
  finalizePrinter(): Promise<{ message: string }>;
}

export interface DiscoveryOptions {
  /**
   * @default 10000 (milliseconds)
   */
  timeout?: number;
  /**
   * @default  '255.255.255.255'
   */
  broadcast?: string;
  /**
   * @default 'ALL'
   */
  portType?: EpsonEposPortType;
}

export interface DiscoveryResult {
  printers: DiscoveryResultPrinter[];
}

export interface DiscoveryResultPrinter {
  PrinterName: string;
  Target: string;
}

export type EpsonEposPortType = 'ALL' | 'TCP' | 'BLUETOOTH' | 'USB';

export type EpsonEposPrinterSerie =
  | 'TM_M10'
  | 'TM_M30'
  | 'TM_P20'
  | 'TM_P60'
  | 'TM_P60II'
  | 'TM_P80'
  | 'TM_T20'
  | 'TM_T60'
  | 'TM_T70'
  | 'TM_T81'
  | 'TM_T82'
  | 'TM_T83'
  | 'TM_T83III'
  | 'TM_T88'
  | 'TM_T90'
  | 'TM_T90KP'
  | 'TM_T100'
  | 'TM_U220'
  | 'TM_U330'
  | 'TM_L90'
  | 'TM_H6000'
  | 'TM_M30II'
  | 'TS_100'
  | 'TM_M50'
  | 'TM_T88VII'
  | 'TM_L90LFC'
  | 'EU_M30'
  | 'TM_L100'
  | 'TM_P20II'
  | 'TM_P80II'
  | 'TM_M30III'
  | 'TM_M50II'
  | 'TM_M55'
  | 'TM_U220II';

export interface PrintOptions {
  target: string;
  instructions: PrintInstruction[];
  modelCode?: EpsonEposPrinterSerie;
  /**
   * @default 'ANK'
   */
  langCode?: string;
}

export interface PrintInstruction {
  /**
   * Integer from 0 to 65535
   * @description Specifies the horizontal print start position in dots.
   * @description Horizontal print start position (in dots )
   */
  addHPosition?: number;
  /**
   * Integer from 0 to 255
   * @description Line spacing (in dots)
   */
  addLineSpace?: number;
  /**
   * Integer from 0 to 255
   * @description Specifies the paper feed amount (in lines).
   * @description Paper feed amount (in lines)
   */
  addFeedLine?: number;
  /**
   * Integer from 0 to 255
   * @description Specifies the paper feed amount (in dots).
   * @description Paper feed amount (in dots)
   */
  addFeedUnit?: number;
  addTextAlign?: TextAlign;
  addText?: PrintText;
  addTextStyle?: PrintTextStyle;
  addBase64Image?: PrintBase64Image;
  addBarcode?: PrintBarcode;
  addSymbol?: PrintSymbol;
  /**
   * @description Integer from 1 to 8
   */
  addTextSize?: [number, number];

  /**
   * @description Adds a horizontal ruled line print command to the command buffer.
   * @description Draws a horizontal ruled line.
   */
  addHLine?: PrintHorizontalLine;
  /**
   * @description Adds a command to start drawing a vertical ruled line to the command buffer.
   * @description Starts drawing a vertical line.
   */
  addVLineBegin?: PrintVerticalLine;
  /**
   * @description Adds a command to stop drawing a vertical ruled line to the command buffer.
   * @description Ends drawing a vertical line.
   */
  addVLineEnd?: Pick<PrintVerticalLine, 'lineId'>;

  /**
   * @description Adds a sheet cut command to the command buffer.
   * @description Specifies how to cut paper.
   */
  addCut?: Cut;

  /**
   * @description Specifies the drawer kick connector.
   * @description The drawer and optional external buzzer cannot be connected simultaneously.
   */
  addPulse?: PrintWithPulse;

  /**
   * @description Specifies the ESC/POS command.
   * @description Specifies the binary data.
   */
  addCommand?: BinaryType;

  /**
   * @description Adds a label sheet/black mark sheet feed command to the command buffer.
   */
  addFeedPosition?: FeedPosition;
  /**
   * @description Adds layout setting of the label sheet/black mark sheet to the command buffer.
   */
  addLayout?: PrintLayout;
}

// method: PrintInstructionMethod;
// value?: any;

export interface PrintTextStyle {
  /**
   * @default false
   */
  reverse?: boolean;
  /**
   * @default false
   */
  ul?: boolean;
  /**
   * @default false
   */
  em?: boolean;
}

export interface PrintText {
  value: string | string[];
  size?: [number, number];
  align?: TextAlign;
  style?: PrintTextStyle;
}

export interface PrintBarcode {
  /**
   * @description Specifies barcode data as a text string. specify a string in accordance with the standard of the barcode specified in type.
   * @description When specifying binary data which cannot be represented as a string, use the following escape sequences.
   * @description \xnn Control code (set nn in hexadecimal)
   * @description \\ Back slash
   */
  value: string;
  /**
   * @default CODE_39
   * @description Specifies the barcode type.
   */
  type: BarcodeType;
  /**
   * @default FONT_A
   * @description Specifies the HRI font.
   */
  font?: BarcodeFont;
  /**
   * @default HRI_BELOW
   * @description Specifies the HRI position.
   */
  hri?: BarcodeHri;
  /**
   * @default 2 Integer from 2 to 6
   * @description Specifies the width of a single module in dots.
   */
  width?: number;
  /**
   * @default 100 Integer from 1 to 255
   * @description Specifies the height of the barcode in dots.
   */
  height?: number;
}
export interface PrintSymbol {
  /**
   * @description Specify a string in accordance with the standard of the 2D symbol specified in type.
   * @description When specifying binary data which cannot be represented as a string, use the following escape sequences.
   * @description \xnn Control code (set nn in hexadecimal)
   * @description \\ Back slash
   */
  value: string;
  /**
   * @description Specifies the 2D symbol type.
   */
  type: SymbolType;
  /**
   * @description Specifies the error correction level.
   */
  level?: SymbolLevelPDF | SymbolLevelQRCode | SymbolLevelAztecCode;
  /**
   * The range differs depending on the 2D symbol type.
   * @default PDF417 from 2 to 8
   * @default QRCode from 3 to 16
   * @default MaxiCode from 1 to 255 (Ignored)
   * @default 2DGS1 DataBar from 2 to 8
   * @default Aztec from 2 to 16
   * @default DataMatrix from 2 to 16
   */
  width?: number;
  /**
   * The range differs depending on the 2D symbol type.
   * @default PDF417 from 2 to 8
   * @default Others from 1 to 255 (Ignored)
   */
  height?: number;
  /**
   * The range differs depending on the 2D symbol type.
   * @default PDF417 0
   * @default QRCode from 0 to 65535 (Ignored)
   * @default MaxiCode from 0 to 65535 (Ignored)
   * @default 2DGS1 Expanded Stacked 0 Specifies the maximum width of the 2D symbol (106 or more).
   * @default 2DGS1 Other Stacked from 0 to 65535 (Ignored)
   * @default Aztec from 0 to 65535 (Ignored)
   * @default DataMatrix from 0 to 65535 (Ignored)
   */
  size?: number;
}

export interface PrintLayout {
  type: LayoutType;
  /**
   * @description Specifies the paper width (in 0.1 mm units).
   * @description TM Printer Models Integer from 1 to 10000
   * @description POS Terminal Model Integer from 290 to 600
   */
  width: number;
  /**
   * @description Specifies the distance from the print reference mark to the next print reference mark (in 0.1mm units).
   * @description Receipt (without black mark) 0
   * @description TM Printer Models Integer from 1 to 10000
   * @description POS Terminal Model Integer from 0 to 1550
   */
  height: number;
  /**
   * @description Specifies the distance from the print reference mark to the top of the sheet (in 0.1mm units).
   * @description Receipt (without black mark) 0
   * @description TM Printer Models Receipt (with black mark) Integer from -9999 to 10000
   * @description TM Printer Models Label (without black mark) Integer from 0 to 10000
   * @description TM Printer Models Label (with black mark) Integer from -9999 to 10000
   * @description POS Terminal Model Receipt (with black mark) Integer from -150 to 1500
   * @description POS Terminal Model Label (without black mark) Integer from 0 to 1500
   * @description POS Terminal Model Label (with black mark) Integer from -15 to 1500
   */
  marginTop: number;
  /**
   * @description Specifies the distance from the eject reference mark to the bottom edge of the printable area (in 0.1mm units).
   * @description Receipt (with / without black mark) 0
   * @description TM Printer Models Label (without black mark) Integer from -9999 to 0
   * @description TM Printer Models Label (with black mark) Integer from -9999 to 10000
   * @description POS Terminal Model Label (without black mark) Integer from -15 to 0
   * @description POS Terminal Model Label (with black mark) Integer from -15 to 15
   */
  marginBottom: number;
  /**
   * @description Specifies the distance from the eject reference mark to the cut position (in 0.1mm units).
   * @description Receipt (without black mark) 0
   * @description TM Printer Models Receipt (with black mark) Integer from -9999 to 10000
   * @description TM Printer Models Label (without black mark) Integer from 0 to 10000
   * @description TM Printer Models Label (with black mark) Integer from 0 to 10000
   * @description POS Terminal Model Receipt (with black mark) Integer from -290 to 50
   * @description POS Terminal Model Label (without black mark) Integer from 0 to 50
   * @description POS Terminal Model Label (with black mark) Integer from 0 to 50
   */
  offsetCut: number;
  /**
   * @description Specifies the distance from the eject reference mark to the bottom edge of the label (in 0.1mm units).
   * @description Receipt (with without black mark) 0
   * @description TM Printer Models Label (without black mark) 0
   * @description TM Printer Models Label (with black mark) Integer from 0 to 10000
   * @description POS Terminal Model Label (without black mark) 0
   * @description POS Terminal Model Label (with black mark) Integer from 0 to 15
   */
  offsetLabel: number;
}

export interface PrintWithPulse {
  /**
   * @default 2pin
   */
  drawer?: DrawerPin;
  /**
   * @default 100
   */
  time?: PulseTime;
}

export interface PrintHorizontalLine {
  /**
   * @description x1 Specifies the start position to draw a horizontal ruled line (in dots). Integer from 0 to 65535
   * @description x2 Specifies the end position to draw a horizontal ruled line (in dots). Integer from 0 to 65535
   */
  position: [number, number];
  /**
   * @description Specifies the ruled line type.
   */
  lineStyle: LineStyle;
}
export interface PrintVerticalLine {
  /**
   * @description Integer from 0 to 65535
   */
  position: number;
  /**
   * @description Specifies the ruled line type.
   */
  lineStyle: LineStyle;
  /**
   * @description Returns the ID of the ruled line printed by this API.
   */
  lineId: number[];
}

export type TextAlign = 'left' | 'right' | 'center';

export type LayoutType = 'receipt' | 'receipt_bm' | 'label' | 'label_bm';

export type FeedPosition = 'peeling' | 'cutting' | 'current_tof' | 'next_tof';

export type Cut =
  | 'cut_feed'
  | 'cut_no_feed'
  | 'cut_reserve'
  | 'full_cut_feed'
  | 'full_cut_no_feed'
  | 'full_cut_reserve';

export type DrawerPin = '2pin' | '5pin';
export type PulseTime = 'pulse_100' | 'pulse_200' | 'pulse_300' | 'pulse_300' | 'pulse_400' | 'pulse_500';

export interface PrintBase64Image {
  /**
   * Base64 image string
   */
  value: string;
  /**
   * @description Specifies the horizontal start position of the print area (in pixels).
   */
  x?: number;
  /**
   * @description Specifies the vertical start position of the print area (in pixels).
   */
  y?: number;
  /**
   * Specifies the width of the print area (in pixels).
   */
  width?: number;
  /**
   * Specifies the height of the print area (in pixels).
   */
  height?: number;
}

export type LineStyle = 'thin' | 'medium' | 'thick' | 'thin_double' | 'medium_double' | 'thick_double';

/**
 * Types representing the allowed barcode types.
 */
export type BarcodeType =
  | 'UPC_A'
  | 'UPC_E'
  | 'EAN13'
  | 'JAN13'
  | 'EAN8'
  | 'JAN8'
  | 'ITF'
  | 'CODA_BAR'
  | 'CODE_39' // Default type
  | 'CODE_93'
  | 'CODE_128'
  | 'CODE_128_AUTO'
  | 'GS1_128'
  | 'GS1_DATA_BAR_OMNIDIRECTIONAL'
  | 'GS1_DATA_BAR_TRUNCATED'
  | 'GS1_DATA_BAR_LIMITED'
  | 'GS1_DATA_BAR_EXPANDED';

/**
 * Types representing the allowed barcode fonts.
 */
export type BarcodeFont =
  | 'FONT_A' // Default font
  | 'FONT_B'
  | 'FONT_C'
  | 'FONT_D'
  | 'FONT_E';

/**
 * Types representing the allowed barcode HRI (Human Readable Interpretation) positions.
 */
export type BarcodeHri =
  | 'HRI_NONE' // No HRI
  | 'HRI_ABOVE' // HRI above the barcode
  | 'HRI_BELOW' // Default: HRI below the barcode
  | 'HRI_BOTH'; // HRI above and below the barcode

export type SymbolType =
  | 'PDF417_TRUNCATED'
  | 'QRCODE_MODEL_1'
  | 'QRCODE_MODEL_2'
  | 'QRCODE_MICRO'
  | 'MAXICODE_MODE_2'
  | 'MAXICODE_MODE_3'
  | 'MAXICODE_MODE_4'
  | 'MAXICODE_MODE_5'
  | 'MAXICODE_MODE_6'
  | 'GS1_DATABAR_STACKED'
  | 'GS1_DATABAR_STACKED_OMNIDIRECTIONAL'
  | 'GS1_DATABAR_EXPANDED_STACKED'
  | 'AZTECCODE_FULLRANGE'
  | 'AZTECCODE_COMPACT'
  | 'DATAMATRIX_SQUARE'
  | 'DATAMATRIX_RECTANGLE_8'
  | 'DATAMATRIX_RECTANGLE_12'
  | 'DATAMATRIX_RECTANGLE_16';

/**
 * @default LEVEL_1
 */
export type SymbolLevelPDF =
  | 'LEVEL_0'
  | 'LEVEL_1'
  | 'LEVEL_2'
  | 'LEVEL_3'
  | 'LEVEL_4'
  | 'LEVEL_5'
  | 'LEVEL_6'
  | 'LEVEL_7'
  | 'LEVEL_8';

/**
 * @default LEVEL_M
 */
export type SymbolLevelQRCode = 'LEVEL_L' | 'LEVEL_M' | 'LEVEL_Q' | 'LEVEL_H';

/**
 * @default 23 Integer from 5 to 95,
 */
export type SymbolLevelAztecCode = number;
