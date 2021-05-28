package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Building;
import simulation.Clock;
import simulation.IMailDelivery;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The robot delivers mail!
 */
public class Robot {
    //TODO finish updateDeliveryMode()
	
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;

    IMailDelivery delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    public enum DeliveryMode {FOOD, MAIL}
    public DeliveryMode currentMode;
    public RobotState current_state;
    private Floor currentFloor;
    private Floor destinationFloor;
    private MailPool mailPool;
    private boolean receivedDispatch;

    private StorageComponents storageComponents;
    private MailItem deliveryItem = null;
    //private MailItem tube = null;

    private int deliveryCounter;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, MailPool mailPool, int number){
    	this.id = "R" + number;
        current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
        currentFloor = new Floor(Building.MAILROOM_LOCATION);
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        this.storageComponents = new MailTube();
        this.currentMode = DeliveryMode.MAIL;
    }
    
    /**
     * This is called when a robot is assigned the mail items and ready to dispatch for the delivery 
     */
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void operate() throws ExcessiveDeliveryException  {
    	switch(current_state) {
            //TODO fix tube logic here, make storage components work instead
            /** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(currentFloor.getFloorNumber() == Building.MAILROOM_LOCATION){
                	if (!this.storageComponents.getMail().isEmpty()) {
                		for(int i = 0; i<this.storageComponents.getMail().size(); i++){
                            mailPool.addToPool(this.storageComponents.getMail().get(i));
                            System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), this.storageComponents.getMail().get(i).toString());
                            this.storageComponents.getMail().remove(i);
                        }
                	}
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
                	setDestinationFloor(this.getStorageComponents().nextToDeliver().getDestFloor());
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
                this.storageComponents.deliver(this);
                if(currentFloor.getFloorNumber()!=destinationFloor.getFloorNumber()) {
	        		/** The robot is not at the destination yet, move towards it! */
	                moveTowards(destinationFloor.getFloorNumber());
    			}
                break;
    	}
    }

    /**
     * Sets the route for the robot
     */
    private void setDestination() {
        /** Set the destination floor */
        destinationFloor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    private void moveTowards(int destination) {
        if(currentFloor.getFloorNumber() < destination){
            currentFloor = new Floor(currentFloor.getFloorNumber()+1);
        } else {
            currentFloor = new Floor(currentFloor.getFloorNumber() - 1);
        }
    }
    
    private String getIdTube() {
    	return String.format("%s(%1d)", this.id, this.getStorageComponents().getMail().size());
    	//(tube == null ? 0 : 1) was the second argument in return
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    public void changeState(RobotState nextState){
    	assert(!(this.storageComponents.getMail().isEmpty()));
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
        //TODO for some reason when we get here mailtube is empty but it must have something in it to deliver if its state is delivering?

        if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), this.getStorageComponents().nextToDeliver().toString());
    	}
    }

	public ArrayList<MailItem> getTube() {
		return this.storageComponents.getMail();
	}
    
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

//	@Override
//	public int hashCode() {
//		Integer hash0 = super.hashCode();
//		Integer hash = hashMap.get(hash0);
//		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
//		return hash;
//	}

	public boolean isEmpty() {
		return (this.getStorageComponents().getMail().isEmpty());
	}

    /** if the current robot is empty and its mode does not match the mailItems mode then change it to the right mode**/
    public void updateDeliveryMode(MailItem mailItem){
	    if(this.storageComponents.getMail().isEmpty()){
	        if(mailItem.getMailType() == 1){
	            this.currentMode = DeliveryMode.FOOD;
                this.storageComponents = new FoodTube();
            }
	        else {
	            this.currentMode = DeliveryMode.MAIL;
                this.storageComponents = new MailTube();
            }
        }
    }

    public Floor getCurrentFloor(){
	    return this.currentFloor;
    }
    public Floor getDestinationFloor(){
	    return this.destinationFloor;
    }
    public IMailDelivery getDelivery(){
	    return this.delivery;
    }
    public void setDeliveryItem(MailItem item){
	    this.deliveryItem = item;
    }
    public void setDeliveryCounter(int val){
	    this.deliveryCounter = val;
    }
    public int getDeliveryCounter(){
	    return this.deliveryCounter;
    }
    public void setDestinationFloor(Floor floor){
	    this.destinationFloor = floor;
    }

    public MailItem getDeliveryItem() {
        return deliveryItem;
    }
    public StorageComponents getStorageComponents(){
	    return this.storageComponents;
    }
}
