package org.solq.dht.test.rxtxcom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SimpleRead implements SerialPortEventListener { // SerialPortEventListener
							     // 监听器,我的理解是独立开辟一个线程监听串口数据
    static CommPortIdentifier portId; // 串口通信管理类
    InputStream inputStream; // 从串口来的输入流
    OutputStream outputStream;// 向串口输出的流
    SerialPort serialPort; // 串口的引用

    public SimpleRead() {
	try {
	    serialPort = (SerialPort) portId.open("myApp", 2000);// 打开串口名字为myapp,延迟为2毫秒
	} catch (PortInUseException e) {
	}
	try {
	    inputStream = serialPort.getInputStream();
	    outputStream = serialPort.getOutputStream();
	} catch (IOException e) {
	}
	try {
	    serialPort.addEventListener(this); // 给当前串口天加一个监听器
	} catch (TooManyListenersException e) {
	}
	serialPort.notifyOnDataAvailable(true); // 当有数据时通知
	try {
	    serialPort.setSerialPortParams(2400, SerialPort.DATABITS_8, // 设置串口读写参数
		    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	} catch (UnsupportedCommOperationException e) {
	}
    }

    public void serialEvent(SerialPortEvent event) {// SerialPortEventListener
						    // 的方法,监听的时候会不断执行
	switch (event.getEventType()) {
	case SerialPortEvent.BI:
	case SerialPortEvent.OE:
	case SerialPortEvent.FE:
	case SerialPortEvent.PE:
	case SerialPortEvent.CD:
	case SerialPortEvent.CTS:
	case SerialPortEvent.DSR:
	case SerialPortEvent.RI:
	case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	    break;
	case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据,并且给串口返回数据
	    byte[] readBuffer = new byte[20];
	    try {
		while (inputStream.available() > 0) {
		    int numBytes = inputStream.read(readBuffer);
		}
		outputStream.write("xiaogang".getBytes());
		System.out.println(new String(readBuffer));
	    } catch (IOException e) {
	    }
	    break;
	}
    }

    public static void main(String[] args) {
	try {
	    Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers(); // 得到当前连接上的端口
	    while (portList.hasMoreElements()) {
		portId = (CommPortIdentifier) portList.nextElement();
		System.out.println("name : " + portId.getName());
		if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {// 判断如果端口类型是串口
		    if (portId.getName().equals("COM3")) { // 判断如果COM3端口已经启动就连接
			SimpleRead reader = new SimpleRead(); // 实例一个
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}