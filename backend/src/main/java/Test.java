/**
 * Created by liuziwei on 2017/12/29.
 */
import java.util.*;
public class Test {

    public static void main(String[] args){
//        System.out.println(getResult(210));
        LinkedList<String> a = new LinkedList<>();
        TreeMap<Integer,Integer> m = new TreeMap<>();
        TreeSet<Integer> s = new TreeSet<>(Arrays.asList(1,2,3,4));
        System.out.println(s.floor(4));
        System.out.println(s.lower(4));
        m.floorKey(0);
        a.forEach((String s1) -> System.out.print(s));
    }
    public static int getResult(int n) {
        // write code here
        int[] r= new int[n+1];
        int round = 2;
        int start = 1;
        int N = n;
        while(n!=1){
            int now = start;
            int i=1;
            int nn = n;
            System.out.println(now + "  " +n);
            while(i<=nn){
                if(r[now] != -1){
                    System.out.println(now+"---");
                    if(i % round !=1){
                        r[now]=-1;
                        n--;
                    }
                    i++;
                }
                start = now;
                now = now==N? 1:now+1;
            }
            if(r[start]==-1){
                while(r[start]==-1) start = start==1? N:start-1;
            }
            round++;
        }
        return start;
    }

    public static int Comlen(String str1, String str2)
    {
        int i = 0;
        while(i < str2.length() && i<str1.length() && (str1.charAt(i) == str2.charAt(i)))
        ++i;
        return i;
    }
    //char *a[11];
    public static String MaxLength(String str)
    {
        if(str.length() == 0)
            return "";
        int maxLen = 0;
        String maxString = "";
        int len = str.length();
        String[] a = new String[len];
        for (int i = 0; i < len ; ++i)
            a[i] = str.substring(i,len);
        for(int i=0;i<len;i++) System.out.println(a[i]);
        Arrays.sort(a, String::compareTo);
        for (int i = 0; i < len - 1; ++i)
            if(Comlen(a[i], a[i+1]) > maxLen) {
                maxLen = Comlen(a[i], a[i + 1]);
                maxString = a[i].substring(0,maxLen);
            }
        return maxString;
    }
}
