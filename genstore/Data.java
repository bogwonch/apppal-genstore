package genstore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashSet;
import java.util.Set;
import genstore.Log;

class Data
{
  private final Set<File> apks;

  public Data()
  {
    this.apks = new LinkedHashSet<>();
  }

  public void load(File dir)
  { Log.debug("loading data dir '"+dir+"'");
    this.validate(dir);
    this.populate(dir);
  }


  private final File apkDir(File dir) { return new File(dir+"/apks"); }

  private void populateAPKs(File dir)
  {
    final File[] apks = this.apkDir(dir).listFiles(new APKFilter());
    if (apks != null) {
      for(final File apk: apks)
        this.apks.add(apk);
      Log.debug("found "+this.apks.size()+" apks");
    }
  }

  private void validate(File dir)
  {
    if (! dir.isDirectory())
      Log.error("data directory '"+dir+"' is not a directory");

    if (! apkDir(dir).isDirectory())
      Log.warn("data directory '"+dir+"' missing apks");
  }

  private void populate(File dir)
  {
    this.populateAPKs(dir);
    File permissions_dir = new File(dir+"/permissions");
    if (! permissions_dir.exists())
      try
      { Log.debug("creating permissions dir '"+permissions_dir+"'");
        permissions_dir.mkdir();
      }
      catch (SecurityException e)
      { Log.error("couldn't create permissions directory: "+e); }
  }

  class APKFilter implements FilenameFilter
  { public boolean accept(final File dir, final String file)
    { return file.endsWith(".apk"); }
  }
}
