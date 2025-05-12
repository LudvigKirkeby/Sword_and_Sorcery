import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Initialise_Locations {
    Path projectRoot = Paths.get(System.getProperty("user.dir"));
    Path locationsJson = projectRoot.resolve("data").resolve("Locations.json");

    public void loadJsonFile(ObjectMapper objectMapper, List<Location> locations, List<Edge> edges) throws IOException {
        try {
            String location_name;
            Location l = null;
            HashMap<String, toLocation> to_location_map = new HashMap<>();

            HashMap<String, Location> location_map = new HashMap<>();
            JsonNode file = objectMapper.readTree(new File(String.valueOf(locationsJson)));
            JsonNode locations_JSON = file.get("locations");

            for (JsonNode n : locations_JSON) {
                location_name = n.get("name").asText();
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
                        actions.add(new Action(action_desc, action_keyword, action_result, action_stats));
                    }
                    l = new Location(location_name, location_desc, actions);
                } else if (n.has("fight")) {

                } else {
                    l = new Location(location_name, location_desc);
                }
                location_map.put(location_name, l);
                locations.add(l);

                int s = n.get("to").size();
                toLocation t = new toLocation(new String[s], new String[s], new Double[s]);
                to_location_map.put(location_name, t);
                for (int j = 0; j < s; j++) {
                    t.to[j] = n.get("to").get(j).asText();
                    t.exitName[j] = n.get("exitName").get(j).asText();
                    if (n.get("exitLength").get(j) == null) { t.exitLength[j] = 0.0; } else {
                        t.exitLength[j] = n.get("exitLength").get(j).asDouble();
                    }
                }
            }
            for (Location loc : locations) {
                toLocation to_loc = to_location_map.get(loc.getName());
                if (to_loc == null) { continue; }
                for (int i = 0; i < to_loc.to.length; i++) {
                    Edge e = new Edge(loc, location_map.get(to_loc.to[i]), to_loc.exitLength[i], to_loc.exitName[i]);
                    loc.adjacent_edges.add(e);
                    edges.add(e);
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static class toLocation {
        String[] to, exitName;
        Double[] exitLength;

        toLocation(String[] to, String[] exitName, Double[] exitLength) {
            this.to = to;
            this.exitName = exitName;
            this.exitLength = exitLength;
        }
    }
}
