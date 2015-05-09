package edu.utas.kit418.assig3.worker;

import edu.utas.kit418.assig3.Node;
import edu.utas.kit418.assig3.task.TASKTYPE;
import edu.utas.kit418.assig3.task.Task;
import edu.utas.kit418.assig3.task.TaskResult;

public class Worker implements Runnable {

	public enum WORKERSTATUS {
		READY, RUNNING, RESULT
	}

	public WORKERSTATUS workerStatus = WORKERSTATUS.READY;
	private TASKTYPE taskType;
	private Task task;
	private int taskid;

	public TaskResult result;

	@Override
	public void run() {
		// TODO: working here

		result = new TaskResult();
		result.taskId = taskid;
		result.type = taskType;
		result.content = ""; // TODO: put serialized result content
		workerStatus = WORKERSTATUS.RESULT;
		Node.sSync.notifyAll();
	}

	public void setTask(int id, TASKTYPE type, Task task) {
		taskid = id;
		this.taskType = type;
		this.task = task;
	}
}
