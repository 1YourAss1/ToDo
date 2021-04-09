package sample.model;

public class Task {
    private final String strTask;

    public Task(String newTask) {
        this.strTask = newTask;
    }

    @Override
    public String toString() {
        return this.strTask;
    }
}
