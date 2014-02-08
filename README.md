GoogleGlassHomeAutomation
=========================

This is a google glass application to allow you to do some simple home automation.

The arduino webserver code is also included in the `arduino_webserver` folder.


![Imgur](http://i.imgur.com/bSrnav2.jpg)

Say `Hello Home` via google glass and you can communicate with an arduino webserver, getting indoor/outdoor temperature at home or controlling the lights

This is demonstration code to help get you on your way to developing apps that can interact with the real world.

Unfortunately, Don't expect to be able to download and run this code and get any useful data (unless you have the same setup)


# Commands implemented so far

Say `Okay Glass, hello home` and then you can say:

* Any variation of `what's the temperature?"
** It will display the indoor and outdoor temperatures in the apartment
* Any variation of `turn the lights on`
** It will enable a switch (in this case turning on the LED)
* Any variation of `turn the lights off`
** It will disable a switch (in this case turning on the LED)
