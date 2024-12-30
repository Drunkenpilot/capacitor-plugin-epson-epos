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

| Prop         | Type                |
| ------------ | ------------------- |
| **`method`** | <code>string</code> |
| **`value`**  | <code>any</code>    |


### Type Aliases


#### EpsonEposPortType

<code>'ALL' | 'TCP' | 'BLUETOOTH' | 'USB'</code>


#### EpsonEposPrinterSerie

<code>'TM_M10' | 'TM_M30' | 'TM_P20' | 'TM_P60' | 'TM_P60II' | 'TM_P80' | 'TM_T20' | 'TM_T60' | 'TM_T70' | 'TM_T81' | 'TM_T82' | 'TM_T83' | 'TM_T83III' | 'TM_T88' | 'TM_T90' | 'TM_T90KP' | 'TM_T100' | 'TM_U220' | 'TM_U330' | 'TM_L90' | 'TM_H6000' | 'TM_M30II' | 'TS_100' | 'TM_M50' | 'TM_T88VII' | 'TM_L90LFC' | 'EU_M30' | 'TM_L100' | 'TM_P20II' | 'TM_P80II' | 'TM_M30III' | 'TM_M50II' | 'TM_M55' | 'TM_U220II'</code>

</docgen-api>
