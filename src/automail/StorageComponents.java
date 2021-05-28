package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;

import java.util.ArrayList;

public class StorageComponents {
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;
    private int capacity;
    private ArrayList<MailItem> mail;
    public StorageComponents(){
        this.mail = new ArrayList<>();
    }

    public void deliver(Robot robot) throws ExcessiveDeliveryException {
        throw new ExcessiveDeliveryException();
    }
    public void setCapacity(int value){
        this.capacity = value;
    }
    public int getCapacity(){
        return this.capacity;
    }

    public ArrayList<MailItem> getMail(){
        return this.mail;
    }
    public void addToTube(Robot robot, MailItem mailItem) throws ItemTooHeavyException {
        assert(this.getMail().size()<3);
        this.getMail().add(mailItem);
        if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }

    public MailItem nextToDeliver(){
        return null;
    }
}
