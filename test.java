import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BigNumber{
    String a;

    public BigNumber(String a){
        this.a = a;
    }

    public String toString(){
        return this.a;
    }

    public BigNumber add(BigNumber b){
        // verify if strings are valid (-- not valid)
        if(Pattern.matches("-?[0-9]+", a) == false || Pattern.matches("-?[0-9]+", b.toString()) == false){
            System.out.println( a + " and " + b.toString() + " contain invalid string(s)");
            return new BigNumber(null);
        }

        // calc length variables, length is the shorter string length between a and b
        int aLen = a.length();
        int bLen = b.toString().length();
        int length = bLen;
        if(bLen > aLen){
            length = aLen;
        }

        int digit, carry = 0, aLast, bLast;
        String sum = "";
        // if both negative
        if((a.contains("-") && b.toString().contains("-"))){
            for(int i = length - 1; i >= 1; i--){
                aLast = Integer.valueOf(a.charAt(aLen - 1)) - '0';
                bLast = b.toString().charAt(bLen - 1) - '0';

                digit = (aLast + bLast + carry) % 10;
                carry = (aLast + bLast + carry) / 10;

                sum = Integer.toString(digit) + sum;
                aLen --;
                bLen --;
            }

            // leftover carry
            if(a.length() > length){
                sum = Integer.toString(carry + Integer.valueOf(a.substring(1, aLen))) + sum;
            } else if(b.toString().length() > length){
                sum = Integer.toString(carry + Integer.valueOf(b.toString().substring(1, bLen))) + sum;
            } else{
                sum = Integer.toString(carry) + sum;
            }
            // add back the '-'
            sum = '-' + sum;

        } else if(!a.contains("-") && !b.toString().contains("-")){
            // both positive
            for(int i = length - 1; i >= 0; i--){
                aLast = Integer.valueOf(a.charAt(aLen - 1)) - '0';
                bLast = b.toString().charAt(bLen - 1) - '0';

                digit = (aLast + bLast + carry) % 10;
                carry = (aLast + bLast + carry) / 10;

                sum = Integer.toString(digit) + sum;
                aLen --;
                bLen --;
            }

            // leftover carry
            if(a.length() > length){
                sum = Integer.toString(carry + Integer.valueOf(a.substring(0, aLen))) + sum;
            } else if(b.toString().length() > length){
                sum = Integer.toString(carry + Integer.valueOf(b.toString().substring(0, aLen))) + sum;
            } else{
                sum = Integer.toString(carry) + sum;
            }
        } else{
            // one postive, one negative

            // which is the positive
            String positive, negative;
            int posLen, negLen;
            // if neg bigger than pos, switch and negate
            if(a.contains("-")){
                positive = b.toString();
                posLen = b.toString().length();
                negative = a;
                negLen = a.length();
                if(a.length() == length){
                    length -= 1;
                }else{
                    sum = new BigNumber(Integer.toString(Math.abs(Integer.parseInt(negative)))).add(new BigNumber('-' + positive)).toString();
                    sum = '-' + sum;
                    return new BigNumber(sum);
                }
            }else{
                positive = a;
                posLen = a.length();
                negative = b.toString();
                negLen = b.toString().length();
                if(b.toString().length() == length){
                    length -= 1;
                }else{
                    sum = new BigNumber(Integer.toString(Math.abs(Integer.parseInt(negative)))).add(new BigNumber('-' + positive)).toString();
                    sum = '-' + sum;
                    return new BigNumber(sum);
                }
            }
            for(int i = length - 1; i >= 0; i--){
                int posLast = positive.charAt(posLen - 1) - '0';
                int negLast = negative.toString().charAt(negLen - 1) - '0';

                digit = (posLast - negLast - carry) % 10;
                if(digit < 0){
                    digit = (posLast - negLast - carry + 10) % 10;;
                    carry = 1;
                }

                sum = Integer.toString(digit) + sum;
                posLen--;
                negLen--;
            }
            // determine if need to add the minus sign
            if(positive.length() > length){
                sum = Integer.toString(Integer.valueOf(positive.substring(0, posLen)) - carry) + sum;
            } else if((negative.length() - 1) > length){
                sum = negative.toString().substring(0, negLen) + sum;
                sum = '-' + sum;
            } else if(carry != 0){
                sum = new BigNumber(Integer.toString(Math.abs(Integer.parseInt(negative)))).add(new BigNumber('-' + positive)).toString();
                sum = '-' + sum;
            }
        }

        // pretty print: get rid of leading zero 
        int count = 0;
        for(int i = 0; i <= sum.length() - 1; i++){
            if(sum.charAt(i) == '0'){
                count++;
            } else{
                break;
            }
        }
        sum = sum.substring(count, sum.length());
        
        return new BigNumber(sum);
    }

    public static void main(String[] args){
        // valid strings could contain up to one leading '-' with integer digits, no '+' or char allowed

        BigNumber big1 = new BigNumber("1234");
        BigNumber big2 = new BigNumber("999");
        BigNumber big3 = new BigNumber("-56");
        BigNumber big4 = new BigNumber("-9999");
        BigNumber big5 = new BigNumber("--1"); // invalid
        BigNumber big6 = new BigNumber("12hi9"); // invalid
        BigNumber big7 = new BigNumber("123");
        BigNumber big8 = new BigNumber("10000");
        BigNumber big9 = new BigNumber("8888");
        BigNumber big10 = new BigNumber("-1111");
        BigNumber big11 = new BigNumber("111");

        BigNumber sum1 = big7.add(big2); // both pos, same length with carries, 1122
        BigNumber sum2 = big7.add(big11); // both pos, same length no carries, 234
        BigNumber sum3 = big1.add(big2); // both pos, diff length with carries, 2233
        BigNumber sum4 = big1.add(big7); // both pos, diff length no carries, 1357

        BigNumber sum5 = big1.add(big3); // one pos one neg, pos greater than neg, diff length with borrows, 1178
        BigNumber sum6 = big9.add(big3); // one pos one neg, pos greater than neg, diff length no borrows, 8832
        BigNumber sum7 = big10.add(big7); // one pos one neg, neg greater than pos, diff length with borrows, -988

        BigNumber sum8 = big3.add(big4); // both neg, diff length with carries, -10055
        BigNumber sum9 = big3.add(big10); // both neg, diff length no carries, -1167

        BigNumber sum10 = big5.add(big6); // invalid, null

        System.out.print(sum1.toString() + ' ' + sum2.toString() + ' ' + sum3.toString() + " " + sum4.toString());
        System.out.print(" " + sum5.toString() + " " + sum6.toString() + " " + sum7.toString());
        System.out.println(" " + sum8.toString() + " " + sum9.toString() + " " + sum10.toString());
    }
}
