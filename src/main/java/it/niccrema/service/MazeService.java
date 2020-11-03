package it.niccrema.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.gson.Gson;

import it.niccrema.exceptions.RoomNotFoundException;
import it.niccrema.model.Direction;
import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Room;
import it.niccrema.model.Route;
import it.niccrema.model.Step;

public class MazeService {
    
    Maze maze;

    public MazeService(String mazeFilePath) throws IOException {
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

    public Maze getMaze() {
        return maze;
    }

    public Route findItems(Integer roomId, Set<Item> itemsToCollect) {
        Route route = new Route();
        Queue<Room> roomsQueue = new LinkedList<Room>();
        Room startRoom = maze.getMazeMap().get(roomId);

        if(startRoom == null){
            throw new RoomNotFoundException(roomId);
        }

        visitRoom(startRoom, itemsToCollect, route);
        roomsQueue.add(startRoom);
        
        while (!roomsQueue.isEmpty() && !itemsToCollect.isEmpty()) {
            Room room = roomsQueue.poll();
            
            while(!itemsToCollect.isEmpty() && !allRoomsAreVisited() && !allNeighboursAreVisited(room)){
                //get next neighbour to visit, according to N - S - W - E precedence order
                Optional<Room> nextRoomToVisit = getNextNeighbourRoomToVisit(room);
                
                if(nextRoomToVisit.isPresent()){
                    Room visitingRoom = nextRoomToVisit.get();
                
                    visitRoom(visitingRoom, itemsToCollect, route);
                                
                    if(itemsToCollect.isEmpty() || allRoomsAreVisited()){
                        break;
                    }

                    //current node has more neighbors, step back into it
                    if(!allNeighboursAreVisited(room)){
                        visitRoom(room, itemsToCollect, route);
                        roomsQueue.add(room);
                    }else{  
                        //all neighbours visited, just stay in the last neighbour
                        roomsQueue.add(visitingRoom);         
                    }
                } 
            }//neighbours visited
        }

        return route;
    }

    private void createMazeMap() {
        if (maze.getMazeMap() == null) {
            maze.getRooms().forEach(room -> maze.getMazeMap().put(room.getId(), room));
        }
    }

    private void calculateConnectedRooms(Room room) {

        Set<Room> connectedRooms = Stream.of(room.getNorth(), room.getSouth(), room.getWest(), room.getEast())
                .filter(Objects::nonNull).map(roomId -> maze.getMazeMap().get(roomId))
                .collect(Collectors.toCollection(HashSet::new));
        room.setConnectedRooms(connectedRooms);
    }

    private void calculateDirectionsMap(Room room) {
        room.getDirectionsMap().put(Direction.NORTH, room.getNorth());
        room.getDirectionsMap().put(Direction.SOUTH, room.getSouth());
        room.getDirectionsMap().put(Direction.WEST, room.getWest());
        room.getDirectionsMap().put(Direction.EAST, room.getEast());
        room.getDirectionsMap().values().removeAll(Collections.singleton(null));
    }

    private Optional<Room> getNextNeighbourRoomToVisit(Room currentRoom) {

        Room nextRoom = null;
        for(Direction direction : Direction.values()){
            Optional<Integer> idToCheck = Optional.ofNullable(currentRoom.getDirectionsMap().get(direction));
            if(idToCheck.isPresent()){
                Room roomToCheck = maze.getMazeMap().get(idToCheck.get());
                if(!roomToCheck.isVisited()){
                    nextRoom = roomToCheck;
                    break;
                }
            }
        }
        
        return Optional.ofNullable(nextRoom);
    }

    private void visitRoom(Room room, Set<Item> itemsToCollect, Route route){
        Step step = new Step(room);
        
        Set<Item> collectedItems = new HashSet<>(room.getItems());
        //keep only required items
        collectedItems.retainAll(itemsToCollect);
        itemsToCollect.removeAll(collectedItems);
        step.setCollectedItems(collectedItems);

        room.setVisited(true);
        route.getSteps().add(step);
    }

    private boolean allRoomsAreVisited() {
        return maze.getRooms().stream().allMatch(room -> room.isVisited());
    }

    private boolean allNeighboursAreVisited(Room room) {
        return room.getConnectedRooms().stream().allMatch(neighbour -> neighbour.isVisited());
    }
}
