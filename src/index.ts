import { registerPlugin } from '@capacitor/core';

import type { EpsonEposPlugin } from './definitions';

const EpsonEpos = registerPlugin<EpsonEposPlugin>('EpsonEpos', {
  web: () => import('./web').then((m) => new m.EpsonEposWeb()),
});

export * from './definitions';
export { EpsonEpos };
