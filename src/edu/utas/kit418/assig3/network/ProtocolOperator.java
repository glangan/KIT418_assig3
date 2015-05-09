package edu.utas.kit418.assig3.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.utas.kit418.assig3.network.ProtocolWrapper.CODE;

public class ProtocolOperator {

	private ObjectInputStream objI;
	private ObjectOutputStream objO;

	public ProtocolOperator(Socket c) throws IOException {
		objI = new ObjectInputStream(c.getInputStream());
		objO = new ObjectOutputStream(c.getOutputStream());
	}

	public void readyToWork() throws IOException {
		ProtocolWrapper pe = new ProtocolWrapper();
		pe.code = CODE.READYTOWORK;
			objO.writeObject(pe);
	}

	public void sysStartUp() throws IOException {
		ProtocolWrapper pe = new ProtocolWrapper();
		pe.code = CODE.SYSSTARTUP;
			objO.writeObject(pe);
	}
	public ProtocolWrapper requestTask() throws ClassNotFoundException, IOException {
		ProtocolWrapper pe = null;
			pe = (ProtocolWrapper) objI.readObject(); // need NIO
		return pe;
	}

	public void backResult(ProtocolWrapper pe) throws IOException {
		objO.writeObject(pe);
	}


}
