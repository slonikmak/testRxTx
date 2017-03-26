#include <Wire.h>

#include <Multiservo.h>

#include <Servo.h>

//* Simple Serial ECHO script : Written by ScottC 03/07/2012 *//*

//* Use a variable called byteRead to temporarily store   the data coming from the computer *//*
byte byteRead;
int num, b1, b2, b3, resA, resB, resY;
Servo servo;
Multiservo servo6;
Multiservo servo9;
Multiservo servo10;
Multiservo servo11;

void setup() {
	// Turn the Serial Protocol ON
	Serial.begin(9600);
	servo6.attach(6);
	servo10.attach(10);
	servo11.attach(11);
	servo9.attach(9);
}

void loop() {
	//delay(100);
	//Serial.print("aaa");
//*  check if data has been sent from the computer: *//*
	if (Serial.available() > 0) {
		int a = Serial.read();
		if (a == 97) {
			while (Serial.available() < 0);
			while (Serial.available() <= 0);
			b1 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b2 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b3 = (Serial.read() - '0');
			resA = b1 * 100 + b2 * 10 + b3;

			//Serial.print("s");
			while (Serial.available() <= 0);
			b1 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b2 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b3 = (Serial.read() - '0');
			resB = b1 * 100 + b2 * 10 + b3;

			//Serial.print("e");
			//Serial.print("s");
			while (Serial.available() <= 0);
			b1 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b2 = (Serial.read() - '0');
			while (Serial.available() <= 0);
			b3 = (Serial.read() - '0');
			resY = b1 * 100 + b2 * 10 + b3;
			if (resA > 180 || resB > 180 || resY > 180) {
				return;
			}
			else {
				Serial.println(resA);
				servo6.write(resA);
				servo9.write(resB);
				Serial.println(resB);
				servo10.write(resY);
				Serial.println(resY);
			}


		}
		else return;
		//num = (Serial.read()-'0');
		//Serial.println("s");

		//Serial.print("e");
		//Serial.println(res);
		//switch (num) {
		//case 6:
		//if(res<100) res=100;
		//servo6.write(res);
		// break;
		//case 9:
		//servo9.write(res);
		//break;
		//case 0:
		//servo10.write(res);
		// break;
		//case 1:
		//servo11.write(res);
		//break;
		//}

	}
}
