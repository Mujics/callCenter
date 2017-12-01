package callCenter;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public abstract class Employee {
	
	private String name;
	
	private boolean free;
	
	public Employee(String name) {
		this.name = name;
		this.free = true;
	}
	
	public synchronized boolean isFree() {
		return free;
	}
	
	public String getName() {
		return name;
	}
	
	public synchronized void makeUnavailable( ) { free = false; }

	public synchronized void makeAvailable( ) { free = true; }
	
	public void handleCall(Call call) throws InterruptedException {
		int callDuration = call.getCallDuration();
		System.out.println("ID" + Thread.currentThread().getId() + ", Calling in progress" + String.join("", Collections.nCopies(callDuration, ".")));
		//System.out.println(greetingMessage + call.getCaller() + " how can i help you with " + call.getSubject());
		TimeUnit.SECONDS.sleep(callDuration);
		//this.makeAvailable();
	}
}
