package genstore;

import apppal.logic.evaluation.AC;
import apppal.logic.evaluation.Evaluation;
import apppal.logic.evaluation.Result;
import apppal.logic.language.Assertion;
import genstore.APK;
import genstore.Data;
import genstore.Log;
import java.lang.Boolean;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoreBuilder
{
  private final Data data;
  private final AC ac;
  private final Set<APK> sellable;
  private final Map<String, List<APK>> categorizations;
  private final File output;
  private Connection conn;

  public StoreBuilder(Data data, AC ac)
  {
    this.data = data;
    this.ac = ac;

    this.sellable = new LinkedHashSet<>();
    this.categorizations = new HashMap<>();

    this.output = new File("output/" + this.data.config.name);
    this.conn = null;
  }

  public int prepare()
  {
    Log.debug("preparing store");
    this.pickApps();
    this.pickCategories();
    return this.sellable.size();
  }

  public void build()
  {
    Log.debug("building store");
    output.mkdirs();

    try
    {
      this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.output.toString() + "/store.db");
      this.createTables();
    }
    catch (SQLException e)
    {
      Log.error("database error: " + e);
    }
    finally
    {
      try
      {
        if (this.conn != null) this.conn.close();
      }
      catch (SQLException e)
      {
        Log.error("cannot close database");
      }
    }

  }

  private void pickApps()
  {
    Log.debug("picking apps");

    final Evaluation eval = new Evaluation(this.ac);
    for (final APK apk : data.apks)
    {
      final Result result = eval.run(apk.isSellable());

      if (result.isProven())
        Log.debug(apk + " is sellable.");

      this.sellable.add(apk);
    }
  }

  private void pickCategories()
  {
    Log.debug("picking categories");

    for (final String category : this.data.config.categories)
    {
      final List<APK> found = new LinkedList<>();
      final Evaluation eval = new Evaluation(this.ac);
      for (final APK apk : this.sellable)
      {
        final Assertion hasCategory = apk.hasCategory(category);
        final Result result = eval.run(hasCategory);
        if (result.isProven())
          Log.debug(apk + " has category " + category + ".");

        if (result.isProven())
          found.add(apk);
      }
      this.categorizations.put(category, found);
      Log.debug("found " + found.size() + " apps with category '" + category + "'");
    }
  }

  public void createTables() throws SQLException
  {
    final Statement statement = this.conn.createStatement();
    statement.setQueryTimeout(30);

    Log.debug("creating tables");
    statement.executeUpdate("DROP TABLE IF EXISTS apk");
    statement.executeUpdate("DROP TABLE IF EXISTS category");
    statement.executeUpdate("DROP TABLE IF EXISTS apk_category");
    statement.executeUpdate("CREATE TABLE apk(id TEXT PRIMARY KEY)");
    statement.executeUpdate("CREATE TABLE category(name TEXT PRIMARY KEY)");
    statement.executeUpdate("CREATE TABLE apk_category(apk TEXT, category TEXT, FOREIGN KEY(apk) REFERENCES apk(id), FOREIGN KEY(category) REFERENCES category(name))");

    Log.debug("adding apks");
    final PreparedStatement insertAPK = this.conn.prepareStatement("INSERT INTO apk VALUES (?)");
    for (APK apk : this.sellable)
    {
      insertAPK.setString(1, apk.toString());
      insertAPK.executeUpdate();
    }

    Log.debug("adding categories");
    final PreparedStatement insertCategory = this.conn.prepareStatement("INSERT INTO category VALUES (?)");
    final PreparedStatement insertCategorization = this.conn.prepareStatement("INSERT INTO apk_category VALUES (?, ?)");
    for (final String category : this.categorizations.keySet())
    {
      insertCategory.setString(1, category);
      insertCategory.executeUpdate();

      for (APK apk : this.categorizations.get(category))
      {
        insertCategorization.setString(1, apk.toString());
        insertCategorization.setString(2, category);
        insertCategorization.executeUpdate();
      }
    }
  }
}
