package it.niccrema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import it.niccrema.exceptions.RoomNotFoundException;
import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Room;
import it.niccrema.model.Route;
import it.niccrema.model.Step;
import it.niccrema.service.MazeService;

public class MazeServiceMap2Test {

    private Maze maze;
    private MazeService mazeService;

    @Before
    public void loadMap() throws IOException {
        mazeService = new MazeService("src/test/resources/map2.json"); 
        maze = mazeService.getMaze();  
    }

    @Test
    public void shouldCreateMaze() {
        assertFalse(maze.getMazeMap().keySet().isEmpty());
    }

    @Test
    public void shouldCalculateConnectedRooms(){
        assertEquals(maze.getMazeMap().get(1).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(2).getConnectedRooms().size(),4);
        assertEquals(maze.getMazeMap().get(3).getConnectedRooms().size(),1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRooms().size(),3);
        assertEquals(maze.getMazeMap().get(5).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(6).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(7).getConnectedRooms().size(),2);
    }

    @Test
    public void shouldCalculateDirectionsMap(){
        assertEquals(maze.getMazeMap().get(1).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(2).getConnectedRooms().size(),4);
        assertEquals(maze.getMazeMap().get(3).getConnectedRooms().size(),1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRooms().size(),3);
        assertEquals(maze.getMazeMap().get(5).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(6).getConnectedRooms().size(),2);
        assertEquals(maze.getMazeMap().get(7).getConnectedRooms().size(),2);
    }

    @Test(expected = RoomNotFoundException.class)
    public void shouldThrowRoomNotFoundException() {
        mazeService.findItems(99,new HashSet<>());
    }

    @Test
    public void shouldFindItemsInFirtsRoom(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        
        Route route = mazeService.findItems(3,itemsToCollect);

        assertTrue(itemsToCollect.isEmpty());
        assertEquals(route.getSteps().size(), 1);
    }

    @Test
    public void shouldFindAllItemsUsingExpectedRoute(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        itemsToCollect.add(new Item("Potted Plant"));
        itemsToCollect.add(new Item("Pillow"));
        
        Route rightRoute = new Route();
        rightRoute.getSteps().add(new Step(new Room(4)));
        rightRoute.getSteps().add(new Step(new Room(6)));
        rightRoute.getSteps().add(new Step(new Room(4)));
        rightRoute.getSteps().add(new Step(new Room(7)));
        rightRoute.getSteps().add(new Step(new Room(4)));
        rightRoute.getSteps().add(new Step(new Room(2)));
        rightRoute.getSteps().add(new Step(new Room(5)));
        rightRoute.getSteps().add(new Step(new Room(2)));
        rightRoute.getSteps().add(new Step(new Room(1)));
        rightRoute.getSteps().add(new Step(new Room(2)));
        rightRoute.getSteps().add(new Step(new Room(3)));
        
        Route route = mazeService.findItems(4,itemsToCollect);    
        
        assertTrue(itemsToCollect.isEmpty());
        assertEquals(rightRoute, route);
    }

    @Test
    public void shouldFindAllItemsExceptOne(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        itemsToCollect.add(new Item("Potted Plant"));
        itemsToCollect.add(new Item("rifle"));

        mazeService.findItems(2,itemsToCollect);
        
        assertEquals(itemsToCollect.size(),1);
    }

    @Test
    public void shouldNotFindItems(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("rifle"));
        itemsToCollect.add(new Item("garter"));
        
        mazeService.findItems(2, itemsToCollect);
    
        assertEquals(itemsToCollect.size(),2);
    }

    @Test
    public void shouldPickUpOnlyRequiredItems() {
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        itemsToCollect.add(new Item("Potted Plant"));
        itemsToCollect.add(new Item("Pillow"));

        Route route = mazeService.findItems(2, itemsToCollect);

        Set<Item> collectedItems = new HashSet<>();

        route.getSteps().forEach(step -> collectedItems.addAll(step.getCollectedItems()));

        assertEquals(collectedItems.size(),3);

    }
}
