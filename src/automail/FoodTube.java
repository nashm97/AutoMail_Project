package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;

public class FoodTube extends StorageComponents {
    //TODO finish deliver method adding functionality from the operate method in robot, workout how to do moving
    //used as a first in first out list

    public FoodTube() {
        super();
        this.setCapacity(3);
    }

    @Override
    public void deliver(Robot robot) throws ExcessiveDeliveryException {
        if (robot.getCurrentFloor().getFloorNumber() == robot.getDestinationFloor().getFloorNumber()) { // If already here drop off either way
            /** Delivery complete, report this to the simulator! */
            robot.getDelivery().deliver(this.getMail().get(this.getMail().size()));// deliver the food at the top of the tube
            this.getMail().remove(this.getMail().size());
            robot.setDeliveryCounter(robot.getDeliveryCounter() + 1);
            if (robot.getDeliveryCounter() > 3) {  // Implies a simulation bug
                throw new ExcessiveDeliveryException();
            }
            /** Check if want to return, i.e. if there is no item in the tube*/
            if (this.getMail().isEmpty()) {
                robot.changeState(Robot.RobotState.RETURNING);
            } else {
                /** If there is another item, set the robot's route to the location to deliver the item */
                //robot.setDeliveryItem(this.getMail().get(this.getMail().size()));
                robot.setDestinationFloor(this.getMail().get(this.getMail().size()).destinationFloor);
                robot.changeState(Robot.RobotState.DELIVERING);
            }
        }
    }

    @Override
    public MailItem nextToDeliver() {
        return this.getMail().get(this.getMail().size());
    }
}
