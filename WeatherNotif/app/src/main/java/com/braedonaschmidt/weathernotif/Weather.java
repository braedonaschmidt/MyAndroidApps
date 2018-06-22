package com.braedonaschmidt.weathernotif;

public class Weather {
	private double high, low;
	private String desc;
	
	public Weather(double high, double low, String desc) {
		this.high = high;
		this.low = low;
		this.desc = desc;
	}
	
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
