# Internet Checker

Library to easily check if the device has an internet connection available, with `LiveData` update when the internet state changes.

## Install

Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency

```
	dependencies {
	        implementation 'com.github.Vnicius:internet-checker:1.0.0'
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
