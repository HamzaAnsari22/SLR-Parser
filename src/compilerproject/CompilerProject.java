/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerproject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * @author ansar
 */
public class CompilerProject {
    public static boolean flag=false,flag1=false,strflag=false;
    public static int linno=0;
    public static LinkedHashSet<LinkedHashSet<String> > symbolTable
            = new LinkedHashSet<LinkedHashSet<String> >();
    
    public static String[][] table = { { "do", "do","0","yes" },
                                       { "while", "while","1","yes" },
                                       { "if", "if","2","yes" },
                                       { "then", "then","3","yes" },
                                       { "else", "else","4","yes" },
                                       { "int", "int","5","yes" },
                                       { "char", "char","6","yes" },
                                       { "string", "String","7","yes" },
                                       { "Identifier", "id","(next value)","yes" },
                                       { "any string value", "sl","(next value)","yes" },
                                       { "any int value", "in","(next value)","yes" },
                                       { "<", "ro","lt","no" },
                                       { "<=", "ro","le","no" },
                                       { "==", "ro","eq","no" },
                                       { "<>", "ro","ne","no" },
                                       { ">=", "ro","ge","no" },
                                       { ">", "ro","gt","no" },
                                       { "+", "ao","ad","no" },
                                       { "-", "ao","sb","no" },
                                       { "*", "ao","ml","no" },
                                       { "/", "ao","dv","no" },
                                       { "=", "oo","as","no" },
                                       { "{", "oo","op","no" },
                                       { "}", "oo","cp","no" },
                                       { "{", "oo","ob","no" },
                                       { "}", "oo","cb","no" },
                                       { ";", "oo","lt","no" },
                                       { "KeyWord", "KeyWord","KW","no" }
                                    };
    static boolean iskeyword(String str)
    {
        String keyword[] ={"int","float","if","while","then","char","string", "else", "return", "break", "continue" ,"do"};
        if(!Character.isLowerCase(str.charAt(0)))
        {
           return false;
        }
        for(int i=0;i<9;i++)
        {
            if(str.matches(keyword[i]))
            {     
               return true;
            }
        }
        return false;
    }
    static void print(String a,String b,String c) 
    {
        if(!flag){
        
            System.out.println(a+" : "+b);
            for(int i=0;i<table.length;i++){
                if(a==table[i][1]&&table[i][3]=="yes")
                {
                     symbolTable.add(new LinkedHashSet<String>(
                Arrays.asList(Integer.toString(i), "\t\t\t"+a,"\t\t"+b)));
                }
            }
        }
    }
   static void tokenize(String str,int o) throws IOException{
       if(!flag)
       {
           System.out.println("\nLine Number: "+linno);
       }
            String lexeme="";
            for(int i=0;i<str.length();i++){
                if(Character.isLetter(str.charAt(i))){
                    lexeme+=str.charAt(i++);
                    while(i<str.length()&&(Character.isLetterOrDigit(str.charAt(i))||str.charAt(i)=='_')){
                        lexeme+=str.charAt(i++);
                    }
                    i--;
                    if(iskeyword(lexeme)){
                        print("KeyWord",lexeme,lexeme);                        
                    }
                    else{
                        if(strflag)
                        {
                              print("string",lexeme,"string");
                        } 
                        else{
                        print("id",lexeme,"identifier");}
                    }
                    lexeme="";
                }
                else if(Character.isDigit(str.charAt(i))){
                    int flag1=0,flag2=0;
                    lexeme+=str.charAt(i++);
                    while(i<str.length()&&(Character.isDigit(str.charAt(i)))){
                        if(Character.isDigit(str.charAt(i))){
                            lexeme+=str.charAt(i++);
                        }
                        else{
                            break;
                        }
                        
                    }
                    i--;
                        print("int",lexeme,"num ");
                    lexeme="";
                }
                else if(str.charAt(i)=='+'||str.charAt(i)=='-'){
                        print("ao",Character.toString(str.charAt(i)),"addOp ");
                }
                else if(str.charAt(i)=='*'){ 
                    int k=i+1;
                    if(str.charAt(k)=='/')
                     {
                         flag=false;
                         i++;
                     }
                    else
                    {
                         print("ao",Character.toString(str.charAt(i)),"mulop ");
                    }           
                }
                 else if(str.charAt(i)=='/'){
                    int k=i+1;
                     if(str.charAt(k)=='/')
                     {
                         print("Comment","//","Comment ");
                         return;
                     }
                     else if(str.charAt(k)=='*')
                     {
                         System.out.println("Comment : /*.....*/");
                         i++;
                         flag=true;
                     }
                     else
                     {
                        print("ao",Character.toString(str.charAt(i)),"ao ");
                     }
                }
                    else if(str.charAt(i)=='"'){ 
                        if(strflag){
                            strflag=false;
                        }
                        else{
                            strflag=true;
                        }
                    int k=i+1;                         
                         flag=false;                                 
                        print("st",Character.toString(str.charAt(i)),"st ");
                     
                }
                else if(str.charAt(i)=='<'||str.charAt(i)=='>'||str.charAt(i)=='='||str.charAt(i)=='!'){
                    lexeme+=str.charAt(i++);
                    if(i<str.length()&&str.charAt(i)=='='){
                        lexeme+=str.charAt(i++);
                    }
                    i--;
                    if(lexeme.matches("=")){
                        print("oo",lexeme,"= ");
                    }
                    else if(lexeme.matches("!")){
                        print("not",lexeme,"not ");
                    }
                    else
                    {
                        print("ro",lexeme,"relop ");
                    }
                    lexeme="";
                }
                else if(i<str.length()-1 &&(str.charAt(i)=='&'&& str.charAt(i+1)=='&')){
                    lexeme+=str.charAt(i++);
                    lexeme+=str.charAt(i);
                    print("and",lexeme,"and ");
                    lexeme="";
                }
                else if(i<str.length()-1 &&(str.charAt(i)=='|'&& str.charAt(i+1)=='|')){
                    lexeme+=str.charAt(i++);
                    lexeme+=str.charAt(i);
                    print("or",lexeme,"or ");
                    lexeme="";
                }
                else if(str.charAt(i)=='('){
                    print("oo",Character.toString(str.charAt(i)),"( ");
                }
                else if(str.charAt(i)==')'){
                    print("oo",Character.toString(str.charAt(i)),") ");
                }
                else if(str.charAt(i)=='{'){
                    print("oo",Character.toString(str.charAt(i)),"({ ");
                }
                else if(str.charAt(i)=='}'){
                    print("oo",Character.toString(str.charAt(i)),"} ");
                }
                else if(str.charAt(i)=='['){
                    print("[",Character.toString(str.charAt(i)),"[ ");
                }
                else if(str.charAt(i)==']'){
                    print("]",Character.toString(str.charAt(i)),"] ");
                }
                else if(str.charAt(i)==';'){
                    print("oo",Character.toString(str.charAt(i)),"; ");
                }
                else if(str.charAt(i)==','){
                    print(",",Character.toString(str.charAt(i)),", ");
                }
                else if(Character.isWhitespace(str.charAt(i))){
                    
                }
                else{
                    System.out.println("Error : "+str.charAt(i));
                    flag1=true;
                }
            }

        }
   
  
  
