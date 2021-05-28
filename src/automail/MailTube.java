package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;

public class MailTube extends StorageComponents {
    //TODO finish deliver method adding functionality from the operate method in robot, workout how to do moving, probably in deliver.
    //used as a last in first out list
    public MailTube() {
        super();
        this.setCapacity(2);
    }

    @Override
    public void deliver(Robot robot)throws ExcessiveDeliveryException{
        if(robot.getCurrentFloor().getFloorNumber() == robot.getDestinationFloor().getFloorNumber()) { // If already here drop off either way
            /** Delivery complete, report this to the simulator! */
            robot.getDelivery().deliver(robot.getStorageComponents().nextToDeliver());
            robot.setDeliveryItem(null);
            robot.setDeliveryCounter(robot.getDeliveryCounter() + 1);
            if (robot.getDeliveryCounter() > 2) {  // Implies a simulation bug
                throw new ExcessiveDeliveryException();
            }
            /** Check if want to return, i.e. if there is no item in the tube*/
            if (this.getMail().isEmpty() && (robot.getDeliveryItem() == null)) {
                robot.changeState(Robot.RobotState.RETURNING);
            } else {
                /** If there is another item, set the robot's route to the location to deliver the item */
                robot.setDeliveryItem(this.getMail().get(0));
                this.getMail().remove(0);
                robot.setDestinationFloor(robot.getDeliveryItem().destinationFloor);
                robot.changeState(Robot.RobotState.DELIVERING);
            }
        }
    }

    @Override
    public MailItem nextToDeliver() {
        assert(!(this.getMail().isEmpty()));
        return this.getMail().get(0);
    }
}
