import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class Tests {
    private String filesPath = System.getProperty("user.dir") + "\\",
            tempDir = "testFiles\\",
            nameInput1 = "input1.txt",
            nameInput2 = "input2.txt",
            nameInput3 = "input3.txt",
            nameOutput = "output.txt";

    private File fileInput1, fileInput2, fileInput3, fileOutput;

    private void fillFile(File file, String msg) {
        try {
            FileWriter fWriter = new FileWriter(file);
            fWriter.write(msg);
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mkInputs() {
        fileInput1 = new File(filesPath + tempDir + nameInput1);
        fileInput2 = new File(filesPath + tempDir + nameInput2);
        fileInput3 = new File(filesPath + tempDir + nameInput3);
        fileOutput = new File(filesPath + nameOutput);

        fillFile(fileInput1, "Это дурацкий план...");
        fillFile(fileInput2, "Я знаю. Так вы со мною?");
        fillFile(fileInput3, "Естественно.");
        fillFile(fileOutput,
                "@Input[" + tempDir + nameInput1 + "]\n" +
                        "Это дурацкий план...\n" +
                        "@Input[" + tempDir + nameInput2 + "]\n" +
                        "Я знаю. Так вы со мною?\n" +
                        "@Input[" + tempDir + nameInput3 + "]\n" +
                        "Естественно.\n");
    }

    public void mkOutputs() {
        fileInput1 = new File(filesPath + nameInput1);
        fileInput2 = new File(filesPath + nameInput2);
        fileInput3 = new File(filesPath + nameInput3);
        fileOutput = new File(filesPath + tempDir + nameOutput);

        fillFile(fileInput1, "Это дурацкий план...");
        fillFile(fileInput2, "Я знаю. Так вы со мною?");
        fillFile(fileInput3, "Естественно.");
        fillFile(fileOutput,
                "@Input[" + tempDir + nameInput1 + "]\n" +
                        "Это дурацкий план...\n" +
                        "@Input[" + tempDir + nameInput2 + "]\n" +
                        "Я знаю. Так вы со мною?\n" +
                        "@Input[" + tempDir + nameInput3 + "]\n" +
                        "Естественно.\n");
    }

    @Before
    public void init() {
        File dir = new File(filesPath + tempDir);
        dir.mkdirs();
    }

    private boolean compareFiles(File first, File second) {
        if (!first.exists() || !second.exists()) return false;
        try {
            BufferedReader bufFirst = new BufferedReader(new FileReader(first)),
                    bufSecond = new BufferedReader(new FileReader(second));

            String line1 = bufFirst.readLine(),
                    line2 = bufSecond.readLine();

            while(line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    return false;
                } else if (!line1.equals(line2)) {
                    System.err.println("Несовпадение строк:\n" + line1 + " \n " + line2);
                    return false;
                }

                line1 = bufFirst.readLine();
                line2 = bufSecond.readLine();
            }
        } catch(IOException e) {
            System.err.println("Ошибка при сравнении файлов");
            return false;
        }
        return true;
    }

    @Test
    public void checkConnection() {
        mkInputs();
        ConnectFiles cFiles = new ConnectFiles(tempDir + nameOutput);
        cFiles.create(new String[]{tempDir + nameInput1, tempDir + nameInput2, tempDir + nameInput3});

        assertTrue(compareFiles(new File(filesPath + tempDir + nameOutput), fileOutput));
    }

    @Test
    public void  checkSplitting() {
        mkOutputs();
        SplitFile sFile = new SplitFile(tempDir + nameOutput);
        sFile.split();
        boolean allEquals = compareFiles(new File(filesPath + nameInput1), fileInput1) &&
                compareFiles(new File(filesPath + nameInput2), fileInput2) &&
                compareFiles(new File(filesPath + nameInput3), fileInput3);

        assertTrue(allEquals);
    }
}
