package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
        int score = 0;
		Color[][] board_array = board.flatten();
        int arrsize = board_array.length;

        for (int i = 0; i < arrsize; i++) {
               for (int j= 0; j < arrsize; j++) {
                   if (i == 0) {
                       if (board_array[i][j] == targetGoal)
                           score++;
                   }
                   else if (i == arrsize-1) {
                       if (board_array[i][j] == targetGoal)
                           score++;
                   }
                   if (j == 0) {
                       if (board_array[i][j] == targetGoal)
                           score++;
                   }
                   else if (j == arrsize-1) {
                       if (board_array [i][j] == targetGoal)
                           score++;
                   }
               }
        }
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
