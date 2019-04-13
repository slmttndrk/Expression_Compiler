# Expression_Compiler
The aim of the project is that to produce the assembly instructions of a given expression.

# Procedure
The program takes "example.co" file as input and gives "example.asm" file as output.
All my output files work with a86.

# Execution

1) At first the example.co and the COMPCompiler.java must be in the same directory

2) And then try to compile COMPCompiler.java in command line 
	- javac COMPCompiler.java
  
3) And then try to execute COMPCompiler.class in command line 
	- java COMPCompiler
  
4) There will be an output file named "example.asm" in the directory

5) And then try to run "example.asm" with a86
	- a86 example.asm
  
6) Finally run the "example.com"
	- example.com
