package Management System.DesignElevator;

enum Direction {
	UP, DOWN
}

enum Status {
	UP, DOWN, IDLE
}

class Request {
	private int level;
	
	public Request(int _level) {
		level = _level;
	}
	
	public int getLevel() {
		return level;
	}
}

class ElevatorButton {
	private int level;
	private Elevator elevator;
	
	public ElevatorButton(int _level, Elevator _elevator) {
		level = _level;
		elevator = _elevator;
	}
	
	public void pressButton() {
		InternalRequest request = new InternalRequest(level);
		elevator.handleInternalRequest(request);
	}
}

class ExternalRequest extends Request{

	private Direction direction;
	
	public ExternalRequest(int level, Direction _direction) {
		super(level);
		direction = _direction;
	}
	
	public Direction getDirection() {
		return direction;
	}
}

class InternalRequest extends Request{

	public InternalRequest(int level) {
		super(level);
	}
}

public class Elevator {
	
	private List<ElevatorButton> buttons;
	private List<Boolean> upStops;
	private List<Boolean> downStops;
	
	private int currLevel;
	private Status status;
	
	public Elevator(int n) {
		buttons = new ArrayList<ElevatorButton>();
		upStops = new ArrayList<Boolean>();
		downStops = new ArrayList<Boolean>();
		currLevel = 0;
		status = Status.IDLE;
		
		for(int i = 0; i < n; i++) {
			upStops.add(false);
			downStops.add(false);
		}
	}
	
	public void insertButton(ElevatorButton eb) {
		buttons.add(eb);
	}
	
	public void handleExternalRequest(ExternalRequest r) {
        // Write your code here
	}
	
	public void handleInternalRequest(InternalRequest r) {
		// Write your code here
	}
	
	public void openGate() throws Exception
	{
		// Write your code here
	}
	
	public void closeGate()
	{
		// Write your code here
	}
	
	private boolean noRequests(List<Boolean> stops)
	{
		for(int i = 0; i < stops.size(); i++)
		{
			if(stops.get(i))
			{
				return false;
			}
		}
		return true;
	}
	
	public String elevatorStatusDescription()
	{	
		String description = "Currently elevator status is : " + status 
				+ ".\nCurrent level is at: " + (currLevel + 1)
				+ ".\nup stop list looks like: " + upStops
				+ ".\ndown stop list looks like:  " + downStops
				+ ".\n*****************************************\n";
		return description;
	}
}
