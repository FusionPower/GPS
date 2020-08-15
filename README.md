# GPS
License:
  Android Tracker
  Copyright (C) 2020 Android-Tracker

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

...

Summary

This is an Android Studio Application that measures the time it takes to travel the euclidian distance from your location to ENES, Morelia, Mexico at your current speed using GPS services from your phone.

Information for installation

You must have “Android Studio” to run this app. You can download the latest version in this website:
https://developer.android.com/studio
You have to have kvm on your computer with the appropriate permissions. To do so you can follow the steps in this forum:
https://stackoverflow.com/questions/37300811/android-studio-dev-kvm-device-permission-denied
Through Android Studio, you can download any SDK that is missing or not up to date.
Make sure you have a virtual device installed. If you do not have one click on the AVD Manager shown below:
![Screenshot from 2020-08-14 19-36-45](https://user-images.githubusercontent.com/54119843/90303414-69737100-de73-11ea-9cad-90ab933b1142.png)
Then clic on “Create Virtual Device”. You can select any device you want as long as you download the android version 9.0 or above. We recommend using the Pixel 3.

After that, you can download the project from this Github repository and run it by clicking on the play button next to the device you selected. 

Introduction

One of the drawbacks in public transportation in Morelia is the time wasted waiting for a vehicle to arrive. If there were a way to know when the next bus is arriving at our school to pick us up, we could save some waiting-time. This is the motivation of our project.

Definition
We intend to set up a tracker for an android device. It ought to be updated automatically and show the true location of a moving vehicle using GPS. Then our app will estimate a time to get to ENES at the current pace. This iteration of the project shows your current location on a map and calculates the time it would take to get to ENES at your current speed.

Methodology
Software Tools
We will use Android Studio to develop our application. We are using Google's native API's for location services.

Application Operation
In this iteration of development, this is the general mode of operation:
The center of communication operations between the phone components is in “MainActivity.java”. The first thing we check is whether the user has granted access to the GPS services and has internet access to load the MapFragment. This action, along with some implementation dependencies, is specified in the project manifest. If so, we can start the service.

We run the  “Localizar.java” file and create the receiver to catch the coordinates. There, we use some builtin functions from various libraries (FusedLocationProviderClient, LocationCallback,etc. (some explained below)) to get the latitude and longitude. Then we broadcast them and catch them in “MainActivity.java”.

We know to catch them through the intent “LOCALIZAR” which was defined along with the receiver. All that is left is catching the coordinates and calculating the distance between the current coordinates and the last coordinates using the  Haverstine Formula (which take into account the Earth's curvature). We update the location every 10 seconds so from that information we can get speed. We use that speed to get the time it takes to ENES from the current location. We get that by using Haverstine's formula once again to calculate the distance from your current location to ENES and dividing it by your current velocity.

Haverstine's Formula

"a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
c = 2 ⋅ atan2( √a, √(1−a) )
d = R ⋅ c" 
(Veness, 2020)

This all happens internally however the time and distance information is not displayed until the user clics on "compute!". This is a button defined in the "mapa.xml" layout. It's behaviour is included in the "MainActivity.java" file in the function "onClic" which does the computation described above and prints it to the screen.

Functions
Location Callback: It gets notified with location changes or location indeterminations. It yields a LocationResult with which we can obtain latitude and longitude coordinates.

LocationRequest: It helps to specify personalized parameters (performance, interval update etc.) for the application. It is a parameter of the FusedLocationProviderClient.

mapFragment: It is a wrapper that facilitates the access to a map.

SetContentView: It is a method that renders information to the screen.

LocationBroadcastReciever: It listens to intents and extracts the latitude and longitude computations. Then it can move the marker.


Data source
An android phone will be our only data source. It is important that it has GPS activated and with the necessary permissions granted.

Usage
To use the application you just have to clic on the button "compute!"

Results
Tha application works mostly as expected. The biggest drawback is GPS feedback. There is generally a slight error with the exact location of the coordinates. Because of this we usually will get a velocity that is not precise. To counter this error, we have a parameter that detects if speed is very slow. If so, we assume it is due to GPS error and discard it as an unmoving object. This parameter will probably have to be fine tuned depending on the GPS on your individual phone. 

Another possible improvement to gain precision is to increase the rate of gps feedback. Fight now it happens every 10 seconds. If you increase this parameter you will have a chance to travel a larger distance and the GPS uncertainty will be increasingly negligible. However you will have to wait longet to get feedback from the app.

Below we provide some expected behaviour from the app.
Loading
![Screenshot_20200813_210928_com example ontoy](https://user-images.githubusercontent.com/54119843/90303448-a9d2ef00-de73-11ea-9110-4119a5326936.jpg)
Standing Still
![Screenshot_20200813_211326_com example ontoy](https://user-images.githubusercontent.com/54119843/90303801-cae90f00-de76-11ea-88e7-9ec405074328.jpg)
Traversing
![Screenshot_20200813_213516_com example ontoy](https://user-images.githubusercontent.com/54119843/90303457-beaf8280-de73-11ea-9da2-61fea7fe87bb.jpg)
![Screenshot_20200813_213519_com example ontoy](https://user-images.githubusercontent.com/54119843/90303460-c4a56380-de73-11ea-80f5-cd4d3ec101f5.jpg)
![Screenshot_20200813_213627_com example ontoy](https://user-images.githubusercontent.com/54119843/90303462-ce2ecb80-de73-11ea-8661-e85ef08d11c8.jpg)
![Screenshot_20200813_213631_com example ontoy](https://user-images.githubusercontent.com/54119843/90303463-cff88f00-de73-11ea-9571-81024db2c16f.jpg)



Conclusions
This project is actually very complex to complete fully. As a first step the application has to communicate with each piece of hardware independently to coordinate them. The GPS has to receive the location through the FusedLocationClient which receives the coordinates and broadcasts them to the screen. However the next iteration would be to broadcast them to a server from which they can be retrieved and then broadcast to the screen. After that we could estimate the distance from the position of the closest bus that hasn't passed. Overall, the biggest drawback of the project was learning about Android Studio and its protocols. I hope the knowledge acquired in this assignment will transfer to my interests in the future.

Bibliography

IntelliJAmiya (2014), GPS Tracker cannot resolve in android studio. Retrieved on March 15 from: https://stackoverflow.com/questions/27637086/gpstracker-can-not-resolve-in-android-studio

Google Maps Plataform (2020), Location Data. Retrieved on March 15 from: https://developers.google.com/maps/documentation/android-sdk/location

Venees C (2020) in Movable Type Scripts. Retrieved on August 14 from: https://www.movable-type.co.uk/scripts/latlong.html

TheCodeCity (2020), GPS Location Tracker in Android. Retrieved on August 6th from:
https://www.youtube.com/watch?v=ycBVe3iYtqQ
