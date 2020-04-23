import java.io.*;
import java.util.Arrays;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Tar {
    @Option(name = "-out")
    private boolean out;
    @Option(name = "-u")
    private boolean u;
    @Argument(required = true)
    private File[] files;

    public static void main(String[] args) {
        new Tar().launch(args);
    }

    File launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            UniteFiles uniteFiles = new UniteFiles();
            SplitUpFiles splitUpFiles = new SplitUpFiles();
            if (out) {
               return uniteFiles.outputStream();
            } else if (u) {
                return new File(Arrays.toString(splitUpFiles.split()));
            }
        } catch (CmdLineException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}