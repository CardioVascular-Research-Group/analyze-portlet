package edu.jhu.cvrg.waveform.utility;

import java.io.Serializable;
import java.util.Collection;

public class ThreadController extends Thread implements Serializable{

	private static final long serialVersionUID = 348093133316110634L;
	
	private static int threadPoolSize = 50;
	private static int threadPoolSleepTime = 500;
	
	private ThreadGroup group;
	private Collection<? extends Thread> threads;
	
	public ThreadController(ThreadGroup tGroup, Collection<? extends Thread> threadCollection) {
		group = tGroup;
		threads = threadCollection;
	}
	
	@Override
	public void run() {
		try{
			if(threads != null && !threads.isEmpty()){
				for (Thread t : threads) {
					t.start();
					
					while (group.activeCount() >= threadPoolSize) {
						ThreadController.sleep(threadPoolSleepTime);
					}
				}
			}
		
		}catch (Exception e) {
			System.out.println("#### ThreadController Error #### "+ e.getMessage());
		}
	}
	
	public int getThreadCount(){
		return threads != null ? threads.size() : 0;
	}

	public Collection<? extends Thread> getThreads() {
		return threads;
	}

}
