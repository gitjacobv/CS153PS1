import java.io.*;
import java.util.*;
import javax.swing.*;
import java.math.BigInteger;
public class DESTest{

  public static String partitionedString(String s){

    if(s.length()>16) return s.substring(0, 16);
    else{
      for(int a=s.length(); a<16; a++){
        s=s+"0";
      }
      return s;
    }
  }

  public static boolean checkHex(String s){

    if(s == null){
      return false;
    }

    if(s.equals("")){
      return false;
    }

    for(int i=0; i<s.length(); i++){

      int j = (int)s.charAt(i);

      if( j<47 || (j>57 && j <65) || (j>70&&j<97) || j>102 ){
        System.out.println("Please input characters from 0-9 and a-f or A-F");
        return false;
      }

    }

    return true;
  }

  public static void main(String args[]){

    String plaintext = "";
    String key = "";

    //Input plaintext

    String[] stringSource = { "String Input", "File"};
    String sinput = "";

    while( sinput.equals("") ){
      sinput = (String)JOptionPane.showInputDialog(null,
      "Where to get plaintext", "Input",
      JOptionPane.INFORMATION_MESSAGE, null,
      stringSource, stringSource[0]);

      if(sinput == null){
        sinput = "";
      }
    }

    if(sinput.equals("String Input")){
      while(plaintext.equals("") || !checkHex(plaintext)){
        plaintext = JOptionPane.showInputDialog("Please input plaintext");
        if(plaintext == null){
          plaintext = "";
        }
      }
    }

    else if(sinput.equals("File")){

      String filenameInput="";

      while(filenameInput.equals("")){
        filenameInput = JOptionPane.showInputDialog("Please input filename(<filename>.txt)");
        if(filenameInput == null){
          filenameInput = "";
        }
      }

      try{
        plaintext = new Scanner(new File(filenameInput)).useDelimiter("\\Z").next();
      }catch(Exception e){
        e.printStackTrace();
      }

    }

    //Input Key

    String[] keySource = { "String Input", "File", "0101010101010101", "fefefefefefefefe", "1f1f1f1f1f1f1f1f", "e0e0e0e0e0e0e0e0"};
    String kinput = "";

    while( kinput.equals("")){
      kinput = (String)JOptionPane.showInputDialog(null,
      "Where to get key", "Input",
      JOptionPane.INFORMATION_MESSAGE, null,
      keySource, keySource[0]);

      if(kinput == null){
        kinput = "";
      }
    }

    if(kinput.equals("String Input")){
      // System.out.println(input);
      while(key.equals("") || !checkHex(key)){
        key = JOptionPane.showInputDialog("Please input key in Hex");

        if(key.length() > 16){
          System.out.println("Please input characters for key of length <16 \n");
        }

        if(key == null){
          key = "";
        }
      }
    }

    else if(kinput.equals("File")){
      String filenameInput="";

      while(filenameInput.equals("")){
        filenameInput = JOptionPane.showInputDialog("Please input filename(<filename>.txt)");
        if(filenameInput == null){
          filenameInput = "";
        }
      }

      try{
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

    //plaintext = "0123456789ABCDEF";
    //key = "0123456789ABCDEF";
    //output 56CC09E7CFDC4CEF

    //plaintext = "0123456789ABCDEF";
    //key = "133457799BBCDFF1";
    //output 85e813540f0ab405

    System.out.println("Plaintext(in hex): " + plaintext);
    System.out.println("Key(in hex): " + key  + "\n");

    ArrayList<String> segmented = new ArrayList<String>();

    for(int a=0; a<plaintext.length(); a+=16){

      segmented.add(partitionedString(plaintext.substring(a)));


      System.out.println("***********Encrypting string: " + segmented.get(a/16) + "***********\n");

      DES d = new DES(segmented.get(a/16), key);
      d.desCipher();
    }


  }

}
