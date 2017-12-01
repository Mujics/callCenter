package callCenter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Test;

public class DispatcherTest {
	

	// build up
	protected Dispatcher buildCallCenter() {
		Dispatcher dispatcher = Dispatcher.getInstance();
		addCallCenterEmployees(dispatcher);
		addCalls(dispatcher);
		return dispatcher;
	}
	
	protected void addCallCenterEmployees(Dispatcher dispatcher) {
		Director directorOne = new Director("Director 1");
		Director directorTwo = new Director("Director 2");
		dispatcher.addDirectors(directorOne);
		dispatcher.addDirectors(directorTwo);
		Supervisor supervisorOne = new Supervisor("Supervisor 1");
		Supervisor supervisorTwo = new Supervisor("Supervisor 2");
		dispatcher.addSupervisor(supervisorOne);
		dispatcher.addSupervisor(supervisorTwo);
		Operator operatorOne = new Operator("Operator 1");
		Operator operatorTwo = new Operator("Operator 2");
		dispatcher.addOperator(operatorOne);
		dispatcher.addOperator(operatorTwo);
	}
	
	protected void addCalls(Dispatcher dispatcher) {
		Call call1 = new Call("Juan", "Descontento con el servicio");
		Call call2 = new Call("Pablo", "Consulta Tecnica");
		Call call3 = new Call("Lucas", "Consulta Tecnica 2");
		Call call4 = new Call("Andres", "Consulta Tecnica 3");
		Call call5 = new Call("Gustavo", "Consulta Tecnica 4");
		Call call6 = new Call("Carla", "Consulta Tecnica 5");
		//Call call7 = new Call("Maria", "Consulta Tecnica 6");
		dispatcher.addCallToWaitingList(call1);
		dispatcher.addCallToWaitingList(call2);
		dispatcher.addCallToWaitingList(call3);
		dispatcher.addCallToWaitingList(call4);
		dispatcher.addCallToWaitingList(call5);
		dispatcher.addCallToWaitingList(call6);
		//dispatcher.addCallToWaitingList(call7);
	}

	// tear down
	@AfterClass
	public static void tearDownCallCenter() {
		Dispatcher dispatcher = Dispatcher.getInstance();
		dispatcher.resetEmployees();
	}
	
	@Test
	public void WaitingCallListShouldBeEmptyAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter();
		dispatcher.dispatchCall();
		try {
			Thread.sleep(3000);
			boolean waitingCallIsEmpty = dispatcher.getWaitingCalls().isEmpty();
			assertTrue(waitingCallIsEmpty);
		} catch (InterruptedException e) {
			fail("Exception: " + e);
		}
	}
	
	@Test
	public void AllDirectorsShouldBeFreeAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter();
		dispatcher.dispatchCall();
		try {
			Thread.sleep(10000);
			List<Director> nonFreeDirectors = dispatcher.directors.stream().filter(x -> (!x.isFree())).collect(Collectors.toList());
			assertTrue(nonFreeDirectors.isEmpty());
		} catch (InterruptedException e) {
			fail("Exception: " + e);
		}
	}
	
	@Test
	public void AllOperatorsShouldBeFreeAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter();
		dispatcher.dispatchCall();
		try {
			Thread.sleep(10000);
			List<Operator> nonFreeOperators = dispatcher.operators.stream().filter(x -> (!x.isFree())).collect(Collectors.toList());
			assertTrue(nonFreeOperators.isEmpty());
		} catch (InterruptedException e) {
			fail("Exception: " + e);
		}
	}
	
	@Test
	public void AllSupervisorsShouldBeFreeAfterDispatch() {
		Dispatcher dispatcher = buildCallCenter();
		dispatcher.dispatchCall();
		try {
			Thread.sleep(10000);
			List<Supervisor> nonFreeSupervisors = dispatcher.supervisors.stream().filter(x -> (!x.isFree())).collect(Collectors.toList());
			assertTrue(nonFreeSupervisors.isEmpty());
		} catch (InterruptedException e) {
			fail("Exception: " + e);
		}
	}

}
