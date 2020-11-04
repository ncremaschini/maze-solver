package it.niccrema.model;

import java.util.Set;

public class Step {
    
    private Room room;
    private Set<Item> collectedItems;

    public Step(Integer roomId){
        this(new Room(roomId));
    }

    public Step(Room room){
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Set<Item> getCollectedItems() {
        return collectedItems;
    }

    public void setCollectedItems(Set<Item> collectedItems) {
        this.collectedItems = collectedItems;
    }

    @Override
    public String toString() {
        return "Step [collectedItems=" + collectedItems + ", room=" + room + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((room == null) ? 0 : room.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Step other = (Step) obj;
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        return true;
    }

}
