import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

class ElectionServant extends UnicastRemoteObject implements Election {

    private HashMap<String, Integer> candidates = new HashMap<>();
    // Initial voter population
    private HashMap<Integer, Boolean> voters = new HashMap<Integer, Boolean>() {{
        put(0, false);
        put(1, false);
        put(2, false);
        put(3, false);
        put(4, false);
        put(5, false);
        put(6, false);
        put(7, false);
    }};


    ElectionServant() throws IOException {
        super();
        // ------- POPULATE CANDIDATE VOTES ------
        BufferedReader in = new BufferedReader(new FileReader("votesRecorded.txt"));
        String voteLine;
        int votesForCandidate;
        String candidate;
        while ((voteLine = in.readLine()) != null) {
            if (!voteLine.equals("")) {
                candidate = voteLine.split(": ")[0];
                votesForCandidate = Integer.parseInt(voteLine.split(": ")[1]);
                candidates.put(candidate, votesForCandidate);
            }
        }
        // ---------------------------------------
        // ------ POPULATE VOTER STATUS ----------
        File file = new File("voterStatus.txt");
        if (file.exists()) {
            BufferedReader vIn = new BufferedReader(new FileReader("voterStatus.txt"));
            String voterLine;
            boolean voted;
            int voteNumber;
            while ((voterLine = vIn.readLine()) != null) {
                if (!voterLine.equals("")) {
                    voteNumber = Integer.parseInt(voterLine.split(": ")[0]);
                    voted = Boolean.parseBoolean(voterLine.split(": ")[1]);
                    voters.put(voteNumber, voted);
                }
            }
        }
        //----------------------------------------
    }

    @Override //Synchonized class
    public synchronized boolean vote(String candidate, int voterNum) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        int currentVotes = candidates.get(candidate);
        boolean successfullyVoted;
        if (!voters.get(voterNum)) { // Process vote that is valid
            System.out.println("Voter No. " + voterNum + " is voting for " + candidate);
            voters.put(voterNum, true);
            currentVotes++;
            candidates.put(candidate, currentVotes);
            successfullyVoted = true;
        } else { // Reject invalid vote
            System.out.println("Voter No. " + voterNum + " attempted to vote more than once, attempt was rejected.");
            successfullyVoted = false;
        }
        // File writers for voters and candidates
        PrintWriter votedOut = new PrintWriter("voterStatus.txt");
        PrintWriter out = new PrintWriter("votesRecorded.txt", "UTF-8");

        // Empty strings that will be populated and written to file
        String results = "";
        String vote_tracker = "";

        // Create String from HashMap for candidate status
        for (String this_candidate : candidates.keySet()) {
            results += this_candidate + ": " + candidates.get(this_candidate) + "\n";
        }

        // Create String from HashMap for vote status
        for (int this_voter : voters.keySet()) {
            vote_tracker += this_voter + ": " + voters.get(this_voter) + "\n";
        }

        // Write Strings to respective files, and close the files.
        votedOut.print(vote_tracker);
        votedOut.close();
        out.print(results);
        out.close();

        // Return whether the vote succeeded
        return successfullyVoted;
    }

    @Override
    public int result(String candidate, int numOfVotes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("votesRecorded.txt"));
        String voteLine;
        int votesForCandidate = -1;
        while ((voteLine = in.readLine()) != null) {
            if (voteLine.contains(candidate)) {
                votesForCandidate = Integer.parseInt(voteLine.split(": ")[1]);
            }
        }
        if (votesForCandidate == -1) {
            System.err.println("Attempted to get results for nonexistent candidate");
        }
        return votesForCandidate;
    }
}
