export interface EpsonEposPlugin {
  startDiscovery(options: StartDiscoveryOptions): Promise<DiscoveryResult>;
  stopDiscovery(): Promise<{ message: string }>;
  // Print with flexible instructions
  print(options: PrintOptions): Promise<{ success: boolean }>;
  // Finalize the printer to clean up resources
  finalizePrinter(): Promise<{ message: string }>;
}

export interface StartDiscoveryOptions {
  /**
   * @default 10000
   * @description default value 10 seconds
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
   * @description Specifies the horizontal print start position in dots.
   * @description Horizontal print start position (in dots )
   * @description Integer from 0 to 65535
   */
  addHPosition?: number;
  /**
   * @description Integer from 0 to 255
   * @description Line spacing (in dots)
   */
  addLineSpace?: number;
  /**
   * @description Integer from 0 to 255
   * @description Specifies the paper feed amount (in lines).
   * @description Paper feed amount (in lines)
   */
  addFeedLine?: number;
  /**
   * @description Integer from 0 to 255
   * @description Specifies the paper feed amount (in dots).
   * @description Paper feed amount (in dots)
   */
  addFeedUnit?: number;
  addTextAlign?: PrintTextAlign;
  addText?: PrintText;
  addTextStyle?: PrintTextStyle;
  addBase64Image?: PrintBase64Image;
  addBarcode?: PrintBarcode;
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
  addCut?: PrintCut;

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
  addFeedPosition?: PrintFeedPosition;
  /**
   * @description Adds layout setting of the label sheet/black mark sheet to the command buffer.
   */
  addLayout?: PrintLayout;
}

// method: PrintInstructionMethod;
// value?: any;

export interface PrintTextStyle {
  /**
   * default false
   */
  reverse?: boolean;
  /**
   * default false
   */
  ul?: boolean;
  /**
   * default false
   */
  em?: boolean;
}

export interface PrintText {
  value: string;
  size?: [number, number];
  align?: PrintTextAlign;
  style?: PrintTextStyle;
}

export interface PrintBarcode {
  value: string;
}

export interface PrintLayout {
  type: PrintLayoutType;
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
  lineStyle: PrintLineStyle;
}
export interface PrintVerticalLine {
  /**
   * @description Integer from 0 to 65535
   */
  position: number;
  /**
   * @description Specifies the ruled line type.
   */
  lineStyle: PrintLineStyle;
  /**
   * @description Returns the ID of the ruled line printed by this API.
   */
  lineId: number[];
}

export type PrintTextAlign = 'left' | 'right' | 'center';

export type PrintLayoutType = 'receipt' | 'receipt_bm' | 'label' | 'label_bm';

export type PrintFeedPosition = 'peeling' | 'cutting' | 'current_tof' | 'next_tof';

export type PrintCut =
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
   * value Base64 image string
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
   * @description Specifies the width of the print area (in pixels).
   */
  width?: number;
  /**
   * @description Specifies the height of the print area (in pixels).
   */
  height?: number;
}

export type PrintLineStyle = 'thin' | 'medium' | 'thick' | 'thin_double' | 'medium_double' | 'thick_double';
