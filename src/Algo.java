import java.util.*;

public class Algo {

    private Node start;
    private Node goal;
    private String algo;
    static int counter=0;
    boolean isopen;

    Algo(Node start,Node goal,String algo,boolean isopen){
        this.start=start;
        this.goal=goal;
        this.algo=algo;
        this.isopen=isopen;
    }
    public String[] wich_algo_to_use(){
        String[] ans=null;
        double time=0;
        switch (this.algo) {
            case "BFS":
                long startTime = System.nanoTime();
                ans=BFS(this.start, this.goal).split(",");
                long estimatedTime = System.nanoTime() - startTime;
                time=estimatedTime / Math.pow(10, 9);
                break;
            case "DFID":
                startTime = System.nanoTime();
                ans=DFID(this.start, this.goal).split(",");
                estimatedTime = System.nanoTime() - startTime;
                time=estimatedTime / Math.pow(10, 9);
                break;
            case "A*":
                startTime = System.nanoTime();
                ans=A_star(this.start, this.goal).split(",");
                estimatedTime = System.nanoTime() - startTime;
                time=estimatedTime / Math.pow(10, 9);
                break;
            case "IDA*":
                startTime = System.nanoTime();
                ans=IDA_star(this.start, this.goal).split(",");
                estimatedTime = System.nanoTime() - startTime;
                time=estimatedTime / Math.pow(10, 9);
                break;
            case "DFBnB":
                startTime = System.nanoTime();
                ans=DFBnB(this.start, this.goal).split(",");
                estimatedTime = System.nanoTime() - startTime;
                time=estimatedTime / Math.pow(10, 9);
                break;
        }
        return ans;
    }


    public String BFS(Node start, Node Goal) {
        LinkedList<Node> q = new LinkedList<>();
        String ans = "fail";
        int counter = 0;
        Hashtable<String, Node> open_list = start.getMap();
        Hashtable<String, Node> close_list = new Hashtable<>();
        q.add(start);
        while (!q.isEmpty()) {
            Node n = q.removeFirst();
            close_list.put(n.toString(), n);
            if (isopen){
                print_state(open_list);
            }
            n.setVisited(true);
            List<Node> list = n.childrens_list();
            counter += list.size();
            for (Node s : list) {
                if (!close_list.containsKey(s.toString())) {
                    if ((!open_list.containsKey(s.toString()))) {
                        if (s.equals(Goal)) {
                            ans=getPath(s);
                            ans+=",Num: " + counter;
                            ans+=",Cost: " + s.getCost();
                            return ans;
                        }
                        else {
                            open_list.put(s.toString(), s);
                            q.add(s);
                        }
                    }
                }
            }
        }
        return ans;
    }
    public String DFID(Node start, Node goal) {
        String result="";
        String ans="fail";
        for (int depth = 0; depth < Integer.MAX_VALUE; depth++) {
            Hashtable<String, Node> open_list = new Hashtable<>();
            result=Limited_DFS(start,goal,depth,open_list);
            String[] path=result.split(",");
            if (!path[0].equals("cutoff")){
                ans=path[0];
                ans+=",Num: " + counter;
                ans+=",Cost: " + path[1];
                return ans;
            }
        }
        return ans;
    }
    public String Limited_DFS(Node n,Node goal,int limit,Hashtable<String,Node> open_list){
        String result="";
        String fail="fail";
        String cutoff="cutoff";
        boolean isCutoff=false;
        if (n.equals(goal)){
            result=getPath(n)+","+n.getCost();
            return result;
        }
        else if(limit==0){
            return cutoff;
        }

        else{
            open_list.put(n.toString(),n);
            isCutoff=false;
            List<Node> lists=n.childrens_list();
            for (Node g:lists){
                if (open_list.containsKey(g.toString())){
                    continue;
                }
                counter++;
                result=Limited_DFS(g,goal,limit-1,open_list);
                if (result.equals(cutoff)){
                    isCutoff=true;
                }
                else if(!result.equals(fail)){
                    return result;
                }
            }
            if (isopen){
                print_state(open_list);
            }
            open_list.remove(n);
            if (isCutoff==true){
                return cutoff;
            }
            else{
                return fail;
            }
        }
    }

    public String A_star(Node start, Node goal) {
        Queue<Node> q = new PriorityQueue<>();
        String ans="fail";
        int i = 0;
        int counter = 0;
        Hashtable<String, Node> open_list = start.getMap();
        Hashtable<String, Node> close_list = new Hashtable<>();
        goal.index_goal_map(goal);
        q.add(start);
        while (!q.isEmpty()) {
            Node n = q.remove();
            if (n.equals(goal)) {
                ans=getPath(n);
                ans+=",Num: " + counter;
                ans+=",Cost: " + n.getCost();
                return ans;
            }
            n.setVisited(true);
            close_list.put(n.toString(), n);
            List<Node> list = n.childrens_list();
            for (Node s : list) {
                if (!close_list.containsKey(s.toString())) {
                    if ((!open_list.containsKey(s.toString()))) {
                        s.Manhattan(s);
                        open_list.put(s.toString(), s);
                        q.add(s);
                    } else if (s.getheuristic() < open_list.get(s.toString()).getheuristic()) {
                        if (isopen){
                            print_state(open_list);
                        }
                        open_list.remove(s.toString());
                        open_list.put(s.toString(), s);
                        s.setFather(n);
                    }
                    counter++;
                }
            }
        }
        return ans;
    }


