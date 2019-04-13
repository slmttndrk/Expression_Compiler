 /*

	Compiler called COMPCompiler

*/

import java.util.*;
import java.io.*;

public class COMPCompiler{

	public static BufferedReader in;
	public static BufferedWriter out; 
	public static int nofv;
	public static String[] variables;

   
   public static void main(String args[]) throws IOException
	{
		
		try {
			in = new BufferedReader (new FileReader("example.co"));
		    out = new BufferedWriter(new FileWriter("example.asm"));	//creating an output file with .asm extension
			
		    variables= new String[200];	//asumming max 200 variable address wil be needed
		    nofv=0;
		    program();
		    
			
		}finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
   public static void program()throws IOException 
	//a function for reading input file and send it to stmt 
   { 						
	String str,read;
        str =""; 
        while((read=in.readLine())!=null)
           str+=" "+read;
	

		out.write("\n\ncode segment\n");
        stmt(str);
       
		out.write("   call myprint\n   int  20h\nmyread:\n  mov  cx,0\n  morechar:\n  mov  ah, 01h\n  int  21h\n  mov  dx,0\n  mov  dl,al\n  mov  ax,cx\n  cmp  dl,0d\n  je   myret\n  sub  dx,48d\n  mov  bp,dx\n  mov  ax,cx\n  mov  cx,10d\n  mul  cx\n  add  ax,bp\n  mov  cx,ax\n  jmp  morechar\nmyret:\n  ret\n");
		out.write("myprint:\n  mov  si,10d\n  xor  dx,dx\n  push 0Ah\n  mov  cx,1d\n");
		out.write("nonzero:\n  div  si\n  add  dx,48d\n  push dx\n  inc  cx\n  xor  dx,dx\n  cmp  ax,0h\n  jne  nonzero\n");
		out.write("writeloop:\n  pop  dx\n  mov  ah,02h\n  int  21h\n  dec  cx\n  jnz  writeloop\n  ret\n");
		int i=0;
	
	while(nofv>i){
	    out.write("v"+variables[i]+"    dw ?\n");
	    i++;
	}
	out.write("code ends\n");
    }
    public static void stmt(String exp)throws IOException
	// a function for processing a statement
    {
       exp = exp.trim();	// I always trim the " " and \n to make it easier to process

       
	  if(exp.contains("=")){	//creates code for 'id = expr'
          out.write("  push offset v"+exp.split("=",2)[0]+"\n");
	  String temp;
	  temp=exp.split("=",2)[0].trim();
          int i=0;
          while(i<nofv){// checks if 'id' has been stored
	      if(temp.compareTo(variables[i])==0){
		break;}
	      else
		i++;
	  }
	  if(i==nofv){//store 'id' if it has not been stored
            variables[nofv]=temp;
	    nofv++;
          }
          expr(exp.split("=",2)[1]);	//sends 'expr' to be processed
          out.write("  pop  ax\n  pop  bx\n  mov  [bx], ax\n");
          return;
       }
   }

    public static void expr(String exp)throws IOException
    {//proceses expr
	// finds the first term which can be in '()' so it checks for '()'
	//finds appropriate ')' for '('
	//send remaing to moreterm if exist
       exp=exp.trim(); 
       if(exp.startsWith("+")){
          System.out.println("ERROR  : invalid expr start");
          return;
       } 
      int i=0 , k=0,l=0,m=0;

      while(m<exp.length()){
          
          if(exp.charAt(m)=='(')
             k++;
          if(exp.charAt(m)==')')
             k--;
          if((exp.charAt(m)=='+')&&k==0)
            l++;
          if(l==2) break;
          m++;
      }

      if(!(l==2&&k==0) || i==exp.length()-1){
         i=0;
         k=0;
         while(i<exp.length()){
            if(exp.charAt(i)=='(')
               k++;
            if(exp.charAt(i)==')')
               k--;
            if((exp.charAt(i)=='+')&&k==0)break;
            i++;
		}
			term(exp.substring(0,i));
			if(i<exp.length())
				moreterm(exp.substring(i,exp.length()));
      }
      else{
         i=m;
         expr(exp.substring(0,m));
         if(i<exp.length())
            moreterm(exp.substring(m,exp.length()));
      }
   }

    public static void moreterm(String exp)throws IOException
    {  // removes '+' from the 'moreterm' and process remainning as 'expr'
       exp=exp.trim(); 
       expr(exp.substring(1,exp.length()));
       if(exp.startsWith("+")){ 
         out.write("  pop  cx\n  pop  ax\n  add  ax,cx\n  push ax\n");
       }
    }
    public static void term(String exp)throws IOException
    {//proceses term
	// finds the first factor which can be in '()' so it checks for '()'
	//finds appropriate ')' for '('
	//send remaing to morefactor if exist
       exp=exp.trim(); 
       if(exp.startsWith("*")){
          System.out.println("ERROR  : invalid expr start");
          return;
       }
       int i=0 , k=0,l=0 ,m=0;
       while(m<exp.length()){
          if(exp.charAt(i)=='(')
             k++;
          if(exp.charAt(i)==')')
             k--;
          if( exp.substring(m).startsWith("*") && k==0 )
             l++;
          if( l==2 ) break;
          m++;    
        }

	if(!(l==2&&k==0)){
           i=0;k=0;
           while(i<exp.length()){
              if(exp.charAt(i)=='(')
                 k++;
              if(exp.charAt(i)==')')
                 k--;
              if( exp.substring(i).startsWith("*"))
              if( k==0 )
                 break;
              i++; 
           }   
         
       factor(exp.substring(0,i));
       if(i<exp.length())
          morefactor(exp.substring(i,exp.length()));
    }
    else{

       term(exp.substring(0,m));
       if(m<exp.length())
          morefactor(exp.substring(m,exp.length()));
     }
   }

   public static void morefactor(String exp)throws IOException
   {	// removes '*' from the 'morefactor' and process remainning as 'term'
      exp=exp.trim();
      if(exp.startsWith("*")){
         term(exp.substring(1));
         out.write("  pop  cx\n  pop  ax\n  mul cx\n  push ax\n");
        }
      
   }

   public static void factor(String exp)throws IOException
   {//processes factor
	//if factor is '(expr)' then it removes '()' send its to be processed as expr
      exp=exp.trim();

      if(exp.startsWith("(")&&exp.contains(")")){
          expr(exp.substring(1,exp.length()-1));
          return;
       }
    //else if factor is an power() writes code for push-power()
      else if(exp.startsWith("pow(")&&exp.contains(")")){
    	  String pow;
          pow=exp.substring(4,exp.length()-1);
          int n1 = Integer.parseInt( pow.split(",",2)[0].trim() );
		  int n2 = Integer.parseInt( pow.split(",",2)[1].trim() );
          
          
          out.write("  push "+mypower(n1,n2));
     	  out.write("d");
     	  out.write("\n");
       }
      else if(   exp.contains("a") ||exp.contains("b") 
    		  || exp.contains("c") || exp.contains("d") 
    		  || exp.contains("d") || exp.contains("e") ||exp.contains("f")) {
         out.write("  push "+exp);
	 if(exp.compareTo("0")!=0)
		out.write("h");
	 out.write("\n");
      }
	//else if factor is an Int writes code for push-Num
       
      else if(isInt(exp)){
         out.write("  push "+exp);
	    if(exp.compareTo("0")!=0)
		out.write("d");
	    out.write("\n");
      }
	//else writes code for push-val-var
      else
         out.write("  push v"+exp+" w\n");
   }
   public static boolean isInt(String s){//it checks if 's' is an Int
      char[] factor;
      factor = new char[s.length()];
      factor = s.toCharArray();
      for(char ch:factor){
	 if(!(ch>='0' && ch <='9'))
	    return false;
     }
     return true;
  }
   public static int mypower(int a,int n)
	{
	int p = 1 ;
	int p2 = a ;
	for(int i=1;i<n;i++ ) {
	
		p2=a*p2;
		
	}
	if(n == 1) {
		p=a;
	}
	else p=p2;
	
	return(p) ;
	}
}     