import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Backend extends Interface {
    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static String run(String algo, String mode, String input, int dictSize, int buffSize){
        String result="";

        Log.initialEntry();
        Log.writeLog("Input: "+input+"\n",true);
        Log.writeLog("Algorithm: "+algo+"\nMode: "+mode+"\n",true);

        if(Objects.equals(algo,"LZ77")) {
            if(Objects.equals(mode,"Encode")) {
                LZ77 lz77 = new LZ77("", "", 0, 0, "", "");
                result = lz77.encodeLZ77(input, dictSize, buffSize);
            }else{
                ;
            }
        }else{
            LZ78 lz78 = new LZ78(new String[dictSize],"", "", 0, "", "");
            result = lz78.encodeLZ78(input, dictSize, buffSize);
        }

        Log.writeLog("ANSWER:\n"+"bin: "+result+"\nhex: 0x"+binaryToHex(result),true);

        return result;
    }

    public static String[] createDict(String string){
        //create string without repeated symbols
        //create array with length as that string
        //insert symbols in
        //sort array
        //index of each symbols in binary equals to it "code"
        String individual=string.substring(0,1);
        for(int i=0;i<string.length();i++){
            int k=0;
            for(int n=0;n<individual.length();n++) {
                if (!Objects.equals(string.substring(i, i + 1), individual.substring(n, n + 1))) {
                    k += 1;
                }
            }
            if(k==individual.length()){
                individual+=string.substring(i,i+1);
            }
        }
        String[] dict = new String[individual.length()+1];
        for(int i=0;i<dict.length-1;i++){
            dict[i]=individual.substring(i,i+1);
        }
        dict[dict.length-1]="";
        //null symbol will be encoded
        Arrays.sort(dict);
        return dict;
    }

    public static String addLeadingZeros(int num,int finalLen){
        String zeros="";
        for(int i=0;i<finalLen-(Integer.toString(num)).length();i++){
            zeros+="0";
        }
        return (zeros+Integer.toString(num));
    }

    public static String binaryToHex(String binary){
        String ans="";
        String zeros="0000";
        for(int i=0;i<binary.length()/4;i++){
            if(i!=binary.length()/4-1){
                ans+=Integer.toHexString(Integer.parseInt(binary.substring(i*4,(i+1)*4),2));
            }else{
                ans+=Integer.toHexString(Integer.parseInt(binary.substring(i*4,(i+1)*4),2));
                String temp=binary.substring((i+1)*4);
                if (temp.length()!=0) {
                    ans += Integer.toHexString(Integer.parseInt(temp + zeros.substring(temp.length()),2));
                }
            }
            //System.out.println(i+") "+ans);
        }
        return ans;
    }
}