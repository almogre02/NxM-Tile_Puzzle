import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.*;
public class main {
    public static void main(String[] args) {

        List<String> my_text = new ArrayList<>();
        String temp_ans="{";
        try {
            File myObj = new File("C:\\Users\\almog\\IdeaProjects\\ProblemSolvingBySearch\\src\\input.txt");
            Scanner myReader = new Scanner(myObj);
            String algo = myReader.nextLine();
            Map<Object, List<String>> map=Stream.of(algo.split(" ")).filter(x->x.length()>=5 && x.length()<=9).collect(Collectors.groupingBy(x->x.length()));
            for (Object temp:map.keySet()) {
                temp_ans+=temp+"_letters="+map.get(temp).size()+",";
            }
            String ans=temp_ans.substring(0,temp_ans.length()-1)+"}";
            System.out.println(ans);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

    }
}
