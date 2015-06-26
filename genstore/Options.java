package genstore;

import java.util.List;
import java.util.LinkedList;
import java.io.File;

import genstore.Log;

public class Options
{
  private final static String USAGE =
    "genstore: generate an app store                      \n"+
    "                                                     \n"+
    "Usage:                                               \n"+
    "    genstore [options] --policy <FILE> --data <DIR>  \n"+
    "                                                     \n"+
    "Options:                                             \n"+
    "    -h --help          display this message          \n"+
    "    -p --policy <FILE> a policy describing the store \n"+
    "    -d --data <DIR>    a data directory of apps      \n"+
    "    --debug            enable debugging messages     \n"+
    "\n";

  public final List<File> policies;
  public final List<File> data;

  public String toString()
  { return "policies: "+this.policies+" data: "+this.data; }

  public Options(final String[] args)
  {
    this.policies = new LinkedList<>();
    this.data = new LinkedList<>();

    final int LAST_ARG = args.length;
    if (LAST_ARG <= 0) {
      System.out.println(Options.USAGE);
      System.exit(0);
    } else {
      for (int i = 0; i < LAST_ARG; i++)
      {
        final String arg = args[i];
        switch(arg)
        {
          case "-h": case "--help":
            System.out.println(Options.USAGE);
            System.exit(0);
            break;

          case "-p": case "--policy":
            if (i+1 < LAST_ARG) this.policies.add(new File(args[++i]));
            else Log.error("missing argument to '"+arg+"'");
            break;

          case "-d": case "--data":
            if (i+1 < LAST_ARG) this.data.add(new File(args[++i]));
            else Log.error("missing argument to '"+arg+"'");
            break;

          case "--debug":
            Log.debug = true;
            break;

          default:
            Log.warn("ignoring unrecognized argument '"+arg+"'");
            break;
        }
      }
    }
  }
}

