package callCenter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Test;

public class DispatcherTest {
	

	// Buidls Call Center with 10 Employees and n incoming calls
	protected Dispatcher buildCallCenter(int callNumber) {
		Dispatcher dispatcher = Dispatcher.getInstance();
		addTenCallCenterEmployees(dispatcher);
		addCalls(dispatcher, callNumber);
		return dispatcher;
	}
	
	// Adds 10 Call center employees
	protected void addTenCallCenterEmployees(Dispatcher dispatcher) {
		Employee directorOne = new Employee("Director 1", EmployeeType.DIRECTOR);
		Employee directorTwo = new Employee("Director 2", EmployeeType.DIRECTOR);
		Employee directorThree = new Employee("Director 3", EmployeeType.DIRECTOR);
		dispatcher.addEmployee(directorOne);
		dispatcher.addEmployee(directorTwo);
		dispatcher.addEmployee(directorThree);
		Employee supervisorOne = new Employee("Supervisor 1", EmployeeType.SUPERVISOR);
		Employee supervisorTwo = new Employee("Supervisor 2", EmployeeType.SUPERVISOR);
		Employee supervisorThree = new Employee("Supervisor 3", EmployeeType.SUPERVISOR);
		dispatcher.addEmployee(supervisorOne);
		dispatcher.addEmployee(supervisorTwo);
		dispatcher.addEmployee(supervisorThree);
		Employee operatorOne = new Employee("Operator 1", EmployeeType.OPERATOR);
		Employee operatorTwo = new Employee("Operator 2", EmployeeType.OPERATOR);
		Employee operatorThree = new Employee("Operator 3", EmployeeType.OPERATOR);
		Employee operatorFour = new Employee("Operator 4", EmployeeType.OPERATOR);
		dispatcher.addEmployee(operatorOne);
		dispatcher.addEmployee(operatorTwo);
		dispatcher.addEmployee(operatorThree);
		dispatcher.addEmployee(operatorFour);
	}
	
	// Adds n incoming calls
	protected void addCalls(Dispatcher dispatcher, int callNumber) {
		String[] names = {
				"Juan", "Pablo", "Lucas", "Andres", "Gustavo", "Carla", "Maria", "Marta", "Jessica", "Gimena", "Nicolas"
		};
		for (int i = 0; i < callNumber; i++) {
			dispatcher.addCallToWaitingList(new Call(names[i], "Technical assistance"));
		}
	}

	@After
	// Removes employees and waiting calls from call center
	public void tearDownCallCenter() {
		Dispatcher dispatcher = Dispatcher.getInstance();
		dispatcher.resetEmployees();
		dispatcher.resetWaitingCalls();
	}
	
	@Test
	// Asserts that after the execution of dispatcher loop there's no waiting calls left
	public void WaitingCallListShouldBeEmptyAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter(10);
		dispatcher.dispatchLoop();
		boolean waitingCallIsEmpty = dispatcher.getWaitingCalls().isEmpty();
		assertTrue(waitingCallIsEmpty);
	}
	
	@Test
	// Asserts that after the execution of dispatcher loop there's no busy employees
	public void AllEmployeesShouldBeFreeAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter(10);
		dispatcher.dispatchCall();
		List<Employee> nonFreeSupervisors = dispatcher.employees.stream().filter(x -> (!x.isFree())).collect(Collectors.toList());
		assertTrue(nonFreeSupervisors.isEmpty());
	}
	

	@Test
	// Asserts that when there's no employees free to take a call 
	// and a single dispatch is made there's still calls waiting
	public void UnhandleCallsShouldBeInTheWaitingCalls() {
		Dispatcher dispatcher = buildCallCenter(8);
		dispatcher.resetEmployees();
		Employee operatorOne = new Employee("Operator 1", EmployeeType.OPERATOR);
		dispatcher.addEmployee(operatorOne);
		dispatcher.dispatchCall();
		boolean waitingCallIsEmpty = dispatcher.getWaitingCalls().isEmpty();
		assertFalse(waitingCallIsEmpty);
	}

	@Test
	// Asserts that when there's no employees free to take a call at first, 
	// in the second dispatcher loop there are all handle 
	public void RemainingCallsShouldBeHandleInTheSecondLoop() {
		Dispatcher dispatcher = buildCallCenter(11);
		dispatcher.dispatchLoop();
		boolean waitingCallIsEmpty = dispatcher.getWaitingCalls().isEmpty();
		assertTrue(waitingCallIsEmpty);
	}
	

	@Test
	// Asserts that when there's more than ten concurrent calls and a a 
	// single dispatch is made the calls stays in the waiting list 
	public void RemainingCallsShouldBeInTheWaitingCalls() {
		Dispatcher dispatcher = buildCallCenter(11);
		dispatcher.dispatchCall();
		boolean waitingCallIsEmpty = dispatcher.getWaitingCalls().isEmpty();
		assertFalse(waitingCallIsEmpty);
	}

}
