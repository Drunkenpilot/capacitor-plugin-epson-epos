import { registerPlugin } from '@capacitor/core';

import type { EpsonEposPlugin } from './definitions';

export * from './definitions';
export { EpsonEpos };
const EpsonEpos = registerPlugin<EpsonEposPlugin>('EpsonEpos', {
  web: () => import('./web').then((m) => new m.EpsonEposWeb()),
});
