import app.GameOrchestrator;
import app.Orchestrator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        Scanner      scanner      = new Scanner(System.in);
        Orchestrator orchestrator = new GameOrchestrator(scanner);
        orchestrator.start();
        scanner.close();
        
    }
}