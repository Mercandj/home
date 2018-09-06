
- Connect via WIFI:
    - adb connect <ip>
- Flash img
    - https://www.raspberrypi.org/documentation/installation/installing-images/mac.md
    - diskutil list
    - diskutil unmountDisk /dev/disk<disk# from diskutil>
    - sudo dd bs=1m if=image.img of=/dev/rdisk<disk# from diskutil> conv=sync