# OptionType — Java

A comprehensive Java 24 library of option contract type definitions covering the full spectrum of standard and exotic derivatives. Built on a sealed interface with 15 permitted record and class variants, it provides a type-safe, exhaustive taxonomy of option structures — from plain vanilla European and American contracts through path-dependent exotics (Asian, Barrier, Lookback, Cliquet) to multi-asset and modified-payoff forms — with full Jackson JSON polymorphic serialization and Java 21+ pattern-matching support out of the box.

---

## Features

- **15 option type variants** modelled as a Java `sealed interface` with `record` implementations for compact, immutable value semantics
- **Standard options** — European, American, Bermuda
- **Path-dependent exotics** — Asian, Barrier, Lookback, Cliquet
- **Multi-asset options** — Rainbow, Spread, Exchange
- **Modified-payoff options** — Power, Quanto, Binary, Chooser, Compound
- **Sub-type enums** with compact ordinal layout for BarrierType, AsianAveragingType, BinaryType, LookbackType, and RainbowType
- **Full Jackson JSON polymorphic serialization** via `@JsonTypeInfo` / `@JsonSubTypes` — serialize and deserialize any variant through the `OptionType` interface
- **Classification predicates** — `isExotic()`, `isPathDependent()`, `isMultiAsset()`, `isKnockIn()`, `isKnockOut()`
- **`displayName()`** on every variant for human-readable labels
- **`OptionBasicType`** record combining `Side` (CALL / PUT) with `OptionStyle` (EUROPEAN / AMERICAN)
- Requires `--enable-preview` for sealed types; configured automatically in the Maven build

---

## Requirements

| Dependency | Minimum version |
|---|---|
| Java | 24+ |
| Maven | 3.9+ |

---

## Build & Test

```bash
mvn compile
mvn test        # 45 tests
mvn package
```

The Maven Surefire configuration automatically passes `--enable-preview` and enables ZGC Generational mode, so no extra flags are needed on the command line.

---

## Project Structure

```
OptionType-java/
├── pom.xml                                          # Maven build — Java 24, preview features, ZGC
└── src/
    ├── main/java/com/optiontype/
    │   ├── OptionType.java                          # Sealed interface — 15 permitted variants
    │   ├── Prelude.java                             # Package-level re-exports / convenience
    │   └── model/
    │       ├── AsianAveragingType.java              # ARITHMETIC | GEOMETRIC
    │       ├── BarrierType.java                     # UP_AND_IN | UP_AND_OUT | DOWN_AND_IN | DOWN_AND_OUT
    │       ├── BinaryType.java                      # CASH_OR_NOTHING | ASSET_OR_NOTHING | GAP
    │       ├── LookbackType.java                    # FIXED_STRIKE | FLOATING_STRIKE
    │       ├── OptionBasicType.java                 # Record combining Side + OptionStyle
    │       ├── OptionStyle.java                     # EUROPEAN | AMERICAN
    │       ├── RainbowType.java                     # BEST_OF | WORST_OF
    │       └── Side.java                            # CALL | PUT
    └── test/java/com/optiontype/
        └── OptionTypeTest.java                      # 45 JUnit 5 tests
```

---

## Option Type Taxonomy

| Type | Category | Fields | Description |
|---|---|---|---|
| `European` | Standard | — | Exercise only at expiry |
| `American` | Standard | — | Exercise any time before expiry |
| `Bermuda` | Standard | `exerciseDates: List<Double>` | Exercise on a fixed set of specified dates |
| `Asian` | Path-dependent | `averagingType: AsianAveragingType` | Payoff based on the average price over the option's life |
| `Barrier` | Path-dependent | `barrierType: BarrierType`, `barrierLevel: double`, `rebate: Double` | Activates or deactivates when the underlying crosses a barrier level; optional rebate paid on knock-out |
| `Lookback` | Path-dependent | `lookbackType: LookbackType` | Payoff determined by the maximum or minimum price observed over the option's life |
| `Cliquet` | Path-dependent | `resetDates: List<Double>` | Strike resets periodically at the forward price on each reset date |
| `Binary` | Structural | `binaryType: BinaryType` | Pays a fixed amount (cash or asset) at expiry if in-the-money |
| `Chooser` | Structural | `chooseDate: double` | Holder elects call or put on a specified choice date |
| `Compound` | Structural | `underlyingOption: OptionType` | An option whose underlying is itself another option |
| `Power` | Modified payoff | `exponent: double` | Payoff of the underlying option raised to a specified power |
| `Quanto` | Modified payoff | `exchangeRate: double` | Payoff settled in a domestic currency at a fixed exchange rate |
| `Rainbow` | Multi-asset | `rainbowType: RainbowType`, `numAssets: int` | Payoff based on the best-of or worst-of across multiple assets |
| `Spread` | Multi-asset | `secondAsset: double` | Payoff based on the spread (difference) between two assets |
| `Exchange` | Multi-asset | `secondAsset: double` | Right to exchange one asset for another at expiry |

