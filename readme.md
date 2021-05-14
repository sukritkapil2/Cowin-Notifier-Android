# Cowin Notifier Android

[![GitHub license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Cowin Notifier is an android app that runs in the background and informs you whenever an available slot is found for the mentioned slot and age group.

![Cowin Notifier Logo](https://github.com/sukritkapil2/Cowin-Notifier-Android/blob/master/app/src/main/ic_launcher_cowin_notifier-playstore.png?raw=true)

## Unqiue Features

### Background Application
* Will continuously run in the background and ping the server every 10 seconds, so that you will know immediately whenever a slot is found.
### Run Once Listen Forever
* Notifier checks for the next 7 days from the current date not the selected date. So the user can keep the app running and not worry about changing the date again and again.
### Bad Network? - It handles it well
* The notifier takes care of bad network conditions. It will keep running even if internet is not available and whenever the interent is available it will again start pinging the server
### Minimal data usage
* Minimal data usage is there. It only gets a JSON response every 10 seconds. So even after 4-5 days of continuous background running, the maximum data usage will not exceed 10 Mb

## Installation

Download the .apk file for your android phone from here.

[Google Drive](https://drive.google.com/file/d/11pKVg08d-ilzCI8Xjnn7z_VidSVGOK1H/view?fbclid=IwAR3HC07pNP84zIolLENgABhifg-RV8XslrS2iVd996tzwC0Cu4Kj1bXMmOk)

[Media Fire](http://www.mediafire.com/file/if5h59wgok8b0h5/Cowin_Notifier_v1.3.1.apk/file)

## Usage

After installation, you can add a pincode or select a district from the edit place section.

Then you can view slots or start a notification service for the selected place and mentioned age group, by clicking on the 'Start Notification Service'.

The application can be closed as well after starting the service as it will start a foregorund service that will notify you whenever a slot is available.

## Demo

Here is a working demo of the application

https://user-images.githubusercontent.com/56361047/118229879-62cd6500-b4aa-11eb-96c3-3dff535f3734.mp4

## Languages and Libraries

* Java
* Retrofit
* Material UI
* Gson

## Environment Setup

Download the source code and simply import it as a project in android studio.
Let it sync all the libraries after which you can start building the code.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
