# Flutter Channel Demo

A Flutter project that demonstrates how to use Flutter platform channels to communicate with native code in Android (Kotlin).

## Overview

This Flutter project showcases the implementation of platform channels to interact with native Android code. It provides examples of calling native methods to start and stop a service, retrieve battery level, and obtain the user's current location.

## Screenshots

![Screenshot 1](/screenshots/screenshot_1.png)

## Getting Started

To run this project, follow these steps:

1. Make sure you have Flutter installed. If not, you can download it from the official website: [Flutter Installation Guide](https://flutter.dev/docs/get-started/install)

2. Clone this repository:
    ```git clone https://github.com/nvtanhh/flutter-channel-demo.git``
3. Navigate to the project directory:
    ```cd flutter-channel-demo```
4. Run the app:
    ```flutter run```

## How It Works

The main Flutter code resides in `lib/main.dart`. It creates a simple app with buttons to trigger various native operations.

The app uses the `MethodChannel` from `flutter/services.dart` to communicate with native Android code. It calls native methods to start and stop a service, retrieve battery level, and get the user's current location.

The Android native code resides in `android/app/src/main/kotlin/com/example/channel_demo/MainActivity.kt`. It sets up a `MethodChannel` and listens for method calls from the Flutter app.

The native code provides implementations for the following methods:

- `startService`: Starts a service in Android and returns a success message.
- `stopService`: Stops the previously started service and returns a success message.
- `getBatteryLevel`: Retrieves the device's battery level and returns it as a string.
- `getLocation`: Obtains the user's current location (latitude and longitude) using the Fused Location Provider API and returns it as a string.

The app requests necessary permissions (e.g., ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION) from the user before trying to access sensitive features like location.



