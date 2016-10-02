import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ElectionClient {
    private ElectionClient() {
    }

    public static void main(String args[]) {
        // args[0]: hostname of server where registry resides
        // args[1]: port of registry
        if (args.length < 5) {
            System.out.println("Proper Usage is: java ElectionClient <hostname> <port> <voternum> <candidate> <want_results>");
            System.exit(0);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int voter = Integer.parseInt(args[2]);
        String candidate = args[3];
        boolean wantResults = Boolean.parseBoolean(args[4]);
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            Election stub = (Election) registry.lookup("Election");
            boolean response = stub.vote(candidate, voter);
            if (response) {
                System.out.println("Successfully Voted");
            } else {
                System.out.println("Vote Failed");
            }
            if (wantResults) {
                int c1results = stub.result("Candidate_1", -1);
                int c2results = stub.result("Candidate_2", -1);
                System.out.println("Election Results:\nCandidate_1: " + c1results + "\nCandidate_2: " + c2results);
            }
        } catch (ConnectException e) {
            System.err.println("Connection to server was lost. Please try voting again later");
        } catch (Exception e) {
            System.err.println("ElectionClient exception: " + e.toString());
            e.printStackTrace();
        }

    }
}
