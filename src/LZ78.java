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
    public LZ78(String[] dictionary, String buff, String foundString, int pos, String letter, String binaryCode){
        this.dictionary= dictionary;
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
            if(Objects.equals(array[i],"") || Objects.equals(array[i],null)) {
                array[i] = elem;
                return;
            }
        }
        for(int i=0;i<array.length-1;i++){
            array[i]=array[i+1];
        }
        array[array.length-1]=elem;
    }

    public static int findSubString(String buff, String[] dict){
        int startPos = 0;
        String elem="";

        for(int i=0;i<dict.length;i++){
            for(int n=0;n<buff.length();n++){
                if(Objects.equals(dict[i],buff.substring(0,n+1))){
                    if(dict[i].length()>elem.length()) {
                        elem = dict[i];
                        startPos = i + 1;
                    }
                }
            }
        }
        return startPos;
    }

    public String encodeLZ78(String inputString,int dictSize, int buffSize){
        this.dictSize=dictSize;
        this.buffSize=buffSize;
        this.dictSymbols = Backend.createDict(inputString);
        Log.writeLog("Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int currentPos=0;
        int posBits=(int)(Math.ceil(Math.log(dictSize)/Math.log(2)));
        int symbsBits=(int)(Math.ceil(Math.log(this.dictSymbols.length)/Math.log(2)));

        String binaryPos="";
        String binarySymb="";

        if(dictSize<2 || buffSize<4)
        {
            Log.writeLog(String.format("%" + "s", "D"), false);
            Log.writeLog(String.format("%" + ((4 * dictSize + 2 * (dictSize - 1) + 2 )+ 2 - 1 + 1) + "s", "B"), false);
            Log.writeLog(String.format("%" + (buffSize-1 + 2 + 5) + "s", "Found"), false);
        }else {
            Log.writeLog(String.format("%" + "s", "Dict"), false);
            Log.writeLog(String.format("%" + (4 * dictSize + 2 * (dictSize - 1) + 2 + 2 - 4 + 4) + "s", "Buff"), false);
            Log.writeLog(String.format("%" + (buffSize + 2 + 5 - 4) + "s", "Found"), false);
        }
        Log.writeLog(String.format("%" + (2+(3+buffSize*2-5)+3) + "s", "Pos"),false);
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+4) + "s", "Code"),false);
        Log.writeLog("",true);

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

            this.pos = findSubString(this.buff,this.dictionary);
            if(this.pos!=0){
                this.foundString=this.dictionary[this.pos-1];
                len=this.dictionary[this.pos-1].length();
            }else{
                this.foundString="";
            }
            try {
                this.letter = inputString.substring(currentPos + len, currentPos + len + 1);
            }catch (Exception e){
                this.letter = inputString.substring(currentPos + len);
            }

            binaryPos=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(this.pos)),posBits);
            for(int i=0;i<(this.dictSymbols).length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.parseInt(Integer.toBinaryString(i)),symbsBits);
                    break;
                }else{
                    binarySymb="";
                }
            }

            this.finalCode+=(binaryPos+binarySymb);

            Log.writeLog(String.format("%" + (4*dictSize+2*(dictSize-1)+2) + "s", Arrays.toString(this.dictionary)),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+3+buffSize*2) + "s", this.foundString),false);
            Log.writeLog(String.format("%" + (2+1) + "d", this.pos),false);
            Log.writeLog(String.format("%" + (2-(dictSize/100)+2+1) + "s", this.letter),false);
            Log.writeLog(String.format("%" + (5+2+posBits) + "s", binaryPos),false);
            Log.writeLog(String.format("%" + (1+symbsBits) + "s", binarySymb),false);
            Log.writeLog("",true);

            currentPos+=1+len;

            if(len!=0){
                pop(this.dictionary,this.foundString);
                push(this.dictionary,this.foundString);
                push(this.dictionary,this.foundString+this.letter);
            }else{
                push(this.dictionary,this.letter);
            }
        }
        return this.finalCode;
    }

    public String decodeLZ78(String inputString, int dictSize, int buffSize){
        return this.finalCode;
    }
}
