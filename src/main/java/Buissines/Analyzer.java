package Buissines;

import java.util.LinkedList;

public class Analyzer {

    public String expression = "";

    enum States{
        S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,S11,S12,S13,S14,S15,S16,
        S17,S18,S19,S20,S21,S22,S23,S24,S25,S26,S27,S28,S29,S30,
        S31,S32,
        S177,S188,S199,S277,S278,F,
        W,Z,
        E
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Метод для проверки правильности ввода синтаксической
     * конструкции "do{...}while(...);
     * @return если метод вернул "-1" - то пользователь не верно,
     *         с точки зрения синтаксиса ввёл конструкцию
     *
     *         если метод вернул "1" - то всё введено верно,
     *         и данный цикл выполниться больше одного раза
     *
     *         если метод вернул "2" - то конструкция введена
     *         пользователем верно, но она не выполниться
     *         более одного раза
     * */
    public int checkExpression(){
        LinkedList<Character> names = new LinkedList<>();
        LinkedList<Integer> values = new LinkedList<>();
        LinkedList<Boolean> booleans = new LinkedList<>();

        int answer = -1;
        boolean orFlag = false;
        int indexOfCompare = -1;
        int indexOfSign = -1;
        int firstIndex = -1;
        int secondIndex = -1;
        int thirdindex = -1;
        int buf1 = 0;
        int buf2 = 0;
        States currentState = States.S1;
        expression = expression.replace("\n"," ");
        int i = 0;
        char[] chars = expression.toCharArray();
        while (i < chars.length && currentState != States.E){
            char symbol = chars[i];
            switch (currentState){
                case S1:
                    if(symbol == ' ') currentState = States.S1;
                    else if(symbol == 'i') currentState = States.S2;
                    else currentState = States.E;
                    break;
                case S2:
                    if(symbol == 'n') currentState = States.S3;
                    else currentState = States.E;
                    break;
                case S3:
                    if(symbol == 't') currentState = States.S4;
                    else currentState = States.E;
                    break;
                case S5:
                    if(symbol == ' ') currentState = States.S4;
                    else currentState = States.E;
                    break;
                case S4:
                    if(symbol == ' ') currentState = States.S4;
                    else if(Character.isLetter(symbol)) {
                        names.add(symbol);
                        currentState = States.S6;
                    }
                    else currentState = States.E;
                    break;
                case S6:
                    if(symbol == ' ') currentState = States.S6;
                    else if(symbol == '=') currentState = States.S7;
                    else currentState = States.E;
                    break;
                case S7:
                    if(Character.isDigit(symbol)) {
                        values.add(Integer.parseInt(""+symbol));
                        currentState = States.S8;
                    }
                    else if (symbol == ' ') currentState = States.S7;
                    else currentState = States.E;
                    break;
                case S8:
                    if(symbol == ';') currentState = States.S9;
                    else if(symbol == ' ') currentState = States.S8;
                    else currentState = States.E;
                    break;
                case S9:
                    if(symbol == 'i') currentState = States.S2;
                    else if(symbol == ' ') currentState = States.S9;
                    else if(symbol == 'd')currentState = States.S10;
                    else currentState = States.E;
                    break;
                case S10:
                    if(symbol == 'o') currentState = States.S11;
                    else currentState = States.E;
                    break;
                case S11:
                    if(symbol == '{') currentState = States.S12;
                    else if(symbol == ' ') currentState = States.S11;
                    else currentState = States.E;
                    break;
                case S12:
                    if(symbol == ' ') currentState = States.S12;
                    else if(names.contains(symbol)) {
                        firstIndex = names.indexOf(symbol);
                        currentState = States.S13;
                    }
                    else if(symbol == '}') currentState = States.W;
                    else currentState = States.E;
                    break;
                case S13:
                    if(symbol == ' ') currentState = States.S13;
                    else if(symbol == '=') currentState = States.S14;
                    else currentState = States.E;
                    break;
                case S14:
                    if(symbol == ' ') currentState = States.S14;
                    else if(names.contains(symbol)) {
                        secondIndex = names.indexOf(symbol);
                        currentState = States.S15;
                    }
                    else if(Character.isDigit(symbol)) {
                        buf1 = Integer.parseInt(""+symbol);
                        currentState = States.S16;
                    }
                    else currentState = States.E;
                    break;
                case S15:
                    if(symbol == ' ') currentState = States.S15;
                    else if(symbol == ';') {
                        values.set(firstIndex,values.get(secondIndex));
                        currentState = States.Z;
                    }
                    else if(symbol == '+'){
                        currentState = States.S17;
                        indexOfSign = 0;
                    }
                    else if(symbol == '-') {
                        currentState = States.S17;
                        indexOfSign = 1;
                    }
                    else if(symbol == '*') {
                        currentState = States.S17;
                        indexOfSign = 2;
                    }
                    else currentState = States.E;
                    break;
                case S16:
                    if(symbol == ' ') currentState = States.S16;
                    else if(symbol == ';') {
                        values.set(firstIndex,buf1);
                        currentState = States.Z;
                    }
                    else if(symbol == '+'){
                        currentState = States.S177;
                        indexOfSign = 0;
                    }
                    else if(symbol == '-') {
                        currentState = States.S177;
                        indexOfSign = 1;
                    }
                    else if(symbol == '*') {
                        currentState = States.S177;
                        indexOfSign = 2;
                    }
                    else currentState = States.E;
                    break;
                case S17:
                    if(symbol == ' ') currentState = States.S17;
                    else if(names.contains(symbol)) {
                        thirdindex = names.indexOf(symbol);
                        currentState = States.S18;
                    }
                    else if(Character.isDigit(symbol)) {
                        buf2 = Integer.parseInt(""+symbol);
                        currentState = States.S19;
                    }
                    else currentState = States.E;
                    break;
                case S18:
                    if(symbol == ' ') currentState = States.S18;
                    else if (symbol == ';')
                    {
                        if(indexOfSign == 0){
                            values.set(firstIndex, values.get(secondIndex) + values.get(thirdindex) );
                        }
                        if(indexOfSign == 1){
                            values.set(firstIndex, values.get(secondIndex) - values.get(thirdindex) );
                        }
                        if(indexOfSign == 2){
                            values.set(firstIndex, values.get(secondIndex) * values.get(thirdindex) );
                        }
                        currentState = States.Z;
                    }
                    else currentState = States.E;
                    break;
                case S19:
                    if(symbol == ' ') currentState = States.S19;
                    else if(symbol == ';') {
                        if(indexOfSign == 0){
                            values.set(firstIndex, values.get(secondIndex) + buf2 );
                        }
                        if(indexOfSign == 1){
                            values.set(firstIndex, values.get(secondIndex) - buf2 );
                        }
                        if(indexOfSign == 2){
                            values.set(firstIndex, values.get(secondIndex) * buf2 );
                        }
                        currentState = States.Z;
                    }
                    else currentState = States.E;
                    break;
                case S177:
                    if(symbol == ' ') currentState = States.S177;
                    else if(names.contains(symbol)) {
                        thirdindex = names.indexOf(symbol);
                        currentState = States.S188;
                    }
                    else if(Character.isDigit(symbol)){
                        buf2 = Integer.parseInt(""+symbol);
                        currentState = States.S199;
                    }
                    else currentState = States.E;
                    break;
                case S188:
                    if(symbol == ' ') currentState = States.S188;
                    else if(symbol == ';'){
                        if(indexOfSign == 0) values.set(firstIndex,buf1 + values.get(thirdindex));
                        else if(indexOfSign == 1) values.set(firstIndex,buf1 - values.get(thirdindex));
                        else if(indexOfSign == 2) values.set(firstIndex,buf1 * values.get(thirdindex));
                        currentState = States.Z;
                    }
                    else currentState = States.E;
                    break;
                case S199:
                    if(symbol == ' ') currentState = States.S199;
                    else if(symbol == ';'){
                        if(indexOfSign == 0) values.set(firstIndex,buf1 + buf2);
                        else if(indexOfSign == 1) values.set(firstIndex,buf1 - buf2);
                        else if(indexOfSign == 2) values.set(firstIndex,buf1 * buf2);
                        currentState = States.Z;
                    }
                    else currentState = States.E;
                    break;
                case Z:
                    if(symbol == ' ') currentState = States.S12;
                    else if(symbol == '}') currentState = States.W;
                    else currentState = States.E;
                    break;
                case W:
                    if(symbol == ' ') currentState = States.W;
                    else if(symbol == 'w') currentState = States.S20;
                    else currentState = States.E;
                    break;
                case S20:
                    if(symbol == 'h') currentState = States.S21;
                    else currentState = States.E;
                    break;
                case S21:
                    if(symbol == 'i') currentState = States.S22;
                    else currentState = States.E;
                    break;
                case S22:
                    if(symbol == 'l') currentState = States.S23;
                    else currentState = States.E;
                    break;
                case S23:
                    if(symbol == 'e') currentState = States.S24;
                    else currentState = States.E;
                    break;
                case S24:
                    if(symbol == '(') currentState = States.S25;
                    else if (symbol == ' ') currentState = States.S24;
                    else currentState = States.E;
                    break;
                case S25:
                    if(symbol == ' ') currentState = States.S25;
                    else if(names.contains(symbol)) {
                        firstIndex = names.indexOf(symbol);
                        currentState = States.S26;
                    }
                    else currentState = States.E;
                    break;
                case S26:
                    if(symbol == ' ') currentState = States.S26;
                    else if(symbol == '<') {
                        currentState = States.S27;
                        indexOfCompare = 0;
                    }
                    else if(symbol == '>') {
                        currentState = States.S27;
                        indexOfCompare = 1;
                    }
                    else if(symbol == '=') {
                        currentState = States.S277;
                    }
                    else if(symbol == '!') {
                        currentState = States.S278;
                    }
                    else currentState = States.E;
                    break;
                case S278:
                    if(symbol == '=') {
                        indexOfCompare = 3;
                        currentState = States.S27;
                    }
                    else currentState = States.E;
                    break;
                case S277:
                    if(symbol == '=') {
                        indexOfCompare = 2;
                        currentState = States.S27;
                    }
                    else currentState = States.E;
                    break;
                case S27:
                    if(symbol == ' ') currentState = States.S27;
                    else if(names.contains(symbol)) {
                        currentState = States.S28;
                        secondIndex = names.indexOf(symbol);
                    }
                    else if(Character.isDigit(symbol)){
                        currentState = States.S30;
                        buf1 = Integer.parseInt(""+symbol);
                    }
                    else currentState = States.E;
                    break;
                case S28:
                    if(symbol == ' ') currentState = States.S28;
                    else if(symbol == '&' || symbol == '|' || symbol ==')'){
                        if(symbol == '|') {
                            currentState = States.S32;
                            orFlag = true;
                        }
                        else if(symbol == '&') currentState = States.S29;
                        else if(symbol == ')') currentState = States.S31;
                        if(indexOfCompare == 0){
                            if (values.get(secondIndex) - values.get(firstIndex) > 0){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 1){
                            if (values.get(secondIndex) - values.get(firstIndex) < 0){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 2){
                            if (values.get(secondIndex) == values.get(firstIndex)){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 3){
                            if (values.get(secondIndex) != values.get(firstIndex)){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                    }
                    else currentState = States.E;
                    break;
                case S29:
                    if(symbol == '&') currentState = States.S25;
                    else currentState = States.E;
                    break;
                case S32:
                    if(symbol == '|') currentState = States.S25;
                    else currentState = States.E;
                    break;
                case S30:
                    if(symbol == ' ') currentState = States.S30;
                    else if(symbol == '&' || symbol == '|' || symbol == ')'){
                        if(symbol == '|') {
                            currentState = States.S32;
                            orFlag = true;
                        }
                        else if(symbol == '&') currentState = States.S29;
                        else if(symbol == ')') currentState = States.S31;
                        if(indexOfCompare == 0){
                            if (buf1 - values.get(firstIndex) > 0){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 1){
                            if (buf1 - values.get(firstIndex) < 0){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 2){
                            if (buf1 == values.get(firstIndex)){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                        else if(indexOfCompare == 3){
                            if (buf1 != values.get(firstIndex)){
                                booleans.add(true);
                            }
                            else booleans.add(false);
                        }
                    }
                    else currentState = States.E;
                    break;
                case S31:
                    if(symbol == ';')
                    {
                        if(orFlag == true){
                            if(booleans.contains(true)) answer = 1;
                            else answer = 2;
                        }
                        if(orFlag == false){
                            if(booleans.contains(false)) answer = 2;
                            else answer = 1;
                        }
                        currentState = States.F;
                    }
                    else if(symbol == ' ') currentState = States.S31;
                    else currentState = States.E;
                    break;
                case E:
                    return answer;
            }
            i++;
        }
        return answer;
    }
}
