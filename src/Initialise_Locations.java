import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Initialise_Locations {
    private ObjectMapper objectMapper;

    Initialise_Locations(ObjectMapper objectMapper, Map<String, Location> locations, String[] exits, Map<String, String[]> exit_map) {
       try {
           JsonNode file = objectMapper.readTree(new File("Locations.json"));
           JsonNode locations_JSON = file.get("locations");

           for (JsonNode n : locations_JSON) {
               String location_name = n.get("name").asText();
               String location_desc = n.get("description").asText();
               if (n.has("actions")) {
                   for (JsonNode a : n.get("actions")) {
                       String action_result = a.get("action_result").asText();
                       String action_desc = a.get("action_description").asText();
                       String action_keyword = a.get("action_keyword").asText();

                       int[] action_stats = {0,0,0,0};
                       for (int i = 0; i < a.get("stat_change").size(); i++) {
                           action_stats[i] = a.get("stat_change").get(i).asInt();
                       }
                       Action newaction = new Action(action_desc, action_keyword, action_result, action_stats);
                       locations.put(location_name, new Location(location_desc, new Action[]{newaction}));
                   }
               } else if (n.has("fight")) {

               } else {
                   locations.put(location_name, new Location(location_desc));
               }
               exits = new String[4];
               for (int j = 0; j < exits.length; j++) {
                   exits[j] = n.get("exits").get(j).asText();
               }
               exit_map.put(location_name, exits);
           }

           for (String locationName : exit_map.keySet()) {
               exits = exit_map.get(locationName);

               Location northExit = exits[0].equals("null") ? null : locations.get(exits[0]);
               Location eastExit = exits[1].equals("null") ? null : locations.get(exits[1]);
               Location southExit = exits[2].equals("null") ? null : locations.get(exits[2]);
               Location westExit = exits[3].equals("null") ? null : locations.get(exits[3]);

               locations.get(locationName).setExits(northExit, eastExit, southExit, westExit);
           }
       } catch(IOException e) {
           throw new RuntimeException();
       }
    }
}
