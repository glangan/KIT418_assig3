package edu.utas.kit418.assig3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;

import edu.utas.kit418.assig3.monitoring.SysMonitor;
import edu.utas.kit418.assig3.network.ProtocolOperator;
import edu.utas.kit418.assig3.network.ProtocolWrapper;
import edu.utas.kit418.assig3.network.ProtocolWrapper.CODE;
import edu.utas.kit418.assig3.task.TaskResult;
import edu.utas.kit418.assig3.worker.TaskRequester;
import edu.utas.kit418.assig3.worker.WorkerManager;

public class Node {

	private static boolean running = true;
	private static WorkerManager workerManager;
	private static Socket c;
	private static InetAddress localIP;
	private static int port = 4444;
	private static ProtocolOperator pOperator;
	private static SysMonitor sysMonitor;
	public static Object[] sSync = new Object[0];
	private static TaskRequester tRequester;

	public static void main(String[] args) throws Exception {
		try {
			localIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// TODO: retrieve serverIP, serverPort, numOfWorker from args
		String serverIP = "127.0.0.1";
		int serverPort = 4444;

		Sigar sigar = new Sigar();
		CpuInfo cpuInfo = sigar.getCpuInfoList()[0];
		int totalCore = cpuInfo.getCoresPerSocket() * cpuInfo.getTotalSockets();
		int totalWorker = totalCore > 2 ? totalCore - 2 : (totalCore > 1 ? totalCore - 1 : totalCore);
		workerManager = new WorkerManager(totalWorker);

		sysMonitor = new SysMonitor(serverIP, serverPort);
		sysMonitor.start();

		Thread.sleep(10000);
		

		boolean ok = connectToServer(serverIP, serverPort);
		if (!ok) {
			throw new Exception("Connect to Server failed");
		}

		pOperator = new ProtocolOperator(c);
		pOperator.sysStartUp();
		tRequester = new TaskRequester(workerManager, pOperator);
		new Thread(tRequester).start();
		while (running) {
			TaskResult result = workerManager.getResult();
			if (result != null) {
				ProtocolWrapper pe = new ProtocolWrapper();
				pe.taskResult = result;
				pe.code = CODE.TASKRESULT;
				pOperator.backResult(pe);
			}else{
				sSync.wait();
			}
		}
		dispose();
	}

	private static void dispose() {
		if(tRequester != null) {
			tRequester.running = false;
		}
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (sysMonitor != null) {
			sysMonitor.stop();
		}
	}

	private static boolean connectToServer(String serverIP, int serverPort) {
		boolean status = false;
		try {
			c = new Socket(serverIP, serverPort);
			status = true;
		} catch (IOException e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
}
