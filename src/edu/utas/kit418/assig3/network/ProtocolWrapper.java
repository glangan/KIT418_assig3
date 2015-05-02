package edu.utas.kit418.assig3.network;

import edu.utas.kit418.assig3.monitoring.SystemInfo;
import edu.utas.kit418.assig3.task.Task;
import edu.utas.kit418.assig3.task.TaskResult;

public class ProtocolWrapper {

	public enum CODE {
		READY, STOP, TASK, TASKRESULT, SYSINFO
	}

	public CODE code;

	public Task task;
	public TaskResult taskResult;
	public SystemInfo sysInfo;
}
