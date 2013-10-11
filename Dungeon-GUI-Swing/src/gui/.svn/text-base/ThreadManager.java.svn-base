package gui;

public class ThreadManager {
	
	private Thread[] threads;
	
	public ThreadManager(int k ) {
		initThreads(k);
	}
	public ThreadManager() {
		initThreads(20);
		
	}
	
	private void initThreads(int k) {
		threads = new Thread[k];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread();
		}
	}
	public Thread getIdleThread() {
		Thread th = null;
		for (int i = 0; i < threads.length; i++) {
			if(!threads[i].isAlive()) {
				th = threads[i];
			}
		}
		return th;
	}
	

}
