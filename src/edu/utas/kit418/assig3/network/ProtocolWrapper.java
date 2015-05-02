package edu.utas.kit418.assig3.network;

import edu.utas.kit418.assig3.task.Task;
import edu.utas.kit418.assig3.task.TaskResult;

public class ProtocolWrapper {

	public enum STATUS{READY, SUCCESS, FAIL, STOP}
	
	
	public STATUS code;
	
	public Task task;
	public TaskResult taskResult;
	
}
