import { WebPlugin } from '@capacitor/core';

import type { EpsonEposPlugin } from './definitions';

export class EpsonEposWeb extends WebPlugin implements EpsonEposPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
