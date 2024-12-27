import { WebPlugin } from '@capacitor/core';

import type { EpsonEposPlugin } from './definitions';

export class EpsonEposWeb extends WebPlugin implements EpsonEposPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async connect(options: { ip: string; port: number }): Promise<{ success: boolean }> {
    console.log('Connect to printer via Web: ', options);
    return { success: true };
  }

  async print(options: { data: string }): Promise<{ success: boolean }> {
    console.log('Print data via Web: ', options);
    return { success: true };
  }

  async discoverPrinters(): Promise<{ printers: { ip: string; port: number }[] }> {
    console.log('Discovering printers on Web...');
    // Example: Returning a mock list of printers
    return {
      printers: [
        { ip: '192.168.1.100', port: 9100 },
        { ip: '192.168.1.101', port: 9100 },
      ],
    };
  }
}
