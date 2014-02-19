import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {

	static int shortestWord = 4;
	
	static int[] dx = {-1,  0,  1,  1,  1,  0, -1, -1};
	static int[] dy = {-1, -1, -1,  0,  1,  1,  1,  0};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/* read dictionary from file */
		ArrayList<String> dictionary = readLinesFromFile("croatian-wordlist-checked-iso8859-2.txt", "ISO8859-2");
		System.out.println("Dictionary loaded");
		/* */
		
		/* read board */
		String[][] board = new String[4][4];
		System.out.println("Enter board(4x4): ");
		Scanner sc = new Scanner(System.in);
		for (int y = 0; y < 4; y++) {
			String line = sc.next();
			for (int x = 0; x < 4; x++)
				board[x][y] = ""+line.charAt(x);
		}
		/* */
		System.out.println("Krecem traziti matcheve.");
		/* find all matches */
		ArrayList<Match> matches = findMatches(dictionary, board);
		/* */
		
		/* sort and print matches */
		Collections.sort(matches);
		for (int i = 0; i < matches.size(); i++)
			matches.get(i).print();
		/* */
	}
	
	public static ArrayList<Match> findMatches(ArrayList<String> dictionary, String[][] board) {
		ArrayList<Match> matches = new ArrayList<Match>();
		Set<String> foundWords = new HashSet<String>();
		int[][] used = new int[4][4];
		
		for (int y = 0; y < 4; y++)
			for (int x = 0; x < 4; x++)
				findMatchesRec(dictionary, board, "", x, y, used, foundWords, matches);	// investigates all words starting from given coordinate
		
		return matches;
	}
	
	public static void findMatchesRec(ArrayList<String> dictionary, String[][] board, String word, 
							int lastX, int lastY, int[][] used, Set<String> foundWords, ArrayList<Match> matches) {
		String newWord = word+board[lastX][lastY];
		used[lastX][lastY] = newWord.length();
		
		if (newWord.length() >= shortestWord && !foundWords.contains(newWord) && Collections.binarySearch(dictionary, newWord) >= 0) {
			matches.add(new Match(newWord, used));
			foundWords.add(newWord);
			System.out.println("Match found!");
		}
		
		for (int i = 0; i < dx.length; i++) {
			int nextX = lastX + dx[i];
			int nextY = lastY + dy[i];
			if (nextX >= 0 && nextX < 4 && nextY >= 0 && nextY < 4 && used[nextX][nextY] == 0) {
				findMatchesRec(dictionary, board, newWord, nextX, nextY, used, foundWords, matches);
			}
		}
		
		used[lastX][lastY] = 0;
	}
	
	public static ArrayList<String> readLinesFromFile(String filename, String encoding) {
		BufferedReader dictFile = null;
		try {
			dictFile = new BufferedReader(
							new InputStreamReader(
								new FileInputStream(filename),
								encoding
								)
							);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ArrayList<String> lines = new ArrayList<String>();
		try {
			while (dictFile.ready()) {
				lines.add(dictFile.readLine());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		Collections.sort(lines);
		return lines;
	}
	
	public static class Match implements Comparable<Match> {
		String word;
		int[][] board;
		
		public Match(String word, int[][] board) {
			this.word = word;
			this.board = new int[4][4];
			for (int x = 0; x < board.length; x++)
				for (int y = 0; y < board.length; y++)
					this.board[x][y] = board[x][y];
		}
		
		public int length() {
			return word.length();
		}
		
		public void print() {
			System.out.println(word);
			for (int y = 0; y < board.length; y++) {
				for (int x = 0; x < board.length; x++)
					if (board[x][y] == 0)
						System.out.print((char)215);
					else
						System.out.print(board[x][y]);
				System.out.println();
			}
			System.out.println();
		}

		@Override
		public int compareTo(Match match) {
			if (this.length() < match.length())
				return -1;
			if (this.length() == match.length())
				return 0;
			return 1;
		}
	}
}