    public String IDA_star(Node start, Node goal){
        Stack<Node> stack=new Stack<>();
        goal.index_goal_map(goal);
        String ans="fail";
        Hashtable<String, Node> open_list = start.getMap();
        start.Manhattan(start);
        int trash_hold=start.getheuristic();
        int minF=0;
        int counter=0;
        while (trash_hold!=Integer.MAX_VALUE){
            start.setVisited(false);
            minF=Integer.MAX_VALUE;
            stack.push(start);
            open_list.put(start.toString(),start);
            while (!stack.empty()){
                Node s=stack.pop();
                if (s.getVisited()==true){
                    if (isopen){
                        print_state(open_list);
                    }
                    open_list.remove(s.toString());
                }
                else{
                    s.setVisited(true);
                    stack.push(s);
                    List<Node> list=s.childrens_list();
                    for(Node temp:list){
                        temp.Manhattan(temp);
                        if (temp.getheuristic()>trash_hold){
                            minF=Math.min(minF,temp.getheuristic());
                            continue;
                        }
                        if (open_list.containsKey(temp.toString())&&temp.getVisited()==true){
                            continue;
                        }
                        if (open_list.containsKey(temp.toString())&&temp.getVisited()==false){
                            if (temp.getheuristic()<open_list.get(temp.toString()).getheuristic()){
                                if (isopen){
                                    print_state(open_list);
                                }
                                stack.remove(open_list.get(temp.toString()));
                                open_list.remove(temp.toString());
                            }
                            else{
                                continue;
                            }
                        }
                        if (temp.equals(goal)){
                            ans=getPath(temp);
                            ans+=",Num: " + counter;
                            ans+=",Cost: " + temp.getCost();
                            return ans;
                        }
                        open_list.put(temp.toString(),temp);
                        stack.push(temp);
                        counter++;
                    }
                }
            }
            trash_hold=minF;
        }
        return ans;
    }


    public String DFBnB(Node start,Node goal){
        String ans="fail";
        Stack<Node> stack=new Stack<>();
        List<Node> copy_list=new LinkedList<>();
        Hashtable<String,Node> open_list= start.getMap();
        int trash_hold=Integer.MAX_VALUE;
        String path="";
        int counter=0;
        stack.push(start);
        int cost=0;
        goal.index_goal_map(goal);
        while(!stack.empty()){
            Node n=stack.pop();
            if (n.getVisited()==true){
                if (isopen){
                    print_state(open_list);
                }
                open_list.remove(n.toString());
            }
            else{
                n.setVisited(true);
                stack.push(n);
                List<Node> lists=n.childrens_list();
                for (Node temp:lists){
                    temp.Manhattan(temp);
                }
                Collections.sort(lists);

                copy_list.clear();
                for (Node copy:lists){
                    copy_list.add(copy);
                }
                for (Node g:lists){
                    if (g.getheuristic()>=trash_hold){
                        lists.subList(lists.indexOf(g), lists.size()).clear();
                    }
                    else if(open_list.containsKey(g.toString()) && open_list.get(g.toString()).getVisited()==true){
                        copy_list.remove(g);
                    }
                    else if(open_list.containsKey(g.toString()) && open_list.get(g.toString()).getVisited()==false){
                        if (g.getheuristic()>=open_list.get(g.toString()).getheuristic()){
                            copy_list.remove(g);
                        }
                        else {
                            stack.remove(open_list.get(g.toString()));
                            open_list.remove(open_list.get(g.toString()));
                        }
                    }
                    else if (g.equals(goal)==true){
                        cost=g.getCost();
                        trash_hold=g.getheuristic();
                        lists.subList(lists.indexOf(g), lists.size()).clear();
                        ans=getPath(g);
                        ans+=",Num: " + counter;
                        ans+=",Cost: " + cost;
                    }
                }
                List<Node> ans_list=new LinkedList<>();
                for (Node check:copy_list){
                    if (lists.contains(check))
                        ans_list.add(check);
                }
                counter+=ans_list.size();
                Collections.reverse(ans_list);
                for (Node temp:ans_list){
                    open_list.put(temp.toString(),temp);
                    stack.push(temp);
                }
            }
        }
        return ans;
    }


    public String getPath(Node n) {
        Stack<Node> path = new Stack<>();
        String ans="";
        Node temp = n;
        while (temp.getFather() != null) {
            path.push(temp);
            temp = temp.getFather();
        }
        while (!path.empty()) {
            Node n1 = path.pop();
            if (path.size() != 0)
                ans+=n1.getMove() + "-";
            else
                ans+=n1.getMove();
        }
        return ans;
    }



    public void print_state(Hashtable<String ,Node> open_list){
        for (Node state:open_list.values()){
            System.out.println(Arrays.deepToString(state.getPuzzle()));
        }
    }


}
