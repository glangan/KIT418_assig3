package edu.utas.kit418.assig3.worker;

import java.util.ArrayList;
import java.util.List;

import edu.utas.kit418.assig3.network.ProtocolWrapper;
import edu.utas.kit418.assig3.task.TASKTYPE;
import edu.utas.kit418.assig3.task.TaskResult;

public class WorkerManager {

	private List<Worker> workerList = new ArrayList<Worker>();
	private Worker nextWorker = null;

	public WorkerManager(int numOfWorker) throws Exception {
		if(numOfWorker <=0) throw new Exception("numOfWorker <= 0, "+numOfWorker);
		while (numOfWorker > 0) {
			workerList.add(new Worker());
			numOfWorker--;
		}
		nextWorker = workerList.get(0);
	}

	public boolean hasReadyWorker() {
		for (Worker next : workerList) {
			if (next.workerStatus == Worker.WORKERSTATUS.READY) {
				nextWorker = next;
				return true;
			}
		}
		return false;
	}

	public void startWorker(ProtocolWrapper pe) throws Exception {
		if(nextWorker.workerStatus == Worker.WORKERSTATUS.READY) throw new Exception("Assign task on a Non-Ready worker");
		nextWorker.setTask(pe.task.id, pe.task.type, pe.task);
		nextWorker.workerStatus = Worker.WORKERSTATUS.RUNNING;
		new Thread(nextWorker).start();
	}

	public TaskResult getResult() {
		for (Worker next : workerList) {
			if(next.workerStatus == Worker.WORKERSTATUS.RESULT){
				return next.result;
			}
		}
		return null;
	}
}


