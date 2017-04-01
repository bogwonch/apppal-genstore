package genstore;

import apppal.logic.language.Assertion;
import genstore.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class APK
{
  public final File path;
  public final String name;
  public final File icon;

  public APK(final File path)
  {
    this.path = path;

    // Pretty sure I should put some verification code in here...
    // Also HAHAHAHHAAHAHAHA.  Java is a TERRIBLE language.
    final String[] split_path = path.toString().split("/");
    final String temp = split_path[split_path.length - 1];
    this.name = temp.substring(0, temp.length() - 4);
    this.icon = new File(this.path.getParent().toString() + "/../icons/" + this.name + ".png");

    if (! this.icon.exists())
      Log.warn("missing icon for '" + this.name + "'");

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

  // Should probably be refactored
  private static void copy(final File from, final File to)
  {
    // All this code just to copy a file?
    // WTF Java?
    InputStream is = null;
    OutputStream os = null;
    try
    {
      is = new FileInputStream(from);
      os = new FileOutputStream(to);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0)
      {
        os.write(buffer, 0, length);
      }
    }
    catch (FileNotFoundException e)
    {
      Log.error("can't find file: " + e);
    }
    catch (IOException e)
    {
      Log.error("something went wrong during copy: " + e);
    }
    finally
    {
      try
      {
        if (is != null) is.close();
        if (os != null) os.close();
      }
      catch (IOException e)
      {
        Log.error("cannot close file: " + e);
      }
    }
  }

  public void copyAPK(final File path)
  {
    try
    {
      // Create the path if it doesn't exist
      path.mkdirs();

      if (! this.path.isFile()) Log.error("apk '" + this.name + "' is not a file");
      if (! path.isDirectory()) Log.error("cannot copy apk to '" + path + "': not a directory");
      APK.copy(this.path, new File(path + "/" + this.name + ".apk"));
    }
    catch (SecurityException e)
    {
      Log.error("error copying apk: " + e);
    }
  }

  public void copyIcon(final File path)
  {
    try
    {
      path.mkdirs();
      if (this.icon.exists())
      {
        APK.copy(this.icon, new File(path + "/" + this.name + ".png"));
      }
      else
        Log.warn(this.name + " is missing icon");
    }
    catch (SecurityException e)
    {
      Log.error("error copying icon: " + e);
    }
  }
}
