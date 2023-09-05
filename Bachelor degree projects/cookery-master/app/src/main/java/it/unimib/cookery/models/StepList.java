package it.unimib.cookery.models;

import java.util.List;

public class StepList {

    private String name;
    private List<StepApi> steps;


    public StepList() {
    }

    public StepList(String name, List<StepApi> steps) {
        this.name = name;
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "StepList{" +
                "name='" + name + '\'' +
                ", steps=" + steps +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StepApi> getSteps() {
        return steps;
    }

    public void setSteps(List<StepApi> steps) {
        this.steps = steps;
    }
}
