import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Adventure {
    boolean new_Game = false;
    ObjectMapper objectMapper = new ObjectMapper();
    Character character;
    Map<String, Location> locations;
    Map<String, String[]> exit_map;
    Map<String, Integer> Equipment;
    JsonNode savefile;
    int[] maxStats = new int[3];
    String[] exits;
    boolean inCombat = false;
    int currentRations;
    int[] currentStats = new int[4];
    Location currentLocation;

    public Adventure() {
        character = new Character();
        locations = new HashMap<>();
        exit_map = new HashMap<>();
        Equipment = new HashMap<>();

        new Initialise_Locations(objectMapper, locations, exits, exit_map);
    }

    void NewGame_or_Load() {
        if (new_Game) {
            character.createNewCharacter(objectMapper, locations, this);
        } else {
            character.loadSaveData(objectMapper, locations, this);
        }
    }

    void combat() {
        inCombat = true;
        Monster monster = new Monster("Ghoul", 10, 2);
        System.out.println("This room contains combat! You are fighting a " + monster.getName());
        while (character.getEndurance() > 0 && monster.getEndurance() > 0) {
            if (character.RollStrength() > monster.RollStrength()) {
                monster.takeDamage(2, inCombat);
            } else {
                character.takeDamage(this, 2);
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
        if (command.equals("go")) {
            System.out.println("Go where?");
            String direction = userInput();
            go(direction);
        } else if (command.equals("quit")) {
            saving();
            return true;
        } else if (command.equals("stats")) {
            character.StatOverview(this);
        } else if (!attemptAction(command)) {
            System.out.println("Speak louder, Friend. I didnt quite catch that.");
        }
        return false;
    }

    public boolean attemptAction(String keyword) {
        Action[] currentLocationActions = currentLocation.getActions();

        for (Action action : currentLocationActions) {
            if (action.canExecute(keyword)) {
                System.out.println(action.execute());
                action.change_stats(currentStats);
                return true;
            }
        }
        return false;
    }

    private void saving() throws IOException {
        savefile = objectMapper.readTree(new File("Playerdata.json"));
        try {
            System.out.println("Player data saved to " + "C:/Users/ludvi/IdeaProjects/Sword and Sorcery/Playerdata.json");

            for (Entry<String, Location> entry : locations.entrySet()) {
                if (entry.getValue() == currentLocation) {
                    ((ObjectNode) savefile).put("location_name", entry.getKey());
                }
            }
            for (int i = 0; i < 4; i++) {
                ((ArrayNode) savefile.get("stats")).set(i, currentStats[i]);
            }

            ((ObjectNode) savefile.get("inventory")).put("rations", currentRations);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:/Users/ludvi/IdeaProjects/Sword and Sorcery/Playerdata.json"), savefile);
            System.out.println("Successfully saved the game.");
        } catch (IOException e) {
            System.out.println("Error: Game did not save.");
        }
    }

    private void printLocationInfo() {
        System.out.println(currentLocation);
        for (Action x : currentLocation.getActions()) {
            System.out.println(x.getDescription());
        }
        currentLocation.getExitString();
    }

    private void go(String direction) {
        Location nextLocation = null;

        if (!inCombat) {
            nextLocation = currentLocation.getDirections(direction);
        }

        if (nextLocation != null) {
            currentLocation = nextLocation;
            printLocationInfo();
        } else {
            System.out.println("There is no door!");
        }
    }

}
