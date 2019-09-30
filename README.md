# Hubitat
A working device driver for the ZConnect Garage Door Controller. 

After not finding any existing multichannel device drivers that would work with the ZConnect garage door controller I pieced this one together using/modifying an existing dual relay driver and adding another child device to handle the contact sensors.
When installed you should have two switches (one in the parent and one child) and two child contact sensors.
Note: The childon and childoff commands on the parent device do not work and apparently are not meant to.

Instructions - The Mfr fingerprint method seems to cause Hubitat to hang at "initializing" when including the module in the network so use the following procedure for the safest option.
1. Install the parent and both child device files.
2. Include your ZConnect module as a generic device - at this point check to see that secure inclusion has been successful.
3. Select ZConnect Garage Door Control as your new driver, then save.
4. Click Create Child Devices, rename them to anything you like.
5.Set whatever parameters you need to.

As per the manual, located here https://www.smartliving.com.au/pub/media/productattach/s/m/smart_living_zconnect_z-wave_garage_door_manual.pdf R1 is setup for momentary actuation (for door control) and R2 operates as a normal relay. S1 can be used for an IR safety beam and S2 for a door position switch.

Feel free to suggest any modifications that will make the driver more useful or streamlined.