   public static void main(String[] args) throws FileNotFoundException, IOException {
       char choice=' ';
       
       while(true)
       {
            System.out.println("\nEnter Command:\n");
            System.out.println("L - Read input to perfom Lexiacl Analysis");
            System.out.println("P - Read input to perfom Lexiacl Analysis and Parsing");
            System.out.println("X - Exit");
            Scanner sc=new Scanner(System.in);
            System.out.print("Choice: ");
            choice=sc.next().charAt(0);
            if(choice=='L'||choice=='l'){
                String line;     
                try {
                    FileReader fileReader =new FileReader("input.txt");
                    try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                        int i=1;
                        while((line = bufferedReader.readLine()) != null) {
                            linno=i++;
                            tokenize(line,linno);
                              if(flag1)
                        {
                            System.out.println("Lexeme not Found in given table");
                            flag1=false;
                        }
                        }
                       
                        System.out.println("\n\nSYMBOL TABLE\nAttribute Value\t\tToken Name\tValue");
                        for (LinkedHashSet o : symbolTable) {
                            System.out.println(o);
                        }
                    }
                }
        
                catch(Exception ex) {}
            }
            else if(choice=='P'||choice=='p')
            {
                System.out.println("\nSubmission Part 2\n");
                SLRparser slr=new SLRparser();
                slr.slr();
                
            }
            else if(choice=='X'||choice=='x')
            {
                break;
            }
            else{
                System.out.println("Invalid Input\nKindly Try Again\n");
            }
            
            
       }
      
      
    }
}
 
    
