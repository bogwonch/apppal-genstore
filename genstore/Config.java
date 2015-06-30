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

public class Config
{
  private final JSONParser parser;
  public final Set<String> categories;

  public Config(File file)
  {
    Log.debug("reading config file '" + file + "'");
    this.parser = new JSONParser();
    this.categories = new LinkedHashSet<>();
    try
    {
      final JSONObject config;
      config = (JSONObject) parser.parse(new FileReader(file));

      Log.debug("extracting categories");
      for (final Object obj : ((JSONArray) config.get("categories")))
      {
        final String category = (String) obj;
        Log.debug("adding category '" + obj + "'");
        this.categories.add(category);
      }
    }
    catch (FileNotFoundException e)
    {
      Log.error("cannot read config file: " + e);
    }
    catch (ParseException e)
    {
      Log.error("cannot parse config file: " + e);
    }
    catch (IOException e)
    {
      Log.error("error while reading config file: " + e);
    }
  }
}
