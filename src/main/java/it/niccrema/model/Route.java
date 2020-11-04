package it.niccrema.model;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Route{
    
    private LinkedList<Step> steps;

    public Route(){
        this.steps = new LinkedList<>();
    }

    public LinkedList<Step> getSteps() {
        return this.steps;
    }

    public void setSteps(LinkedList<Step> steps) {
        this.steps = steps;
    }

    public void print() {
        int separatorLineLength = 37;
        StringBuffer separatorLineBuffer = new StringBuffer(separatorLineLength);
        
        for (int i = 0; i < separatorLineLength; i++){
            separatorLineBuffer.append("-");
        }
        
        System.out.printf("\n%-3s %-16s %-50s\n", "ID", "Room", "Object collected");
        System.out.println(separatorLineBuffer.toString());

        steps.stream().forEach(move -> {
            String items = String.join(",", move.getCollectedItems().stream().map(Item::getName).collect(Collectors.toList()));
            System.out.printf("%-3d %-16s %-50s\n", move.getRoom().getId(), move.getRoom().getName(), items.isEmpty() ? "None" : items);
        });   
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Route)) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(steps, route.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(steps);
    }

    @Override
    public String toString() {
        return "{" +
            " steps='" + getSteps() + "'" +
            "}";
    }
    
}
