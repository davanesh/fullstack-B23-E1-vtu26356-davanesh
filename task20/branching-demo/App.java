public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

    // Resolved: Combined formal and casual greetings
    public String greet(String name) {
        return "Hello, " + name + "! Welcome to our application.";
    }

    public String casualGreet(String name) {
        return "Hey there, " + name + "! How are you doing?";
    }

    public String formalGreet(String name) {
        return "Good day, " + name + ". Welcome to our application.";
    }
}
