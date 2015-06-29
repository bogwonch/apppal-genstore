package genstore;

import genstore.Data;
import apppal.logic.evaluation.AC;

public class StoreBuilder
{
  private final Data data;
  private final AC ac;

  public StoreBuilder(Data data, AC ac)
  {
    this.data = data;
    this.ac = ac;
  }
}
