package callCenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

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
	// --------------- CALLS---------------

	// --------------- EMPLOYEES ---------------
	// Cambiar a una qeue
	public List<Operator> operators = new ArrayList<Operator>();
	public List<Supervisor> supervisors = new ArrayList<Supervisor>();
	public List<Director> directors = new ArrayList<Director>();
	
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
	
	public void dispatchCall() {
		Runnable runnable = () -> {
			Employee employee = availableEmployee();
			Call call = waitingCalls.poll();
			// Verificar anttes de scar una call que tengo un empleado para atenderla
		    try {
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
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i =0; i <6; i++){   
			executorService.execute(runnable);
		}
		executorService.shutdown();
	}
	
	public Employee availableEmployee() {
		for (Employee operator : operators) {
			System.out.println("ID" + Thread.currentThread().getId() + ", Finding operator, operator " + operator.getName() + "...available: " + operator.isFree() );
			if ( operator.isFree() ) { return operator; }
		}
		for (Employee supervisor : supervisors) {
			System.out.println("ID" + Thread.currentThread().getId() + ", Finding supervisor, supervisor " + supervisor.getName() + "...available: " + supervisor.isFree() );
			if ( supervisor.isFree() ) { return supervisor; }
		}
		for (Employee director: directors) {
			System.out.println("ID" + Thread.currentThread().getId() + ",Finding director, director " + director.getName() + "...available: " + director.isFree() );
			if ( director.isFree() ) { return director; }
		}
		//throw new RuntimeErrorException(null, "No emplyee available");
		return null;
	}
	
}
