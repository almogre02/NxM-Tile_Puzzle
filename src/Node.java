import java.util.*;

public class Node implements Comparable{

    private static Hashtable<String,Node> map = new Hashtable<>();
    private static Hashtable<String,int[]>index_goal = new Hashtable<>();

    private int cost;
    private int row;
    private int col;
    private String[][] puzzle;
    private String moves;
    private Node father;
    private boolean visited;
    private int[] first_index;
    private int[] second_index;
    private int empty_spot;
    private int heuristic;


    Node(String[][] puzzle, Node father, int cost, int num_of_spots, String moves) {
        this.puzzle = puzzle;
        this.row = puzzle.length;
        this.col = puzzle[0].length;
        this.cost = cost;
        this.father = father;
        this.first_index = new int[2];
        this.second_index = new int[2];
        this.visited = false;
        this.moves = moves;
        this.empty_spot = num_of_spots;
        this.heuristic=0;
        underscore_location();
    }

    Node(Node other) {
        this.row = other.row;
        this.col = other.col;
        this.cost = other.cost;
        this.puzzle=copy_puzzle(other.puzzle);
        this.visited = other.visited;
        this.moves = other.moves;
        this.empty_spot= other.empty_spot;
        this.father = other.father;
        this.heuristic=other.heuristic;
        underscore_location();
    }


    public void underscore_location() {
        int count = 0;
        String under="_";
        int[] under1 = new int[2];
        int[] under2 = new int[2];
        for (int i = 0; i <= this.puzzle.length-1; i++) {
            for (int j = 0; j <= this.puzzle[i].length-1; j++) {
                if (this.empty_spot == 2) {
                    if (this.puzzle[i][j].equals(under)==true && count == 0) {
                        under1[0] = i;
                        under1[1] = j;
                        count++;
                        this.first_index = under1;
                    }
                    else if (this.puzzle[i][j].equals(under)==true && count == 1) {
                        under2[0] = i;
                        under2[1] = j;
                        count++;
                        this.second_index = under2;
                    }
                }
                if (this.empty_spot == 1) {
                    if (this.puzzle[i][j].equals(under)) {
                        under1[0] = i;
                        under1[1] = j;
                        this.first_index = under1;
                    }
                }
            }
        }
    }

    /**
     * Produces all possible nodes for a single index
     * The function checks if moving the position right / left / up / down is possible and thus creates the situations
     * @param father
     * @param index
     * @return
     */
    public List<Node> one_move(Node father, int[] index) {
        int[] index_location = new int[2];
        String[][] state = new String[row][col];
        List<Node> list = new LinkedList<>();
        if (Arrays.equals(father.first_index,index)){
            index_location = this.first_index;
        }
        if (Arrays.equals(father.second_index,index))
        {
            index_location = this.second_index;
        }

        int i = index_location[0];
        int j = index_location[1];
        if ((j + 1) < col) {
            state = copy_puzzle(father.getPuzzle());
            state[i][j] = state[i][j + 1];
            state[i][j + 1] = "_";
            String moves = state[i][j] + "L";
            Node a = new Node(state, father, father.getCost() + 5, father.empty_spot,moves);
            if (!a.equals(father.father))
                list.add(a);
        }
        if ((i + 1) < row) {
            state = copy_puzzle(father.getPuzzle());
            state[i][j] = state[i + 1][j];
            state[i + 1][j] = "_";
            String moves = state[i][j] + "U";
            Node a = new Node(state, father, father.getCost() + 5, father.empty_spot,moves);
            if (!a.equals(father.father))
                list.add(a);
        }
        if ((j - 1) >= 0) {
            state = copy_puzzle(father.getPuzzle());
            state[i][j] = state[i][j - 1];
            state[i][j - 1] = "_";
            String moves = state[i][j] + "R";
            Node a = new Node(state, father, father.getCost() + 5, father.empty_spot,moves);
            if (!a.equals(father.father))
                list.add(a);
        }
        if ((i - 1) >= 0) {
            state = copy_puzzle(father.getPuzzle());
            state[i][j] = state[i - 1][j];
            state[i - 1][j] = "_";
            String moves = state[i][j] + "D";
            Node a = new Node(state, father, father.getCost() + 5, father.empty_spot,moves);
            if (!a.equals(father.father))
                list.add(a);
        }
        return list;
    }

