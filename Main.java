package assignment3;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.ArrayList;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Block b = new Block(0, 2);
        b.updateSizeAndPosition(16, 0, 0);
        ArrayList<BlockToDraw> blocksToDraw = b.getBlocksToDraw();


        Block blockDepth3 = new Block(0,3);
        blockDepth3.updateSizeAndPosition(16,0,0);
        blockDepth3.printBlock();
        System.out.println(" ");
        Block b1 = blockDepth3.getSelectedBlock(2,15,1);
        //b1.printBlock();

        blockDepth3.reflect(1);
        blockDepth3.printBlock();
    }
}