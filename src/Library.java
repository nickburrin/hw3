public class Library {
	final int numBooks;
	int checkedOut[];

	public Library(int numBooks) {
		this.numBooks = numBooks;
		checkedOut = new int[numBooks];
	}

	public synchronized int reserveBook(int ci, int bookNum) { 
		if (bookNum > numBooks || bookNum < 0 || checkedOut[bookNum-1] != 0) {
			return -1;
		}
		
		checkedOut[bookNum-1] = ci;
		return 1;
	}

	public synchronized int returnBook(int ci, int bookNum) {
		if((bookNum <= numBooks && bookNum > 0) && (checkedOut[bookNum-1] == ci)) {
			checkedOut[bookNum-1] = 0;
			return 2;
		}
		
		return -1;
	}

	public int reserveBook(String ci, String num) {
		return reserveBook(extractNumber(ci),extractNumber(num));
	}

	public int returnBook(String ci, String num) {
		return returnBook(extractNumber(ci),extractNumber(num));
	}

	private int extractNumber(String s) {
		return Character.getNumericValue(s.charAt(1));
	}

	public String printLibrary() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nBookNo\tClientId\n");

		String id;
		int i = 0;
		for (int clientId : checkedOut) {
			if (clientId == 0) 	id = "-";
			else 				id = Integer.toString(clientId);
			sb.append(i++ + "\t" + id + "\n");
		}
		return sb.toString();
	}
}