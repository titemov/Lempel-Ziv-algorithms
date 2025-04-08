import java.util.Arrays;
import java.util.Objects;

public class LZ77 {
    int dictSize;
    int buffSize;
    String[] dictSymbols;
    String finalCode="";
    String dictionary;
    String buff;
    int len;
    int pos;
    String letter;
    String binaryCode;
    public LZ77(String dictionary, String buff, int len, int pos, String letter, String binaryCode){
        this.dictionary=dictionary;
        this.buff=buff;
        this.len=len;
        this.pos=pos;
        this.letter =letter;
        this.binaryCode=binaryCode;
    }

    public static String[] findSubString(String buff, String dict, int dictSize){
        String[] ans = {"","",""};//substring,startPos,Len
        int startPos = 0;
        int minLen = Math.min(buff.length(), dict.length());
        String elem;

        try {
            elem = buff.substring(0, 1);
        }catch (Exception e){
            ans[0]=dict.substring(0,1);
            ans[1]=Integer.toString(startPos);
            ans[2]=Integer.toString(0);
            return ans;
        }

        String[] splitted = dict.split(elem);

        for(int i=1;i<splitted.length;i++){
            //first in splitted always skipped because standing before "elem"
            for(int n=0;n<minLen;n++){
                try {
                    //System.out.println("COMPARING: "+buff.substring(0, n + 1)+" "+(elem+splitted[i].substring(0, n)));
                    if (Objects.equals(buff.substring(0, n + 1), (elem+splitted[i].substring(0, n)))) {
                        if(ans[0].length()<(elem+splitted[i].substring(0, n)).length()){
                            ans[0]=elem+splitted[i].substring(0, n);
                        }
                    }
                    else{
                        break;
                    }
                }catch (Exception e){
                    continue;
                }
            }
        }
        if(splitted[0].length() != dict.length() && splitted.length==1){
            ans[0]=dict.substring(dict.length()-1,dict.length());
        }
        //System.out.println("ans[0] "+ans[0]);

        for(int i=0;i<dict.length()-ans[0].length()+1;i++){
            //System.out.println("Checking: "+dict.substring(i,i+ans[0].length()));
            if(Objects.equals(ans[0],dict.substring(i,i+ans[0].length()))){
                startPos=i;
                break;
            }
        }

        if(startPos!=0){
            ans[1]=Integer.toString(startPos+(dictSize-dict.length()));
        }else ans[1]=Integer.toString(startPos);
        ans[2]=Integer.toString(ans[0].length());
        if (Objects.equals(ans[0],"") || buff.length()==1){
            ans[0]=elem;
        }else{
            ans[0] = buff.substring(Integer.parseInt(ans[2]), Integer.parseInt(ans[2]) + 1);
        }
//        System.out.print("ANSWER: ");
//        System.out.print(" "+ans[0]);
//        System.out.print(" "+ans[1]);
//        System.out.println(" "+ans[2]);
        return ans;
    }

    public String solveLZ77(String inputString,int dictSize, int buffSize){
        this.dictSize=dictSize;
        this.buffSize=buffSize;
        this.dictSymbols = Backend.createDict(inputString);
        Log.writeLog(" Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int currentPos=0;
        int lenBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int posBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int symbsBits=(int)(Math.ceil(Math.log(this.dictSymbols.length)/Math.log(2)));

        String binaryLen="";
        String binaryPos="";
        String binarySymb="";

//        System.out.printf("%" + "s", "Dict");
//        System.out.printf("%" + ((dictSize-4)+2+4) + "s", "Buff");
//        System.out.printf("%" + ((buffSize-4)+2+3) + "s", "Len");
//        System.out.printf("%" + (2+3) + "s", "Pos");
//        System.out.printf("%" + (2+6) + "s", "Letter");
//        System.out.printf("%" + (2+4) + "s", "Code");
//        System.out.println(" ");
        Log.writeLog(String.format("%" + "s", " Dict"),false);
        Log.writeLog(String.format("%" + ((dictSize-4)+2+4) + "s", "Buff"),false);
        Log.writeLog(String.format("%" + ((buffSize-4)+2+3) + "s", "Len"),false);
        Log.writeLog(String.format("%" + (2+3) + "s", "Pos"),false);
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+4) + "s", "Code"),false);
        Log.writeLog(" ",true);

        while(true){
            try {
                if (inputString.length() - currentPos <= this.buffSize) this.buff = inputString.substring(currentPos);
                else this.buff = inputString.substring(currentPos, this.buffSize + currentPos);
            }catch (Exception e){
                break;
                //this done in case if last currentpos value will be bigger than inputString length
            }

            if(this.buff.length()==0){
                break;
            }

            String[] temp = findSubString(this.buff,this.dictionary,this.dictSize);
            this.len=Integer.valueOf(temp[2]);
            this.pos=Integer.valueOf(temp[1]);
            this.letter =temp[0];

            binaryLen=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.len)),lenBits);
            binaryPos=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.pos)),posBits);
            for(int i=0;i<this.dictSymbols.length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(i)),symbsBits);
                    break;
                }
            }

            this.finalCode+=(binaryLen+binaryPos+binarySymb);

//            System.out.printf("%" + (dictSize) + "s", this.dictionary);
//            System.out.printf("%" + (2+buffSize) + "s", this.buff);
//            System.out.printf("%" + (2+1+(Math.min(dictSize,buffSize)/10)) + "d", this.len);
//            System.out.printf("%" + (5) + "d", this.pos);
//            System.out.printf("%" + (2-(dictSize/10)+2+1) + "s", this.letter);
//            System.out.printf("%" + ((6-1)+2+lenBits) + "s", binaryLen);
//            System.out.printf("%" + (1+posBits) + "s", binaryPos);
//            System.out.printf("%" + (1+symbsBits) + "s", binarySymb);
//            System.out.println(" ");
            Log.writeLog(String.format("%" + (dictSize) + "s", this.dictionary),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+1+(Math.min(dictSize,buffSize)/10)) + "d", this.len),false);
            Log.writeLog(String.format("%" + (5) + "d", this.pos),false);
            Log.writeLog(String.format("%" + (2-(dictSize/10)+2+1) + "s", this.letter),false);
            Log.writeLog(String.format("%" + ((6-1)+2+lenBits) + "s", binaryLen),false);
            Log.writeLog(String.format("%" + (1+posBits) + "s", binaryPos),false);
            Log.writeLog(String.format("%" + (1+symbsBits) + "s", binarySymb),false);
            Log.writeLog(" ",true);

            currentPos+=1+this.len;

            if(currentPos<dictSize){
                this.dictionary=inputString.substring(0,currentPos);
            }else{
                if(currentPos>=inputString.length()) {
                    this.dictionary = inputString.substring(currentPos-dictSize);
                }else{
                    this.dictionary=inputString.substring(currentPos-dictSize,currentPos);
                }
            }
        }
        return this.finalCode;
    }
}
/*
Пока есть символы для чтения:
    в буфер заносится максимальное количество (по размеру буфера) доступных для чтения символов
    рассматривается первый символ буфера
    если есть подстрока начинающаяся с того же символа (этот же символ):
        сравнить второй символ в буфере со вторым, после найденного, в словаре
        делать до тех пор, пока есть совпадения или закончися размер словаря
    заносится длина найденной подстроки (от нуля до размера словаря)
    заносится позиция начала найденной подстроки в словаре (от нуля до размера словаря)
    заносится начальный символ подстроки
    последние три пункта кодируются в двоичную систему согласно таблице соответствия символов
    (сделать автогенерируемую на основе лексикографического порядка символов)
*/