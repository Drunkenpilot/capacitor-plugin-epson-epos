export interface EpsonEposPlugin {
  // echo(options: { value: string }): Promise<{ value: string }>;
  // connect(options: { ip: string; port: number }): Promise<{ success: boolean }>;
  // print(options: { data: string }): Promise<{ success: boolean }>;
  startDiscovery(options: StartDiscoveryOptions): Promise<DiscoveryResult>;
  stopDiscovery(): Promise<{ message: string }>;
}

export interface StartDiscoveryOptions {
  timeout: number;
  broadcast?: string;
  portType?: 'ALL' | 'TCP' | 'BLUETOOTH' | 'USB';
}

export interface DiscoveryResult {
  printers: { PrinterName: string; Target: string }[];
}
