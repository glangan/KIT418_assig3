package edu.utas.kit418.assig3.monitoring;

import org.hyperic.sigar.CpuPerc;

public class SystemInfo {
	public double cpusPerc[];
	public double memPerc;
	public long memTotal;
	public void print() {
		System.out.println("--CPU--");
		for(int i = 0; i< cpusPerc.length;i++){
		System.out.println("CPU"+i+": "+CpuPerc.format(cpusPerc[i]));
		}
		System.out.println("--Memory--");
		System.out.println(String.format("%.2f",memPerc));
	}
}
