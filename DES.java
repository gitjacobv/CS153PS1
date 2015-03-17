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

 private static final int[] P = {
    16, 7,  20, 21, 29, 12, 28, 17,
    1,  15, 23, 26, 5,  18, 31, 10,
    2,  8,  24, 14, 32, 27,  3,  9,
    19, 13, 30,  6, 22, 11,  4, 25
  };

  private static final int[]
  PC1 = {
    57, 49, 41, 33, 25, 17,  9,
    1, 58, 50, 42, 34, 26, 18,
    10,  2, 59, 51, 43, 35, 27,
    19, 11,  3, 60, 52, 44, 36,
    63, 55, 47, 39, 31, 23, 15,
    7, 62, 54, 46, 38, 30, 22,
    14,  6, 61, 53, 45, 37, 29,
    21, 13,  5, 28, 20, 12,  4
  };

  private static final int[]
  PC2 = {
    14, 17, 11, 24,  1,  5, 3, 28,
    15,  6, 21, 10, 23, 19, 12, 4,
    26,  8, 16, 7, 27, 20, 13,  2,
    41, 52, 31, 37, 47, 55, 30, 40,
    51, 45, 33, 48, 44, 49, 39, 56,
    34, 53, 46, 42, 50, 36, 29, 32
  };

  public DES(String plaintext, String key){

    BigInteger pbi = new BigInteger(plaintext, 16);

    String pbis = pbi.toString(2);

    while(pbis.length() != 64){
      pbis = "0" + pbis;
    }

    for(int i=0; i<64; i++){

      bits64[i] = Character.digit(pbis.charAt(i), 10);

    }

    System.out.println("Original Bits: " + partString(bitsToString(this.bits64), 4));

    //Key

    BigInteger kbi = new BigInteger(key, 16);

    String kbis = kbi.toString(2);

    while(kbis.length() != 64){
      kbis = "0" + kbis;
    }

    for(int i=0; i<64; i++){

      deskey[i] = Character.digit(kbis.charAt(i), 10);

    }

    System.out.println("Key(in Binary): " + partString(bitsToString(this.deskey), 8) + "\n");

  }

  public int[] initialPermutation(){

    int[] ipbits = new int[64];

    for(int i=0; i<64; i++){

      ipbits[i] = (new Integer(this.bits64[(IP[i]) - 1])).intValue();
    }

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

    //expanded bits xor with rndkeybits
    int[] sbits = xor(exprbits, rndkeybits, 48);

    //sboxes
    for(int i=0; i<8; i++){
      int[] bits4 = new int[4];

      Arrays.toString(Arrays.copyOfRange(sbits, i*6, (i*6)+6));
      bits4 = sbox(Arrays.copyOfRange(sbits, i*6, (i*6)+6), i);

      for(int j=0; j<4 ; j++){
        fbits[(i*4)+j] = (new Integer(bits4[j])).intValue();
      }
    }

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

    int sval = (new Integer(this.S[snumber][row.intValue()][column.intValue()])).intValue();

    BigInteger bibits = new BigInteger(Integer.toString(sval), 10);

    String strbits4 = bibits.toString(2);

    for(int i=0; i< strbits4.length() ;i++){
      int strl = strbits4.length()-1;
      bits4[3-i] = Character.digit(strbits4.charAt(strl-i), 10);
    }

    return bits4;
  }

  public int[] xor(int[] bits1, int[] bits2, int size){

    if(bits1.length != bits2.length){
      return null;
    }

    int[] xorbits = new int[size];

    for(int i=0; i<size; i++){
      xorbits[i] = ( bits1[i] + bits2[i] ) % 2;
    }

    return xorbits;
  }

  public int[] lrotate(int[] bits, int value){

    int[] rotbits = new int[bits.length];

    int pos;

    for(int i=0; i<bits.length; i++){
      pos = (i-value)%bits.length;

      if(pos < 0){
        pos = pos + bits.length;
      }

      rotbits[pos] = (new Integer(bits[i])).intValue();
    }

    return rotbits;

  }

  public int[][] transform(int inum, int[] cbits, int[] dbits){

    int value;

    if(inum  == 0 || inum  == 1 || inum  == 8 || inum  == 15){
      value = 1;
    }

    else{
      value = 2;
    }

    int[][] cdbits = new int[][]{
      Arrays.copyOf( lrotate(cbits, value), 28 ),
      Arrays.copyOf( lrotate(dbits, value), 28 )
    };

    return cdbits;

  }

  public int[] finalPermutation(){
    int[] fpbits = new int[64];

    for(int i=0; i<64; i++){
      fpbits[i] = (new Integer(this.bits64[(FP[i]) - 1])).intValue();
    }

    return fpbits;

  }

  public String bitsToString(int[] bits){
    String s = "";
    for(int i=0; i<bits.length; i++){
      s = s + Integer.toString(bits[i]);
    }
    return s;
  }

  public String partString(String s, int n){

    String sss = "";

    for(int i=0; i< s.length(); i++){

      sss = sss + s.charAt(i);


      if( ((i+1) % n ) == 0){
        sss = sss + " ";
      }
    }

    return sss;
  }

  public ArrayList<String> desCipher(){

    //Initial Permutation
    int[] ipbits = initialPermutation();

    System.out.println("Initial Permutation: " + partString(bitsToString(ipbits), 4) + "\n");

    //Round 0
    System.out.println("------------------------------- Round 0 ------------------------------\n");

    //PC1
    int[] pc1bits = new int[56];

    for(int i = 0; i<56; i++){
      pc1bits[i] = (new Integer( deskey[PC1[i] - 1] )).intValue();
    }

    System.out.println("PC: \t\t" + partString(bitsToString(pc1bits), 7));

    //Transform 0
    int[][] cdbits = new int[][]{ Arrays.copyOfRange(pc1bits, 0, 28), Arrays.copyOfRange(pc1bits, 28, 56) };

    System.out.println("C: \t\t" + partString(bitsToString(cdbits[0]), 7));
    System.out.println("D: \t\t" + partString(bitsToString(cdbits[1]), 7));

    //Round 0
    int[][] lrbits = new int[][]{ Arrays.copyOfRange(ipbits, 0, 32), Arrays.copyOfRange(ipbits, 32, 64) };

    System.out.println("L: \t\t" + partString(bitsToString(lrbits[0]), 4));
    System.out.println("R: \t\t" + partString(bitsToString(lrbits[1]), 4) +"\n");

    for(int i=0; i<16; i++){

      System.out.println("------------------------------- Round " + (i+1) + " ------------------------------\n");

      //Transform i
      cdbits = transform(i, cdbits[0], cdbits[1]);

      System.out.println("C: \t\t" + partString(bitsToString(cdbits[0]), 28));
      System.out.println("D: \t\t" + partString(bitsToString(cdbits[1]), 28));

      //CD for PC2
      int[] cdkey = new int[56];
      System.arraycopy(cdbits[0], 0, cdkey, 0, 28);
      System.arraycopy(cdbits[1], 0, cdkey, 28, 28);

      //System.out.println("CD': \t\t" + partString(bitsToString(cdkey), 7));

      int[] pc2bits = new int[48];

      for(int j = 0; j<48; j++){
        pc2bits[j] = (new Integer(cdkey[ PC2[j] - 1])).intValue();
      }

      System.out.println("\nROUNDKEY(2): \t" + partString(bitsToString(pc2bits), 6) + "\n");

      BigInteger rkbi = new BigInteger(bitsToString(pc2bits), 2);

      System.out.println("ROUNDKEY(16): \t" + rkbi.toString(16) + "\n");

      //Round i
      lrbits = round(lrbits[0], lrbits[1], pc2bits);
      System.out.println("L: \t\t" + partString(bitsToString(lrbits[0]), 4));
      System.out.println("R: \t\t" + partString(bitsToString(lrbits[1]), 4) +"\n");

    }

    System.arraycopy(lrbits[1], 0, this.bits64, 0, 32);
    System.arraycopy(lrbits[0], 0, this.bits64, 32, 32);

    int[] fpbits = this.finalPermutation();

    System.out.println("-----------------------------------------------------------------------\n");

    System.out.println("Final Permutation(2):  " + bitsToString(fpbits) + "\n");

    BigInteger fpbi = new BigInteger(bitsToString(fpbits), 2);

    System.out.println("Final Permutation(16): " + fpbi.toString(16) + "\n");

    ArrayList<String> als = new ArrayList<String>();
    als.add(bitsToString(fpbits));
    als.add(fpbi.toString(16));

    return als;

  }


}
