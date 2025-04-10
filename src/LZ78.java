import java.util.Arrays;
import java.util.Objects;

public class LZ78 {
    int dictSize;
    int buffSize;
    String[] dictSymbols;
    String finalCode="";
    String[] dictionary;
    String buff;
    String foundString;
    int pos;
    String letter;
    String binaryCode;
    public LZ78(String buff, String foundString, int pos, String letter, String binaryCode){
        this.dictionary= new String[dictSize];
        this.buff=buff;
        this.foundString=foundString;
        this.pos=pos;
        this.letter =letter;
        this.binaryCode=binaryCode;
    }

    public static void pop(String[] array, String elem){
        int index=-1;
        for(int i=0;i<array.length;i++){
            if(Objects.equals(array[i],elem)){
                array[i]="";
                index=i;
            }
        }
        if(index==-1) return;
        for(int i=index;i<array.length-1;i++){
            array[i]=array[i+1];
        }
        array[array.length-1]="";
    }
    public static void push(String[] array,String elem){
        for(int i=0;i<array.length;i++){
            if(Objects.equals(array[i],"")) {
                array[i] = elem;
                return;
            }
        }
        for(int i=0;i<array.length-1;i++){
            array[i]=array[i+1];
        }
        array[array.length-1]=elem;
    }

    public static int findSubString(String buff, String[] dict, int dictSize){
        int startPos = 0;
        String elem="";

        for(int i=0;i<dict.length;i++){
            for(int n=0;n<buff.length();n++){
                if(Objects.equals(dict[i],buff.substring(0,n+1))){
                    if(dict[i].length()>elem.length())
                        elem=dict[i];
                        startPos=i+1;
                }
            }
        }
        return startPos;
    }

    public String encodeLZ78(String inputString,int dictSize, int buffSize){
        this.dictSize=dictSize;
        this.buffSize=buffSize;
        this.dictSymbols = Backend.createDict(inputString);
        Log.writeLog(" Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int currentPos=0;
        int posBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int symbsBits=(int)(Math.ceil(Math.log(this.dictSymbols.length)/Math.log(2)));

        String binaryPos="";
        String binarySymb="";

        Log.writeLog(String.format("%" + "s", " Dict"),false);
        Log.writeLog(String.format("%" + ((dictSize-4)+2+4) + "s", "Buff"),false);
        Log.writeLog(String.format("%" + (2+3) + "s", "Pos"),false);
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+4) + "s", "Code"),false);
        Log.writeLog(" ",true);

        while(true){
            int len=0;
            try {
                if (inputString.length() - currentPos <= this.buffSize) this.buff = inputString.substring(currentPos);
                else this.buff = inputString.substring(currentPos, this.buffSize + currentPos);
                if(this.buff.isEmpty()){
                    throw new Exception();
                }
            }catch (Exception e){
                break;
                //this done in case if last currentpos value will be bigger than inputString length
            }

            this.pos = findSubString(this.buff,this.dictionary,this.dictSize);
            if(this.pos!=0){
                this.foundString=this.dictionary[this.pos-1];
                len=this.dictionary[this.pos-1].length();
            }


            binaryPos=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.pos)),posBits);
            for(int i=1;i<(this.dictionary).length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(i)),symbsBits);
                    break;
                }
            }

            this.finalCode+=(binaryPos+binarySymb);

            Log.writeLog(String.format("%" + (dictSize) + "s", this.dictionary),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+1+(Math.min(dictSize,buffSize)/10)) + "d", this.foundString),false);
            Log.writeLog(String.format("%" + (5) + "d", this.pos),false);
            Log.writeLog(String.format("%" + (2-(dictSize/10)+2+1) + "s", this.letter),false);
            Log.writeLog(String.format("%" + (1+posBits) + "s", binaryPos),false);
            Log.writeLog(String.format("%" + (1+symbsBits) + "s", binarySymb),false);
            Log.writeLog(" ",true);

            currentPos+=1+len;

            if(len!=0){
                pop(this.dictionary,this.dictionary[this.pos-1]);
                push();
            }

//            if(currentPos<dictSize){
//                try {//костыль :)
//                    this.dictionary = inputString.substring(0, currentPos);
//                }catch (Exception e){
//                    this.dictionary = inputString.substring(0);
//                }
//            }else{
//                if(currentPos>=inputString.length()) {
//                    this.dictionary = inputString.substring(currentPos-dictSize);
//                }else{
//                    this.dictionary=inputString.substring(currentPos-dictSize,currentPos);
//                }
//            }
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
