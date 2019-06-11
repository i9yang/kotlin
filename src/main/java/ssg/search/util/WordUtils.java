package ssg.search.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class WordUtils{
	enum KorType{
		Chosung{
			int getCode(String s){
				int index = "rRseEfaqQtTdwWczxvg".indexOf(s);
				if(index != -1){
					return index * 21 * 28;
				}
				return -1;
			}
		},
		Jungsung{
			int getCode(String s){
				String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
				for(int i = 0; i < mid.length; i++){
			    	if(mid[i].equals(s)){
			    		return i * 28;
			    	}
			    }
				return -1;
			}
		},
		Jongsung{
			int getCode(String s){
				String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
				for(int i = 0; i < fin.length; i++){
			    	if(fin[i].equals(s)){
			    		return i + 1;
			    	}
			    }
				return -1;
			}
		};
		abstract int getCode(String s);
	}
	/**
     * 영문자 치환(오타보정)
     */
	public static String rplcTypoErr(String query){
		// sjsj, spao, theory 에 대해서는 escape 처리함
		if(query.equalsIgnoreCase("sjsj")||
				query.equalsIgnoreCase("spao")||
				query.equalsIgnoreCase("theory")
				){
			return "";
		}
		if(query.replaceAll("\\s", "").matches("^[0-9a-zA-Z]+$")){
			String retString = rplcTest(query);
			// 결과가 없는 경우 대소문자 서로 바꾼후 한번더
			if(retString.equals("")){
				StringBuilder sb = new StringBuilder();
				// 대소문자 서로 변환
				for(char c : query.toCharArray()){
					if(Character.isUpperCase(c)){//대문자
						sb.append(Character.toLowerCase(c));
					}else if(Character.isLowerCase(c)){//소문자
						sb.append(Character.toUpperCase(c));
					}else{
						sb.append(c);
					}
				}
				return rplcTest(sb.toString());
			}else{
				return retString;
			}
		}
		return "";
	}
    public static String rplcTest(String query){
	    try{
		    StringBuilder sb = new StringBuilder();
		    List<String> resultList = Lists.newArrayList();
		    int index = 0;
		    for(Iterator<String> token = Splitter.on(" ").trimResults().split(query).iterator();token.hasNext();){
			    String str = token.next();
			    int[]  structure = new int[str.length()];
			    if(index!=0)resultList.add(" ");
			    int cho  = 0;
			    int jung = 0;
			    int jong = 0;

			    for(int i = 0; i < str.length(); i++){
				    char c = str.charAt(i);
				    // 중성의 경우 선행하는 글자가 무조건 초성이다.
				    if(KorType.Jungsung.getCode(String.valueOf(c))!=-1){
					    structure[i-1] = 1;
					    if(i+2 <= str.length() && KorType.Jungsung.getCode(str.substring(i,i+2))!=-1){
						    structure[i]   = 2;
						    structure[i+1] = 2;
						    i++;
					    }else{
						    structure[i]   = 2;
					    }
				    }
			    }

			    // 초성으로 시작되지 않으면 안됨(방어로직)
			    if(structure[0] != 1){
				    return "";
			    }


			    // 만들어진 어순의 Structure를 확인하고,초/중/종성에 맞도록 ascii code를 더해서 리턴한다.
			    for(int i=0;i<structure.length;i++){
				    int x = structure[i];
				    if(x == 1){
					    cho = KorType.Chosung.getCode(String.valueOf(str.charAt(i)));
					    if(cho == -1){
						    return "";
					    }
				    }
				    else if(x == 2){
					    if(i+1 < structure.length && structure[i+1] == 2){
						    jung = KorType.Jungsung.getCode(str.substring(i,i+2));
						    i++;
						    if(jung == -1){
							    return "";
						    }
					    }else{
						    jung = KorType.Jungsung.getCode(String.valueOf(str.charAt(i)));
						    if(jung == -1){
							    return "";
						    }
					    }
				    }
				    else if(x == 0){
					    if(i+1 < structure.length && structure[i+1] == 0){
						    jong = KorType.Jongsung.getCode(str.substring(i,i+2));
						    if(jong == -1){
							    return "";
						    }
						    i++;
					    }else{
						    jong = KorType.Jongsung.getCode(String.valueOf(str.charAt(i)));
						    if(jong == -1){
							    return "";
						    }
					    }
				    }
				    if( ( i+1 < structure.length && structure[i+1] == 1 ) || i+1 == structure.length){
					    resultList.add(String.valueOf((char)(0xAC00 + cho + jung + jong)));
					    cho  = 0;
					    jung = 0;
					    jong = 0;
				    }
			    }
			    index++;
		    }
		    for(String str : resultList){
			    sb.append(str);
		    }
		    return sb.toString();
	    }catch(Exception e){
		    return "";
	    }
    }

}
