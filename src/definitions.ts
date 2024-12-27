export interface EpsonEposPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  connect(options: { ip: string; port: number }): Promise<{ success: boolean }>;
  print(options: { data: string }): Promise<{ success: boolean }>;
  discoverPrinters(): Promise<{ printers: { ip: string; port: number }[] }>;
}
