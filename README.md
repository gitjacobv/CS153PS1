# CS153PS1
CS 153 Problem Set 1

The main executable file is in the DESTest.java. To test the encryption, run the DESTest class and input the correct inputs.

Input
Input1: Input the plaintext to encrypt, in hexadecimal form. The input can be from a Keyboard Input or from a File input where
only characters from 0-9, a-f, A-F are allowed. In the case where the number of characters are not a multiple of 16 for
hexadecimal parsing, zeroes(0) are padded at the end of the plaintext to complete the blocks.

Input2: Input the key for encryption, in hexadecimal form. The input can be from a Keyboard Input, from a File input, or from
the selected keys given for the Problem set where only characters from 0-9, a-f, A-F are allowed. The key should only be of
length less than 16; in the case that it is less than 16, zeroes(0) are padded at infront of the key - unlike for the plaintext
where it is padded at the end.

Encryption
In the case that the plaintext should exceed 16 characters(>1 block), encryption is done per 16 characters in hex(64 bits).

Display: The original plaintext and key is displayed in binary form. The key per round is also displayed in binary and
hexadecimal. Also each ciphertext is displayed.

Output
The ciphertext is displayed in binary and hexadecimal form.
