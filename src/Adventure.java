import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Adventure {
    boolean new_Game = false;
    ObjectMapper objectMapper = new ObjectMapper();
    Character character;
    boolean inCombat = false;
    Location currentLocation = new Location();
    List<Location> locations = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    public Adventure() throws IOException {
        character = new Character();
        Initialise_Locations init = new Initialise_Locations();
        LocationGraph G = new LocationGraph();
        init.loadJsonFile(objectMapper, locations, edges); // Updates the locations and edges list via reading a JSON file
        G.addNodes(locations);
        G.addEdges(edges);
        currentLocation = G.getLocations().getFirst();
    }

    void NewGame_or_Load() {
        if (new_Game) {
            character.createNewCharacter(objectMapper);
        } else {
            character.loadSaveData(objectMapper);
        }
    }

    void combat() {
        inCombat = true;
        Enemy monster = new Enemy("Ghoul", 10, 2);
        System.out.println("This room contains combat! You are fighting a " + monster.getName());
        while (character.getEndurance() > 0 && monster.getEndurance() > 0) {
            if (character.RollStrength() > monster.RollStrength()) {
                monster.takeDamage(2, inCombat);
            } else {
                character.takeDamage(2);
            }
        }
    }

    public String userInput() { // get input string from user
        return (new Scanner(System.in)).nextLine();
    }

    public void play() throws IOException {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            String command = userInput();
            finished = processCommand(command);
        }
    }

    public void printWelcome() {
        System.out.println();
        System.out.println("The Labyrinth of Death awaits thee, traveller. Let us get thy prepared.");
        System.out.println();
        printLocationInfo();
    }

    public boolean processCommand(String command) throws IOException {
        switch (command) {
            case "quit":
                character.saveCharacterData(objectMapper, currentLocation);
                return true;

            case "stats":
                character.StatOverview();
                break;

            default:
                attemptAction(command);
                go(command);
                break;
        }
        return false;

//        if (command.equals("go")) {
//            System.out.println("Go where?");
//            String direction = userInput();
//            go(direction);
//        } else if (command.equals("quit")) {
//
//        } else if (command.equals("stats")) {
//
//        } else if (!attemptAction(command)) {
//            System.out.println("Speak louder, Friend. I didnt quite catch that.");
//        }
//        return false;
    }

    public boolean attemptAction(String keyword) {
        List<Action> currentLocationActions = currentLocation.getActions();

        if (currentLocationActions == null) { return false; }
        for (Action action : currentLocationActions) {
            if (action.canExecute(keyword)) {
                action.printResult();
                action.change_stats(character.getCurrentStats());
                return true;
            }
        }
        return false;
    }

    private void printLocationInfo() {
        System.out.println("You are at " + currentLocation.getName());
        System.out.println(" ");
        currentLocation.printDescription();
        System.out.println(" ");

        if (currentLocation.getActions() != null) {
            System.out.println("You have the following actions ");
            for (Action a : currentLocation.getActions()) {
            System.out.println(a.getDescription());
            }
        }

        System.out.print("Exits: ");
        for (Edge e : currentLocation.getAdjacentEdges()) {
            System.out.print(e.getDirection() + " ");
        }
        System.out.println();
    }

    private void go(String direction) {
        for (Edge e : currentLocation.getAdjacentEdges()) {
            if (e.getDirection().equals(direction)) {
                currentLocation = e.getTo();
                printLocationInfo();
            }
        }
    }
}
