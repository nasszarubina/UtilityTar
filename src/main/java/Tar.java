/**
 * Консольная утилита для работы с текстовыми файлами
 * Примеры команд:
 * tar -u input.txt
 * tar input1.txt input2.txt [... input*.txt] -out output.txt
 */


public class Tar {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Неверное число аргументов");
            return;
        }

        if (args.length == 3 && !args[1].equals("-u") || !args[0].equals("tar")) {
            System.out.println("Неверные аргументы");
            return;
        }

        if (args[1].equals("-u")) {
            SplitFile inputFile = new SplitFile(args[2]);
            inputFile.split();
        } else {
            String[] textFiles = new String[args.length-3];
            System.arraycopy(args, 1, textFiles, 0, args.length-3);
            if (!args[args.length-2].equals("-out")) {
                System.out.println("Неверные аргументы");
                return;
            }
            ConnectFiles totalFile = new ConnectFiles(args[args.length-1]);
            totalFile.create(textFiles);
        }
    }
}