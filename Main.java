package cssc;

public class Main {

    public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("[?] usage: cscc [css file path] [html dir path]");
		}
		else {
            if(args.length < 2) {
                System.out.println("[!] HTML path missing");
            }
            else {
                Parser parser = new Parser(args[0], args[1]);
                Scanner scanner = new Scanner(parser);
            }
		}
    }
}
