package it.niccrema;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.niccrema.exceptions.RoomNotFoundException;
import it.niccrema.model.Item;
import it.niccrema.model.Route;
import it.niccrema.service.CommandLineParserService;
import it.niccrema.service.MazeService;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try {
            
            Optional<CommandLineParserService> optCommandLineParserService = CommandLineParserService.parse(args);
            if (!optCommandLineParserService.isPresent()) {
                System.exit(1);
            }

            CommandLineParserService commandLineParserService = optCommandLineParserService.get();
        
            MazeService mazeService = new MazeService(commandLineParserService.getMapFileName());
        
            Set<Item> itemsToCollect = Arrays.asList(commandLineParserService.getItemsToCollect())
                                            .stream()
                                            .map(Item::new)
                                            .collect(Collectors.toSet());
            
            Route route = mazeService.findItems(commandLineParserService.getStartingRoomId(), itemsToCollect);

        } catch (RoomNotFoundException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
