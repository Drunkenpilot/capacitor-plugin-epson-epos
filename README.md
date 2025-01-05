# capacitor-plugin-epson-epos

A capacitorjs plugin for Epson Pos Printer

## Android
- ePos SDK: V2.29.0.a
- Only tested with TM_T20III Ethernet / TM_T20 USB

## Roadmap
- Add iOS support

## Install

```bash
npm install capacitor-plugin-epson-epos
npx cap sync
```

## How to use
- [See examples here](docs/how-to-use.md)

- [ePOS SDK Android - User's manual](docs/ePOS_SDK_Android_um_en_revAE.pdf)

## API

<docgen-index>

* [`requestPermission()`](#requestpermission)
* [`startDiscovery(...)`](#startdiscovery)
* [`stopDiscovery()`](#stopdiscovery)
* [`print(...)`](#print)
* [`finalizePrinter()`](#finalizeprinter)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestPermission()

```typescript
requestPermission() => Promise<{ success: boolean; }>
```

**Returns:** <code>Promise&lt;{ success: boolean; }&gt;</code>

--------------------


### startDiscovery(...)

```typescript
startDiscovery(options: DiscoveryOptions) => Promise<DiscoveryResult>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#discoveryoptions">DiscoveryOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#discoveryresult">DiscoveryResult</a>&gt;</code>

--------------------


### stopDiscovery()

```typescript
stopDiscovery() => Promise<{ message: string; }>
```

**Returns:** <code>Promise&lt;{ message: string; }&gt;</code>

--------------------


### print(...)

```typescript
print(options: PrintOptions) => Promise<{ success: boolean; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#printoptions">PrintOptions</a></code> |

**Returns:** <code>Promise&lt;{ success: boolean; }&gt;</code>

--------------------


### finalizePrinter()

```typescript
finalizePrinter() => Promise<{ message: string; }>
```

**Returns:** <code>Promise&lt;{ message: string; }&gt;</code>

--------------------


### Interfaces


#### DiscoveryResult

| Prop           | Type                                  |
| -------------- | ------------------------------------- |
| **`printers`** | <code>DiscoveryResultPrinter[]</code> |


#### DiscoveryResultPrinter

| Prop              | Type                |
| ----------------- | ------------------- |
| **`PrinterName`** | <code>string</code> |
| **`Target`**      | <code>string</code> |


#### DiscoveryOptions

| Prop            | Type                                                            | Default                           |
| --------------- | --------------------------------------------------------------- | --------------------------------- |
| **`timeout`**   | <code>number</code>                                             | <code>10000 (milliseconds)</code> |
| **`broadcast`** | <code>string</code>                                             | <code>'255.255.255.255'</code>    |
| **`portType`**  | <code><a href="#epsoneposporttype">EpsonEposPortType</a></code> | <code>'ALL'</code>                |


#### PrintOptions

| Prop               | Type                                                                    | Default            |
| ------------------ | ----------------------------------------------------------------------- | ------------------ |
| **`target`**       | <code>string</code>                                                     |                    |
| **`instructions`** | <code>PrintInstruction[]</code>                                         |                    |
| **`modelCode`**    | <code><a href="#epsoneposprinterserie">EpsonEposPrinterSerie</a></code> |                    |
| **`langCode`**     | <code>string</code>                                                     | <code>'ANK'</code> |


#### PrintInstruction

| Prop                  | Type                                                                                                      | Description             |
| --------------------- | --------------------------------------------------------------------------------------------------------- | ----------------------- |
| **`addHPosition`**    | <code>number</code>                                                                                       | Integer from 0 to 65535 |
| **`addLineSpace`**    | <code>number</code>                                                                                       | Integer from 0 to 255   |
| **`addFeedLine`**     | <code>number</code>                                                                                       | Integer from 0 to 255   |
| **`addFeedUnit`**     | <code>number</code>                                                                                       | Integer from 0 to 255   |
| **`addTextAlign`**    | <code><a href="#textalign">TextAlign</a></code>                                                           |                         |
| **`addText`**         | <code><a href="#printtext">PrintText</a></code>                                                           |                         |
| **`addTextStyle`**    | <code><a href="#printtextstyle">PrintTextStyle</a></code>                                                 |                         |
| **`addBase64Image`**  | <code><a href="#printbase64image">PrintBase64Image</a></code>                                             |                         |
| **`addBarcode`**      | <code><a href="#printbarcode">PrintBarcode</a></code>                                                     |                         |
| **`addSymbol`**       | <code><a href="#printsymbol">PrintSymbol</a></code>                                                       |                         |
| **`addTextSize`**     | <code>[number, number]</code>                                                                             |                         |
| **`addHLine`**        | <code><a href="#printhorizontalline">PrintHorizontalLine</a></code>                                       |                         |
| **`addVLineBegin`**   | <code><a href="#printverticalline">PrintVerticalLine</a></code>                                           |                         |
| **`addVLineEnd`**     | <code><a href="#pick">Pick</a>&lt;<a href="#printverticalline">PrintVerticalLine</a>, 'lineId'&gt;</code> |                         |
| **`addCut`**          | <code><a href="#cut">Cut</a></code>                                                                       |                         |
| **`addPulse`**        | <code><a href="#printwithpulse">PrintWithPulse</a></code>                                                 |                         |
| **`addCommand`**      | <code>BinaryType</code>                                                                                   |                         |
| **`addFeedPosition`** | <code><a href="#feedposition">FeedPosition</a></code>                                                     |                         |
| **`addLayout`**       | <code><a href="#printlayout">PrintLayout</a></code>                                                       |                         |


#### PrintText

| Prop        | Type                                                      |
| ----------- | --------------------------------------------------------- |
| **`value`** | <code>string \| string[]</code>                           |
| **`size`**  | <code>[number, number]</code>                             |
| **`align`** | <code><a href="#textalign">TextAlign</a></code>           |
| **`style`** | <code><a href="#printtextstyle">PrintTextStyle</a></code> |


#### PrintTextStyle

| Prop          | Type                 | Default            |
| ------------- | -------------------- | ------------------ |
| **`reverse`** | <code>boolean</code> | <code>false</code> |
| **`ul`**      | <code>boolean</code> | <code>false</code> |
| **`em`**      | <code>boolean</code> | <code>false</code> |


#### PrintBase64Image

| Prop         | Type                | Description                                         |
| ------------ | ------------------- | --------------------------------------------------- |
| **`value`**  | <code>string</code> | Base64 image string                                 |
| **`x`**      | <code>number</code> |                                                     |
| **`y`**      | <code>number</code> |                                                     |
| **`width`**  | <code>number</code> | Specifies the width of the print area (in pixels).  |
| **`height`** | <code>number</code> | Specifies the height of the print area (in pixels). |


#### PrintBarcode

| Prop         | Type                                                | Default                                |
| ------------ | --------------------------------------------------- | -------------------------------------- |
| **`value`**  | <code>string</code>                                 |                                        |
| **`type`**   | <code><a href="#barcodetype">BarcodeType</a></code> | <code>CODE_39</code>                   |
| **`font`**   | <code><a href="#barcodefont">BarcodeFont</a></code> | <code>FONT_A</code>                    |
| **`hri`**    | <code><a href="#barcodehri">BarcodeHri</a></code>   | <code>HRI_BELOW</code>                 |
| **`width`**  | <code>number</code>                                 | <code>2 Integer from 2 to 6</code>     |
| **`height`** | <code>number</code>                                 | <code>100 Integer from 1 to 255</code> |


#### PrintSymbol

| Prop         | Type                                                                                                                      | Description                                        | Default                         |
| ------------ | ------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------- | ------------------------------- |
| **`value`**  | <code>string</code>                                                                                                       |                                                    |                                 |
| **`type`**   | <code><a href="#symboltype">SymbolType</a></code>                                                                         |                                                    |                                 |
| **`level`**  | <code>number \| <a href="#symbollevelpdf">SymbolLevelPDF</a> \| <a href="#symbollevelqrcode">SymbolLevelQRCode</a></code> |                                                    |                                 |
| **`width`**  | <code>number</code>                                                                                                       | The range differs depending on the 2D symbol type. | <code>PDF417 from 2 to 8</code> |
| **`height`** | <code>number</code>                                                                                                       | The range differs depending on the 2D symbol type. | <code>PDF417 from 2 to 8</code> |
| **`size`**   | <code>number</code>                                                                                                       | The range differs depending on the 2D symbol type. | <code>PDF417 0</code>           |


#### PrintHorizontalLine

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`position`**  | <code>[number, number]</code>                   |
| **`lineStyle`** | <code><a href="#linestyle">LineStyle</a></code> |


#### PrintVerticalLine

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`position`**  | <code>number</code>                             |
| **`lineStyle`** | <code><a href="#linestyle">LineStyle</a></code> |
| **`lineId`**    | <code>number[]</code>                           |


#### PrintWithPulse

| Prop         | Type                                            | Default           |
| ------------ | ----------------------------------------------- | ----------------- |
| **`drawer`** | <code><a href="#drawerpin">DrawerPin</a></code> | <code>2pin</code> |
| **`time`**   | <code><a href="#pulsetime">PulseTime</a></code> | <code>100</code>  |


#### PrintLayout

| Prop               | Type                                              |
| ------------------ | ------------------------------------------------- |
| **`type`**         | <code><a href="#layouttype">LayoutType</a></code> |
| **`width`**        | <code>number</code>                               |
| **`height`**       | <code>number</code>                               |
| **`marginTop`**    | <code>number</code>                               |
| **`marginBottom`** | <code>number</code>                               |
| **`offsetCut`**    | <code>number</code>                               |
| **`offsetLabel`**  | <code>number</code>                               |


### Type Aliases


#### EpsonEposPortType

<code>'ALL' | 'TCP' | 'BLUETOOTH' | 'USB'</code>


#### TextAlign

<code>'left' | 'right' | 'center'</code>


#### BarcodeType

Types representing the allowed barcode types.

<code>'UPC_A' | 'UPC_E' | 'EAN13' | 'JAN13' | 'EAN8' | 'JAN8' | 'ITF' | 'CODA_BAR' | 'CODE_39' | 'CODE_93' | 'CODE_128' | 'CODE_128_AUTO' | 'GS1_128' | 'GS1_DATA_BAR_OMNIDIRECTIONAL' | 'GS1_DATA_BAR_TRUNCATED' | 'GS1_DATA_BAR_LIMITED' | 'GS1_DATA_BAR_EXPANDED'</code>


#### BarcodeFont

Types representing the allowed barcode fonts.

<code>'FONT_A' | 'FONT_B' | 'FONT_C' | 'FONT_D' | 'FONT_E'</code>


#### BarcodeHri

Types representing the allowed barcode HRI (Human Readable Interpretation) positions.

<code>'HRI_NONE' | 'HRI_ABOVE' | 'HRI_BELOW' | 'HRI_BOTH'</code>


#### SymbolType

<code>'PDF417_TRUNCATED' | 'QRCODE_MODEL_1' | 'QRCODE_MODEL_2' | 'QRCODE_MICRO' | 'MAXICODE_MODE_2' | 'MAXICODE_MODE_3' | 'MAXICODE_MODE_4' | 'MAXICODE_MODE_5' | 'MAXICODE_MODE_6' | 'GS1_DATABAR_STACKED' | 'GS1_DATABAR_STACKED_OMNIDIRECTIONAL' | 'GS1_DATABAR_EXPANDED_STACKED' | 'AZTECCODE_FULLRANGE' | 'AZTECCODE_COMPACT' | 'DATAMATRIX_SQUARE' | 'DATAMATRIX_RECTANGLE_8' | 'DATAMATRIX_RECTANGLE_12' | 'DATAMATRIX_RECTANGLE_16'</code>


#### SymbolLevelPDF

<code>'LEVEL_0' | 'LEVEL_1' | 'LEVEL_2' | 'LEVEL_3' | 'LEVEL_4' | 'LEVEL_5' | 'LEVEL_6' | 'LEVEL_7' | 'LEVEL_8'</code>


#### SymbolLevelQRCode

<code>'LEVEL_L' | 'LEVEL_M' | 'LEVEL_Q' | 'LEVEL_H'</code>


#### SymbolLevelAztecCode

<code>number</code>


#### LineStyle

<code>'thin' | 'medium' | 'thick' | 'thin_double' | 'medium_double' | 'thick_double'</code>


#### Pick

From T, pick a set of properties whose keys are in the union K

<code>{ [P in K]: T[P]; }</code>


#### Cut

<code>'cut_feed' | 'cut_no_feed' | 'cut_reserve' | 'full_cut_feed' | 'full_cut_no_feed' | 'full_cut_reserve'</code>


#### DrawerPin

<code>'2pin' | '5pin'</code>


#### PulseTime

<code>'pulse_100' | 'pulse_200' | 'pulse_300' | 'pulse_300' | 'pulse_400' | 'pulse_500'</code>


#### FeedPosition

<code>'peeling' | 'cutting' | 'current_tof' | 'next_tof'</code>


#### LayoutType

<code>'receipt' | 'receipt_bm' | 'label' | 'label_bm'</code>


#### EpsonEposPrinterSerie

<code>'TM_M10' | 'TM_M30' | 'TM_P20' | 'TM_P60' | 'TM_P60II' | 'TM_P80' | 'TM_T20' | 'TM_T60' | 'TM_T70' | 'TM_T81' | 'TM_T82' | 'TM_T83' | 'TM_T83III' | 'TM_T88' | 'TM_T90' | 'TM_T90KP' | 'TM_T100' | 'TM_U220' | 'TM_U330' | 'TM_L90' | 'TM_H6000' | 'TM_M30II' | 'TS_100' | 'TM_M50' | 'TM_T88VII' | 'TM_L90LFC' | 'EU_M30' | 'TM_L100' | 'TM_P20II' | 'TM_P80II' | 'TM_M30III' | 'TM_M50II' | 'TM_M55' | 'TM_U220II'</code>

</docgen-api>
