package kenner.ko.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogFile implements Runnable {

	private AtomicBoolean running = new AtomicBoolean(true);
	private LinkedBlockingQueue<String> log = new LinkedBlockingQueue<String>();
	
	/**
	 * There should only be 1 of these threads running.
	 */
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running.get()){
			try {
				String userInput = log.poll(1, TimeUnit.SECONDS);
				if(userInput != null){
					//here we would normally log to file.  Right now, I'm just going to do logger.
					Logger.info(userInput);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized void addToLog(final String input){
		log.add(input);
	}
	
	public void setRunning(boolean run){
		running.set(run);
	}
}
