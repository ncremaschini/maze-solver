package it.niccrema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Test;
import it.niccrema.service.CommandLineParserService;

public class CommandLineParserServiceTest {

    
    @Test
    public void shouldFailForRequiredArgsMising() {
        String[] args = new String[0];
        Optional<CommandLineParserService> commandLineParserService = CommandLineParserService.parse(args);
        assertFalse(commandLineParserService.isPresent());
    }

    @Test
    public void shouldReadAllArgs() {
        String[] args = {"-m=map01.json","-r=1","-i=Item1,Item2"};
        Optional<CommandLineParserService> commandLineParserService = CommandLineParserService.parse(args);
        
        assertTrue(commandLineParserService.isPresent());
        assertEquals(commandLineParserService.get().getItemsToCollect()[0],"Item1");
        assertEquals(commandLineParserService.get().getItemsToCollect()[1],"Item2");
        assertEquals(commandLineParserService.get().getMapFilePath(),"map01.json");
        assertEquals(commandLineParserService.get().getStartingRoomId(),new Integer(1));
    }
}
