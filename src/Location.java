import java.util.ArrayList;
import java.util.List;

public class Location {
    public String description;
    public String name;
    List<Action> actions;
    List<Edge> adjacent_edges; // Knows its adjacent connections (and thus rooms, with a getter for "to")

    Location(String name, String description, List<Action> actions) {
        this(name, description);
        this.actions = actions;
    }

    Location(String name, String description) {
        this.description = description;
        this.name = name;
    }

    Location() {}

    public void addAction(Action action) {
        actions.add(action);
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public List<Action> getActions() { return actions; }

    public List<Edge> getAdjacentEdges() { return adjacent_edges; }

    public String getDescription() { return description; }

    public String getName() { return name; }

}
