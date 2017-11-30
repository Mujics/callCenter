package callCenter;
import java.util.Random;

public class Call {

	private String caller;
	private int callDuration;
	private String subject;
	
	private static final int MAX_CALL_DURATION = 100;
	private static final int MIN_CALL_DURATION = 50;

	public Call(String caller, String subject) {
		this.caller = caller;
		this.subject = subject;
		generateCallDuration();
	}

	public String getCaller() {
		return caller;
	}

	public String getSubject() {
		return subject;
	}

	public int getCallDuration() {
		return callDuration;
	}
	
	public void generateCallDuration() {
		Random rand = new Random();
		this.callDuration = rand.nextInt((MAX_CALL_DURATION - MIN_CALL_DURATION) + 1) + MIN_CALL_DURATION;
	}
	
}