    public List<Node> two_moves(Node father) {
        String[][] state = new String[row][col];
        List<Node> list = new LinkedList<>();
        int[] first = this.first_index;
        int[] second = this.second_index;
        int i = first[0];
        int j = first[1];
        int i1 = second[0];
        int j1 = second[1];
        if (i1 > i) {
            if ((j + 1) < col && (j1 + 1) < col) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1][j1 + 1];
                state[i][j] = state[i][j + 1];
                state[i][j + 1] = "_";
                state[i1][j1 + 1] = "_";
                String moves = state[i][j] + "&" + state[i1][j1] + "L";
                Node a = new Node(state, father, father.getCost() + 6,father.empty_spot,moves);
                if (!a.equals(father.father))
                    list.add(a);
            }
            if ((j - 1) >= 0 && (j1 - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1][j1 - 1];
                state[i][j] = state[i][j - 1];
                state[i][j - 1] = "_";
                state[i1][j1 - 1] = "_";
                String moves = state[i][j] + "&" + state[i1][j1] + "R";
                Node a = new Node(state, father, father.getCost() + 6,father.empty_spot,moves);
                if (!a.equals(father.father))
                    list.add(a);
            }
            if ((j + 1) < col) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i][j + 1];
                state[i][j + 1] = "_";
                String moves = state[i][j] + "L";
                Node a = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!a.equals(father.father))
                    list.add(a);
            }
            if ((j - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i][j - 1];
                state[i][j - 1] = "_";
                String moves = state[i][j] + "R";
                Node a = new Node(state, father, father.getCost() + 5 ,father.empty_spot,moves);
                if (!a.equals(father.father))
                     list.add(a);
            }
            if ((i - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i - 1][j];
                state[i - 1][j] = "_";
                String moves = state[i][j] + "D";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                     list.add(new_state);
            }
            if ((j1 + 1) < col) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1][j1 + 1];
                state[i1][j1 + 1] = "_";
                String moves = state[i1][j1] + "L";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            if ((i1 + 1) < row) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1 + 1][j1];
                state[i1 + 1][j1] = "_";
                String moves = state[i1][j1] + "U";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                     list.add(new_state);
            }
            if ((j1 - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1][j1 - 1];
                state[i1][j1 - 1] = "_";
                String moves = state[i1][j1] + "R";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
                return list;
            }
        }

        else if (j1 > j) {
            if ((i + 1) < row && (i1 + 1) < row) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i + 1][j];
                state[i1][j1] = state[i1 + 1][j1];
                state[i + 1][j] = "_";
                state[i1 + 1][j1] = "_";
                String moves = state[i][j] + "&" + state[i1][j1] + "U";
                Node new_state = new Node(state, father, father.getCost() + 7,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            if ((i - 1) >= 0 && (i1 - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i - 1][j];
                state[i - 1][j] = "_";
                state[i1][j1] = state[i1 - 1][j1];
                state[i1 - 1][j1] = "_";
                String moves = state[i][j] + "&" + state[i1][j1] + "D";
                Node new_state = new Node(state, father, father.getCost() + 7,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            if ((i + 1) < row) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i + 1][j];
                state[i + 1][j] = "_";
                String moves = state[i][j] + "U";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                     list.add(new_state);
            }
            if ((j - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i][j - 1];
                state[i1][j - 1] = "_";
                String moves = state[i][j] + "R";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                     list.add(new_state);
            }
            if ((i - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i][j] = state[i - 1][j];
                state[i - 1][j] = "_";
                String moves = state[i][j] + "D";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            if ((j1 + 1) < col) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1][j1 + 1];
                state[i1][j1 + 1] = "_";
                String moves = state[i1][j1] + "L";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            if ((i1 + 1) < row) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1 + 1][j1];
                state[i1 + 1][j1] = "_";
                String moves = state[i1][j1] + "U";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                     list.add(new_state);
            }
            if ((i1 - 1) >= 0) {
                state = copy_puzzle(father.getPuzzle());
                state[i1][j1] = state[i1 - 1][j1];
                state[i1 - 1][j1] = "_";
                String moves = state[i1][j1] + "D";
                Node new_state = new Node(state, father, father.getCost() + 5,father.empty_spot,moves);
                if (!new_state.equals(father.father))
                    list.add(new_state);
            }
            return list;
        }
        return list;
    }

    /**
     * The function creates a list of all possible nodes with the request order
     * @return a List<Node>
     */
    public List<Node> childrens_list() {
        List<Node> child_list = new LinkedList<>();
        int[] first;
        int[] second;
        String[][] board = this.puzzle;
        if (this.empty_spot == 1) {
            child_list = one_move(this, this.first_index);
        }
        if (this.empty_spot == 2) {
            first = Arrays.copyOf(this.first_index,2);
            second = Arrays.copyOf(this.second_index,2);
            int algo=this.index_dist();
            if (algo==1){
                Node t_temp = new Node(this);
                child_list = two_moves(t_temp);
                return child_list;
            }

            if (algo==2){
                Node temp = new Node(this);
                List<Node> first_spot_list = one_move(temp, first);
                Node temp2 = new Node(this);
                List<Node> second_spot_list = one_move(temp2, second);
                for (Node n1 : first_spot_list) {
                    child_list.add(n1);
                }
                for (Node n2 : second_spot_list) {
                    child_list.add(n2);
                }
                return child_list;
            }
        }
        return child_list;
    }

    /**
     * Calculate the distance from a particular index to the goal index by  Manhattan distance(meaning-with no conditions)
     * @param temp
     */
    public void Manhattan(Node temp){
        String under="_";
        int sum=0;
        for (int i = 0; i <=temp.getPuzzle().length-1; i++) {
            for (int j = 0; j <= temp.getPuzzle()[0].length-1; j++) {
                if((temp.getPuzzle()[i][j].equals(under))==false){
                    int [] index=index_goal.get(temp.getPuzzle()[i][j]);
                    sum += 3*(Math.abs(index[0] - i) + Math.abs(index[1] - j));
                }
            }
        }
        this.heuristic=this.cost+sum;
    }
    public void index_goal_map(Node goal){
        for (int i = 0; i <= goal.getPuzzle().length-1; i++) {
            for (int j = 0; j <= goal.getPuzzle()[i].length-1; j++) {
                int [] index = new int[2];
                index[0]=i;
                index[1]=j;
                index_goal.put(goal.getPuzzle()[i][j],index);
            }
        }
    }
    public int index_dist() {
        int i1 = this.first_index[0];
        int j1 = this.first_index[1];
        int i2 = this.second_index[0];
        int j2 = this.second_index[1];
        int distance_between = (int)Math.sqrt(Math.pow((i1 - i2),2)+Math.pow((j1 - j2),2));
        if (distance_between == 1)
            return 1;
        else
            return 2;
    }

    private static String[][] copy_puzzle(String[][] puzzle) {
        String[][] copy = new String[puzzle.length][puzzle[0].length];
        for (int i = 0; i <= puzzle.length-1; i++) {
            for (int j = 0; j <= puzzle[i].length-1; j++) {
                copy[i][j] = puzzle[i][j];
            }
        }
        return copy;
    }

    public boolean getVisited() { return this.visited; }
    public int getCost() { return cost; }
    public Node getFather() { return father; }
    public String getMove() { return this.moves; }
    public int getheuristic() { return this.heuristic; }
    public Hashtable<String,Node> getMap(){ return this.map; }
    public void setFather(Node father) { this.father = father; }
    public void setVisited(boolean flag) { this.visited = flag; }
    public String[][] getPuzzle() { return puzzle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) {
            return false;
        }
        for (int i = 0; i<= this.getPuzzle().length-1; i++) {
            for (int j = 0; j<= this.getPuzzle()[0].length-1; j++) {
                if ((this.puzzle[i][j].equals(((Node)o).getPuzzle()[i][j]))==false) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int compareTo(Object o) {
        if(this.getheuristic()<((Node) o).getheuristic()){return -1;}
        else if(this.getheuristic()>((Node) o).getheuristic()){return 1;}
        return 0;
    }

    @Override
    public String toString() {
        String state = "{";
        for(int i=0; i<=this.getPuzzle().length-1; i++){
            state += "(";
            for(int j=0; j<=this.getPuzzle()[i].length-1; j++){
                state += this.getPuzzle()[i][j];
                state+=",";
            }
            state=state.substring(0,state.length());
            state+=")\n ";
        }
        state += "}\n";
        return state;
    }
}
