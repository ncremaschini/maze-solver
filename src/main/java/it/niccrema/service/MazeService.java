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

import it.niccrema.model.Direction;
import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Room;
import it.niccrema.model.Route;

public class MazeService {
    Maze maze;
    Route route;

    Queue<Room> roomsQueue;
    Set<Room> visitedRooms;

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

    private void createMazeMap() {
        if (maze.getMazeMap() == null) {
            maze.getRooms().forEach(room -> maze.getMazeMap().put(room.getId(), room));
        }
    }

    private void calculateConnectedRooms(Room room) {

        Set<Room> connectedRooms = Stream.of(room.getNorth(), room.getSouth(), room.getWest(), room.getEast())
                .filter(Objects::nonNull).map(roomId -> maze.getMazeMap().get(roomId))
                .collect(Collectors.toCollection(HashSet::new));
        room.setConnectedRoomsToVisit(connectedRooms);
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
                if(!visitedRooms.contains(roomToCheck)){
                    nextRoom = roomToCheck;
                    break;
                }
            }
        }
        
        return Optional.ofNullable(nextRoom);
    }

    private void visitRoom(Room room, Set<Item> itemsToCollect){
        itemsToCollect.removeAll(room.getItems());
        visitedRooms.add(room);
        route.getSteps().add(room);
    }

    public Route findItems(Integer roomId, Set<Item> itemsToCollect) {
        route = new Route();
        Room startRoom = maze.getMazeMap().get(roomId);

        visitedRooms = new HashSet<Room>();
        roomsQueue = new LinkedList<Room>();
        
        visitRoom(startRoom, itemsToCollect);
        roomsQueue.add(startRoom);
        
        while ((!roomsQueue.isEmpty() && !itemsToCollect.isEmpty())) {
            Optional<Room> roomOpt = Optional.ofNullable(roomsQueue.poll());
            
            if(roomOpt.isPresent()){
                
                Room room = roomOpt.get();

                while(!itemsToCollect.isEmpty() && !allRoomsAreVisited() && !room.getConnectedRoomsToVisit().isEmpty()){
                    Optional<Room> nextRoomToVisit = getNextNeighbourRoomToVisit(room);
                    if(nextRoomToVisit.isPresent()){
                        Room visitingRoom = nextRoomToVisit.get();
                        room.getConnectedRoomsToVisit().remove(visitingRoom);
                        visitRoom(visitingRoom, itemsToCollect);
                                 
                        if(itemsToCollect.isEmpty() || allRoomsAreVisited()){
                            break;
                        }
    
                        //current node has more neighbors, step back into it
                        if(!room.getConnectedRoomsToVisit().isEmpty()){
                            visitRoom(room, itemsToCollect);
                            roomsQueue.add(room);
                        }else{  
                            //all neighbours visited, stay in the last neighbour
                            roomsQueue.add(visitingRoom);         
                        }
                    } 
                }//neighbours visited
            }
        }

        return route;
    }

    private boolean allRoomsAreVisited() {
        return visitedRooms.containsAll(maze.getRooms());
    }


    public Maze getMaze() {
        return maze;
    }
}
