# RFID Project - Yocto Scarthgap for Raspberry Pi 0 2W
This repository contains the custom Yocto metadata and configuration used to build a minimal RFID-enabled image for the Raspberry Pi Zero 2W.
## Project Structure
This build is based on the Scarthgap release of Yocto. Your build directory should look like this:
```text
.
├── build/
├── meta-openembedded/
├── meta-raspberrypi/
├── meta-rfid/
└── poky/
```
## Setup and Installation
1. **Clone Dependencies**:
 Ensure you have poky, meta-openembedded, and meta-raspberrypi set to the scarthgap branch.
2. **Add Custom Layer**: 
Place the meta-rfid folder from this repo into your root project directory.
3. **Initialize Environment**:
```bash
source poky/oe-init-build-env build
```
4. **Configure Build**:
Copy the provided local.conf into your build/conf/ directory:
```bash
cp ../local.conf conf/local.conf
```
5. **Connectivity Setup (WiFi)**
Before building, you must provide your own network credentials. Edit the following file:
meta-rfid/recipes-connectivity/connman/connman/wifi.config
Change these lines to match your router:
```text
Name = YOUR_SSID_HERE
Passphrase = YOUR_PASSWORD_HERE
```
6. **Build the Image** 
```bash
bitbake core-image-minimal
```
7. **Generate the Image** 
Once configured, run the following command to generate the .wic image:
```bash
ls -lh tmp/deploy/images/raspberrypi0-2w-64/core-image-minimal*.wic.bz2
 bzip2 -dfk tmp/deploy/images/raspberrypi0-2w-64/core-image-minimal-raspberrypi0-2w-64.rootfs.wic.bz2
 ```
The resulting image will be located in build/tmp/deploy/images/raspberrypi0-2w-64/.

## Access & Debugging
Once the image is flashed and booted, you can access the Pi using these methods:
1. **SSH via Network (mDNS)**
Since debug-tweaks is enabled, you can log in as root without a password:
```bash
ping raspberrypi0-2w-64.local
ssh root@raspberrypi0-2w-64.local
```
2. **Serial Debugging (FTDI)**
If you are using an FTDI/Serial-to-USB adapter, connect to the Pi's UART pins and run:
```bash
sudo screen /dev/ttyUSB0 115200
```
## Monitoring & Logs
 The system uses systemd to manage the RFID project. The application is split into two services: rfid_hw (Hardware Interface) and rfid_logic (Application Logic).
1. **Monitor Both Services in Real-time**
To see the live output from both the hardware and logic layers simultaneously:
```bash
sudo journalctl -u rfid_hw -u rfid_logic -f
```
2. **Check Service Status**
If you need to check if a specific part of the app is active or has failed:
```bash
systemctl status rfid_hw
systemctl status rfid_logic
```
3. **Restarting the Project**
```bash
sudo systemctl restart rfid_hw
sudo systemctl restart rfid_logic
```
