import java.io.*;
import java.nio.Buffer;

class SplitUpFiles {

    private BufferedReader bufferedReader = new BufferedReader(new FileReader("inputFiles//file1"));
    private File[] output = new File("outputFiles").listFiles();

    SplitUpFiles() throws FileNotFoundException { }

    File[] split() throws IOException {
        boolean mark;
        String line;
            for (File file : output) {
                mark = false;
                BufferedWriter bufferedWritter = new BufferedWriter(new FileWriter(file));
                    while (!mark) {
                        line = bufferedReader.readLine();
                        if (line == null) break;
                        bufferedWritter.write(line);
                        bufferedWritter.flush();
                        bufferedWritter.write("\n");
                        if (line.endsWith("\\n")) mark = true; // граница для разделения файла
                    }
        }
            return output;
    }
}
