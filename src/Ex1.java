import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex1 {
    public static void main(String[] args) {
        boolean withTime = false;
        boolean isOpen = false;
        String start = "";
        String goal = "";
        String[] ans = null;
        double time = 0;
        try {
            File myObj = new File("input.txt");
            int i = 0, count = 0;
            Scanner myReader = new Scanner(myObj);
            String algo = myReader.nextLine();
            if (myReader.nextLine().equals("with time"))
                withTime = true;
            if (myReader.nextLine().equals("with open"))
                isOpen = true;
            String size = myReader.nextLine();
            int row = Integer.parseInt("" + size.charAt(0));
            int col = Integer.parseInt("" + size.charAt(2));
            String[][] startBoard = new String[row][col];
            String[][] goalBoard = new String[row][col];
            String temp = myReader.nextLine();
            while (temp.charAt(0) != 'G') {
                String[] ar = temp.split(",");
                startBoard[i++] = ar;
                if (temp.contains("_")) {
                    for (int j = 0; j < ar.length; j++) {
                        if (ar[j].equals("_"))
                            count++;
                    }
                }
                temp = myReader.nextLine();
            }
            i = 0;
            while (myReader.hasNextLine()) {
                String temp_goal = myReader.nextLine();
                String[] ar = temp_goal.split(",");
                goalBoard[i++] = ar;
            }
            myReader.close();
            Node gameNode = new Node(startBoard, null, 0, count, "");
            gameNode.getMap().put(gameNode.getPuzzle().toString(), gameNode);
            Node goalNode = new Node(goalBoard, null, 0, count, "");

            Algo algorithm = new Algo(gameNode, goalNode, algo,isOpen);
            long startTime = System.nanoTime();
            ans = algorithm.wich_algo_to_use();
            long estimatedTime = System.nanoTime() - startTime;
            time = estimatedTime / Math.pow(10, 9);


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(ans[0] + "\n" + ans[1] + "\n" + ans[2] + "\n");
            if (withTime)
                myWriter.write(String.valueOf(time+" seconds"));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

