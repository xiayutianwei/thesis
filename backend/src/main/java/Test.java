/**
 * Created by liuziwei on 2017/12/29.
 */
import java.util.*;
public class Test {
    static int min = Integer.MAX_VALUE;
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<List<String>>();
        List<String> now = new ArrayList<String>();
        int len =0;
        res.add(new ArrayList<String>(Arrays.asList(beginWord)));
        now.add(beginWord);
        boolean find = false;
        while(!find && len < wordList.size()){
            int n= res.size();
            System.out.println(n);
            System.out.println(now);
            for(int i=0;i<n;i++){
                if(now.get(i).equals(endWord)) {
                    find = true;
                    break;
                }
            }
            if(!find){
                for(int i=0;i<n;i++){
                    boolean first = true;
                    Set<String> cur = new HashSet<String>(res.get(i));
                    List<String> cur1 = new ArrayList<String>(res.get(i));
                    String nn = now.get(i);
                    System.out.println(cur);
                    System.out.println(nn);
                    for(String word:wordList){
                        if(!cur.contains(word) && onlyOne(word,nn)){
                            if(!first){
                                List<String> set = new ArrayList<String>(cur1);
                                set.add(word);
                                res.add(set);
                                now.add(word);
                            }else{
                                res.get(i).add(word);
                                now.set(i,word);
                                first = false;
                            }
                        }
                    }
                }
            }
            len++;
        }
        List<List<String>> res1= new ArrayList<List<String>>();
        if(find){
            for(int i=0;i<res.size();i++){
                if(now.get(i).equals(endWord)){
                    res1.add(new ArrayList<String>(res.get(i)));
                }
            }
            return res1;
        }else return res1;
    }
    public static void dfs(String now,String endWord,List<String> wordList,List<String> list,List<List<String>> res,boolean[] used){

        if(now.equals(endWord)){
            min = Math.min(min,list.size());
            res.add(new ArrayList<String>(list));
        }else{
            for(int i=0;i<wordList.size();i++){
                if(!used[i]){
                    used[i] = true;
                    if(onlyOne(wordList.get(i),now)){
                        list.add(wordList.get(i));
                        dfs(wordList.get(i),endWord,wordList,list,res,used);
                        list.remove(list.size()-1);
                    }
                    used[i] = false;
                }
            }
        }
    }
    public static boolean onlyOne(String a,String b){
        boolean has = false;
        for(int i=0;i<a.length();i++){
            if(a.charAt(i) != b.charAt(i)){
                if(has) return false;
                has = true;
            }
        }
        return has;
    }
    public static void main(String[] args){
        System.out.println(findLadders("cet",
                "ism",
                new ArrayList<String>(Arrays.asList("kid","tag","pup","ail","tun","woo","erg","luz","brr","gay","sip","kay","per","val","mes","ohs","now","boa","cet","pal","bar","die","war","hay","eco","pub","lob","rue","fry","lit","rex","jan","cot","bid","ali","pay","col","gum","ger","row","won","dan","rum","fad","tut","sag","yip","sui","ark","has","zip","fez","own","ump","dis","ads","max","jaw","out","btu","ana","gap","cry","led","abe","box","ore","pig","fie","toy","fat","cal","lie","noh","sew","ono","tam","flu","mgm","ply","awe","pry","tit","tie","yet","too","tax","jim","san","pan","map","ski","ova","wed","non","wac","nut","why","bye","lye","oct","old","fin","feb","chi","sap","owl","log","tod","dot","bow","fob","for","joe","ivy","fan","age","fax","hip","jib","mel","hus","sob","ifs","tab","ara","dab","jag","jar","arm","lot","tom","sax","tex","yum","pei","wen","wry","ire","irk","far","mew","wit","doe","gas","rte","ian","pot","ask","wag","hag","amy","nag","ron","soy","gin","don","tug","fay","vic","boo","nam","ave","buy","sop","but","orb","fen","paw","his","sub","bob","yea","oft","inn","rod","yam","pew","web","hod","hun","gyp","wei","wis","rob","gad","pie","mon","dog","bib","rub","ere","dig","era","cat","fox","bee","mod","day","apr","vie","nev","jam","pam","new","aye","ani","and","ibm","yap","can","pyx","tar","kin","fog","hum","pip","cup","dye","lyx","jog","nun","par","wan","fey","bus","oak","bad","ats","set","qom","vat","eat","pus","rev","axe","ion","six","ila","lao","mom","mas","pro","few","opt","poe","art","ash","oar","cap","lop","may","shy","rid","bat","sum","rim","fee","bmw","sky","maj","hue","thy","ava","rap","den","fla","auk","cox","ibo","hey","saw","vim","sec","ltd","you","its","tat","dew","eva","tog","ram","let","see","zit","maw","nix","ate","gig","rep","owe","ind","hog","eve","sam","zoo","any","dow","cod","bed","vet","ham","sis","hex","via","fir","nod","mao","aug","mum","hoe","bah","hal","keg","hew","zed","tow","gog","ass","dem","who","bet","gos","son","ear","spy","kit","boy","due","sen","oaf","mix","hep","fur","ada","bin","nil","mia","ewe","hit","fix","sad","rib","eye","hop","haw","wax","mid","tad","ken","wad","rye","pap","bog","gut","ito","woe","our","ado","sin","mad","ray","hon","roy","dip","hen","iva","lug","asp","hui","yak","bay","poi","yep","bun","try","lad","elm","nat","wyo","gym","dug","toe","dee","wig","sly","rip","geo","cog","pas","zen","odd","nan","lay","pod","fit","hem","joy","bum","rio","yon","dec","leg","put","sue","dim","pet","yaw","nub","bit","bur","sid","sun","oil","red","doc","moe","caw","eel","dix","cub","end","gem","off","yew","hug","pop","tub","sgt","lid","pun","ton","sol","din","yup","jab","pea","bug","gag","mil","jig","hub","low","did","tin","get","gte","sox","lei","mig","fig","lon","use","ban","flo","nov","jut","bag","mir","sty","lap","two","ins","con","ant","net","tux","ode","stu","mug","cad","nap","gun","fop","tot","sow","sal","sic","ted","wot","del","imp","cob","way","ann","tan","mci","job","wet","ism","err","him","all","pad","hah","hie","aim","ike","jed","ego","mac","baa","min","com","ill","was","cab","ago","ina","big","ilk","gal","tap","duh","ola","ran","lab","top","gob","hot","ora","tia","kip","han","met","hut","she","sac","fed","goo","tee","ell","not","act","gil","rut","ala","ape","rig","cid","god","duo","lin","aid","gel","awl","lag","elf","liz","ref","aha","fib","oho","tho","her","nor","ace","adz","fun","ned","coo","win","tao","coy","van","man","pit","guy","foe","hid","mai","sup","jay","hob","mow","jot","are","pol","arc","lax","aft","alb","len","air","pug","pox","vow","got","meg","zoe","amp","ale","bud","gee","pin","dun","pat","ten","mob"))));
//        List<Integer> a = new ArrayList<Integer>(Arrays.asList(1,2,3));
//        for(int i=0;i<a.size();i++){
//            System.out.println(a.get(i));
//            a.add(4);
//        }
        String a="";
        char[] s = a.toCharArray();
        StringBuilder sb = new StringBuilder(a);
        sb.replace(0,0,"");
        char c = 'a';
        String ss = c+"";
        String.valueOf(s);
        Set<String> set = new HashSet<String>();
        List<String> aa = new ArrayList<String>();
        
           }
}
