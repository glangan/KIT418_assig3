package edu.utas.kit418.assig3.worker;

import edu.utas.kit418.assig3.network.ProtocolWrapper;
import edu.utas.kit418.assig3.task.TaskResult;

public class WorkerManager {

	private Worker[] workerList;
	private Worker nextWorker = null;

	public WorkerManager(int totalWorker) throws Exception {
		if (totalWorker <= 0)
			throw new Exception("numOfWorker <= 0, " + totalWorker);
		workerList = new Worker[totalWorker];
		for (int i = 0; i < workerList.length; i++) {
			workerList[i] = new Worker();
		}
		nextWorker = workerList[0];
	}

	public synchronized boolean hasReadyWorker() {
		for (Worker next : workerList) {
			if (next.workerStatus == Worker.WORKERSTATUS.READY) {
				nextWorker = next;
				return true;
			}
		}
		return false;
	}

	public void startWorker(ProtocolWrapper pw) throws Exception {
		if (nextWorker.workerStatus == Worker.WORKERSTATUS.READY)
			throw new Exception("Assign task on a Non-Ready worker");
		nextWorker.setTask(pw.task.id, pw.task.type, pw.task);
		nextWorker.workerStatus = Worker.WORKERSTATUS.RUNNING;
		new Thread(nextWorker).start();
	}

	public TaskResult getResult() {
		for (Worker next : workerList) {
			if (next.workerStatus == Worker.WORKERSTATUS.RESULT) {
				next.workerStatus = Worker.WORKERSTATUS.READY;
				TaskRequester.rSync.notifyAll();
				return next.result;
			}
		}
		return null;
	}
}
