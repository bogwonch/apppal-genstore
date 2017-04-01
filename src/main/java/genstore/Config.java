package genstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.LinkedHashSet;
import genstore.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {
    private final JSONParser parser;
    public final Set<String> categories;
    public String name;

    public Config(File file) {
        Log.debug("reading config file '" + file + "'");
        this.parser = new JSONParser();
        this.categories = new LinkedHashSet<>();
        this.name = null;
        JSONObject config = null;

        try {
            config = (JSONObject) parser.parse(new FileReader(file));

            Log.debug("extracting name");
            this.name = (String) config.get("name");
            if (this.name == null) {
                Log.warn("missing store name calling store 'store'");
                this.name = "store";
            }

            Log.debug("extracting categories");
            final Object configCategories = config.get("categories");
            if (configCategories != null) {
                for (final Object obj : ((JSONArray) configCategories)) {
                    final String category = (String) obj;
                    Log.debug("adding category '" + obj + "'");
                    this.categories.add(category);
                }
            } else {
                Log.warn("no categories defined in config file");
            }
        } catch (FileNotFoundException e) {
            Log.error("cannot read config file: " + e);
        } catch (ParseException e) {
            Log.error("cannot parse config file: " + e);
        } catch (IOException e) {
            Log.error("error while reading config file: " + e);
        }
    }
}
