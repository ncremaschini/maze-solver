package it.niccrema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import it.niccrema.exceptions.RoomNotFoundException;
import it.niccrema.model.Item;
import it.niccrema.model.Maze;
import it.niccrema.model.Route;
import it.niccrema.model.Step;
import it.niccrema.service.MazeService;

public class MazeServiceMap1Test {

    private static Maze maze;
    private static MazeService mazeService;

    @Before
    public void initService() throws IOException {
        mazeService = new MazeService("src/test/resources/map1.json");
        maze = mazeService.getMaze();
    }

    @Test
    public void shouldCreateMaze() {
        assertFalse(maze.getMazeMap().keySet().isEmpty());
    }

    @Test
    public void shouldCalculateConnectedRooms() {
        assertEquals(maze.getMazeMap().get(1).getConnectedRooms().size(), 1);
        assertEquals(maze.getMazeMap().get(2).getConnectedRooms().size(), 3);
        assertEquals(maze.getMazeMap().get(3).getConnectedRooms().size(), 1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRooms().size(), 1);
    }

    @Test
    public void shouldCalculateDirectionsMap() {
        assertEquals(maze.getMazeMap().get(1).getDirectionsMap().size(), 1);
        assertEquals(maze.getMazeMap().get(2).getDirectionsMap().size(), 3);
        assertEquals(maze.getMazeMap().get(3).getDirectionsMap().size(), 1);
        assertEquals(maze.getMazeMap().get(4).getConnectedRooms().size(), 1);
    }

    @Test(expected = RoomNotFoundException.class)
    public void shouldThrowRoomNotFoundException() {
        mazeService.findItems(99, new HashSet<>());
    }

    @Test
    public void shouldFindItemsInFirtsRoom() {
        Set<Item> itemsToCollect = Stream.of("Knife")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());

        Route route = mazeService.findItems(3, itemsToCollect);

        assertTrue(itemsToCollect.isEmpty());
        assertEquals(route.getSteps().size(), 1);
    }

    @Test
    public void shouldFindAllItemsUsingExpectedRoute() {
        
        Set<Item> itemsToCollect = Stream.of("Knife", "Potted Plant")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());

        Route expectedRoute = new Route();
        LinkedList<Step> expectedSteps = Stream.of(2,1,2,3,2,4)
                                            .map(roomId -> new Step(roomId))
                                            .collect(Collectors.toCollection(LinkedList::new));
        expectedRoute.setSteps(expectedSteps);
        
        Route route = mazeService.findItems(2, itemsToCollect);
        assertTrue(itemsToCollect.isEmpty());

        assertEquals(expectedRoute, route);
    }

    @Test
    public void shouldFindAllItemsInFiveSteps() {
        Set<Item> itemsToCollect = Stream.of("Knife", "Potted Plant")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());
        
        Route route = mazeService.findItems(1, itemsToCollect);
        assertTrue(itemsToCollect.isEmpty());
        assertEquals(route.getSteps().size(), 5);
    }

    @Test
    public void shouldFindAllItemsExceptOne() {
        Set<Item> itemsToCollect = Stream.of("Knife", "Potted Plant","Rifle")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());
    
        mazeService.findItems(2, itemsToCollect);

        assertEquals(itemsToCollect.size(), 1);
    }

    @Test
    public void shouldNotFindItems() {
        Set<Item> itemsToCollect = Stream.of("rifle", "garter")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());

        mazeService.findItems(2, itemsToCollect);

        assertEquals(itemsToCollect.size(), 2);
    }

    @Test
    public void shouldPickUpOnlyRequiredItems() {
        Set<Item> itemsToCollect = Stream.of("Knife")
                                            .map(item -> new Item(item))
                                            .collect(Collectors.toSet());

        Route route = mazeService.findItems(2, itemsToCollect);

        Set<Item> collectedItems = new HashSet<>();
        route.getSteps().forEach(step -> collectedItems.addAll(step.getCollectedItems()));

        assertEquals(collectedItems.size(),1);

    }
}
