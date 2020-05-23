# Internet Checker

[![Release](https://jitpack.io/v/Vnicius/internet-checker.svg)](https://jitpack.io/#Vnicius/internet-checker)

Library to easily check if the device has an internet connection available, with `LiveData` update when the internet state changes.

## Install

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency

```gradle
dependencies {
	implementation 'com.github.Vnicius:internet-checker:1.1.0'
}
```

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
