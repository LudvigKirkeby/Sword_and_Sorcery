import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
    boolean in_combat = false;
    int currentRations;
    int[] currentStats = new int[4];
    Location currentLocation;

    public Adventure() {
        character = new Character();
        locations = new HashMap<>();
        exit_map = new HashMap<>();
        Equipment = new HashMap<>();

        Initialise_Locations initialize = new Initialise_Locations(objectMapper, locations, exits, exit_map);
    }

    void NewGame_or_Load() {
        if (new_Game) {
            character.createNewCharacter(objectMapper, Equipment, currentStats, currentLocation, currentRations, locations, this);
        } else {
            character.loadSaveData(objectMapper, Equipment, currentStats, maxStats, currentLocation, currentRations, locations, this);
        }
    }

    void combat(Monster monster) {
        in_combat = true;
        System.out.println("This room contains combat! You are fighting a " + monster.Name);
        while (character.stats[0] > 0 && monster.stats[0] > 0) {
            if (character.RollStrength() > monster.RollStrength()) {
                monster.stats[0] -= 2;
                if (monster.stats[0] == 0) {
                    monster.Death();
                    in_combat = false;
                }
            } else {
                character.stats[0] -= 2;
                if (character.stats[0] == 0) {
                    character.Death();
                }
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
        System.out.print("Exits: ");
        if (currentLocation.northExit != null) System.out.print("north ");
        if (currentLocation.eastExit != null) System.out.print("east ");
        if (currentLocation.southExit != null) System.out.print("south ");
        if (currentLocation.westExit != null) System.out.print("west ");
        System.out.println();
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
                int[] action_stats = action.change_stats();
                for (int i = 0; i < currentStats.length; i++) {
                    if (i < 3) {
                            currentStats[i] = currentStats[i] + action_stats[i];
                    } else {
                        currentStats[i] += action_stats[i];
                    }
                }
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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void go(String direction) {
        Location nextLocation = null;
        if (!in_combat) {
            if (direction.equals("north")) nextLocation = currentLocation.northExit;
            else if (direction.equals("east")) nextLocation = currentLocation.eastExit;
            else if (direction.equals("south")) nextLocation = currentLocation.southExit;
            else if (direction.equals("west")) nextLocation = currentLocation.westExit;

            if (nextLocation != null) {
                currentLocation = nextLocation;
                System.out.println(currentLocation);
                for (Action x : currentLocation.getActions()) {
                    System.out.println(x.getDescription());
                }
                System.out.print("Exits: ");
                if (currentLocation.northExit != null) System.out.print("north ");
                if (currentLocation.eastExit != null) System.out.print("east ");
                if (currentLocation.southExit != null) System.out.print("south ");
                if (currentLocation.westExit != null) System.out.print("west ");
                System.out.println();
            } else {
                System.out.println("There is no door!");
            }
        }

    }
}
