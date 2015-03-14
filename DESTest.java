import java.io.*;
import java.util.*;
import javax.swing.*;
public class DESTest{

  public static String partitionedString(String s){

    if(s.length()>8) return s.substring(0, 8);
    else{
      for(int a=s.length(); a<8; a++){
        s=s+"0";
      }
      return s;
    }
  }

  public static void main(String args[]){

    String plaintext = "";
    String key = "";

    //Input plaintext

    String[] stringSource = { "String Input", "File"};
    String sinput = (String)JOptionPane.showInputDialog(null,
    "Where to get plaintext", "Input",
    JOptionPane.INFORMATION_MESSAGE, null,
    stringSource, stringSource[0]);

    if(sinput.equals("String Input")){
      // System.out.println(input);
      plaintext = JOptionPane.showInputDialog("Please input plaintext");
    }

    else if(sinput.equals("File")){
      String filenameInput = JOptionPane.showInputDialog("Please input filename(<filename>.txt)");
      try{
        //plaintext = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();

        plaintext = new Scanner(new File(filenameInput)).useDelimiter("\\Z").next();
      }catch(Exception e){
        e.printStackTrace();
      }

    }

    //Input Key

    String[] keySource = { "String Input", "File", "0101010101010101", "fefefefefefefefe", "1f1f1f1f1f1f1f1f", "e0e0e0e0e0e0e0e0"};
    String kinput = (String)JOptionPane.showInputDialog(null,
    "Where to get plaintext", "Input",
    JOptionPane.INFORMATION_MESSAGE, null,
    keySource, keySource[0]);

    if(kinput.equals("String Input")){
      // System.out.println(input);
      key = JOptionPane.showInputDialog("Please input key");
    }

    else if(kinput.equals("File")){
      String filenameInput = JOptionPane.showInputDialog("Please input filename(<filename>.txt)");
      try{
        //plaintext = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();

        key = new Scanner(new File(filenameInput)).useDelimiter("\\Z").next();
      }catch(Exception e){
        e.printStackTrace();
      }

    }

    else if(kinput.equals("0101010101010101")){
      key = keySource[2];
    }

    else if(kinput.equals("fefefefefefefefe")){
      key = keySource[3];
    }

    else if(kinput.equals("1f1f1f1f1f1f1f1f")){
      key = keySource[4];
    }

    else if(kinput.equals("e0e0e0e0e0e0e0e0")){
      key = keySource[5];
    }

    System.out.println("Plaintext: " + plaintext);
    System.out.println("Key(in hex): " + key  + "\n");

    ArrayList<String> segmented = new ArrayList<String>();

    for(int a=0; a<plaintext.length(); a+=8){

      segmented.add(partitionedString(plaintext.substring(a)));

      System.out.println("Encrypting string: " + segmented.get(a/8));

      DES d = new DES(segmented.get(a/8), key);
      d.desCipher();
    }



    /*System.out.println(plaintext);

    byte[] b = plaintext.getBytes();
    //Integer i = new Integer(b);
    System.out.println(new Byte(b[0]));

    DES ds = new DES();*/


  }

}
