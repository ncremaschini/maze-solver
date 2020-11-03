package it.niccrema.model;
import java.util.LinkedList;
import java.util.Objects;

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
