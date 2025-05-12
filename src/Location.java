import java.util.ArrayList;
import java.util.List;

public class Location {
    public String description;
    public String name;
    List<Action> actions;
    List<Edge> adjacent_edges = new ArrayList<>(); // Knows its adjacent connections (and thus rooms, with a getter for "to")

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

    public void printDescription() {  StringBuilder segmented = new StringBuilder();
        String[] words = description .split(" ");
        short count = 0;
        for (String word : words) {
            segmented.append(word);
            segmented.append(" ");
            count++;
            if (count == 20) {
                System.out.println(segmented);
                System.out.println();
                segmented = new StringBuilder();
                count = 0;
            }
        }
        System.out.println(segmented);
    }

    public String getName() { return name; }

}
