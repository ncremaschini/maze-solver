package it.niccrema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Room;
import it.niccrema.model.Route;
import it.niccrema.service.MazeService;

public class MazeServiceTestMap1 {

    private static Maze maze;
    private static MazeService mazeService;

    @Before
    public void loadMap() throws IOException {
        mazeService = new MazeService("src/test/resources/map1.json"); 
        maze = mazeService.getMaze();  
    }

    @Test
    public void shouldCreateMaze() {
        assertFalse(maze.getMazeMap().keySet().isEmpty());
    }

    @Test
    public void shouldCalculateConnectedRooms(){
        assertEquals(maze.getMazeMap().get(1).getConnectedRoomsToVisit().size(),1);
        assertEquals(maze.getMazeMap().get(2).getConnectedRoomsToVisit().size(),3);
        assertEquals(maze.getMazeMap().get(3).getConnectedRoomsToVisit().size(),1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRoomsToVisit().size(),1);
    }

    @Test
    public void shouldCalculateDirectionsMap(){
        assertEquals(maze.getMazeMap().get(1).getDirectionsMap().size(),1);
        assertEquals(maze.getMazeMap().get(2).getDirectionsMap().size(),3);
        assertEquals(maze.getMazeMap().get(3).getDirectionsMap().size(),1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRoomsToVisit().size(),1);
    }

    @Test
    public void shouldFindAllItemsUsingExpectedRoute(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        itemsToCollect.add(new Item("Potted Plant"));
        
        Route rightRoute = new Route();
        rightRoute.getSteps().add(new Room(2));
        rightRoute.getSteps().add(new Room(1));
        rightRoute.getSteps().add(new Room(2));
        rightRoute.getSteps().add(new Room(3));
        rightRoute.getSteps().add(new Room(2));
        rightRoute.getSteps().add(new Room(4));
        
        Route route = mazeService.findItems(2,itemsToCollect);
        
        route.getSteps().forEach(step -> System.out.println(step.getId() + " - " + step.getName() + " - " + step.getItems()));
        
        assertTrue(itemsToCollect.isEmpty());
        
        assertEquals(rightRoute, route);
    }

    @Test
    public void shouldFindAllItemsInFiveSteps(){
        Set<Item> itemsToCollect = new HashSet<>();
        itemsToCollect.add(new Item("Knife"));
        itemsToCollect.add(new Item("Potted Plant"));
            
        Route route = mazeService.findItems(1, itemsToCollect);
        route.getSteps().forEach(step -> System.out.println(step.getId() + " - " + step.getName() + " - " + step.getItems()));
        assertTrue(itemsToCollect.isEmpty());
        assertEquals(route.getSteps().size(), 5);
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

}
