/*
  Формат выходного файла (при успешных чтении и записи):
  @Input[fileName1.txt]
  ***Текст***
  @Input[fileName2.txt]
  ***Текст***
  ...
 */

import java.io.*;

public class ConnectFiles {
    private FileWriter writer;
    private boolean closed = false;

    public ConnectFiles(String outputName){
        File outputFile = new File(System.getProperty("user.dir") + "\\" + outputName);
        try {
            this.writer = new FileWriter(outputFile);
        } catch (IOException e) {
            System.out.println("Не удалось создать выходной файл");
        }
    }

    private void add(String inputName) {
        try {
            String inputPath = System.getProperty("user.dir") + "\\" + inputName;
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(inputPath), "UTF-8");
            BufferedReader bufReader = new BufferedReader(reader);

            try {
                writer.write("@Input["+ inputName + "]\n");
            } catch (IOException e) {
                System.out.println("Не удалось записать в выходной файл");
            }

            String line;
            while((line = bufReader.readLine()) != null) {
                writer.append(line);
            }
            writer.append('\n');
            bufReader.close();
            reader.close();
        } catch(IOException e) {
            System.out.println("Ошибка при работе с входным файлом: " + inputName);
        }
    }

    public void create(String[] inputs) {
        if (closed) {
            System.out.println("Выходной файл уже создан");
        }
        for (String input: inputs) {
            add(input);
        }
        try {
            writer.close();
            closed = true;
            System.out.println("Входные файлы успешно объединены");
        } catch (IOException e) {
            System.out.println("Не удалось сохранить файл");
        }
    }

    @Override
    public String toString() {
        return "ConnectFiles{" +
                "writer=" + writer +
                ", closed=" + closed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectFiles that = (ConnectFiles) o;

        if (closed != that.closed) return false;
        return writer != null ? writer.equals(that.writer) : that.writer == null;
    }

    @Override
    public int hashCode() {
        int result = writer != null ? writer.hashCode() : 0;
        result = 31 * result + (closed ? 1 : 0);
        return result;
    }
}
