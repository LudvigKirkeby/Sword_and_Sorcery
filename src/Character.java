import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Character extends Stats {
    Path characterJson = Paths.get(System.getProperty("user.dir")).resolve("data").resolve("Playerdata.json");
    int[] max_stats = new int[3];
    int currentRations;
    int[] current_stats = new int[4];
    Map<String, Integer> equipment = new HashMap<>();
    // Equipment, some sort of map ????

    Character() {}

    public void Death() {
        System.out.println("The Labyrinth claims another...");
        System.exit(0);
    }

    void createNewCharacter(ObjectMapper objectMapper) {
        System.out.println("The stars will decide your fate. Let us pray they are Merciful.");
        try {
            JsonNode savefile = objectMapper.readTree((characterJson).toFile());
            ObjectNode inventoryNode = (ObjectNode) savefile.get("inventory");
            System.out.println("New journey started.");
            current_stats = new int[]{new Random().nextInt(12) + 1 + 12, // end
                    new Random().nextInt(6) + 1 + 12, // skill
                    new Random().nextInt(6) + 1 + 12, // luck
                    0}; // gold

            for (int x = 0; x < max_stats.length; x++) {
                max_stats[x] = current_stats[x];
            }

            for (int v = 0; v < 3; v++) {
                ((ArrayNode) savefile.get("max_stats")).set(v, current_stats[v]);
            }
            currentRations = 10;
            equipment.put("Broadsword", 0);
            equipment.put("Leather jerkin", 0);

            ArrayNode armorArray = (ArrayNode) inventoryNode.get("armor");
            ObjectNode armorNode = (ObjectNode) armorArray.get(0);
            armorNode.put("armor_name", "Leather Jerkin");
            armorNode.put("armor_strength", 0);

            ArrayNode weaponArray = (ArrayNode) inventoryNode.get("weapon");
            ObjectNode weaponNode = (ObjectNode) weaponArray.get(0);
            weaponNode.put("weapon_name", "Broadsword");
            weaponNode.put("weapon_strength", 0);


            objectMapper.writerWithDefaultPrettyPrinter().writeValue((characterJson).toFile(), savefile);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void saveCharacterData(ObjectMapper object_mapper, Location current_location) throws IOException {
        JsonNode save_file = object_mapper.readTree((characterJson).toFile());
        try {
            ((ObjectNode) save_file).put("location_name", current_location.getName());
            for (int i = 0; i < current_stats.length; i++) {
                ((ArrayNode) save_file.get("stats")).set(i, current_stats[i]);
            }

            ((ObjectNode) save_file.get("inventory")).put("rations", currentRations);
            object_mapper.writerWithDefaultPrettyPrinter().writeValue((characterJson).toFile(), save_file);
            System.out.println("Successfully saved the game.");
        } catch (IOException e) {
            System.out.println("Error: Game did not save.");
        }
    }

    void loadSaveData(ObjectMapper objectMapper) {
        try {
            JsonNode savefile = objectMapper.readTree((characterJson).toFile());
            System.out.println("Loaded save file");
            for (int i = 0; i < current_stats.length; i++) {
                current_stats[i] = savefile.get("stats").get(i).asInt();
            }

            for (int i = 0; i < 3; i++) {
                max_stats[i] = savefile.get("max_stats").get(i).asInt();
                System.out.println(current_stats[i] + " / " + max_stats[i]);
            }
            System.out.println(current_stats[3]);

            currentRations = savefile.get("inventory").get("rations").asInt();

            ArrayNode weaponArray = (ArrayNode) savefile.get("inventory").get("weapon");
            ArrayNode armorArray = (ArrayNode) savefile.get("inventory").get("armor");
            JsonNode weaponNode = weaponArray.get(0);
            JsonNode armorNode = armorArray.get(0);
            equipment.put(weaponNode.get("weapon_name").asText(), weaponNode.get("weapon_strength").asInt());
            equipment.put(armorNode.get("armor_name").asText(), armorNode.get("armor_strength").asInt());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    void StatOverview() {
        System.out.println("Endurance: " + current_stats[0] + " / " + max_stats[0]);
        System.out.println("Skill: " + current_stats[1] + " / " + max_stats[1]);
        System.out.println("Luck: " + current_stats[2] + " / " + max_stats[2]);
        System.out.println("Gold: " + current_stats[3]);
    }

    public int getEndurance() {
        return max_stats[0];
    }

    public int getSkill() {
        return max_stats[1];
    }

    public void takeDamage(int damage) {
        current_stats[0] -= 2;
        if (current_stats[0] <= 0) {
            Death();
        }
    }

    public int[] getCurrentStats() {
        return current_stats;
    }

}


