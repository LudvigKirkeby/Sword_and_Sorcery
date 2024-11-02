public class Location {
    public String description;
    public String name;
    public Location northExit, southExit, eastExit, westExit;
    Action[] actions;

    public Location(String description, Action[] actions) { // Constructor with an Action, for rooms with actions
        this.description = description;
        this.actions = actions;
    }

    public Location(String description) { // Constructor without Action, for rooms with no actions
        this.description = description;
        this.actions = new Action[0];
    }

    public Action[] getActions() {
        return actions;
    }

    public void setExits(Location north, Location east, Location south, Location west) {
        northExit = north;
        eastExit = east;
        southExit = south;
        westExit = west;
    }

    public String toString() {
        return description;
    }

}
