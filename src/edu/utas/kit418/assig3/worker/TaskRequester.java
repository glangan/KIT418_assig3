package edu.utas.kit418.assig3.worker;

import java.io.IOException;

import edu.utas.kit418.assig3.network.ProtocolOperator;
import edu.utas.kit418.assig3.network.ProtocolWrapper;

public class TaskRequester implements Runnable {

	private ProtocolOperator pOperator;
	private WorkerManager workerManager;
	public boolean running = true;
	public static Object[] rSync = new Object[0];

	public TaskRequester(WorkerManager workerManager, ProtocolOperator pOperator) {
		this.pOperator = pOperator;
		this.workerManager = workerManager;
	}

	@Override
	public void run() {
		while (running) {
			if (workerManager.hasReadyWorker()) {
				ProtocolWrapper pw = null;
				try {
					pOperator.readyToWork();
					pw = pOperator.requestTask();
					workerManager.startWorker(pw);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					rSync.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
