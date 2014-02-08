/*
  Web server for arduino with ethernet shield
  Normal queries show temperature from thermistor on A0
  Query with the line "applicationName: Glass Hello Home" 
  And then query with the line "command: [info|on|off]"
  will get info or turn the led on or off.
 */

#include <SPI.h>
#include <Ethernet.h>
#include <ThermistorSensor.h>

// Indoor Thermistor
#define THERMISTOR_PIN_A A0
// Outdoor Thermistor
#define THERMISTOR_PIN_B A1

// LED Pin
#define LED_PIN 7

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192,168,62,177);

// Initialize the Ethernet server library
// with the IP address and port you want to use 
// (port 80 is default for HTTP):
EthernetServer server(80);

// Thermistor object
ThermistorSensor thermistorA(THERMISTOR_PIN_A);
ThermistorSensor thermistorB(THERMISTOR_PIN_B);

String message;

void setup() {
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
  
  // Reserve & initialize space for string
  message.reserve(1024);
  message = "";
  
  // Initialize LED off
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);

  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
}


void loop() {
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client) {
    Serial.println("new client");
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    
    // Checks if glass is connecting
    boolean isGlass = false;
    
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        Serial.write(c);
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n' && currentLineIsBlank) {
          // send a standard http response header
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");  // the connection will be closed after completion of the response
	  client.println("Refresh: 5");  // refresh the page automatically every 5 sec
          client.println();
          client.println("<!DOCTYPE HTML>");
          client.println("<html>");
          // output Temperature
          client.print("Inside Temperature: ");
          client.print(thermistorA.getReading());
          client.print((char)186); // degree symbol
          client.println("C<br />");
          client.print("Outside Temperature: ");
          client.print(thermistorB.getReading());
          client.print((char)186); // degree symbol
          client.println("C<br />");
          
          client.println("</html>");
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
          //Serial.print("::");
          //Serial.println(message);
          
          if (message.startsWith("Connection: ")) {
            Serial.println("Got a connection.");
            if (message.endsWith("Keep-Alive")) {
              Serial.println("stayin alive.");
            }
          }
          else if (message.startsWith("applicationName: ")) {
            Serial.println("Got an applicationName");
            // Get Info!
            // get info
            if (message.endsWith("Glass Hello Home")) {
              Serial.println("Query from Google Glass!");
              // Got a message from google glass Hello Home! app
              isGlass = true;
            } else {
              Serial.print("Got unusual application name == ");
              Serial.println(message);
            }
          } 
          else if (isGlass && message.startsWith("command: ")) {
            Serial.println("Got a command!");
            // Commands!
            // command: [on|off]            
            if (message.endsWith("info")) {
              Serial.println("Sending info");
              // Output Temperature
              client.println("HTTP/1.1 200 OK");
              client.println("Content-Type: text/html");
              client.println("Connection: close");  // the connection will be closed after completion of the response              
              client.println();
              client.print("Inside: ");
              client.print(thermistorA.getReading());
              client.print((char)186); // degree symbol
              client.println("C");
              client.print("Outside: ");
              client.print(thermistorB.getReading());
              client.print((char)186); // degree symbol
              client.println("C");       
              break;
            }
            else if (message.endsWith("led on")) {
              Serial.println("Turning on LED");
              digitalWrite(LED_PIN, HIGH);
              client.println("HTTP/1.1 200 OK");
              client.println("Content-Type: text/html");
              client.println("Connection: close");  // the connection will be closed after completion of the response              
              client.println();
              client.println("Led turned on.");
              break;
            }
            else if (message.endsWith("led off")) {
              Serial.println("Turning off LED");
              digitalWrite(LED_PIN, LOW);
              client.println("HTTP/1.1 200 OK");
              client.println("Content-Type: text/html");
              client.println("Connection: close");  // the connection will be closed after completion of the response              
              client.println();
              client.println("Led turned off.");
              break;
            } 
            else {
              Serial.print("Unknown command == ");
              Serial.println(message);
              client.println("HTTP/1.1 200 OK");
              client.println("Content-Type: text/html");
              client.println("Connection: close");  // the connection will be closed after completion of the response              
              client.println();
              client.println("Unknown command:");
              client.println(message);
              break;
            }
          }
          
          // Clear message for next line
          message = "";
        } 
        else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
          
          // Message on current line
          message += c;
        }
      }
    }
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disconnected\n-----");
  }
}

