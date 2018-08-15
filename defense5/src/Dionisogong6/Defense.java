package Dionisogong6;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.*;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Defense {
	static EV3TouchSensor touch_sensor = new EV3TouchSensor(SensorPort.S1);
	static EV3UltrasonicSensor front_sonar = new EV3UltrasonicSensor(SensorPort.S4);
	static EV3UltrasonicSensor rear_sonar = new EV3UltrasonicSensor(SensorPort.S2);
	static NXTLightSensor light = new NXTLightSensor(SensorPort.S3);
	
	static RegulatedMotor push_up = Motor.A;
	static RegulatedMotor left = Motor.B;
	static RegulatedMotor right = Motor.C;
	
	static int count=0;
	
	public static void i_will_be_back(){
		count=0;
		while(true){ 
			SampleProvider light_itr=light.getAmbientMode();
			float[] lightSample = new float[light_itr.sampleSize()];
			light_itr.fetchSample(lightSample, 0);
			SampleProvider distance1=front_sonar.getMode("Distance");
			float[] distSample1 = new float[distance1.sampleSize()];
			distance1.fetchSample(distSample1, 0);
			SampleProvider distance2=rear_sonar.getMode("Distance");
			float[] distSample2 = new float[distance2.sampleSize()];
			distance2.fetchSample(distSample2, 0);
			
			left.setSpeed(2000);
			right.setSpeed(2000);
			
			//System.out.println(lightSample[0]);
			if(lightSample[0]>=0.25){
				left.rotate(-90);
				right.rotate(90);
				try{
					Thread.sleep(100);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				if(count>=7){
					if(distSample1[0]<=1.5){
						left.backward();
						right.backward();
						try{
							Thread.sleep(500);
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					else if(distSample2[0]<=1.5){
						left.forward();
						right.forward();
						try{
							Thread.sleep(100);
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					else{
						left.forward();
						right.forward();
						try{
							Thread.sleep(100);
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
						//left.stop(true);
						//right.stop(true);
						count=0;
					}
				}
				count++;
			}
			else{
				left.stop(true);
				right.stop(true);
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start=System.currentTimeMillis();
		
		left.setSpeed(2000);
		right.setSpeed(2000);
		
		/*DEFENSE MODE*/
		while(true){
			long end=System.currentTimeMillis();
			if((end-start)/1000.0>=90.0)
				break;
			
			SampleProvider distance=front_sonar.getMode("Distance");
			float[] distSample = new float[distance.sampleSize()];
			distance.fetchSample(distSample, 0);
			SampleProvider touch_itr=touch_sensor.getTouchMode();
			float[] touchSample = new float[touch_itr.sampleSize()];
			touch_itr.fetchSample(touchSample, 0);
			
			System.out.println(touchSample[0]);
			
			if(distSample[0]<=0.15 || touchSample[0]==1.0){
				left.stop(true);
				right.stop(true);
				left.backward();
				right.backward();
				try{
					Thread.sleep(500);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				left.rotate(-60);
				right.rotate(60);
				try{
					Thread.sleep(1000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				i_will_be_back();
			}
			else{
				left.forward();
				right.forward();
				try{
					Thread.sleep(50);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				i_will_be_back();
			}
		}
	}

}
