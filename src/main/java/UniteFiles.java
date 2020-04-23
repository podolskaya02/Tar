import java.io.*;

class UniteFiles {

        private File[] input = new File("inputFiles").listFiles();
        private File outFile = new File("outputFiles\\out");
        private FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        UniteFiles() throws FileNotFoundException { }

    File outputStream() throws IOException {
            for (File file: input) {
                FileInputStream fileInputStream = new FileInputStream(file);
                while (fileInputStream.available() > 0) {
                    int i = fileInputStream.read();
                    fileOutputStream.write(i);
                }
            }
            return outFile;
        }
}