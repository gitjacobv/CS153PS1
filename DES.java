import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class DES{

  private int[] deskey = new int[64];
  public int[] bits64 = new int[64];

  private static final int[]
  IP = {
    58, 50, 42, 34, 26, 18, 10, 2,
    60, 52, 44, 36, 28, 20, 12, 4,
    62, 54, 46, 38, 30, 22, 14, 6,
    64, 56, 48, 40, 32, 24, 16, 8,
    57, 49, 41, 33, 25, 17, 9, 1,
    59, 51, 43, 35, 27, 19, 11, 3,
    61, 53, 45, 37, 29, 21, 13, 5,
    63, 55, 47, 39, 31, 23, 15, 7
  };

  private static final int[]
  FP = {
    40, 8, 48, 16, 56, 24, 64, 32,
    39, 7, 47, 15, 55, 23, 63, 31,
    38, 6, 46, 14, 54, 22, 62, 30,
    37, 5, 45, 13, 53, 21, 61, 29,
    36, 4, 44, 12, 52, 20, 60, 28,
    35, 3, 43, 11, 51, 19, 59, 27,
    34, 2, 42, 10, 50, 18, 58, 26,
    33, 1, 41,  9, 49, 17, 57, 25
  };

  private static final int[]
  E = {
    32, 1,  2,  3,  4,  5,
    4,  5,  6,  7,  8,  9,
    8,  9, 10, 11, 12, 13,
    12, 13, 14, 15, 16, 17,
    16, 17, 18, 19, 20, 21,
    20, 21, 22, 23, 24, 25,
    24, 25, 26, 27, 28, 29,
    28, 29, 30, 31, 32,  1
  };

  private static final int[][][]
  S = new int[][][] {
    { // S1
      {14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
      {0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8},
      {4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
      {15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13}
    },

    { // S2
      {15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10},
      {3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5},
      {0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
      {13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9}
    },

    { // S3
      {10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8},
      {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1},
      {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7},
      {1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2,  12}
    },

    { // S4
      {7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
      {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9},
      {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4},
      {3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}
    },

    { // S5
      {2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
      {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6},
      {4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
      {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3}
    },

    { // S6
      {12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11},
      {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8},
      {9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6},
      {4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}
    },

    { // S7
      {4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
      {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6},
      {1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
      {6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}
    },

    { // S8
      {13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7},
      {1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
      {7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
      {2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}
    }
  };

 private static final int[]
  P = {
    16, 7,  20, 21, 29, 12, 28, 17,
    1,  15, 23, 26, 5,  18, 31, 10,
    2,  8,  24, 14, 32, 27,  3,  9,
    19, 13, 30,  6, 22, 11,  4, 25
  };

  public DES(){

  }

  public DES(String char8, String key16){

    //Plaintext

    //System.out.println(S[0][0][0]);

    for(int i=0; i<8; i++){

      String bin = "";
      bin = bin.concat(Integer.toBinaryString( (int)char8.charAt(i) ) );

      while(bin.length() != 8){
        bin = "0" + bin;
      }

    //  System.out.println(bin);
    //  System.out.println(bin.length());
    //  System.out.println( char8.charAt(i) );
    //  System.out.println( Integer.toBinaryString( (int)char8.charAt(i) ));

    //  (new Scanner(System.in)).next();

      for(int j = 0; j<8; j++){

      //  System.out.println(Character.digit(bin.charAt(j), 10));
        //(new Scanner(System.in)).next();

        bits64[(i*8) + j] = Character.digit(bin.charAt(j), 10);

      }

    }

    System.out.println("Original Bits");
    System.out.println(Arrays.toString(bits64) + "\n");

    //Key

    BigInteger kbi = new BigInteger(key16, 16);

    String kbis = kbi.toString(2);

    while(kbis.length() != 64){
      kbis = "0" + kbis;
    }

    for(int i=0; i<64; i++){

      deskey[i] = Character.digit(kbis.charAt(i), 10);

    }

    //System.out.println(kbis);
    //System.out.println("Deskey: " + Arrays.toString(deskey));

  }

  public int[] initialPermutation(){//int[] bits){

    int[] ipbits = new int[64];

    for(int i=0; i<64; i++){
      //ipbits[(IP[i]) - 1] = (new Integer(bits[i])).intValue();
      ipbits[(IP[i]) - 1] = (new Integer(this.bits64[i])).intValue();
    }


    System.out.println("Initial Permutation");
    System.out.println(Arrays.toString(ipbits) + "\n");
    return ipbits;

  }

  public int[][] round(int[] lbits, int[] rbits, int[] rndkey){

    int[][] lrbits = new int[2][32];

    //operate on R

    System.arraycopy(rbits, 0, lrbits[0], 0, 32);

    int[] fbits = Arrays.copyOf(functionF(rbits, rndkey), 32);

    //Operate on L

    int[] lfbits = Arrays.copyOf(xor(lbits, fbits, 32), 32);
    System.arraycopy(lfbits, 0, lrbits[1], 0, 32);

    //int[] lbits = new int[32];
    //int[] rbits = new int[32];

    //lbits = Arrays.copyOfRange(bits, 0, 32);
    //rbits = Arrays.copyOfRange(bits, 32, 64);

    //System.arraycopy(rbits, 0, rndbits, 0, 32);
    //System.arraycopy(lbits, 0, rndbits, 32, 32);

    //System.out.println("\n" + Arrays.toString(bits));
    //System.out.println("\n" + Arrays.toString(lbits));
    //System.out.println("\n" + Arrays.toString(rbits));

    System.out.println("Round Bits L");
    System.out.println(Arrays.toString(lrbits[0]) + "\n");

    System.out.println("Round Bits R");
    System.out.println(Arrays.toString(lrbits[1]) + "\n");

    return lrbits ;

  }

  public int[] functionF(int[] rbits, int[] rndkeybits){

    int[] fbits = new int[32];
    int[] exprbits = new int[48];
    int[] fpbits = new int[32];
    //Expansion

    for(int i=0; i<48; i++){
      exprbits[i] = rbits[this.E[i] - 1];
    }

    System.out.println("Expanded bits \n" + Arrays.toString(exprbits) + "\n");

    //expanded bits xor with rndkeybits
    int[] sbits = xor(exprbits, rndkeybits, 48);

    System.out.println("sbits\n " + Arrays.toString(sbits) + "\n");


    //sboxes
    for(int i=0; i<8; i++){
      int[] bits4 = new int[4];

      //System.out.println("bits6 of " + i + " " + Arrays.toString(Arrays.copyOfRange(sbits, i*6, (i*6)+6)));
      Arrays.toString(Arrays.copyOfRange(sbits, i*6, (i*6)+6));
      bits4 = sbox(Arrays.copyOfRange(sbits, i*6, (i*6)+6), i);

      for(int j=0; j<4 ; j++){
        fbits[(i*4)+j] = (new Integer(bits4[j])).intValue();
      }
    }

    //System.out.println("FBITS\n" + Arrays.toString(fbits) + "\n");

    //Permutation P

    for(int i=0; i<32; i++){
      fpbits[i] = fbits[this.P[i] - 1];
    }

    return fpbits;

  }

  public int[] sbox(int[] bits, int snumber){

    int[] bits4 = new int[4];
    Arrays.fill(bits4, 0);

    BigInteger row = new BigInteger(Integer.toString(bits[0]) + Integer.toString(bits[5]), 2);
    BigInteger column = new BigInteger(bitsToString(Arrays.copyOfRange(bits, 1, 5)), 2);

    //System.out.println(row.intValue());
    //System.out.println(column.intValue() + "\n");

    int sval = (new Integer(this.S[snumber][row.intValue()][column.intValue()])).intValue();
    //System.out.println("S " + sval);

    BigInteger bibits = new BigInteger(Integer.toString(sval), 10);

    String strbits4 = bibits.toString(2);

    //System.out.println("Bibits " + strbits4);

    for(int i=0; i< strbits4.length() ;i++){
      int strl = strbits4.length()-1;
      bits4[3-i] = Character.digit(strbits4.charAt(strl-i), 10);
    }

    //System.out.println("BITS 4 " + Arrays.toString(bits4) + "\n");

    return bits4;
  }

  public int[] xor(int[] bits1, int[] bits2, int size){

    if(bits1.length != bits2.length){
      return null;
    }

    int[] xorbits = new int[size];

    for(int i=0; i<size; i++){
      xorbits[i] = bits1[i] ^ bits2[i];
    }

    return xorbits;
  }

  public int[] finalPermutation(){ //int[] bits){
    int[] fpbits = new int[64];

    for(int i=0; i<64; i++){
      //fpbits[(FP[i]) - 1] = (new Integer(bits[i])).intValue();
      fpbits[(FP[i]) - 1] = (new Integer(this.bits64[i])).intValue();
    }
    System.out.println("Final Permutation");
    System.out.println(Arrays.toString(fpbits) + "\n");

    return fpbits;

  }

  public String bitsToString(int[] bits){
    String s = "";
    for(int i=0; i<bits.length; i++){
      s = s + Integer.toString(bits[i]);
    }
    return s;
  }

  public int[] desCipher(){

    //Initial Permutation
    int[] ipbits = this.initialPermutation();

    //Round
    int[][] lrbits = new int[][]{ Arrays.copyOfRange(ipbits, 0, 32), Arrays.copyOfRange(ipbits, 32, 64) };

    for(int i=0; i<16; i++){

      int[] bits48 = new int[48];
      Arrays.fill(bits48, 0);

      //call key sched here

      System.out.println("---------------------- Round " + (i+1) + " ----------------------\n");

      lrbits = round(lrbits[0], lrbits[1], bits48);

    }

    /*int[] rndbits = new int[64];
    System.arraycopy(lrbits[0], 0, rndbits, 32);
    System.arraycopy(lrbits[1], 32, rndbits, 32);*/

    System.arraycopy(lrbits[0], 0, this.bits64, 0, 32);
    System.arraycopy(lrbits[1], 0, this.bits64, 32, 32);

    int[] fpbits = this.finalPermutation();

    return fpbits;

  }

  public static void main(String[] args){
    DES d = new DES("jacobvil", "101010101010101");
    //int[] ip = d.initialPermutation(d.bits64);
    int[] ip = d.initialPermutation();

    //ip = d.finalPermutation(d.bits64);
    ip = d.finalPermutation();

    System.out.println("Test IP B \n" +  Arrays.toString(ip) + "\n");

    int[] bit0 = new int[64];
    Arrays.fill(bit0, 0);

    ip = d.xor(ip, bit0, 64);

    System.out.println("Test IP A \n" +  Arrays.toString(ip) + "\n");

    int[] r = d.desCipher();

    System.out.println("Test R \n" +  Arrays.toString(r) + "\n");
    //System.out.println("Test R \n" +  Arrays.toString(r[1]) + "\n");

    //int[] fp = d.finalPermutation(d.bits64);


  }

}
