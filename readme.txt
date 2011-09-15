In order to use this program, go to terminal:

0)Extract Jar File, if applicable

1) change directory to PolishNotation folder

2) $javac *.java
Not necessary if extracted jar file

3) $java PolishNotation file_name
The above will give you the infix, prefix, postfix version of the expression inside file_name

4) $java PolishNotation -r file_name
5) $java PolishNotation file_name -r
The two above will give you the reduced infix, prefix, postfix version of the expression inside file_name

Example:

mate p4.txt
2 * ( 5 + 1 )

java PolishNotation p4.txt
Infix: ( 2 * ( 5 + 1 ) ) 
Postfix: ( 2 ( 5 1 + ) * ) 
Prefix: ( * 2 ( + 5 1 ) ) 

java PolishNotation -r p4.txt
Reduced Infix: 12.0 
Reduced Postfix: 12.0 
Reduced Prefix: 12.0 

--------------------------------------------------------------------------

NOTE: Input expressions are highly restrictive, requiring a space between each input operator/operand

Dependencies: The program uses In.java & StdIn.java (for input from a file, taken from Professor Kevin Wayne's booksite) and Queue.java (for creation of a token list, created by me)

Sample Test cases: files p1.txt, p2.txt, p3.txt, p4.txt, p5.txt, p10.txt, and foil.txt provide sample file_name input test cases.

---------------------------------------------------------------------------

About Program: This program uses Knuth's general principle of evaluating an expression:

1) Parse Infix String

2) Convert from Infix to Postfix (or Prefix)

3) Evaluate the Postfix(Prefix) expression

For step 1), I used a method to parse the string from input into a queue of strings. 
For Step 2), I first represented the queue of strings into a binary expression tree. 
In order to convert to binary expression tree, I use Downey et al.'s method (How to Think like a Computer Scientist).
After having a binary tree representation, printing out the prefix (or postfix, infix) version is fairly straightforward, adding parentheses to the output as appropriate. 
To reduce the expression, I created a class called Reducer that has similar methods to Prefix, but reduces the sub-expressions as they are added to the binary expression tree. 
In order words, I am reducing from the bottom (children) of the tree all the way up to the root. The Reducer class aims to reduce an expression into polynomial form of arbitrary number of variables. 






