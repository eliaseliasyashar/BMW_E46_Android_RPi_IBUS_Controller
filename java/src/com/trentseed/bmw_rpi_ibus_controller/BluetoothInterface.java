package com.trentseed.bmw_rpi_ibus_controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

/**
 * Handles bluetooth communication with Raspberry Pi
 *
 */
public class BluetoothInterface {
	
	// bluetooth objects
	public static Activity mActivity;
	public static List<IBUSPacket> mArrayListIBUSActivity = new ArrayList<IBUSPacket>();
	public static BluetoothAdapter mBluetoothAdapter;
	public static BluetoothDevice mBluetoothDevice;
	public static BluetoothSocket mBluetoothSocket;
	public static InputStream mBluetoothInputStream;
	public static OutputStream mBluetoothOutputStream;
	public static UUID serviceUUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
	public static String remoteBluetoothAddress = "5C:AC:4C:C8:E2:7E";
	public static ConnectedThread listenThread;
	
	/**
	 * Connects to RaspberryPi via bluetooth
	 */
	public static boolean ConnectToRaspberryPi(){
		try{
			// connect to device and get input stream
			mArrayListIBUSActivity = new ArrayList<IBUSPacket>();
			mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(remoteBluetoothAddress);
			mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(serviceUUID);
			mBluetoothSocket.connect();
			mBluetoothInputStream = mBluetoothSocket.getInputStream();
			mBluetoothOutputStream = mBluetoothSocket.getOutputStream();
	
			// start listening for data on new thread
			listenThread = new ConnectedThread();
			listenThread.start();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Disconnect bluetooth RFCOMM connection
	 */
	public static void Disconnect(){
		try{
			mBluetoothSocket.close();
		}catch(Exception e){
			Log.d("BMW", e.getMessage());
		}
		
		mBluetoothAdapter = null;
		mBluetoothDevice = null;
		mBluetoothSocket = null;
	}
	
	/**
	 * Displays each paired bluetooth device info via Toast notification
	 * @param activity
	 */
	public static void ViewPairedDevices(Activity activity){
		// show paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
		    for (BluetoothDevice device : pairedDevices) {
		        Toast.makeText(activity, device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();
		    }
		}
	}
}