---

## Sub-Type Enums

### AsianAveragingType

| Value | Description |
|---|---|
| `ARITHMETIC` | Average is the arithmetic mean of observed prices; standard for equity Asian options |
| `GEOMETRIC` | Average is the geometric mean; admits a closed-form Black–Scholes-style solution |

Helper predicates: `isArithmetic()`, `isGeometric()`.

---

### BarrierType

| Value | Trigger condition |
|---|---|
| `UP_AND_IN` | Option activates when the underlying price rises above the barrier |
| `UP_AND_OUT` | Option is cancelled when the underlying price rises above the barrier |
| `DOWN_AND_IN` | Option activates when the underlying price falls below the barrier |
| `DOWN_AND_OUT` | Option is cancelled when the underlying price falls below the barrier |

Helper predicates on `BarrierType`:

| Method | Returns `true` when |
|---|---|
| `isKnockIn()` | Variant is `UP_AND_IN` or `DOWN_AND_IN` |
| `isKnockOut()` | Variant is `UP_AND_OUT` or `DOWN_AND_OUT` |
| `isUp()` | Variant is `UP_AND_IN` or `UP_AND_OUT` |
| `isDown()` | Variant is `DOWN_AND_IN` or `DOWN_AND_OUT` |

The top-level `OptionType` interface delegates these: `optionType.isKnockIn()` and `optionType.isKnockOut()` return `true` only for `Barrier` instances with the matching sub-type.

---

### BinaryType

| Value | Description |
|---|---|
| `CASH_OR_NOTHING` | Pays a fixed cash amount if the option expires in-the-money; zero otherwise |
| `ASSET_OR_NOTHING` | Pays the value of the underlying asset if in-the-money; zero otherwise |
| `GAP` | Pays the difference between the underlying and a gap strike when above a trigger strike |

---

### LookbackType

| Value | Description |
|---|---|
| `FIXED_STRIKE` | Strike is fixed at inception; payoff references the maximum (call) or minimum (put) price observed over the option's life |
| `FLOATING_STRIKE` | Strike is set to the minimum (call) or maximum (put) price observed; holder always exercises at the most favourable historical price |

---

### RainbowType

| Value | Description |
|---|---|
| `BEST_OF` | Payoff references the best-performing asset among the basket |
| `WORST_OF` | Payoff references the worst-performing asset among the basket |

Helper predicates: `isBestOf()`, `isWorstOf()`.

---

## Quick Start

### Creating option types

```java
import com.optiontype.OptionType;
import com.optiontype.model.AsianAveragingType;
import com.optiontype.model.BarrierType;
import java.util.List;

// Standard options — no fields required
OptionType european = new OptionType.European();
OptionType american = new OptionType.American();

// Asian option with arithmetic averaging
OptionType asian = new OptionType.Asian(AsianAveragingType.ARITHMETIC);

// Barrier option — no rebate (null)
OptionType barrier = new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, null);

// Barrier option with rebate paid on knock-out
OptionType barrierWithRebate = new OptionType.Barrier(BarrierType.DOWN_AND_OUT, 80.0, 5.0);

// Bermuda option exercisable at days 30, 60, 90
OptionType bermuda = new OptionType.Bermuda(List.of(30.0, 60.0, 90.0));

// Compound option — option on a European option
OptionType compound = new OptionType.Compound(new OptionType.European());
```

---

### Classification predicates

```java
OptionType opt = new OptionType.Asian(AsianAveragingType.GEOMETRIC);
opt.isExotic();         // true  — not European or American
opt.isPathDependent();  // true  — Asian is path-dependent
opt.isMultiAsset();     // false — single underlying

OptionType barrier = new OptionType.Barrier(BarrierType.UP_AND_IN, 110.0, null);
barrier.isKnockIn();    // true
barrier.isKnockOut();   // false
barrier.isExotic();     // true
```

