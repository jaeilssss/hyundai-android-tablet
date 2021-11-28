package com.obigo.hkmotors.model;


/**
 * Class for the data of device list
 */
public class DeviceDataListItems {

	final static String TAG = "DeviceDataListItems";


	private String obdListName = "";
	private String obdListMac = "";

	public String getObdListName() {
		return obdListName;
	}

	public void setObdListName(String obdListName) {
		this.obdListName = obdListName;
	}

	public String getObdListMac() {
		return obdListMac;
	}

	public void setObdListMac(String obdListMac) {
		this.obdListMac = obdListMac;
	}
}
