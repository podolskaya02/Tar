import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.Arrays;

public class Tar {
    @Option(name = "-out")
    private boolean out;
    @Option(name = "-u")
    private boolean u;
    @Argument(required = true, hidden = true)
    private File[] files;

    public static void main(String[] args) {
        new Tar().launch(args);
    }

    File launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(Tar.this);
        try {
            parser.parseArgument(args);
            if (out) {
                return unite();
            } else if (u) {
                return new File(Arrays.toString(split()));
            }
        } catch (CmdLineException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringBuilder filesNames() {
        StringBuilder s = new StringBuilder();
        for (File file : files) {
            s.append(file.getName()).append(", ");
        }
        return s.deleteCharAt(s.length() - 2);
    }


    private File unite() throws IOException {
        File outputFile = new File("outputForUnite\\outputForUnite"); // выходной файл после объединения файлов
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        int count = 0;
        String line;
        for (File file : files) { // для каждого файла
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) { // считываем построчно
                if (line.contains("\\")) { // начальная граница
                    while (count != 2) { // пока не дойдем до второй границы
                        StringBuffer lineForWrite = new StringBuffer();
                        lineForWrite.append(line);
                        // удаляем из строки символы до первой границы
                        if (count == 0 && line.contains("\\")) {
                            lineForWrite.delete(0, line.indexOf("\\") + 1);
                            count = count + 1;
                        }
                        // удаляем из строки символы после второй границы
                        else if (count == 1 && line.contains("\\")) {
                            lineForWrite.delete(line.indexOf("\\"), line.length());
                            count = count + 1;
                        }
                        // если вторая граница находится на той же строке, что и первая - удаляем всё, что после неё
                        if (lineForWrite.toString().contains("\\")) {
                            lineForWrite.delete(lineForWrite.indexOf("\\"), line.length());
                            count = count + 1;
                        } else {
                            line = bufferedReader.readLine();
                        } // иначе считываем следующую строку
                        bufferedWriter.write(String.valueOf(lineForWrite));
                        bufferedWriter.flush();
                        bufferedWriter.write("\n");
                    }
                    count = 0;
                }
            }
        }
        bufferedWriter.newLine();
        bufferedWriter.write("Input files: " + filesNames());
        bufferedWriter.close();
        return outputFile;
    }

    private File[] split() throws IOException {
        File[] outputFiles = new File("outputFiles").listFiles(); // выходные файлы после разделения файла
        boolean mark;
        String line;
        StringBuilder lineForWrite;
        StringBuilder anotherLine = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(String.valueOf(files[0]))));
        assert outputFiles != null;
        for (File file : outputFiles) {
            mark = false;
            BufferedWriter bufferedWritter = new BufferedWriter(new FileWriter(file));
            if (!anotherLine.toString().equals("")) {
                bufferedWritter.write(String.valueOf(anotherLine)); // записываем значение строчки после границы в новый файл
                bufferedWritter.write("\n");
            }
            anotherLine = new StringBuilder(); // обнуляем
            while ((line = bufferedReader.readLine()) != null) { // пока не дойдем до границы разделения
                    lineForWrite = new StringBuilder();
                    lineForWrite.append(line);
                    if (line.contains("\\")) {
                        anotherLine.append(lineForWrite);
                        anotherLine.delete(0, line.indexOf("\\") + 1); // сохраняем конец строчки после границы
                        lineForWrite.delete(line.indexOf("\\"), line.length()); // удаляем всё, что после границы
                        mark = true;
                    }
                    bufferedWritter.write(String.valueOf(lineForWrite));
                    bufferedWritter.flush();
                    if (mark) break;
                    bufferedWritter.write("\n");
            }
        }
        return outputFiles;
    }
}