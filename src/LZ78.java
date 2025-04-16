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

    private static void pop(String[] array, String elem){
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
    private static void push(String[] array,String elem){
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

    private static int findSubString(String buff, String[] dict){
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

    private String[][] codeBreakdown(String input, int dictSize){   //func to breakdown binary code
        String[][] result;

        int dictSubBlock = (int)Math.ceil(Math.log(dictSize)/Math.log(2));
        int letterSubBlock = (int)Math.ceil(Math.log((Backend.dict).length)/Math.log(2));
        int blockLen = dictSubBlock+letterSubBlock;

        int amountOfBlocks = (int)Math.floor(input.length()/blockLen);

        result = new String[amountOfBlocks][2];

        for(int i=0;i<result.length;i++){
            String temp = input.substring(i*blockLen,(i+1)*blockLen);
            result[i][0] = temp.substring(0,dictSubBlock);
            result[i][1] = temp.substring(dictSubBlock);
            //System.out.println(Arrays.toString(result[i]));
        }

        return result;
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

            binaryPos=Backend.addLeadingZeros(Integer.toBinaryString(this.pos),posBits);
            for(int i=0;i<(this.dictSymbols).length;i++){
                if(Objects.equals(this.letter,this.dictSymbols[i])){
                    binarySymb=Backend.addLeadingZeros(Integer.toBinaryString(i),symbsBits);
                    break;
                }else{
                    binarySymb="";
                }
            }

            this.finalCode+=(binaryPos+binarySymb);

            Log.writeLog(String.format("%" + (4*dictSize+2*(dictSize-1)+2) + "s", Arrays.toString(this.dictionary)),false);
            Log.writeLog(String.format("%" + (2+buffSize) + "s", this.buff),false);
            Log.writeLog(String.format("%" + (2+3+buffSize*2) + "s", this.foundString),false);
            Log.writeLog(String.format("%" + (2+String.valueOf(this.pos).length()) + "d", this.pos),false);
            Log.writeLog(String.format("%" + ((3-String.valueOf(this.pos).length())+2+1) + "s", this.letter),false);
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
        String temp = Backend.hexToBinary(inputString);
        if(temp != null) inputString=temp;
        //System.out.println(inputString);
        String[][] code = codeBreakdown(inputString,dictSize);

        this.dictSize=dictSize;
        this.dictSymbols=Backend.dict;
        Log.writeLog("Dictionary: "+ Arrays.toString(this.dictSymbols)+"\n\n",true);

        int index=0;
        String binaryPos=code[0][0];
        String binarySymb=code[0][1];

        int codeBlockLen = binaryPos.length()+binarySymb.length();

        if(dictSize<4)
        {
            Log.writeLog(String.format("%" + "s", "C"),false);
            Log.writeLog(String.format("%" + ((codeBlockLen+1-1)+2+1) + "s", "D"),false);
            Log.writeLog(String.format("%" + (((4*dictSize+2*(dictSize-1)+2)-1)+2+3) + "s", "Pos"),false);
        }else{
            Log.writeLog(String.format("%" + "s", "Code"),false);
            Log.writeLog(String.format("%" + (Math.max(0,codeBlockLen+1-4)+2+4) + "s", "Dict"),false);
            Log.writeLog(String.format("%" + (((4*dictSize+2*(dictSize-1)+2)-4)+2+3) + "s", "Pos"),false);
        }
        Log.writeLog(String.format("%" + (2+6) + "s", "Letter"),false);
        Log.writeLog(String.format("%" + (2+Math.max(dictSize,9)) + "s", "Substring"),false);
        Log.writeLog("",true);

        for(int i=0;i<code.length;i++){
            binaryPos=code[index][0];
            binarySymb=code[index][1];

            this.pos=Integer.parseInt(binaryPos,2);
            try {
                this.letter = this.dictSymbols[Integer.parseInt(binarySymb,2)];
                if(this.letter==null) throw new Exception();
            }catch (Exception e){
                this.letter = "?";
            }

            //while decoding buff string used to contain current substring (no need ?)'
            if(this.pos==0){
                this.buff=this.letter;
            }else{
                this.foundString=this.dictionary[this.pos-1];
                if(this.foundString==null) this.foundString="?";
                this.buff=this.foundString+this.letter;
            }

            this.finalCode += this.buff;

            Log.writeLog(String.format("%" + (binaryPos.length()) + "s", binaryPos),false);
            Log.writeLog(String.format("%" + (1+binarySymb.length()) + "s", binarySymb),false);
            Log.writeLog(String.format("%" + (Math.max(4-codeBlockLen+1,0)+2+(4*dictSize+2*(dictSize-1)+2)) + "s", Arrays.toString(this.dictionary)),false);
            Log.writeLog(String.format("%" + (2+String.valueOf(this.pos).length()) + "d", this.pos),false);
            Log.writeLog(String.format("%" + ((3-String.valueOf(this.pos).length())+2+1)+"s",this.letter),false);
            Log.writeLog(String.format("%" + ((6-1)+2+Math.max(dictSize,9)) + "s", this.buff),false);
            Log.writeLog("",true);

            if(this.pos==0){
                push(this.dictionary,this.letter);
            }else{
                pop(this.dictionary,this.foundString);
                push(this.dictionary,this.foundString);
                push(this.dictionary,this.foundString+this.letter);
            }


            index+=1;
        }
        Log.writeLog("ANSWER: "+this.finalCode,true);

        return this.finalCode;
    }
}
