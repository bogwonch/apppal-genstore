package genstore;

import genstore.Log;
import genstore.Options;

public class GenStore
{
  public GenStore(Options options)
  {
    Log.debug("parsed options: "+options);

  }

  public static void main(String[] args) {
    Options options = new Options(args);
    GenStore genstore = new GenStore(options);
  }
}

