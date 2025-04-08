import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Backend extends Interface {
    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
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
        if (Objects.equals(ans[0],"") || ans[0].length()==1){
            ans[0]=elem;
        }else{
            ans[0]=buff.substring(Integer.parseInt(ans[2]),Integer.parseInt(ans[2])+1);
        }
//        System.out.print("ANSWER: ");
//        System.out.print(" "+ans[0]);
//        System.out.print(" "+ans[1]);
//        System.out.println(" "+ans[2]);
        return ans;
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
        String[] dict = new String[individual.length()];
        for(int i=0;i<dict.length;i++){
            dict[i]=individual.substring(i,i+1);
        }
        Arrays.sort(dict);
        Log.writeLog(" Dictionary: "+Arrays.toString(dict)+"\n\n",true);
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