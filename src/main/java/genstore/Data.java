package genstore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashSet;
import java.util.Set;
import genstore.Log;
import genstore.Config;
import genstore.APK;

public class Data {
    private final File dir;
    public final Set<APK> apks;
    public final Config config;

    public Data(File dir) {
        this.dir = dir;
        this.apks = new LinkedHashSet<>();
        this.config = new Config(new File(this.dir.toString() + "/config.json"));
    }

    public void load() {
        Log.debug("loading data dir '" + this.dir + "'");
        this.validate();
        this.populate();
    }

    private final File apkDir() {
        return new File(this.dir + "/apks");
    }

    private void populateAPKs() {
        final File[] apks = this.apkDir().listFiles(new APKFilter());
        if (apks != null) {
            for (final File apk : apks)
                this.apks.add(new APK(apk));
            Log.debug("found " + this.apks.size() + " apks");
        }
    }

    private void validate() {
        if (!this.dir.isDirectory())
            Log.error("data directory '" + this.dir + "' is not a directory");

        if (!apkDir().isDirectory())
            Log.warn("data directory '" + this.dir + "' missing apks");
    }

    private void populate() {
        this.populateAPKs();
        File permissions_dir = new File(this.dir + "/permissions");
        if (!permissions_dir.exists())
            try {
                Log.debug("creating permissions dir '" + permissions_dir + "'");
                permissions_dir.mkdir();
            } catch (SecurityException e) {
                Log.error("couldn't create permissions directory: " + e);
            }
    }

    public class APKFilter implements FilenameFilter {
        public boolean accept(final File dir, final String file) {
            return file.endsWith(".apk");
        }
    }
}
