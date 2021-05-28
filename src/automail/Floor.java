package automail;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;
import simulation.PriorityMailItem;

public class Floor {

    private LinkedList<Robot> waitingRobots;

    private int floorNumber;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        waitingRobots = new LinkedList<Robot>();
    }

    /**
     * checks to see if floor is available, if so approves guard otherwise adds it to unique waiting list
     * @param currentRobot
     * @return
     */
    public boolean isFloorAvailable(Robot currentRobot) {
        if (this.waitingRobots.isEmpty()) {
            return true;
        }
        else {
            this.waitingRobots.add(currentRobot);
            return false;
        }
    }

    public void removeRobotFromWaitingList() {
       this.waitingRobots.remove(0);
    }

    public int getFloorNumber(){
        return this.floorNumber;
    }


}