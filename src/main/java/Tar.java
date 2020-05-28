import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Tar {
    @Argument(required = true)
    private File[] inputFiles;
    @Option(name = "-out")
    private boolean out;
    @Option(name = "-u")
    private boolean u;


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
                return new File(String.valueOf(split()));
            }
        } catch (CmdLineException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String firstString() {
        return "// PACKED WITH TAR //" + "\n" + "\n";
    }
    private String endOfFile() { return "// END OF FILE //" + "\n" + "\n"; }

    private File unite() throws IOException {
        ArrayList<File> listOfInputFiles = new ArrayList<>(Arrays.asList(inputFiles));
        File outputFile = new File(String.valueOf(inputFiles[inputFiles.length - 1])); // создаем выходной файл
        listOfInputFiles.remove(inputFiles.length - 1); // удаляем из входных данных файл, который является выходным
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        bufferedWriter.write(firstString());
        int count = 0;
        String line;
        for (File file : listOfInputFiles) { // для каждого файла
            bufferedWriter.write("// FILENAME: " + file.getName() + " //" + "\n"); // пишем имя входных файлов
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
            bufferedWriter.write(endOfFile()); // конец файла
        }
        bufferedWriter.close();
        return outputFile;
    }

    private ArrayList<File> split() throws IOException {
        ArrayList<File> listOfOutputFiles = new ArrayList<>(Arrays.asList(inputFiles));
        listOfOutputFiles.remove(0); // удаляем файл, который является входным
        boolean mark;
        String line; // строчка, которую мы считываем
        StringBuilder lineForWrite; // строчка, которую напишем в выходной файл
        StringBuilder anotherLine = new StringBuilder(); // для случая, если разделение идет в середине строки
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(String.valueOf(inputFiles[0])))); // входной файл
        for (File file : listOfOutputFiles) {
            mark = false;
            BufferedWriter bufferedWritter = new BufferedWriter(new FileWriter(file));
            bufferedWritter.write(firstString());
            bufferedWritter.write("// FILENAME: " + inputFiles[0].getName() + " //" + "\n"); // пишем имя входного файла
            if (!anotherLine.toString().equals("")) {
                bufferedWritter.write(String.valueOf(anotherLine)); // записываем значение строчки после границы в новый файл
                bufferedWritter.write("\n");
            }
            anotherLine = new StringBuilder(); // обнуляем строчку
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
                    if (mark) break; // если дошли до границы разделения
                    bufferedWritter.write("\n");
            }
            bufferedWritter.write( "\n" + endOfFile()); // конец файла
            bufferedWritter.close();
        }
        return listOfOutputFiles;
    }
}