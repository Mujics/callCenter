package callCenter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
	// --------------- SINGLETON ---------------
	private static Dispatcher instance = null;

	public static Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}
	 private Dispatcher() {} 
	// --------------- SINGLETON ---------------
	// --------------- CALLS---------------
	private BlockingQueue<Call> waitingCalls = new LinkedBlockingQueue<Call>();
	
	public void addCallToWaitingList(Call call) {
		waitingCalls.add(call);
	}
	
	public BlockingQueue<Call> getWaitingCalls() {
		return waitingCalls;
	}
	
	public void resetWaitingCalls() {
		waitingCalls = new LinkedBlockingQueue<Call>();
	}
	// --------------- CALLS---------------

	// --------------- EMPLOYEES ---------------
	public List<Employee> employees = new CopyOnWriteArrayList<Employee>();

	public void addEmployee(Employee employee) {
		employees.add(employee);
	}
	
	public void resetEmployees() {
		employees = new CopyOnWriteArrayList<Employee>();
	}
	// --------------- EMPLOYEES ---------------
	
	// Iterates over the waiting call list until there all handle
	public void dispatchLoop() {
		while ( !waitingCalls.isEmpty() ) {
			dispatchCall();
		}
	}
	
	static Semaphore semaphore = new Semaphore(1);
	public static final int DISPATCHER_THREADS = 10;

	// Assigns a call to an available employee, handles 10 calls concurrently 
	// via a ExecutorService with a 10 thread pool
	public void dispatchCall() {
		Runnable runnable = () -> {
			try {
				Employee employee = availableEmployee();
				if (employee != null && !waitingCalls.isEmpty()) {
					Call call = waitingCalls.poll();
					System.out.println("Thread: " + Thread.currentThread().getId() + ", EMPLOYEE: " + employee.getName() + " - CALLER: " + call.getCaller());
					employee.handleCall(call);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("Dispatch Call Excpetion");
			}
			return;
		};

		// Assigning the task to each thread
		ExecutorService executorService = Executors.newFixedThreadPool(DISPATCHER_THREADS);
		for (int i = 0; i < DISPATCHER_THREADS; i++) {
			executorService.execute(runnable);
		}
		executorService.shutdown();
		
		// Waiting for all thread to end, allowing tests to check end conditions
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Returns the first lowest ranking available employee or NULL otherwise 
	public Employee availableEmployee() throws InterruptedException {
		semaphore.acquire();
		Optional<Employee> operatorOptional = employees.stream()
													   .filter(x -> x.isFree() && x.getEmployeeType().equals(EmployeeType.OPERATOR))
													   .findFirst();
		if ( operatorOptional.isPresent() ) {
			Employee operator = operatorOptional.get();
			operator.makeUnavailable();
			semaphore.release(); 
			return operator; 
		}
		Optional<Employee> supervisorOptional = employees.stream()
														   .filter(x -> x.isFree() && x.getEmployeeType().equals(EmployeeType.SUPERVISOR))
														   .findFirst();
		if ( supervisorOptional.isPresent() ) {
			Employee supervisor = supervisorOptional.get();
			supervisor.makeUnavailable();
			semaphore.release(); 
			return supervisor; 
		}
		Optional<Employee> directorOptional = employees.stream()
													   .filter(x -> x.isFree() && x.getEmployeeType().equals(EmployeeType.DIRECTOR))
													   .findFirst();
		if ( directorOptional.isPresent() ) {
			Employee director = directorOptional.get();
			director.makeUnavailable();
			semaphore.release(); 
			return director; 
		}
		semaphore.release();
		return null;
	}
}
