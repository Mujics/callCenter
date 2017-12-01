package callCenter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

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
	private BlockingQueue<Call> waitingCalls = new LinkedBlockingQueue<Call>();;
	
	public void addCallToWaitingList(Call call) {
		waitingCalls.add(call);
	}
	
	public BlockingQueue<Call> getWaitingCalls() {
		return waitingCalls;
	}
	// --------------- CALLS---------------

	// --------------- EMPLOYEES ---------------
	// Cambiar a una qeue
	public List<Operator> operators = new CopyOnWriteArrayList<Operator>();
	public List<Supervisor> supervisors = new CopyOnWriteArrayList<Supervisor>();
	public List<Director> directors = new CopyOnWriteArrayList<Director>();
	
	public void addOperator(Operator operator) {
		operators.add(operator);
	}
	
	public void addSupervisor(Supervisor supervisor) {
		supervisors.add(supervisor);
	}
	
	public void addDirectors(Director director) {
		directors.add(director);
	}
	// --------------- EMPLOYEES ---------------
	
	static Semaphore semaphore = new Semaphore(1);
	
	public void dispatchCall() {
		Runnable runnable = () -> {
			Employee employee;
			Call call = waitingCalls.poll();
			// Verificar anttes de scar una call que tengo un empleado para atenderla
		    try {
		    	employee = availableEmployee();
		    	System.out.println("ID" + Thread.currentThread().getId() + ", EMPLOYEE-" + employee.getName());
		    	if ( call == null ) {
		    		System.out.println("ID" + Thread.currentThread().getId() + ", CALL ES NULL");
		    		return;
		    	} else {
		    		System.out.println("ID" + Thread.currentThread().getId() + ",CALL-" + call.getCaller());	
		    	}
		        employee.handleCall(call);
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		        // call o employee null
		    }
		};
		
		// Assigning  the task to each thread
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i = 0; i < 6; i++){   
			executorService.execute(runnable);
		}
		executorService.shutdown();
	}
	
	public Employee availableEmployee() throws InterruptedException {
		semaphore.acquire();
		for (Employee operator : operators) {
			System.out.println("ID" + Thread.currentThread().getId() + ", Finding operator, operator " + operator.getName() + "...available: " + operator.isFree() );
			if ( operator.isFree() ) {
				operator.makeUnavailable();
				semaphore.release(); 
				return operator; 
			}
		}
		for (Employee supervisor : supervisors) {
			System.out.println("ID" + Thread.currentThread().getId() + ", Finding supervisor, supervisor " + supervisor.getName() + "...available: " + supervisor.isFree() );
			if ( supervisor.isFree() ) { 
				supervisor.makeUnavailable();
				semaphore.release(); 
				return supervisor;  
			}
		}
		for (Employee director: directors) {
			System.out.println("ID" + Thread.currentThread().getId() + ",Finding director, director " + director.getName() + "...available: " + director.isFree() );
			if ( director.isFree() ) { 
				director.makeUnavailable();
				semaphore.release(); 
				return director;   
			}
		}
		//throw new RuntimeErrorException(null, "No emplyee available");
		semaphore.release();
		return null;
	}
	
	
	public void printEmployeeArray(List<? extends Employee> employees) {
		StringBuilder sb = new StringBuilder();
		sb.append("ID" + Thread.currentThread().getId() + ", ");
		for (Employee employee : employees) {
			sb.append(employee.getName());
			sb.append(" - ");
			sb.append("Free: ");
			sb.append(employee.isFree() + ", ");
		}
		System.out.println(sb.toString());
	}
}
