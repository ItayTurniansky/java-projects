public enum Mark {
	BLANK, X, O;
	public String toString(Mark n){
		switch(n){
			case X: return "X";
			case O: return "O";
			default: return null;
		}
	}
}
