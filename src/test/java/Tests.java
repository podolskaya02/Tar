import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class Tests {
    private Tar tar = new Tar();
    private File outputFile1 = new File("outputFiles\\out");
    private File outputFile2 = new File("outputFiles\\out2");
    private File outputFile3 = new File("outputForUnite\\outputForUnite");

    Tests() throws IOException {
    }

    @Test
    void test1() throws FileNotFoundException {
        assertEquals(outputFile3 , tar.launch(new String[]{"inputFiles\\file1", "inputFiles\\file2", "-out"}));
    }

    @Test
    void test2() throws FileNotFoundException {
        assertEquals(outputFile3, tar.launch(new String[]{"inputFiles\\file1", "inputFiles\\file2", "inputFiles\\file3", "-out"}));
    }

    @Test
    void test3() throws FileNotFoundException {
        File array = new File(Arrays.toString( new File[] {outputFile1, outputFile2}));
        assertEquals(array, tar.launch(new String[]{"inputFiles\\file1", "-u"}));
    }

}
