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

    public static int[] findSubString(String buff, String dict, int dictSize){
        int[] result = {0,0};//startPos,len
        int startPos = 0;
        int len = 0;
        int minLen = Math.min(buff.length(), dict.length());
        String elem;

        try {
            elem = buff.substring(0, 1);
        }catch (Exception e){
            return result;
        }

        String[] splitted = dict.split(elem);

        for(int i=1;i<splitted.length;i++){
            //first in splitted always skipped because standing before "elem"
            for(int n=0;n<minLen;n++){
                try {
                    //System.out.println("COMPARING: "+buff.substring(0, n + 1)+" "+(elem+splitted[i].substring(0, n)));
                    if (Objects.equals(buff.substring(0, n + 1), (elem+splitted[i].substring(0, n)))) {
                        if(len<(elem+splitted[i].substring(0, n)).length()){
                            len=(elem+splitted[i].substring(0, n)).length();
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

        if(splitted.length==1 && splitted[0].length() != dict.length()){
            startPos=dict.length()-1;
            len=1;
        }else{
            if(splitted.length==0){
                startPos=0;
                len=1;
            }
        }
        //System.out.println("ans[0] "+dict.substring(startPos,startPos+len));

        for(int i=0;i<dict.length()-result[1]+1;i++){
            if(Objects.equals(buff.substring(0,len),dict.substring(i,i+len))){
                startPos=i;
                break;
            }
        }

        if(len!=0) {
            result[0] = (dictSize - dict.length()) + startPos;
        }else{
            result[0]=startPos;
        }
        result[1]=len;
        return result;
    }

    public String encodeLZ77(String inputString, int dictSize, int buffSize){
        this.dictSize=dictSize;
        this.buffSize=buffSize;
        this.dictSymbols = Backend.createDict(inputString);
        Log.writeLog("Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int currentPos=0;
        int lenBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int posBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int symbsBits=(int)(Math.ceil(Math.log(this.dictSymbols.length)/Math.log(2)));

        String binaryLen="";
        String binaryPos="";
        String binarySymb="";

        if(dictSize<4 || buffSize<4)
        {
            Log.writeLog(String.format("%" + "s", "D"),false);
            Log.writeLog(String.format("%" + ((dictSize-1)+2+1) + "s", "B"),false);
            Log.writeLog(String.format("%" + ((buffSize-1)+2+3) + "s", "Len"),false);
        }else{
            Log.writeLog(String.format("%" + "s", "Dict"),false);
            Log.writeLog(String.format("%" + ((dictSize-4)+2+4) + "s", "Buff"),false);
            Log.writeLog(String.format("%" + ((buffSize-4)+2+3) + "s", "Len"),false);
        }
        Log.writeLog(String.format("%" + (2+3) + "s", "Pos"),false);
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+4) + "s", "Code"),false);
        Log.writeLog("",true);

        while(true){
            try {
                if (inputString.length() - currentPos <= this.buffSize) this.buff = inputString.substring(currentPos);
                else this.buff = inputString.substring(currentPos, this.buffSize + currentPos);
                if(this.buff.length()==0){
                    throw new Exception();
                }
            }catch (Exception e){
                break;
                //this done in case if last currentpos value will be bigger than inputString length
            }

            int[] temp = findSubString(this.buff,this.dictionary,this.dictSize);
            this.len=temp[1];
            this.pos=temp[0];
            try {
                this.letter = inputString.substring(currentPos + this.len, currentPos + this.len + 1);
            }catch (Exception e){
                this.letter = inputString.substring(currentPos + this.len);
            }

            binaryLen=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.len)),lenBits);
            binaryPos=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.pos)),posBits);
            for(int i=0;i<this.dictSymbols.length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(i)),symbsBits);
                    break;
                }else{
                    binarySymb="";
                }
            }

            this.finalCode+=(binaryLen+binaryPos+binarySymb);

            Log.writeLog(String.format("%" + (dictSize) + "s", this.dictionary),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+1+(Math.min(dictSize,buffSize)/100)) + "d", this.len),false);
            Log.writeLog(String.format("%" + (5) + "d", this.pos),false);
            Log.writeLog(String.format("%" + (2-(dictSize/100)+2+1) + "s", this.letter),false);
            Log.writeLog(String.format("%" + ((6-1)+2+lenBits) + "s", binaryLen),false);
            Log.writeLog(String.format("%" + (1+posBits) + "s", binaryPos),false);//posBits might be equal to zero
            Log.writeLog(String.format("%" + (1+symbsBits) + "s", binarySymb),false);
            Log.writeLog("",true);

            currentPos+=1+this.len;

            if(currentPos<dictSize){
                try {//kludge :)
                    this.dictionary = inputString.substring(0, currentPos);
                }catch (Exception e){
                    this.dictionary = inputString.substring(0);
                }
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