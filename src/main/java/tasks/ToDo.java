package main.java.tasks;

public class ToDo extends Task {

    protected String from;
    protected String to;

    public ToDo(String description) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
