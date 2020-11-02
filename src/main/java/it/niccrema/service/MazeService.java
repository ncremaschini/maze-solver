package it.niccrema.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import it.niccrema.model.Direction;
import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Room;
import it.niccrema.model.Route;

public class MazeService {
    Maze maze;
    Route route = new Route();
    Stack<Room> roomsStack  = new Stack<>();
    Set<Room> visitedRooms = new HashSet<>();
    
    public MazeService(String mazeFilePath) throws IOException{
        try (Reader reader = Files.newBufferedReader(Paths.get(mazeFilePath));) {
            Gson gson = new Gson();
            maze = gson.fromJson(reader, Maze.class);

            createMazeMap();
            maze.getRooms().forEach(room -> calculateConnectedRooms(room));
            maze.getRooms().forEach(room -> calculateDirectionsMap(room));

        } catch (IOException ex) {
            throw ex;
        }
    }

    private void createMazeMap(){
        if(maze.getMazeMap() == null){
            maze.getRooms().forEach(room -> maze.getMazeMap().put(room.getId(), room));
        }
    }

    private void calculateConnectedRooms(Room room) {
        
        Set<Room> connectedRooms = Stream.of(room.getNorth(),room.getSouth(),room.getWest(),room.getEast())
                                    .filter(Objects::nonNull)
                                    .map(roomId -> maze.getMazeMap().get(roomId))
                                    .collect(Collectors.toCollection(HashSet::new));
        room.setConnectedRooms(connectedRooms);
    }

    private void calculateDirectionsMap(Room room){
        room.getDirectionsMap().put(Direction.NORTH,room.getNorth());
        room.getDirectionsMap().put(Direction.SOUTH,room.getSouth());
        room.getDirectionsMap().put(Direction.WEST,room.getWest());
        room.getDirectionsMap().put(Direction.EAST,room.getEast());

        room.getDirectionsMap().values().removeAll(Collections.singleton(null));
        
    }

    private Optional<Room> getNextRoomToVisit(Room currentRoom){

        Room nextRoom = null;
        for(Direction direction : Direction.values()){
            Optional<Integer> idToCheck = Optional.ofNullable(currentRoom.getDirectionsMap().get(direction));
            if(idToCheck.isPresent()){
                Room roomToCheck = maze.getMazeMap().get(idToCheck.get());
                if(!visitedRooms.contains(roomToCheck)){
                    nextRoom = roomToCheck;
                    break;
                }
            }
        }
        
        return Optional.ofNullable(nextRoom);
    }

    public Route findItems(Integer startingRoomId, Set<Item> itemsToCollect){
        Room visitingRoom;

        roomsStack.add(maze.getMazeMap().get(startingRoomId));

        while(!roomsStack.isEmpty() || itemsToCollect.isEmpty()){
            visitingRoom = roomsStack.peek();
        
            route.getSteps().add(visitingRoom);
            
            itemsToCollect.removeAll(visitingRoom.getItems());
            
            if(itemsToCollect.isEmpty()){
                break;
            }else{
                visitedRooms.add(visitingRoom);
                visitingRoom.getConnectedRooms().removeAll(visitedRooms);

                Optional<Room> nextRoomToVisit = getNextRoomToVisit(visitingRoom);
                if(nextRoomToVisit.isPresent()){
                    //go to the next room
                    roomsStack.push(nextRoomToVisit.get());
                }else{
                    //no more connected rooms to visit, step back
                    roomsStack.pop();
                }
            }
        }

        return route;
    }

    public Maze getMaze(){
        return maze;
    }
}
