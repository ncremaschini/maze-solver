package it.niccrema.model;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Maze {

    private Set<Room> rooms;
    private Map<Integer, Room> mazeMap;
    
    public Set<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public Map<Integer,Room> getMazeMap(){
        
        if(mazeMap == null){
            mazeMap = rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
        }

        return mazeMap;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Maze)) {
            return false;
        }
        Maze maze = (Maze) o;
        return Objects.equals(rooms, maze.rooms) && Objects.equals(mazeMap, maze.mazeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rooms, mazeMap);
    }

    @Override
    public String toString() {
        return "{" +
            " rooms='" + getRooms() + "'" +
            ", mazeMap='" + getMazeMap() + "'" +
            "}";
    }
    
}
