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

    public void getExitString() {
        System.out.print("Exits: ");
        if (northExit != null) System.out.print("north ");
        if (eastExit != null) System.out.print("east ");
        if (southExit != null) System.out.print("south ");
        if (westExit != null) System.out.print("west ");
        System.out.println();
    }

    public Location getDirections(String direction) {
        Location next_Location;
        next_Location = switch (direction) {
            case "north" -> northExit;
            case "east" -> eastExit;
            case "south" -> southExit;
            case "west" -> westExit;
            default -> null;
        };
        return next_Location;
    }

}
