package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] board_array = board.flatten();
        int arrsize = board_array.length;
        int score = 0;
        for (int i = 0; i < arrsize-1; i++) {
            for (int j = 0; j < arrsize-1; j++) {
                boolean[][] visited = new boolean[arrsize][arrsize];
                if (board_array[i][j] == targetGoal) {
                    int result = undiscoveredBlobSize(i,j,board_array,visited);
                    if (result > score) {
                        score = result;
                    }
                }
            }
        }
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
        if (i < 0 || j < 0 || i > unitCells.length-1 || j > unitCells.length-1) {
            return 0;
        }
        if (visited[i][j] || unitCells[i][j] != targetGoal) {
            visited[i][j] = true;
            return 0;
        }

        int BlobSize = 1;
        visited[i][j] = true;

        BlobSize += undiscoveredBlobSize(i+1, j, unitCells, visited);
        BlobSize += undiscoveredBlobSize(i,j+1, unitCells, visited);
        BlobSize += undiscoveredBlobSize(i-1, j, unitCells, visited);
        BlobSize += undiscoveredBlobSize(i, j-1, unitCells, visited);

        return BlobSize;
	}

}
