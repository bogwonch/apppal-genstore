package genstore;

import apppal.logic.evaluation.AC;
import apppal.logic.evaluation.Evaluation;
import apppal.logic.evaluation.Result;
import apppal.logic.language.Assertion;
import genstore.APK;
import genstore.Data;
import genstore.Log;
import java.lang.Boolean;
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

  public StoreBuilder(Data data, AC ac)
  {
    this.data = data;
    this.ac = ac;

    this.sellable = new LinkedHashSet<>();
    this.categorizations = new HashMap<>();
  }

  public int prepare()
  {
    Log.debug("preparing store");
    this.pickApps();
    this.pickCategories();

    return this.sellable.size();
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
}
