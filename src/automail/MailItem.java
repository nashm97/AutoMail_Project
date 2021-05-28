package automail;

import java.util.Map;
import java.util.TreeMap;

// import java.util.UUID;

/**
 * mail type enum
 */



/**
 * Represents a mail item
 */
public class MailItem {
    //TODO finish putting typpes in mail. either food or mail types using enum.
    /**

    /** The mail identifier */
    protected final String id;
    /** The time the mail item arrived */
    protected final int arrival_time;
    /** The weight in grams of the mail item */
    protected final int weight;
    /** mailtype mail o, food 1 */
    private static int mailType;
    /** Represents the destination floor to which the mail is intended to go */
    Floor destinationFloor;
    /** floor number int */
    protected final int destFloorNumber;


    // private Type type;

    /**
     * Constructor for a MailItem
     * @param dest_floor the destination floor intended for this mail item
     * @param arrival_time the time that the mail arrived
     * @param weight the weight of this mail item
     */
    public MailItem(int destFloorNumber, int arrival_time, int weight, int mailType){
        //this.destination_floor = dest_floor;
        this.id = String.valueOf(hashCode());
        this.arrival_time = arrival_time;
        this.weight = weight;
        this.mailType = mailType;
        this.destFloorNumber = destFloorNumber;
        destinationFloor = new Floor(destFloorNumber);
    }

    @Override
    public String toString(){
        return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d", id, arrival_time, destFloorNumber, weight);
    }

    /**
     *
     * @return the destination floor of the mail item
     */
    public Floor getDestFloor() {
        return destinationFloor;
    }

    /**
     *
     * @return the ID of the mail item
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return the arrival time of the mail item
     */
    public int getArrivalTime(){
        return arrival_time;
    }

    /**
     *
     * @return the weight of the mail item
     */
    public int getWeight(){
        return weight;
    }

    static private int count = 0;
    static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

    @Override
    public int hashCode() {
        Integer hash0 = super.hashCode();
        Integer hash = hashMap.get(hash0);
        if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
        return hash;
    }

    /**
     * return the mailtype
     * @return
     */
    public int getMailType() {
        return this.mailType;
    }
}
