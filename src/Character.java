import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class Character extends Stats {
    int[] maxStats;

    Character() {
        maxStats = new int[3];
    }

    public void Death() {
        System.out.println("The Labyrinth claims another...");
        System.exit(0);
    }

    void createNewCharacter(ObjectMapper objectMapper, Map<String, Location> locations, Adventure adventure) {
        System.out.println("The stars will decide your fate. Let us pray they are Merciful.");
        try {
            JsonNode savefile = objectMapper.readTree(new File("Playerdata.json"));
            ObjectNode inventoryNode = (ObjectNode) savefile.get("inventory");
            System.out.println("New journey started.");
            adventure.currentStats = new int[]{new Random().nextInt(12) + 1 + 12, // end
                    new Random().nextInt(6) + 1 + 12, // skill
                    new Random().nextInt(6) + 1 + 12, // luck
                    0};

            for (int x = 0; x < maxStats.length; x++) {
                adventure.maxStats[x] = adventure.currentStats[x];
            }

            for (int v = 0; v < 3; v++) {
                ((ArrayNode) savefile.get("maxStats")).set(v, adventure.currentStats[v]);
            }
            adventure.currentLocation = locations.get("beginning");
            adventure.currentRations = 10;
            adventure.Equipment.put("Broadsword", 0);
            adventure.Equipment.put("Leather jerkin", 0);

            ArrayNode armorArray = (ArrayNode) inventoryNode.get("armor");
            ObjectNode armorNode = (ObjectNode) armorArray.get(0);
            armorNode.put("armor_name", "Leather Jerkin");
            armorNode.put("armor_strength", 0);

            ArrayNode weaponArray = (ArrayNode) inventoryNode.get("weapon");
            ObjectNode weaponNode = (ObjectNode) weaponArray.get(0);
            weaponNode.put("weapon_name", "Broadsword");
            weaponNode.put("weapon_strength", 0);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:/Users/ludvi/IdeaProjects/Sword and Sorcery/Playerdata.json"), savefile);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    void loadSaveData(ObjectMapper objectMapper, Map<String, Location> locations, Adventure adventure) {
        try {
            JsonNode savefile = objectMapper.readTree(new File("Playerdata.json"));
            System.out.println("Loaded save file");
            for (int i = 0; i < 4; i++) {
                adventure.currentStats[i] = savefile.get("stats").get(i).asInt();
            }

            for (int i = 0; i < 3; i++) {
                maxStats[i] = savefile.get("maxStats").get(i).asInt();
                System.out.println(adventure.currentStats[i] + " / " + maxStats[i]);
            }
            System.out.println(adventure.currentStats[3]);

            adventure.currentLocation = locations.get(savefile.get("location_name").asText());
            adventure.currentRations = savefile.get("inventory").get("rations").asInt();

            ArrayNode weaponArray = (ArrayNode) savefile.get("inventory").get("weapon");
            ArrayNode armorArray = (ArrayNode) savefile.get("inventory").get("armor");
            JsonNode weaponNode = weaponArray.get(0);
            JsonNode armorNode = armorArray.get(0);
            adventure.Equipment.put(weaponNode.get("weapon_name").asText(), weaponNode.get("weapon_strength").asInt());
            adventure.Equipment.put(armorNode.get("armor_name").asText(), armorNode.get("armor_strength").asInt());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    void StatOverview(Adventure adventure) {
        System.out.println("Endurance: " + adventure.currentStats[0] + " / " + adventure.maxStats[0]);
        System.out.println("Skill: " + adventure.currentStats[1] + " / " + adventure.maxStats[1]);
        System.out.println("Luck: " + adventure.currentStats[2] + " / " + adventure.maxStats[2]);
        System.out.println("Gold: " + adventure.currentStats[3]);
    }

}


