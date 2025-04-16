import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

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

    private static int[] findSubString(String buff, String dict, int dictSize){
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

        String[] splitted = dict.split(Pattern.quote(elem));

        for(int i=1;i<splitted.length;i++){//first in splitted always skipped because standing before "elem"
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
        //System.out.println(Arrays.toString(splitted));
        if(splitted.length==1 && splitted[0].length() != dict.length()){
            startPos=dict.length()-1;
            len=1;
        }else{
            if(splitted.length==0){
                startPos=0;
                len=1;
            }
        }

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

    private String[][] codeBreakdown(String input, int dictSize, int buffSize){   //func to breakdown binary code
        String[][] result;

        int dictSubBlock = (int)Math.ceil(Math.log(dictSize)/Math.log(2));
        int buffSubBlock = (int)Math.ceil(Math.log(buffSize)/Math.log(2));
        int letterSubBlock = (int)Math.ceil(Math.log((Backend.dict).length)/Math.log(2));
        int blockLen = dictSubBlock+buffSubBlock+letterSubBlock;

        int amountOfBlocks = (int)Math.floor(input.length()/blockLen);

        result = new String[amountOfBlocks][3];

        for(int i=0;i<result.length;i++){
            String temp = input.substring(i*blockLen,(i+1)*blockLen);
            result[i][0] = temp.substring(0,dictSubBlock);
            result[i][1] = temp.substring(dictSubBlock,dictSubBlock+buffSubBlock);
            result[i][2] = temp.substring(dictSubBlock+buffSubBlock);
            //System.out.println(Arrays.toString(result[i]));
        }

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

            binaryLen=Backend.addLeadingZeros(Integer.toBinaryString(this.len),lenBits);
            binaryPos=Backend.addLeadingZeros(Integer.toBinaryString(this.pos),posBits);
            for(int i=0;i<this.dictSymbols.length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.toBinaryString(i),symbsBits);
                    break;
                }else{
                    binarySymb="";
                }
            }

            this.finalCode+=(binaryLen+binaryPos+binarySymb);

            Log.writeLog(String.format("%" + (dictSize) + "s", this.dictionary),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+1+(Math.min(dictSize,buffSize)/100)) + "d", this.len),false);
            Log.writeLog(String.format("%" + (3-1+2+String.valueOf(this.pos).length()) + "d", this.pos),false);
            Log.writeLog(String.format("%" + ((3-String.valueOf(this.pos).length())+2+1)+"s",this.letter),false);
            Log.writeLog(String.format("%" + ((6-1)+2+lenBits) + "s", binaryLen),false);
            Log.writeLog(String.format("%" + (1+posBits) + "s", binaryPos),false);//posBits might be = 0
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

    public String decodeLZ77(String inputString, int dictSize, int buffSize){
        String temp = Backend.hexToBinary(inputString);
        if(temp != null) inputString=temp;
        //System.out.println(inputString);
        String[][] code = codeBreakdown(inputString,dictSize,buffSize);

        this.dictSize=dictSize;
        this.dictSymbols=Backend.dict;
        Log.writeLog("Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int index=0;
        String binaryLen=code[0][0];
        String binaryPos=code[0][1];
        String binarySymb=code[0][2];

        int codeBlockLen = binaryLen.length()+binaryPos.length()+binarySymb.length();

        if(dictSize<4)
        {
            Log.writeLog(String.format("%" + "s", "C"),false);
            Log.writeLog(String.format("%" + ((codeBlockLen+2-1)+2+1) + "s", "D"),false);
            Log.writeLog(String.format("%" + ((dictSize-1)+2+3) + "s", "Len"),false);
        }else{
            Log.writeLog(String.format("%" + "s", "Code"),false);
            Log.writeLog(String.format("%" + ((codeBlockLen+2-4)+2+4) + "s", "Dict"),false);
            Log.writeLog(String.format("%" + ((dictSize-4)+2+3) + "s", "Len"),false);
        }
        Log.writeLog(String.format("%" + (2+3) + "s", "Pos"),false);
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+Math.max(dictSize,9)) + "s", "Substring"),false);
        Log.writeLog("",true);

        for(int i=0;i<code.length;i++){
            binaryLen=code[index][0];
            binaryPos=code[index][1];
            binarySymb=code[index][2];

            this.len=Integer.parseInt(binaryLen,2);
            this.pos=Integer.parseInt(binaryPos,2);
            try {
                this.letter = this.dictSymbols[Integer.parseInt(binarySymb,2)];
                if(this.letter==null) throw new Exception();
            }catch (Exception e){
                this.letter = "?";
            }

            //while decoding buff string used to represent substring (no need ?)'
            if(this.len!=0){
                this.buff= (this.dictionary.substring(this.pos-(dictSize-this.dictionary.length()),this.pos-(dictSize-this.dictionary.length())+this.len))+this.letter;
            }else this.buff=this.letter;

            this.finalCode += this.buff;

            Log.writeLog(String.format("%" + (binaryLen.length()) + "s", binaryLen),false);
            Log.writeLog(String.format("%" + (1+binaryPos.length()) + "s", binaryPos),false);
            Log.writeLog(String.format("%" + (1+binarySymb.length()) + "s", binarySymb),false);
            Log.writeLog(String.format("%" + (2+dictSize) + "s", this.dictionary),false);
            Log.writeLog(String.format("%" + (2+1+(dictSize/100)) + "d", this.len),false);
            Log.writeLog(String.format("%" + (3-1+2+String.valueOf(this.pos).length()) + "d", this.pos),false);
            Log.writeLog(String.format("%" + ((3-String.valueOf(this.pos).length())+2+1)+"s",this.letter),false);
            Log.writeLog(String.format("%" + ((6-1)+2+Math.max(dictSize,9)) + "s", this.buff),false);
            Log.writeLog("",true);

            index+=1;

            if(index+1+this.len<=dictSize){
                this.dictionary+=this.buff;
            }else{
                this.dictionary=(this.dictionary).substring(this.buff.length()-(dictSize-this.dictionary.length()))+this.buff;
            }
        }
        Log.writeLog("ANSWER: "+this.finalCode,true);
        return this.finalCode;
    }
}