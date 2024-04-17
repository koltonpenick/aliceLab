import java.util.Scanner;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class controller {
	
	private List<Piece> pieceList;
	
	public static void main(String[]args) {
		controller c = new controller();
		c.doIt();
		
	}
	
	public File fileGetter() {
		JFileChooser c = new JFileChooser();
		int success = c.showOpenDialog(null);
		if(success == JFileChooser.APPROVE_OPTION) {
			return c.getSelectedFile();
		}
		return null;
	}
	
	public void doIt() {
		List<Board> b = createBoard();
		int bn = 1;
		for(Board board: b) {
			List<String> path = solveBoard(board);
			if(path!=null && !path.isEmpty()) {
				System.out.println("Board #" + bn + ": " + String.join("", path));
			}
			else {
				System.out.println("Alice is Stuck on board: " + bn);
			}
			bn++;
			
		}
		
	}
	
	public List<String> solveBoard(Board b){
		int sr = b.getHeight()-1;
		int sc = startColumn(b);
		if(sc == -14) {
			//no starting column therefore no solution
			return null;
			
		}
		else {
			List<String> pathway = new ArrayList<>();
			if(solvable(b,sr,sc,pathway)) {
				return pathway;
			}
			else {
				//instance of no solution found for Alice
				return null;
			}
			
		}
		
		
		
	}
	
	private Piece conversion(char piece) {
		switch(piece) {
			case 'P':
				return new Pawn(null);
			case 'R':
				return new Rook(null);
			case 'N':
				return new Knight(null);
			case 'B':
				return new Bishop(null);
			case 'K':
				return new King(null);
			case 'Q':
				return new Queen(null);
			case '-':
				return null;
			default:
				return null;
		}
		
	}	
	
	public List<Board> createBoard(){
		File f = fileGetter();
		List<Board>  allBoards = new ArrayList<>();
		try(Scanner s = new Scanner(f)){
			int numberOfBoards = s.nextInt();
			s.nextLine();
			//number of boards
			for(int x = 0;x < numberOfBoards; x++) {
				Board b = new Board();
				List<Piece> pieces = new ArrayList<>();
				//loop through rows
				for(int r = 0; r < 8; r++) {
					String nextLine = s.nextLine();
					//loop through column
					for(int c = 0; c < 8; c++) {
						char piece = nextLine.charAt(c);
						if(conversion(piece)!=null) {
							pieces.add(conversion(piece));
						}
						
						
						
						b.setPiece(r, c, conversion(piece));
						
					}
				}
				pieceList = b.getPieces();
				allBoards.add(b);
				b.print();
				System.out.println();
				
				
			}
			
			
			
		}
		catch(FileNotFoundException e) {
			System.out.print("Error opening a file that does not exist");
			
		}
		return allBoards;
		
	}
	
	public List<int[]> legalMove(Board b,int sr,int sc){
		Piece p = b.getPiece(sr,sc);
		List<int[]> sequence = new ArrayList<>();
		if(p!=null) {
			for(int r=0;r<b.getHeight();r++) {
				for(int c=0;c<b.getWidth();c++) {
					if(b.isValidMove(sr, sc, r, c)==true){
						sequence.add(new int[] {r,c});
						
					}
				}
			}
		}
		return sequence;
		}
	
	private int startColumn(Board b) {
		for(int x=0;x<b.getWidth();x++) {
			if(b.getPiece(b.getHeight()-1, x)!=null) {
				return x;
			}
		}
		return -14;
	}

	private boolean solvable(Board b, int r, int c, List<String> pathway) {
	    Piece p = b.getPiece(r, c);
	    if (p != null) { 
	        if (r == 0 && b.getPieces().size() == 1) {
	            pathway.add(b.getPiece(r, c).getSymbol());
	            return true;
	        }
	        List<int[]> possibilities = legalMove(b, r, c);
	        for (int[] x : possibilities) {
	            int er = x[0];
	            int ec = x[1];
	            Piece nextPiece = b.getPiece(er, ec);
	            if (nextPiece != null) { 
	                String pc = p.getSymbol();
	                pathway.add(pc);
	                b.setPiece(r, c, null);
	                if (solvable(b, er, ec, pathway) == false) {
	                    pathway.remove(pathway.size() - 1);
	                    b.setPiece(r, c, p);
	                } else {
	                    return true;
	                }
	            }
	        }
	    }
	    return false;
	}
}

