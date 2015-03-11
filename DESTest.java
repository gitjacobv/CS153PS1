import java.io.*;
import java.util.*;
import javax.swing.*;
public class DESTest{

  public static void main(String args[]){
    //Input plaintext
    String plaintext = "";
    String[] inputSource = { "String Input", "File"};
    String input = (String)JOptionPane.showInputDialog(null,
    "Where to get plaintext", "Input",
    JOptionPane.INFORMATION_MESSAGE, null,
    inputSource, inputSource[0]);

    if(input.equals("String Input")){
      // System.out.println(input);
      plaintext = JOptionPane.showInputDialog("Please input plaintext");
    }

    else if(input.equals("File")){
      //String filenameInput = JOptionPane.showInputDialog("Please input filename(<filename>.txt)");
      try{
        plaintext = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();

        //plaintext = new Scanner(new File(filenameInput)).useDelimiter("\\Z").next();
      }catch(Exception e){
        e.printStackTrace();
      }

    }
    System.out.println(plaintext);

    byte[] b = plaintext.getBytes();
    //Integer i = new Integer(b);
    System.out.println(new Byte(b[0]));

  }

}
