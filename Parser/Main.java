import java.io.FileNotFoundException;
import java.io.IOException;

class Main {
	public static void main(String[] args) throws Exception {
		// Initialize the scanner with the input file
		Scanner S = new Scanner("correct/5.code");

		// Print the token stream
		while (S.currentToken() != Core.EOS && S.currentToken() != Core.ERROR) {
			// Pring the current token, with any extra data needed
			System.out.print(S.currentToken());
			if (S.currentToken() == Core.ID) {
				String value = S.getId();
				System.out.print("[" + value + "]");
			} else if (S.currentToken() == Core.CONST) {
				int value = S.getConst();
				System.out.print("[" + value + "]");
			}
			System.out.print("\n");

			// Advance to the next token
			S.nextToken();
		}
	}
}