---

### Display names

Every variant implements `displayName()` returning a structured human-readable label:

```java
new OptionType.European().displayName();
// "European Option"

new OptionType.Asian(AsianAveragingType.ARITHMETIC).displayName();
// "Asian Option (Averaging Type: ARITHMETIC)"

new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, 5.0).displayName();
// "Barrier Option (Type: Up-And-In Barrier, Level: 120.0, Rebate: 5.0)"

new OptionType.Compound(new OptionType.European()).displayName();
// "Compound Option (Underlying: European Option)"
```

---

### Default value

```java
OptionType def = OptionType.defaultValue();  // returns new European()
```

---

## JSON Serialization

`OptionType` uses Jackson's `@JsonTypeInfo` and `@JsonSubTypes` annotations for full polymorphic serialization. The discriminator field `"type"` is written into every JSON object and used on deserialization to reconstruct the correct concrete variant — no manual switching required.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optiontype.OptionType;
import com.optiontype.model.BarrierType;

ObjectMapper mapper = new ObjectMapper();

// Serialize
OptionType opt = new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, 5.0);
String json = mapper.writeValueAsString(opt);
// {"type":"Barrier","barrierType":"UP_AND_IN","barrierLevel":120.0,"rebate":5.0}

// Deserialize — returns OptionType.Barrier
OptionType restored = mapper.readValue(json, OptionType.class);

// Round-trip any variant through the interface type
OptionType asian = new OptionType.Asian(AsianAveragingType.ARITHMETIC);
String asianJson = mapper.writeValueAsString(asian);
// {"type":"Asian","averagingType":"ARITHMETIC"}
OptionType restoredAsian = mapper.readValue(asianJson, OptionType.class);
```

> **Note:** `Compound` (option-on-option) is included in the `@JsonSubTypes` registration but contains a recursive `OptionType` field. Serialize and deserialize it with care; deeply nested compound structures may require custom handling.

---

## OptionBasicType

`OptionBasicType` is a lightweight record combining a `Side` (CALL or PUT) with an `OptionStyle` (EUROPEAN or AMERICAN). It is intended as a structural building block for higher-level order or position models.

```java
import com.optiontype.model.OptionBasicType;
import com.optiontype.model.Side;
import com.optiontype.model.OptionStyle;

OptionBasicType basic = new OptionBasicType(Side.CALL, OptionStyle.EUROPEAN);

basic.isCall();   // true
basic.isPut();    // false
basic.side();     // Side.CALL
basic.style();    // OptionStyle.EUROPEAN
```

The `Side` enum also carries `isCall()` / `isPut()` predicates directly on the enum constant, and `OptionStyle` distinguishes whether the contract permits early exercise (`AMERICAN`) or only expiry exercise (`EUROPEAN`).

---

## Pattern Matching

`OptionType` is a `sealed interface`, so the Java compiler enforces exhaustiveness in `switch` expressions and statements. Use pattern matching for concise, type-safe dispatch:

```java
switch (optionType) {
    case OptionType.European e   -> System.out.println("Simple European");
    case OptionType.Asian a      -> System.out.println("Asian with " + a.averagingType());
    case OptionType.Barrier b    -> System.out.println("Barrier at " + b.barrierLevel());
    case OptionType.Compound c   -> System.out.println("Compound wrapping " + c.underlyingOption().displayName());
    default                      -> System.out.println("Other exotic: " + optionType.displayName());
}
```

Because the interface is sealed, if all 15 permitted types are listed as `case` arms the compiler will flag the `default` as redundant — a useful completeness check when adding new variants.

---

## Dependencies

| Artifact | Version | Scope |
|---|---|---|
| `com.fasterxml.jackson.core:jackson-databind` | 2.17.1 | compile |
| `com.fasterxml.jackson.core:jackson-annotations` | 2.17.1 | compile |
| `org.slf4j:slf4j-api` | 2.0.13 | compile |
| `ch.qos.logback:logback-classic` | 1.5.6 | compile |
| `org.junit.jupiter:junit-jupiter` | 5.10.3 | test |

---

## License

See [LICENSE](LICENSE) for details.
