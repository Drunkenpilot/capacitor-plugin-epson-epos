# capacitor-plugin-epson-epos

A capacitorjs plugin for Epson Pos Printer

Under development

## Install

```bash
npm install capacitor-plugin-epson-epos
npx cap sync
```

## API

<docgen-index>

* [`startDiscovery(...)`](#startdiscovery)
* [`stopDiscovery()`](#stopdiscovery)
* [`print(...)`](#print)
* [`finalizePrinter()`](#finalizeprinter)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### startDiscovery(...)

```typescript
startDiscovery(options: StartDiscoveryOptions) => Promise<DiscoveryResult>
```

| Param         | Type                                                                    |
| ------------- | ----------------------------------------------------------------------- |
| **`options`** | <code><a href="#startdiscoveryoptions">StartDiscoveryOptions</a></code> |

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


#### StartDiscoveryOptions

| Prop            | Type                                                            | Default                        |
| --------------- | --------------------------------------------------------------- | ------------------------------ |
| **`timeout`**   | <code>number</code>                                             | <code>10000</code>             |
| **`broadcast`** | <code>string</code>                                             | <code>'255.255.255.255'</code> |
| **`portType`**  | <code><a href="#epsoneposporttype">EpsonEposPortType</a></code> | <code>'ALL'</code>             |


#### PrintOptions

| Prop               | Type                                                                    | Default            |
| ------------------ | ----------------------------------------------------------------------- | ------------------ |
| **`target`**       | <code>string</code>                                                     |                    |
| **`instructions`** | <code>PrintInstruction[]</code>                                         |                    |
| **`modelCode`**    | <code><a href="#epsoneposprinterserie">EpsonEposPrinterSerie</a></code> |                    |
| **`langCode`**     | <code>string</code>                                                     | <code>'ANK'</code> |


#### PrintInstruction

| Prop                  | Type                                                            |
| --------------------- | --------------------------------------------------------------- |
| **`addHPosition`**    | <code>number</code>                                             |
| **`addLineSpace`**    | <code>number</code>                                             |
| **`addFeedLine`**     | <code>number</code>                                             |
| **`addFeedUnit`**     | <code>number</code>                                             |
| **`addTextAlign`**    | <code><a href="#printtextalign">PrintTextAlign</a></code>       |
| **`addText`**         | <code><a href="#printtext">PrintText</a></code>                 |
| **`addTextStyle`**    | <code><a href="#printtextstyle">PrintTextStyle</a></code>       |
| **`addBase64Image`**  | <code><a href="#printbase64image">PrintBase64Image</a></code>   |
| **`addBarcode`**      | <code><a href="#printbarcode">PrintBarcode</a></code>           |
| **`addTextSize`**     | <code>[number, number]</code>                                   |
| **`addCut`**          | <code><a href="#printcut">PrintCut</a></code>                   |
| **`addPulse`**        | <code><a href="#printwithpulse">PrintWithPulse</a></code>       |
| **`addCommand`**      | <code>BinaryType</code>                                         |
| **`addFeedPosition`** | <code><a href="#printfeedposition">PrintFeedPosition</a></code> |
| **`addLayout`**       | <code><a href="#printlayout">PrintLayout</a></code>             |


#### PrintText

| Prop        | Type                                                      |
| ----------- | --------------------------------------------------------- |
| **`value`** | <code>string</code>                                       |
| **`size`**  | <code>[number, number]</code>                             |
| **`align`** | <code><a href="#printtextalign">PrintTextAlign</a></code> |
| **`style`** | <code><a href="#printtextstyle">PrintTextStyle</a></code> |


#### PrintTextStyle

| Prop          | Type                 | Description   |
| ------------- | -------------------- | ------------- |
| **`reverse`** | <code>boolean</code> | default false |
| **`ul`**      | <code>boolean</code> | default false |
| **`em`**      | <code>boolean</code> | default false |


#### PrintBase64Image

| Prop         | Type                | Description               |
| ------------ | ------------------- | ------------------------- |
| **`value`**  | <code>string</code> | value Base64 image string |
| **`x`**      | <code>number</code> |                           |
| **`y`**      | <code>number</code> |                           |
| **`width`**  | <code>number</code> |                           |
| **`height`** | <code>number</code> |                           |


#### PrintBarcode

| Prop        | Type                |
| ----------- | ------------------- |
| **`value`** | <code>string</code> |


#### PrintWithPulse

| Prop         | Type                                            | Default           |
| ------------ | ----------------------------------------------- | ----------------- |
| **`drawer`** | <code><a href="#drawerpin">DrawerPin</a></code> | <code>2pin</code> |
| **`time`**   | <code><a href="#pulsetime">PulseTime</a></code> | <code>100</code>  |


#### PrintLayout

| Prop               | Type                                                        |
| ------------------ | ----------------------------------------------------------- |
| **`type`**         | <code><a href="#printlayouttype">PrintLayoutType</a></code> |
| **`width`**        | <code>number</code>                                         |
| **`height`**       | <code>number</code>                                         |
| **`marginTop`**    | <code>number</code>                                         |
| **`marginBottom`** | <code>number</code>                                         |
| **`offsetCut`**    | <code>number</code>                                         |
| **`offsetLabel`**  | <code>number</code>                                         |


### Type Aliases


#### EpsonEposPortType

<code>'ALL' | 'TCP' | 'BLUETOOTH' | 'USB'</code>


#### PrintTextAlign

<code>'left' | 'right' | 'center'</code>


#### PrintCut

<code>'cut_feed' | 'cut_no_feed' | 'cut_reserve' | 'full_cut_feed' | 'full_cut_no_feed' | 'full_cut_reserve'</code>


#### DrawerPin

<code>'2pin' | '5pin'</code>


#### PulseTime

<code>'pulse_100' | 'pulse_200' | 'pulse_300' | 'pulse_300' | 'pulse_400' | 'pulse_500'</code>


#### PrintFeedPosition

<code>'peeling' | 'cutting' | 'current_tof' | 'next_tof'</code>


#### PrintLayoutType

<code>'receipt' | 'receipt_bm' | 'label' | 'label_bm'</code>


#### EpsonEposPrinterSerie

<code>'TM_M10' | 'TM_M30' | 'TM_P20' | 'TM_P60' | 'TM_P60II' | 'TM_P80' | 'TM_T20' | 'TM_T60' | 'TM_T70' | 'TM_T81' | 'TM_T82' | 'TM_T83' | 'TM_T83III' | 'TM_T88' | 'TM_T90' | 'TM_T90KP' | 'TM_T100' | 'TM_U220' | 'TM_U330' | 'TM_L90' | 'TM_H6000' | 'TM_M30II' | 'TS_100' | 'TM_M50' | 'TM_T88VII' | 'TM_L90LFC' | 'EU_M30' | 'TM_L100' | 'TM_P20II' | 'TM_P80II' | 'TM_M30III' | 'TM_M50II' | 'TM_M55' | 'TM_U220II'</code>

</docgen-api>
