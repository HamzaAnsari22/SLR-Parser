/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerproject;



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class SLRparser {

    static String rawInput;
    static int number_Of_Tokens = 0;

    public static void errorMessage() {
        System.out.println("Syntax Error");
        
    }

    public static int getIndex(String token) throws IOException {
        int indexA = -1;
        switch (token) {
            case "id":
                indexA = 0;
                break;
            case "+":
                indexA = 1;
                break;
            case "*":
                indexA = 2;
                break;
            case "(":
                indexA = 3;
                break;
            case ")":
                indexA = 4;
                break;
            case "$":
                indexA = 5;
                break;
            case "E":
                indexA = 6;
                break;
            case "T":
                indexA = 7;
                break;
            case "F":
                indexA = 8;
                break;
            default:
                errorMessage();
        }

        return indexA;
    }

    public static String[][] ParseTable() {
        String[][] parseTable = new String[12][9];
        //id
        parseTable[0][0] = "s5";
        parseTable[4][0] = "s5";
        parseTable[6][0] = "s5";
        parseTable[7][0] = "s5";

        //+
        parseTable[1][1] = "s6";
        parseTable[2][1] = "r2";
        parseTable[3][1] = "r4";
        parseTable[5][1] = "r6";
        parseTable[8][1] = "s6";
        parseTable[9][1] = "r1";
        parseTable[10][1] = "r3";
        parseTable[11][1] = "r5";

        //*
        parseTable[2][2] = "s7";
        parseTable[3][2] = "r4";
        parseTable[5][2] = "r6";
        parseTable[9][2] = "s7";
        parseTable[10][2] = "r3";
        parseTable[11][2] = "r5";

        //(
        parseTable[0][3] = "s4";
        parseTable[4][3] = "s4";
        parseTable[6][3] = "s4";
        parseTable[7][3] = "s4";

        //)
        parseTable[2][4] = "r2";
        parseTable[3][4] = "r4";
        parseTable[5][4] = "r6";
        parseTable[8][4] = "s11";
        parseTable[9][4] = "r1";
        parseTable[10][4] = "r3";
        parseTable[11][4] = "r5";

        //$
        parseTable[1][5] = "acc";
        parseTable[2][5] = "r2";
        parseTable[3][5] = "r4";
        parseTable[5][5] = "r6";
        parseTable[9][5] = "r1";
        parseTable[10][5] = "r3";
        parseTable[11][5] = "r5";

        //E
        parseTable[0][6] = "1";
        parseTable[4][6] = "8";

        //T
        parseTable[0][7] = "2";
        parseTable[4][7] = "2";
        parseTable[6][7] = "9";

        //F
        parseTable[0][8] = "3";
        parseTable[4][8] = "3";
        parseTable[6][8] = "3";
        parseTable[7][8] = "10";

        return parseTable;
    }

    public static String rawInput_to_inputString(String rawInput) {
        String inputString = "";
        while (rawInput.charAt(0) != '$') {
            switch (rawInput.charAt(0)) {
                case 'i':
                    rawInput = rawInput.substring(2);
                    inputString = inputString.concat("id ");
                    break;
                case '+':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("+ ");
                    break;
                case '*':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("* ");
                    break;
                case '(':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("( ");
                    break;
                case ')':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat(") ");
                    break;
                case 'E':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("E ");
                    break;
                case 'T':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("T ");
                    break;
                case 'F':
                    rawInput = rawInput.substring(1);
                    inputString = inputString.concat("F ");
                    break;
                default:
                    errorMessage();
            }
        }
        inputString = inputString.concat("$");
        return inputString;
    }

    public static String rule(String action) {
        String r = "";
        switch (action) {
            case "r1":
                r = "E → E+T";
                break;
            case "r2":
                r = "E → T";
                break;
            case "r3":
                r = "T → T*F";
                break;
            case "r4":
                r = "T → F";
                break;
            case "r5":
                r = "F → (E)";
                break;
            case "r6":
                r = "F → id";
                break;

        }
        return r;
    }

    public static String remainingInput(String inputString, int number_Of_Tokens, int tokenCount) {
        StringTokenizer st = new StringTokenizer(inputString);
        String remaining = "";

        for (int j = 0; j < number_Of_Tokens; j++) {
            st.nextToken();
        }

        for (int j = number_Of_Tokens; j < tokenCount; j++) {
            remaining = remaining.concat(st.nextToken());
        }

        return remaining;
    }

    public  void slr() throws IOException {

        String[][] parseTable = ParseTable();
        


        Scanner input = new Scanner(System.in);
        System.out.println("Please give Input: ");
        rawInput = input.next();
        
        
        String inputString = rawInput_to_inputString(rawInput);
        StringTokenizer st = new StringTokenizer(inputString);
        System.out.println("Input String : " + rawInput);
        System.out.println("\nStack\t\t\t\t\t    Input\tAction");


        Stack s = new Stack();
        s.push("0");
        int tokenCount = st.countTokens();
        String str = st.nextToken();
        do {

            int index = getIndex(str);

            String action = parseTable[Integer.parseInt((String) s.peek())][index];
            switch (action.charAt(0)) {
                case 's':
                    String number = parseTable[Integer.parseInt((String) s.peek())][index].substring(1);
                    String ri = remainingInput(inputString, number_Of_Tokens, tokenCount);
                    number_Of_Tokens++;
                    System.out.printf("%-30s %20s %s %s\n", s.toString(), ri, "Shift by  ", action);
                    s.push(str);
                    s.push(number);
                    str = st.nextToken();
                    break;
                case 'r':
                    number = parseTable[Integer.parseInt((String) s.peek())][index].substring(1);
                    String rule = rule(action);
                    String rin = remainingInput(inputString, number_Of_Tokens, tokenCount);
                    System.out.printf("%-30s %20s %s %s %s %s %s\n", s.toString(), rin, "Reduce by  ", rule, "(", action, ")");
                    int popTimes = 6;
                    if (Integer.parseInt(number) % 2 == 0) {
                        popTimes = 2;
                    }
                    for (int i = 0; i < popTimes; i++) {
                        s.pop();
                    }

                    String ls = "";
                    switch (Integer.parseInt(number)) {
                        case 1:
                            ls = "E";
                            break;
                        case 2:
                            ls = "E";
                            break;
                        case 3:
                            ls = "T";
                            break;
                        case 4:
                            ls = "T";
                            break;
                        case 5:
                            ls = "F";
                            break;
                        case 6:
                            ls = "F";
                            break;
                    }
                    int indexA = -1;
                    switch (ls.charAt(0)) {
                        case 'E':
                            indexA = 6;
                            break;
                        case 'T':
                            indexA = 7;
                            break;
                        case 'F':
                            indexA = 8;
                            break;
                    }
                    int num = Integer.parseInt(parseTable[Integer.parseInt((String) s.peek())][indexA]);
                    s.push(ls + "");
                    s.push(num + "");

                    break;
                case 'a':
                    System.out.printf("%-49s %s\n", s.toString(), "$ ACCEPT");
                   // System.exit(0);
                    return;
            }
        } while (true);
    }
}


