package it.niccrema.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class Room{

    private Integer id;
    private String name;
    private Integer north;
    private Integer south;
    private Integer west;
    private Integer east;
    private Map<Direction,Integer> directionsMap;

    @SerializedName(value = "objects")
    private Set<Item> items;

    @SerializedName(value = "rooms")
    private Set<Room> connectedRooms;

    public Room(){
        connectedRooms = new HashSet<Room>();
        directionsMap = new HashMap<>();
    }

    public Room(Integer roomId){
        this();
        id = roomId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getNorth() {
        return this.north;
    }

    public void setNorth(Integer north) {
        this.north = north;
    }

    public Integer getSouth() {
        return this.south;
    }

    public void setSouth(Integer south) {
        this.south = south;
    }

    public Integer getWest() {
        return this.west;
    }

    public void setWest(Integer west) {
        this.west = west;
    }

    public Integer getEast() {
        return this.east;
    }

    public void setEast(Integer east) {
        this.east = east;
    }
    
    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
    
    public Set<Room> getConnectedRooms(){
        return connectedRooms;
    }

    public void setConnectedRooms(Set<Room> connectedRooms){
        this.connectedRooms = connectedRooms;
    }

    public Map<Direction,Integer> getDirectionsMap(){
        return directionsMap;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", items='" + getItems() + "'}";
    }

}
