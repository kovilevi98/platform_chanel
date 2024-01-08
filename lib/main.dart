import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Channel Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _response = 'No data';
  var methodChannel = const MethodChannel('com.test.messages');

  void startServiceInPlatform() async {
    if (Platform.isAndroid) {
      String response = await methodChannel.invokeMethod('startService');
      debugPrint(response);
      setState(() {
        _response = response;
      });
    }
  }

  void stopServiceInPlatform() async {
    if (Platform.isAndroid) {
      String response = await methodChannel.invokeMethod('stopService');
      debugPrint(response);
      setState(() {
        _response = response;
      });
    }
  }

  void getBatteryLevel() async {
    String response = await methodChannel.invokeMethod('getBatteryLevel');
    debugPrint(response);
    setState(() {
      _response = response;
    });
  }

  void getLocation() async {
    String response = await methodChannel.invokeMethod('getLocation');
    debugPrint(response);
    if (response == 'permission not granted') {}
    setState(() {
      _response = response;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Response'),
            Text(_response),
            MaterialButton(
              color: Colors.green,
              child: const Text('Start service'),
              onPressed: () {
                startServiceInPlatform();
              },
            ),
            MaterialButton(
              color: Colors.red,
              child: const Text('Stop service'),
              onPressed: () {
                stopServiceInPlatform();
              },
            ),
            MaterialButton(
              color: Colors.blue,
              child: const Text('Get battery level'),
              onPressed: () {
                getBatteryLevel();
              },
            ),
            MaterialButton(
              color: Colors.orange,
              child: const Text('Get location'),
              onPressed: () {
                getLocation();
              },
            ),
          ],
        ),
      ),
    );
  }
}
