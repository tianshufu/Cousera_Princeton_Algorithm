import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class BaseballElimination {
    private final int teamNum;
    private final ArrayList<Team> teams;
    private final Map<String,Team> teamsTable;
    private final int[][] leftGames;

    /**
     * create a baseball division from given filename in format specified below
     * @param filename
     */
    public BaseballElimination(String filename){
        teams = new ArrayList<>();
        teamsTable = new Hashtable<>();
        In in = new In(filename);
        // Get the num of teams
        this.teamNum = in.readInt();
        this.leftGames = new int[teamNum][teamNum];
        in.readLine();
        // get the info of each team
        for (int i= 0; i<teamNum;i++){
            String name = in.readString();
            int winNum = in.readInt();
            int loseNum = in.readInt();
            int leftNum = in.readInt();
            Team team = new Team(name,i,winNum,loseNum,leftNum);
            teamsTable.put(name,team);
            teams.add(team);
            // get the left teams within other teams
            for(int j = 0; j < teamNum ; j++){
                leftGames[i][j] = in.readInt();
            }
        }

    }

    /**
     * Date struct to store the team info
     */
    private class Team{
        private final String teamName;
        private final int teamId;
        private final int numWin;
        private final int numLose;
        private final int numLeft;

        private Team(String teamName, int teamId, int numWin, int numLose,int numLeft){
            this.teamName = teamName;
            this.teamId = teamId;
            this.numWin = numWin;
            this.numLose = numLose;
            this.numLeft = numLeft;
        }

        private String getTeamName() {
            return teamName;
        }

        private int getTeamId() {
            return teamId;
        }

        private int getNumWin() {
            return numWin;
        }

        private int getNumLose() {
            return numLose;
        }

        private int getNumLeft() {
            return numLeft;
        }
    }

    /**
     * number of teams
     * @return
     */
    public int numberOfTeams() {
        return this.teamNum;
    }

    /**
     * all teams
     * @return
     */
    public Iterable<String> teams(){
        ArrayList<String> teamsName = new ArrayList<>();
        for(Team team : teams){
            teamsName.add(team.getTeamName());
        }
        return teamsName;
    }


    /**
     * number of wins for given team
     * @param team
     * @return
     */
    public int wins(String team){
        //Team t = findTeam(team);
        Team t = teamsTable.get(team);
        if(t != null){
            return t.getNumWin();
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * number of losses for given team
     * @param team
     * @return
     */
    public int losses(String team){
        Team t = teamsTable.get(team);
        if(t != null){
            return t.getNumLose();
        } else {
            throw new IllegalArgumentException();
        }

    }




    /**
     * number of remaining games for given team
     * @param team
     * @return
     */
    public int remaining(String team){
        Team t = teamsTable.get(team);
        if(t != null){
            return t.getNumLeft();
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * number of remaining games between team1 and team2
     * @param team1
     * @param team2
     * @return
     */
    public int against(String team1, String team2){
       // Team t1 = findTeam(team1);
        Team t1 = teamsTable.get(team1);
        //Team t2 = findTeam(team2);
        Team t2 = teamsTable.get(team2);
        if(t1!=null && t2!=null){
            int t1Id = t1.getTeamId();
            int t2Id = t2.getTeamId();
            return leftGames[t1Id][t2Id];
        } else {
            throw new IllegalArgumentException();
        }
    }



    /**
     * is given team eliminated?
     * @param team
     * @return
     */
    public  boolean isEliminated(String team){
        //Team curTeam = findTeam(team);
        Team curTeam  = teamsTable.get(team);
        if (curTeam == null){
            throw new IllegalArgumentException();
        }
        int maxPossibleWin = curTeam.getNumWin()+curTeam.getNumLeft();
        // Trivial elimination, if w[x] + r[x] < w[i], then team x is mathematically eliminated.
        for(Team t : teams){
            if(t.getNumWin()>maxPossibleWin){
                return true;
            }
        }
        // create the network graph
        FordFulkerson fordFulkerson = createFordFulkerson(team);
        //int flow  = getFlow(team);
        return getFlow(team)>fordFulkerson.value();
    }

    /**
     * Create flow network with the given team
     * @param team
     * @return
     */
    private FlowNetwork createFlowNetWork(String team){
        Team t = teamsTable.get(team);
        int netWorkTeams = teamNum-1;
        // get the best num of wins which the cur team can get to
        int bestWinNum = t.getNumWin()+t.getNumLeft();
        // get the num of game vertices
        int numGameVertices = (netWorkTeams*(netWorkTeams-1))/2;
        // add 2 as the start and the end point
        int numVertices = numGameVertices+netWorkTeams+2;
        FlowNetwork flowNetwork = new FlowNetwork(numVertices);
        // Create the game vertices edges, the value of the edge is the left game within i-j
        // iterate all left games
        int index = 1;
        for(int i=0; i<leftGames.length;i++) {
            // skip the curTeam nodes, since we consider it will win all the games
            if (i == t.getTeamId()) {
                continue;
            }
            for (int j = i + 1; j < leftGames.length; j++) {
                if(j == t.getTeamId()){
                    continue;
                }
                int leftGameNum = leftGames[i][j];
                // Connect the start s to the i-j game nodes
                FlowEdge flowEdgeNode = new FlowEdge(0, index, leftGameNum);
                flowNetwork.addEdge(flowEdgeNode);
                // Connect the i-j game notices to the ith and jth nodes, set the value as infinity, as it's reduced by the previous part
                // check the i,j to see if needs to adjust the index
                if(i < t.getTeamId()){
                    flowNetwork.addEdge(new FlowEdge(index, numGameVertices + i+1, Double.POSITIVE_INFINITY));
                } else {
                    flowNetwork.addEdge(new FlowEdge(index, numGameVertices +i, Double.POSITIVE_INFINITY));
                }
                if(j < t.getTeamId()){
                    flowNetwork.addEdge(new FlowEdge(index, numGameVertices +j+1, Double.POSITIVE_INFINITY));
                } else {
                    flowNetwork.addEdge(new FlowEdge(index, numGameVertices + j, Double.POSITIVE_INFINITY));
                }

                // update the index
                index += 1;
            }
        }

        int nodeIndex = 1;
        // Connect the ith node to the end t
        for(int k=0; k< teamNum;k++){
            if(k != t.getTeamId()){
                // Calculate the value which the th team can win
                int bestWin = bestWinNum - teams.get(k).getNumWin();
                FlowEdge flowEdge = new FlowEdge(numGameVertices+nodeIndex,numVertices-1,bestWin);
                flowNetwork.addEdge(flowEdge);
                nodeIndex += 1;
            }
        }
        return flowNetwork;
    }

    /**
     * Get the num of flow for all the left team
     * @param team
     * @return
     */
    private int getFlow(String team){
        Team t = teamsTable.get(team);
        if (t == null){
            throw new IllegalArgumentException();
        }
        int flow = 0;
        int teamId = t.getTeamId();
        for(int i = 0; i<teams.size(); i++){
            if(i == teamId){
                continue;
            }
            for(int j = i+1; j<teams.size(); j++){
                if(j != teamId){
                    flow += leftGames[i][j];
                }
            }
        }
        return flow;
    }



    /**
     * subset R of teams that eliminates given team; null if not eliminated
     * @param team
     * @return
     */
    public Iterable<String> certificateOfElimination(String team){
        // if not eliminated
        if (!isEliminated(team)){
            return null;
        }
        ArrayList<String> eliminationList = new ArrayList<>();
        //Team curTeam = findTeam(team);
        Team curTeam = teamsTable.get(team);
        int maxPossibleWin = curTeam.getNumWin()+curTeam.getNumLeft();
        // Trivial elimination, if w[x] + r[x] < w[i], then team x is mathematically eliminated.
        for(Team t: teams){
            if(t.getNumWin()>maxPossibleWin){
                eliminationList.add(t.getTeamName());
            }
        }
        if (eliminationList.isEmpty()){
            return eliminationList;
        }

        // Nontrivial elimination
        // create the network
        int netWorkTeams = teamNum-1;
        int numGameVertices = (netWorkTeams*(netWorkTeams-1))/2;
        FordFulkerson fordFulkerson = createFordFulkerson(team);
        for(int i=0; i<teams.size();i++){
            if(i != curTeam.getTeamId()){
                int netWorkIndex;
                if(i<teamsTable.get(team).getTeamId()){
                    netWorkIndex = i+numGameVertices+1;
                } else {
                    netWorkIndex = i+numGameVertices;
                }
                if(fordFulkerson.inCut(netWorkIndex)){
                    eliminationList.add(teams.get(i).getTeamName());
                }
            }
        }

        return eliminationList;
    }

    /**
     * Create FordFulkerson with the given name
     * @param team
     * @return
     */
    private FordFulkerson createFordFulkerson(String team){
        int netWorkTeams = teamNum-1;
        int numGameVertices = (netWorkTeams*(netWorkTeams-1))/2;
        int s = 0;
        int t = numGameVertices+netWorkTeams+2-1;
        return new FordFulkerson(createFlowNetWork(team),s,t);

    }

    public static void main(String[] args) {
        BaseballElimination baseballElimination = new BaseballElimination(args[0]);
        //System.out.println(baseballElimination.against("Toronto","Detroit"));
        //System.out.println(baseballElimination.wins("Detroit"));
        //FlowNetwork flowNetwork = baseballElimination.createFlowNetWork("Detroit");
        //System.out.println(flowNetwork);
        //System.out.println(baseballElimination.);
        //System.out.println(baseballElimination.isEliminated("Hufflepuff"));
        for(String name:baseballElimination.teams()){
            if(baseballElimination.isEliminated(name)){
                System.out.println("Name:" + name);
                System.out.println(baseballElimination.isEliminated(name));
                System.out.println(baseballElimination.certificateOfElimination(name));
            }

        }

        //System.out.println(baseballElimination.isEliminated("Philadelphia"));

    }

}
