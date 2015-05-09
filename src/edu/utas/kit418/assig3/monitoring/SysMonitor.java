package edu.utas.kit418.assig3.monitoring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import edu.utas.kit418.assig3.network.ProtocolWrapper;
import edu.utas.kit418.assig3.network.ProtocolWrapper.CODE;

public class SysMonitor extends TimerTask {
//http://www.coderpanda.com/java-socket-programming-transferring-java-object-through-socket-using-udp/
	private Timer timer;
	private DatagramSocket ds; 
	private ByteArrayOutputStream byteOs;
	private ObjectOutputStream objOs;
	private String serverIP;
	private int serverPort;
	private Sigar sigar;
	
	public SysMonitor(String serverIP, int serverPort) throws IOException {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		ds = new DatagramSocket();
		byteOs = new ByteArrayOutputStream();
		objOs = new ObjectOutputStream(byteOs);
		timer = new Timer();
		sigar = new Sigar();
	}

	public void start() {
//		System.load(new File("lib","sigar-amd64-winnt.dll").getAbsolutePath());
		timer.scheduleAtFixedRate(this, 1000, 5000);
	}

	@Override
	public void run() {
		SystemInfo sysInfo = retrieveSysInfo();
		ProtocolWrapper pw = new ProtocolWrapper();
		pw.sysInfo = sysInfo;
		pw.code = CODE.SYSINFO;
		sysInfo.print();
//		try {
//			objOs.writeObject(sysInfo);
//			byte[] data = byteOs.toByteArray();
//			ds.send(new DatagramPacket(data, data.length, InetAddress.getByName(serverIP),serverPort));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private SystemInfo retrieveSysInfo() {
		SystemInfo info = new SystemInfo();
		try {
			Mem mem = sigar.getMem();
			info.memTotal = mem.getTotal();
			info.memPerc = mem.getUsedPercent();
			CpuPerc[] cpuPercs = sigar.getCpuPercList();
			info.cpusPerc = new double[cpuPercs.length];
			for(int i = 0; i< cpuPercs.length;i++){
				info.cpusPerc[i] = cpuPercs[i].getCombined();
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
		
		return info;
	}

	public void stop() {
		timer.cancel();
	}
}
