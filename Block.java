package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
    private int xCoord;
    private int yCoord;
    private int size; // height/width of the square
    private int level; // the root (outer most block) is at level 0
    private int maxDepth;
    private Color color;

    private Block[] children; // {UR, UL, LL, LR}

    public static Random gen = new Random(4);

 /*
  * These two constructors are here for testing purposes.
  */
     public Block() {}

     public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
         this.xCoord=x;
        this.yCoord=y;
         this.size=size;
         this.level=lvl;
        this.maxDepth = maxD;
        this.color=c;
        this.children = subBlocks;
     }

     /*
      * Creates a random block given its level and a max depth.
      *
      * xCoord, yCoord, size, and highlighted should not be initialized
      * (i.e. they will all be initialized by default)
      */
     public Block(int lvl, int maxDepth) {
        this.maxDepth = maxDepth;
        level = lvl;
        if (lvl != maxDepth) {
            double num = gen.nextDouble(1);
            if (num < Math.exp(-0.25 * lvl)){
                children = new Block[4];
                for (int i = 0; i<4; i++) {
                    children[i] = new Block(lvl+1,maxDepth);
                }
            }
            else {
                children = new Block[0];
                int colornum = gen.nextInt(GameColors.BLOCK_COLORS.length);
                color = GameColors.BLOCK_COLORS[colornum];
            }
        }
        else {
            children = new Block[0];
            int colornum = gen.nextInt(GameColors.BLOCK_COLORS.length);
            color = GameColors.BLOCK_COLORS[colornum];
        }
     }

     /*
      * Updates size and position for the block and all of its sub-blocks, while
      * ensuring consistency between the attributes and the relationship of the
      * blocks.
      *
      *  The size is the height and width of the block. (xCoord, yCoord) are the
      *  coordinates of the top left corner of the block.
      */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
     int temp = maxDepth - level;
     for (int i=0; i<temp; i++) {
         if (size%2 != 0 && size != 1 || size < 0) {
             throw new IllegalArgumentException("INVALID SIZE ARGUMENT!");
         }
     }

     this.size = size;
     this.xCoord = xCoord;
     this.yCoord = yCoord;
     if (children.length != 0) {
         children[0].updateSizeAndPosition(size/2,xCoord+(size/2), yCoord);
         children[1].updateSizeAndPosition(size/2, xCoord, yCoord);
         children[2].updateSizeAndPosition(size/2, xCoord, yCoord+(size/2));
         children[3].updateSizeAndPosition(size/2,xCoord+(size/2), yCoord+(size/2));
     }
 }

 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided assignment3.Block:
  * - one assignment3.BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */

 public ArrayList<BlockToDraw> getBlocksToDraw() {
     ArrayList<BlockToDraw> blocks = new ArrayList<BlockToDraw>();
     BlockToDraw bd = new BlockToDraw(color, xCoord, yCoord, size,0);
     BlockToDraw bd_frame = new BlockToDraw(GameColors.FRAME_COLOR, xCoord, yCoord, size, 3);
     if (bd.getColor() != null){
        blocks.add(bd_frame);
        blocks.add(bd);
     }

     for (Block b : children) {
         blocks.addAll(b.getBlocksToDraw());
     }
     return blocks;
 }

 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
     return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }


 /*
  * Return the assignment3.Block within this assignment3.Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired assignment3.Block. Note that if a assignment3.Block includes the location
  * (x, y), and that assignment3.Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which assignment3.Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this assignment3.Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
     if (lvl < this.level || lvl > this.maxDepth)
         throw new IllegalArgumentException("Provided Level is invalid!");
     if (this.level == lvl) {
         if (xCoord <= x && xCoord + size > x && yCoord <= y && yCoord + size > y) {
             return this;
         }
     }
     else if (xCoord <= x && xCoord + size > x && yCoord <= y && y < yCoord + size && children.length == 0) {
         return this;
     }

     else if(lvl != level) {
         for(Block child : this.children){
             Block result = child.getSelectedBlock(x,y,lvl);
             if(result != null) {
                 return result;
             }
         }
     }

     return null;
 }


    /*
  * Swaps the child Blocks of this assignment3.Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this assignment3.Block has no children, do nothing. The swap
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
     if (direction != 1 && direction != 0) {
         throw new IllegalArgumentException("Invalid argument for direction. Only 1 or 0!");
     }
     if (children.length != 0) {
         ArrayList<Block> child_copy = new ArrayList<>(4);
         if (direction == 0) {
             for (int i = 3; i >= 0; i--) {
                 child_copy.add(children[i]);
             }
         }
         else {
             child_copy.add(children[1]);
             child_copy.add(children[0]);
             child_copy.add(children[3]);
             child_copy.add(children[2]);
         }
         for (int i = 0; i < 4; i++) {
             children[i] = child_copy.get(i);
         }
         this.updateSizeAndPosition(size, xCoord, yCoord);

         for (Block b : children) {
             if (!(b.level > maxDepth))
                b.reflect(direction);
         }
     }
 }

 /*
  * Rotate this assignment3.Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this assignment3.Block has no children, do nothing.
  */
 public void rotate(int direction) {
     if (direction != 0 && direction != 1) {
         throw new IllegalArgumentException("Direction invalid. Has to be 1 or 0.");
     }
     if (children.length == 0) {
         return;
     }

     if (direction == 0) {
             Block temp = children[3];
             for (int i = 3; i > 0; i--) {
                 children[i] = children[i-1];
             }
             children[0] = temp;
     }
     else {
         Block temp = children[0];
         for (int i = 0; i < 3; i++) {
             children[i] = children[i+1];
         }
         children[3] = temp;
     }
     updateSizeAndPosition(size, xCoord, yCoord);
     for (Block b : children) {
         b.rotate(direction);
     }
 }


 /*
  * Smash this assignment3.Block.
  *
  * If this assignment3.Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A assignment3.Block can be smashed iff it is not the top-level assignment3.Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this assignment3.Block was smashed and False otherwise.
  *
  */
 public boolean smash() {
     if (level != maxDepth && level != 0) {
         children = new Block[4];
         for (int i = 0; i<4; i++) {
            children[i] = new Block(level+1,maxDepth);
         }
         this.updateSizeAndPosition(size,xCoord,yCoord);
         this.color = null;
        return true;
     }
  return false;
 }


 /*
  * Return a two-dimensional array representing this assignment3.Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this assignment3.Block.
  */
 public Color[][] flatten() {
     int array_size = (int) Math.pow(2,(maxDepth-level));
     Color[][] colorarray = new Color[array_size][array_size];
     if (this.children.length == 0 ) {
         for (int i = 0; i< array_size; i++) {
             for (int j = 0; j < array_size; j++) {
                 colorarray[i][j] = this.color;
             }
         }
         return colorarray;
     }

     int subArraySize = array_size / 2; // Size of sub-arrays for each child block
     Color[][] upperRight = children[0].flatten();
     Color[][] upperLeft = children[1].flatten();
     Color[][] lowerLeft = children[2].flatten();
     Color[][] lowerRight = children[3].flatten();

     // Merge colors from children into the parent array
     for (int i = 0; i < subArraySize; i++) {
         for (int j = 0; j < subArraySize; j++) {
             colorarray[i][j] = upperLeft[i][j];
             colorarray[i][j + subArraySize] = upperRight[i][j];
             colorarray[i + subArraySize][j] = lowerLeft[i][j];
             colorarray[i + subArraySize][j + subArraySize] = lowerRight[i][j];
         }
     }
     return colorarray;
 }


 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }

}
