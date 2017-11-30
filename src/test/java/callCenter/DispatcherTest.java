package callCenter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DispatcherTest {
	

	// CallCenter setUp
	protected Dispatcher callCenter(){
		Dispatcher dispatcher = Dispatcher.getInstance();
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
		return dispatcher;
	}

	@Test
	public void TenCallDispatcherTest() {
		Call call1 = new Call("Juan", "Descontento con el servicio");
		Call call2 = new Call("Pablo", "Consulta Tecnica");
		Call call3 = new Call("Lucas", "Consulta Tecnica 2");
		Call call4 = new Call("Andres", "Consulta Tecnica 3");
		Call call5 = new Call("Gustavo", "Consulta Tecnica 4");
		Call call6 = new Call("Carla", "Consulta Tecnica 5");
		Dispatcher dispatcher = callCenter();
		dispatcher.addCallToWaitingList(call1);
		dispatcher.addCallToWaitingList(call2);
		dispatcher.addCallToWaitingList(call3);
		dispatcher.addCallToWaitingList(call4);
		dispatcher.addCallToWaitingList(call5);
		dispatcher.addCallToWaitingList(call6);
		dispatcher.dispatchCall();
		dispatcher.dispatchCall();
		dispatcher.dispatchCall();
		dispatcher.dispatchCall();
		dispatcher.dispatchCall();
		dispatcher.dispatchCall();
		boolean dispatcherIsFree = dispatcher.operators.get(0).isFree();
		assertFalse(dispatcherIsFree);
	}

}
