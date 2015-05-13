package edu.utas.kit418.assig3.Server;

import edu.utas.kit418.assig3.task.Task;
import edu.utas.kit418.assig3.worker.WorkerManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server implements Runnable
{
    private int serverPort = 4444;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private Thread runningThread = null;
    private WorkerManager workerList;
    private Task task;

    public Server(int port) {
        this.serverPort = port;
        initializeWorkers();
    }

    @Override
    public void run() {
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped");
                    return;
                }
                throw new RuntimeException("Error excepting client connection", e);
            }

            try {
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                if (workerList.hasReadyWorker()) {
                    //TODO assign task to worker

                }

                //TODO send output
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Open port 4444", e);
        }
    }

    private void initializeWorkers() {
        try {
            workerList = new WorkerManager(1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
