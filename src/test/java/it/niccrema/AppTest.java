package it.niccrema;

import org.junit.Test;

public class AppTest {
    

    @Test    
    public void shouldPrintMap1Route(){
        String[] args = {"-m=/Users/nico/progetti/maze-route/maps/map1.json","-r=2","-i=Knife,Potted Plant"};
        App.main(args);
    }

    @Test    
    public void shouldPrintMap2Route(){
        String[] args = {"-m=/Users/nico/progetti/maze-route/maps/map2.json","-r=4","-i=Knife,Potted Plant,Pillow"};
        App.main(args);
    }
}
