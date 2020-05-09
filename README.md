# Internet Checker

Library to easily check if the device has an internet connection available, with `LiveData` update when the internet state changes.

## How to use

### Initialize

Initialize in your `Application` or `Activity`

```kotlin
    ...
    InternetChecker.init(context)
    ...
```

### Check the internet state

You can check if has internet connection directaly with

```kotlin
    InternetChecker.isInternetAvailable
```

Or using `LiveData`

```kotlin
    InternetChecker.liveDataIsInternetAvailable.observe(/* boolean */)
```
