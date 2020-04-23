import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class Tests {
    private Tar tar = new Tar();
    private File outputFile1 = new File("outputFiles\\out");
    private File outputFile2 = new File("outputFiles\\out2");
    private File outputFile3 = new File("outputFiles\\outputForUnite");

    @Test
    void test1() {
        assertEquals(outputFile1, tar.launch(new String[]{"file1", "file2", "-out"}));
    }

    @Test
    void test2() {
        assertEquals(outputFile3, tar.launch(new String[]{"file1", "file2", "file3", "-out"}));
    }

    @Test
    void test3() {
        File array = new File(Arrays.toString( new File[] {outputFile1, outputFile2}));
        assertEquals(array, tar.launch(new String[]{"file1", "-u"}));
    }
    
}
