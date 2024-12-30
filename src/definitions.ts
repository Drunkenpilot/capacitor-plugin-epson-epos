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
  /**
   * @requires
   */
  target: string;
  /**
   * @requires
   */
  instructions: PrintInstruction[];
  modelCode?: EpsonEposPrinterSerie;
  /**
   * @default 'ANK'
   */
  langCode?: string;
}

export interface PrintInstruction {
  method: string;
  value?: any;
}
