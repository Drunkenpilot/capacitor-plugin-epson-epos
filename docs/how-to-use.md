# How to use

## Install

```bash
npm install capacitor-plugin-epson-epos
npx cap sync
```

## Port Discovery

```typescript
import {
  EpsonEpos,
  DiscoveryOptions
} from "capacitor-plugin-epson-epos";

  async startDiscovery() {
 
    const options: DiscoveryOptions = {
        portType:  'TCP', // optional. or 'USB', 'BLUETOOTH',
        timeout: 10000,  // optional. a little bit longer when use 'TCP' or 'ALL'
        broadcast:  "255.255.255.255"  // optional.
    } 

    try {
      const result = await EpsonEpos.startDiscovery(options);
 
      console.log("Discovered printers:", result.printers);

    } catch (error) {
      console.error("Discovery error:", error);
      await loader.dismiss();
    }
  }
```

## Print Text

```typescript
import {
  EpsonEpos,
  PrintOptions
} from "capacitor-plugin-epson-epos";

  async startDiscovery() {
    const options: PrintOptions = {
      target: "TCP:DeviceMacAddress", // eg: tcp:11:xx:aa:00:nf
      instructions: [
        {
          addFeedLine: 3,
        },
        {
          addTextAlign: "center",
        },
        {
          addText: {
            value: `THE STORE 123 (555) 555 – 5555\n`,
          },
        },
        {
          addText: {
            value: [
              "7/01/07 16:58 6153 05 0191 134",
              "ST# 21 OP# 001 TE# 01 TR# 747",
              "400 OHEIDA 3PK SPRINGF  9.99 R",
            ],
            align: "left",
          },
        },
        {
          addText: {
            value: `Hello World\n`,
            align: "center",
            style: {
              em: true,
              ul: true,
            },
            size: [2, 2],
          },
        },
        {
          addFeedLine: 3,
        },

        {
          addCut: "cut_feed",
        },
      ],
      modelCode: "TM_T20",
    };
    try {
      const result = await EpsonEpos.print(options);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
```


## Print Image

```typescript
import {
  EpsonEpos,
  PrintOptions
} from "capacitor-plugin-epson-epos";

  async print() {
    const options: PrintOptions = {
      target: "TCP:DeviceMacAddress", // eg: tcp:11:xx:aa:00:nf
      instructions: [
        {
          addFeedLine: 3,
        },
        {
          addTextAlign: "center",
        },
        {
          addBase64Image: {
              value: `base64 image string...`, // with or without base64 prefix
              width: 376,
            }
        },
        {
          addFeedLine: 3,
        },

        {
          addCut: "cut_feed",
        },
      ],
      modelCode: "TM_T20",
    };
    try {
      const result = await EpsonEpos.print(options);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
```

## Print Barcode

```typescript
import {
  EpsonEpos,
  PrintOptions
} from "capacitor-plugin-epson-epos";

  async print() {
    const options: PrintOptions = {
      target: "TCP:DeviceMacAddress", // eg: tcp:11:xx:aa:00:nf
      instructions: [
        {
          addFeedLine: 3,
        },
        {
          addTextAlign: "center",
        },
        {
          addBarcode: {
              value: "01209457",
              type: "CODE_39",
              hri: "HRI_BELOW",
            }
        },
        {
          addFeedLine: 3,
        },

        {
          addCut: "cut_feed",
        },
      ],
      modelCode: "TM_T20",
    };
    try {
      const result = await EpsonEpos.print(options);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
```

## Print QRcode

```typescript
import {
  EpsonEpos,
  PrintOptions
} from "capacitor-plugin-epson-epos";

  async print() {
    const options: PrintOptions = {
      target: "TCP:DeviceMacAddress", // eg: tcp:11:xx:aa:00:nf
      instructions: [
        {
          addFeedLine: 3,
        },
        {
          addTextAlign: "center",
        },
        {
           addSymbol: {
              value: "https://www.tec-it.com",
              type: "QRCODE_MODEL_2",
              level: "LEVEL_Q",
              width: 8,
            }
        },
        {
          addFeedLine: 3,
        },

        {
          addCut: "cut_feed",
        },
      ],
      modelCode: "TM_T20",
    };
    try {
      const result = await EpsonEpos.print(options);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
```

## Print & Open Drawer

```typescript
import {
  EpsonEpos,
  PrintOptions
} from "capacitor-plugin-epson-epos";

  async print() {
    const options: PrintOptions = {
      target: "TCP:DeviceMacAddress", // eg: tcp:11:xx:aa:00:nf
      instructions: [
        {
           addPulse: {}
        },
      ],
      modelCode: "TM_T20",
    };
    try {
      const result = await EpsonEpos.print(options);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
```