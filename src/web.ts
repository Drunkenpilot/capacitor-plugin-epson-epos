import { CapacitorException, ExceptionCode, WebPlugin } from '@capacitor/core';

import type { DiscoveryOptions, DiscoveryResult, EpsonEposPlugin, PrintOptions } from './definitions';

export class EpsonEposWeb extends WebPlugin implements EpsonEposPlugin {
  async requestPermission(): Promise<{ success: boolean }> {
    console.log('Simulating requestPermission');
    this.createUnavailableException();
    return Promise.resolve({ success: true });
  }

  async startDiscovery(options: DiscoveryOptions): Promise<DiscoveryResult> {
    console.log('Simulating discovery with options:', options);

    // Simulate a list of discovered printers
    const printers = [
      { PrinterName: 'Printer1', Target: '192.168.1.100' },
      { PrinterName: 'Printer2', Target: '192.168.1.101' },
    ];

    return new Promise((resolve) => setTimeout(() => resolve({ printers }), options.timeout));
  }

  async stopDiscovery(): Promise<{ message: string }> {
    console.log('Simulating stopping discovery');
    return Promise.resolve({ message: 'Discovery stopped (simulated)' });
  }

  async print(options: PrintOptions): Promise<{ success: boolean }> {
    console.log(options);
    console.log('Simulating print');
    this.createUnavailableException();
    return Promise.resolve({ success: true });
  }

  async finalizePrinter(): Promise<{ message: string }> {
    console.log('Simulating finalize printer object');
    return Promise.resolve({ message: 'Printer task Finalized' });
  }

  private createUnavailableException(): CapacitorException {
    return new CapacitorException(
      'This Barcode Scanner plugin method is not available on this platform.',
      ExceptionCode.Unavailable,
    );
  }
}
