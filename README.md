GoogleGlassHomeAutomation
=========================

Say `Hello Home` via google glass and you can communicate with an arduino webserver, asking for the `temperature` or `turning on/off the lights` (the little LED for now).

![Imgur](http://i.imgur.com/bSrnav2.jpg)

Don't expect to be able to download and run this code and expect it to work unless you have the same setup sadly, instead the code is there to learn the tools for making your own.

The arduino webserver code is also included in the `arduino_webserver` folder.

# Commands implemented so far

Say `Okay Glass, hello home` and then you can say:

* Any variation of `what's the temperature?` - It will display the indoor and outdoor temperatures in the apartment
* Any variation of `turn the lights on` - It will enable a switch (in this case turning on the LED)
* Any variation of `turn the lights off` - It will disable a switch (in this case turning on the LED)
