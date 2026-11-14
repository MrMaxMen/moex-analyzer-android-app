# MOEX Assets Analyzer for Android

An Android app that analyzes funds traded on the Moscow Exchange. It downloads price history from the public MOEX ISS API, fits an exponential trend to each fund, and shows yield, maximum drawdown, and how steady the growth is.

It helps find accumulating funds (money market funds, floating-rate bond funds, etc.) with high and stable returns among hundreds of ETFs.

## Install

Requires Android 8.0 or newer.

1. Download `MoexAnalyzer-v<version>.apk` from [Releases](https://github.com/MrMaxMen/moex-analyzer-android-app/releases).
2. Open the file on your phone and allow installing from this source if Android asks.

## Usage

1. Search for funds by ticker and add them with the plus button.
2. Choose the period to analyze.
3. Read the results table. Swipe sideways to see all columns.

## Metrics

| Column | Meaning |
|---|---|
| Daily / Monthly / Annual / Total | Growth rates of the fitted trend (Annual is CAGR) |
| Max DD | Largest drop from a previous price peak |
| R² | How close the price chart is to a smooth exponential (1 means perfectly smooth) |

An accumulating fund reinvests its income, so its price grows roughly exponentially. The app fits a straight line to the logarithm of the daily closing prices, and the slope gives the growth rate.

## Limitations

- Only accumulating funds are supported. Funds that pay out income show payouts as price drops, which distorts the trend.
- Fees, taxes, and spreads are not taken into account.

## Build from source

Requires JDK 25 and the Android SDK.

```
git clone https://github.com/MrMaxMen/moex-analyzer-android-app.git
cd moex-analyzer-android-app
./gradlew assembleDebug
```

Built with Kotlin, Jetpack Compose, Ktor, and kotlinx.serialization.

## Disclaimer

For educational and informational purposes only. This is not investment advice. Past performance does not guarantee future results.

## Author

Maxim Menshchikov ([MrMaxMen](https://github.com/MrMaxMen))