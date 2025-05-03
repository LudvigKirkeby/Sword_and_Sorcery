import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Initialise_Locations {

    public List<Location> loadJsonFile(ObjectMapper objectMapper, List<Edge> edges, Map<String, String[]> exit_map) throws IOException {
        List<Location> locations = new ArrayList<>();
        HashMap<String, Location> location_map = new HashMap<>();
        JsonNode file = objectMapper.readTree(new File("Locations.json"));
        JsonNode locations_JSON = file.get("locations");

        for (JsonNode n : locations_JSON) {
            String location_name = n.get("name").asText();
            String location_desc = n.get("description").asText();

            if (n.has("actions")) {
                List<Action> actions = new ArrayList<>();
                for (JsonNode a : n.get("actions")) {
                    String action_result = a.get("action_result").asText();
                    String action_desc = a.get("action_description").asText();
                    String action_keyword = a.get("action_keyword").asText();

                    int[] action_stats = {0,0,0,0};
                    for (int i = 0; i < a.get("stat_change").size(); i++) {
                        action_stats[i] = a.get("stat_change").get(i).asInt();
                    }
                    actions.add(new Action(action_result, action_desc, action_keyword, action_stats));
                }
                Location l = new Location(location_name, location_desc, actions);
                locations.add(l);
                location_map.put(location_name, l);
            } else if (n.has("fight")) {

            } else {
                Location l = new Location(location_name, location_desc);
                locations.add(l);
                location_map.put(location_name, l);
            }

            int s = n.get("to").size();
            String[] to = new String[s];
            String[] exitName = new String[s];
            Double[] exitLength = new Double[s];
            for (int j = 0; j < s; j++) {
                to[j] = n.get("to").get(j).asText();
                exitName[j] = n.get("exitName").get(j).asText();
                exitLength[j] = n.get("exitLength").get(j).asDouble();
            }
            for (int j = 0; j < to.length; j++) {
                edges.add(new Edge(location_map.get(location_name), location_map.get(to[j]), exitLength[j], exitName[j]));
            }
        }

        return locations;
    }
}
