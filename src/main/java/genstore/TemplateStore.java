package genstore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import genstore.Log;

public class TemplateStore
{
  private final Path template;

  public TemplateStore(final File template)
  {
    this(template.toPath());
  }

  public TemplateStore(final Path template)
  {
    this.template = template;
  }

  public void install(final File dest)
  {
    this.install(dest.toPath());
  }

  private void copy(final Path from, final Path to)
  {
    try
    {
      // Now we have two problems...
      final String[] path = from.toString().split("/(?=[^/]+$)");
      final String basename = path[1];
      if (Files.isRegularFile(from))
      {
        final Path target = new File(to + "/" + basename).toPath();
        Log.debug("installing '" + from + "' to '" + target + "'");
        Files.copy(from, target, StandardCopyOption.REPLACE_EXISTING);
      }
      else if (Files.isDirectory(from))
      {
        final File subdir = new File(to + "/" + basename);
        subdir.mkdirs();
        for (final Path file : Files.newDirectoryStream(from, "*"))
          this.copy(file, subdir.toPath());
      }
      else
        Log.warn("don't know how to copy '" + from + "'");
    }
    catch (IOException e)
    {
      Log.error("error installing template: " + e);
    }
  }

  public void install(final Path dest)
  {
    try
    {
      for (final Path file : Files.newDirectoryStream(this.template, "*"))
        this.copy(file, dest);
    }
    catch (IOException e)
    {
      Log.error("error installing template: " + e);
    }
  }
}
