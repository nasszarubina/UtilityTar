import java.io.*;

public class SplitFile {
    private InputStreamReader reader;
    private BufferedReader bufReader;
    private boolean closed = false;

    public SplitFile(String inputName) {
        String inputPath = System.getProperty("user.dir") + "\\" + inputName;
        try {
            reader = new InputStreamReader(
                    new FileInputStream(inputPath), "UTF-8");
            bufReader = new BufferedReader(reader);
        } catch (IOException e) {
            System.out.println("Не удалось открыть входной файл");
        }
    }

    public void split() {
        if (closed) {
            System.out.println("Выходные файлы уже созданы");
            return;
        }
        String line;
        FileWriter writer = null;
        try {
            while((line = bufReader.readLine()) != null) {
                if (line.contains("@Input")) {
                    if (writer != null) {
                        writer.close();
                    }
                    // 7 - индекс начала названия файла в строке @Input[***.txt]
                    String outputPath = System.getProperty("user.dir") +
                            "\\" + line.substring(7, line.length()-1);
                    writer = new FileWriter(outputPath);
                } else {
                    if (writer == null) {
                        System.out.println("Ошибка при создании выходного файла");
                        break;
                    }
                    writer.append(line);
                }
            }
            if (writer != null) {
                writer.close();
                bufReader.close();
                reader.close();
                closed = true;
                System.out.println("Входной файл успешно разбит");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлами");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SplitFile that = (SplitFile) o;

        return closed == that.closed && (reader != null ? reader.equals(that.reader) : that.reader == null) &&
                (bufReader != null ? bufReader.equals(that.bufReader) : that.bufReader == null);
    }

    @Override
    public int hashCode() {
        int result = reader != null ? reader.hashCode() : 0;
        result = 31 * result + (bufReader != null ? bufReader.hashCode() : 0);
        result = 31 * result + (closed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SplitFile{" +
                "reader=" + reader +
                ", bufReader=" + bufReader +
                ", closed=" + closed +
                '}';
    }
}
