package genstore;

import apppal.logic.language.Assertion;
import genstore.Log;
import java.io.File;
import java.io.IOException;

public class APK
{
  private final File path;
  private final String name;

  public APK(final File path)
  {
    this.path = path;

    // Pretty sure I should put some verification code in here...
    // Also HAHAHAHHAAHAHAHA.  Java is a TERRIBLE language.
    final String[] split_path = path.toString().split("/");
    final String temp = split_path[split_path.length - 1];
    this.name = temp.substring(0, temp.length() - 4);
  }

  public String toString()
  {
    return this.name;
  }

  public String toAppPAL()
  {
    return "'apk://" + this.name + "'";
  }

  public Assertion isSellable()
  {
    final String string = "'store' says " + this.toAppPAL() + " isSellable.";
    try
    {
      return Assertion.parse(string);
    }
    catch (IOException e)
    {
      Log.error("cannot parse \"" + string + "\": " + e);
    }

    // Unreachable
    Log.debug("REACHED UNREACHABLE CODE IN genstore.APK.isSellable");
    return null;
  }

  public Assertion hasCategory(String category)
  {
    final String string = "'store' says " + this.toAppPAL() + " hasCategory('" + category + "').";
    try
    {
      return Assertion.parse(string);
    }
    catch (IOException e)
    {
      Log.error("cannot parse \"" + string + "\": " + e);
    }

    // Unreachable
    Log.debug("REACHED UNREACHABLE CODE IN genstore.APK.isSellable");
    return null;
  }
}
