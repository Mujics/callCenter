package callCenter;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Employee {
	
	private String name;
	private EmployeeType employeeType;
	private boolean free;
	
	public Employee(String name, EmployeeType employeeType) {
		this.name = name;
		this.free = true;
		this.employeeType = employeeType;
	}
	
	public boolean isFree() {
		return free;
	}
	
	public String getName() {
		return name;
	}
	
	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	public void makeUnavailable( ) { free = false; }

	public void makeAvailable( ) { free = true; }
	
	public void handleCall(Call call) throws InterruptedException {
		int callDuration = call.getCallDuration();
		System.out.println("ID" + Thread.currentThread().getId() + ", Calling in progress" + String.join("", Collections.nCopies(callDuration, ".")));
		TimeUnit.SECONDS.sleep(callDuration);
		this.makeAvailable();
	}
}
