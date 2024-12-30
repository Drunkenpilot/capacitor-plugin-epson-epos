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
* [Interfaces](#interfaces)

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


### Interfaces


#### DiscoveryResult

| Prop           | Type                                                    |
| -------------- | ------------------------------------------------------- |
| **`printers`** | <code>{ PrinterName: string; Target: string; }[]</code> |


#### StartDiscoveryOptions

| Prop            | Type                                                |
| --------------- | --------------------------------------------------- |
| **`timeout`**   | <code>number</code>                                 |
| **`broadcast`** | <code>string</code>                                 |
| **`portType`**  | <code>'ALL' \| 'TCP' \| 'BLUETOOTH' \| 'USB'</code> |

</docgen-api>
