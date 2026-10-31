# BroadcastReceiver

**CSE 489 – Mobile Application Development | Assignment 2**

## Overview

BroadcastReceiver is an Android application built with **Kotlin and XML**. It demonstrates navigation using a drawer and fragments, custom and system broadcast receivers, pinch-to-zoom gestures and in-app audio/video playback.

## Features

- **Broadcast Receiver:** A spinner lets users choose between two operations:
  - **Custom Broadcast:** Enter a text message on the second screen and receive/display it through a custom `BroadcastReceiver` on the third screen.
  - **Battery Broadcast:** Read and display the phone's battery percentage using `ACTION_BATTERY_CHANGED`.
- **Image Scale:** Load an image from the internet with Glide, pinch to zoom in or out, drag while zoomed in, and reset the zoom.
- **Video:** Play a local MP4 video in the app using `VideoView` and playback controls.
- **Audio:** Play, pause, or stop a local MP3 using `MediaPlayer`. A seek bar displays playback progress and allows moving to a different time.

## Implementation

The navigation drawer in `MainActivity` switches between four fragments: `BroadcastFragment`, `ImageScaleFragment`, `VideoFragment`, and `AudioFragment`. `MessageInputActivity`, `CustomReceiverActivity`, and `BatteryActivity` handle the broadcast flows. `ZoomableImageView` handles image gestures.

**Technologies:** Kotlin, XML layouts, AndroidX, Material Components, Glide, `BroadcastReceiver`, `ScaleGestureDetector`, `VideoView`, and `MediaPlayer`.

## Running the Project

1. Open the `BroadcastReceiver` project in **Android Studio** and sync Gradle.
2. Ensure the media files exist at:
   - `app/src/main/res/raw/sample_video.mp4`
   - `app/src/main/res/raw/sample_audio.mp3`
3. Connect an Android device or start an emulator and run the `app` configuration.
4. Use the navigation drawer to explore each feature. An internet connection is needed to load the image.

---

