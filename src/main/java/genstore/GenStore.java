package genstore;

import apppal.logic.evaluation.AC;
import genstore.Data;
import genstore.Log;
import genstore.Options;
import genstore.StoreBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class GenStore {
    private final AC ac;
    private final Options options;
    private final Data data;
    private final StoreBuilder builder;

    public GenStore(Options options) {
        Log.debug("options: " + options);
        this.ac = new AC();
        this.options = options;
        this.data = new Data(options.data.get(0));

        this.parsePolicies();
        this.data.load();
        this.builder = new StoreBuilder(this.data, this.ac);

        final int prepared = this.builder.prepare();
        Log.debug("found " + prepared + " apps for the store.");

        this.builder.build();
    }

    private void parsePolicies() {
        Log.debug("importing policies");
        for (final File policy : this.options.policies) {
            if (policy.isFile())
                this.parsePolicy(policy);
            else if (policy.isDirectory())
                this.parsePolicyDirectory(policy);
            else
                Log.error("cannot parse policy '" + policy + "'");
        }
    }

    private void parsePolicy(final File policy) {
        Log.debug("loading: '" + policy + "'");
        try {
            this.ac.merge(new AC(new FileInputStream(policy)));
        } catch (IOException e) {
            Log.error("failed to load policy: " + e);
        }
    }

    private void parsePolicyDirectory(final File dir) {
        Log.debug("loading policies in: '" + dir + "'");
        for (File file : dir.listFiles(new PolicyFilenameFilter())) {
            this.parsePolicy(file);
        }
    }

    public static void main(final String[] args) {
        Options options = new Options(args);
        GenStore genstore = new GenStore(options);
    }

    // This should probably be moved into libAppPAL.
    class PolicyFilenameFilter implements FilenameFilter {
        public boolean accept(final File dir, final String file) {
            return file.endsWith(".policy") || file.endsWith(".spp");
        }
    }
}
