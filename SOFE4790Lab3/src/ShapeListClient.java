import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Color;

public class ShapeListClient {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        boolean registered = false;
        int callBackId = 0;
        do {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String option;
            String type;
            boolean valid = false;

            do
            {
                System.out.println("Read or Write?");
                option = in.nextLine().toLowerCase();
                if(option.equals("read") || option.equals("write"))
                    valid = true;
                else
                    System.out.println("ERROR: Unrecognized input");
            }
            while (!valid);
//            if (args.length > 2) option = args[2]; // read or write
//            if (args.length > 3) type = args[3]; // circle, line, etc.
            // System.out.println("option = " + option + ", shape = " + type);

            try {
                Registry registry = LocateRegistry.getRegistry(host, port);
                ShapeList aList = (ShapeList) registry.lookup("ShapeList");
                WhiteboardCallback callback = new WhiteboardCallbackImpl();
                if (!registered) { // ensures client doesn't register more than once.
                    callBackId = aList.register(callback);
                    registered = true;
                }
                System.out.println("Found server");
                Vector<Shape> shapeVec = aList.allShapes();
                System.out.println("Got vector");
                if (option.equals("read")) {
                    for (int i = 0; i < shapeVec.size(); i++) {
                        GraphicalObject g = shapeVec.elementAt(i).getAllState();
                        g.print();
                    }
                } else {
                    System.out.println("Shape?");
                    type = in.nextLine().toLowerCase();
                    GraphicalObject g = new GraphicalObject(type,
                            new Rectangle(50, 50, 300, 400), Color.red, Color.blue, false);
                    System.out.println("Created graphical object");
                    aList.newShape(g);
                    System.out.println("Stored shape with ShapeList object on server");
                }
                System.out.println("Press enter to continue, or type \"exit\" to stop terminate this client.");
                if(in.nextLine().toLowerCase().equals("exit")){
                    aList.unregister(callBackId);
                    System.exit(0);
                }
            } catch (RemoteException e) {
                System.out.println("allShapes: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Registry: " + e.getMessage());
                e.printStackTrace();
            }
        } while (true);

    }
}
