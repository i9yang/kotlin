	package ssg.search.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Targets;
import ssg.search.parameter.Parameter;

import java.util.List;
import java.util.StringTokenizer;

public class CollectionUtils {
	
	public static Targets getTargets(Parameter parameter){
		String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "all").toUpperCase();
		Targets targets = Targets.ALL;
		try{
			targets = Targets.valueOf(strTarget);
		} catch(IllegalArgumentException e){}
		return targets;
	}
	public static List<Collection> asList(Collection...collections){
		List<Collection> returnList = Lists.newArrayList();
		for(Collection collection : collections){
			returnList.add(collection);
		}
		return returnList;
	}
	public static boolean containsTarget(Targets current, Targets...expects){
		for(Targets expect : expects){
			if(expect.equals(current)){
				return true;
			}
		}
		return false;
	}
	public static String replaceForced(String strQuery){
		if(strQuery.equals("크린랲")){
			return "크린랩";
		}else if(strQuery.equals("케챱")){
			return "케찹";
		}else if(strQuery.equals("케잌")){
			return "케이크";
		}else if(strQuery.equals("랲")){
			return "랩";
		}else if(strQuery.equals("웻에이징")){
			return "wet에이징";
		}else if(strQuery.equals("똠양꿍") || strQuery.equals("똠양쿵") || strQuery.equals("똠얌쿵") || strQuery.equals("똠얌꿍")){
			return "톰얌쿵";
		}
		return strQuery;
	}

	public static String getCommonQuery(Parameter parameter){
		String strQuery = StringUtils.defaultIfEmpty(parameter.getQuery(), "");
        String strInclude = StringUtils.defaultIfEmpty(parameter.getInclude(), "");
        String strExclude = StringUtils.defaultIfEmpty(parameter.getExclude(), "");
        String strReplace = StringUtils.defaultIfEmpty(parameter.getReplaceQuery(), "");

        // 검색어 조합

        // 1. 치환키워드가 있을시 검색어는 치환키워드로
        if(!strReplace.equals("")){
            strQuery = strReplace;
        }
        // 2. 결과내 검색어 생성
        if(!strInclude.equals("")){
            StringBuilder sb = new StringBuilder();
            sb.append(strQuery);
            for(StringTokenizer st = new StringTokenizer(strInclude,"|");st.hasMoreTokens();){
                String t = st.nextToken();
                sb.append(" ").append(t);
            }
            strQuery = sb.toString();
        }
        // 3. 제외 키워드 생성
        if(!strExclude.equals("")){
            StringBuilder sb = new StringBuilder();
            sb.append(strQuery);
            for(StringTokenizer st = new StringTokenizer(strExclude,"|");st.hasMoreTokens();){
                String t = st.nextToken();
                sb.append(" !").append(t);
            }
            strQuery = sb.toString();
        }

        // 4. 태그를 사용한 검색어에서 #이 들어가면 오동작이 난다. # 제거
		if(strQuery.startsWith("#")){
        	strQuery = strQuery.replaceFirst("#", "");
		}

		return strQuery;
	}
	public static String getOriQuery(Parameter parameter){
		String strReplace = StringUtils.defaultIfEmpty(parameter.getReplaceQuery(), "");
		String strOriQuery = StringUtils.defaultIfEmpty(parameter.getQuery(), "");
		if(!strReplace.equals("")){
		    strOriQuery = strReplace;
		}
		return strOriQuery.replaceAll("\\p{Space}", "");
	}
	public static Info getCollectionRanking(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		if(strSiteNo.equals("6005")){
			return new Info("6005".concat("/").concat(CollectionUtils.getOriQuery(parameter)));
		}else if(strSiteNo.equals("6004") || strSiteNo.equals("6009")){
			return new Info("6004".concat("/").concat(CollectionUtils.getOriQuery(parameter)));
		}else if(strSiteNo.equals("6001") || strSiteNo.equals("6002")){  //@ BOOT개발시확인 
			return new Info("6001".concat("/").concat(CollectionUtils.getOriQuery(parameter)));
		}else{
			return null;
		}
	}
	public static Info getCategoryBoosting(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		if(strSiteNo.equals("6005")){
			return new Info("SSGBOOST");
		}else if(strSiteNo.equals("6004") || strSiteNo.equals("6009")){
			return new Info("SHINBOOST");
		}else if(strSiteNo.equals("6001") || strSiteNo.equals("6002")){   //@ BOOT개발시확인 
			return new Info("EMARTBOOST");
		}
		return null;
	}
	public static Info getPageInfo(String strPage, String strCount){
		int start = 0;
        int count = 0;
        try{
            start = Integer.parseInt(strPage);
            count = Integer.parseInt(strCount);
            start =  count * (start-1);
        }catch(NumberFormatException ne){}
        return new Info(start, count);
	}
	public static int getCategoryNextLevel(Parameter parameter){
		if(( parameter.getTarget().equalsIgnoreCase("all") || parameter.getTarget().equalsIgnoreCase("book")) && StringUtils.isEmpty(parameter.getCtgId()) && StringUtils.isEmpty(parameter.getCtgLv())){
			return 0;
		}else{
			String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
			String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
			if(ctgLv.equals("1")){
				return 3;
			}else if(ctgLv.equals("2")){
				if(ctgLast.equals("Y")){
					return 0;
				}
				return 4;
			}else if(ctgLv.equals("3")){
				if(ctgLast.equals("Y")){
					return 4;
				}
				return 0;
			}else if(ctgLv.equals("4")){
				return 0;
			}
		}
		return 0;
	}
	public static int getCategoryCurrentLevel(Parameter parameter){
		if(( parameter.getTarget().equalsIgnoreCase("all") || parameter.getTarget().equalsIgnoreCase("book")) && StringUtils.isEmpty(parameter.getCtgId()) && StringUtils.isEmpty(parameter.getCtgLv())){
			return 0;
		}else{
			String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
			String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
			if(ctgLv.equals("1")){
				return 2;
			}else if(ctgLv.equals("2")){
				if(ctgLast.equals("Y")){
					return 2;
				}
				return 3;
			}else if(ctgLv.equals("3")){
				if(ctgLast.equals("Y")){
					return 3;
				}
				return 4;
			}else if(ctgLv.equals("4")){
				return 4;
			}
		}
		return 0;
	}
	public static Info getCategoryGroupByLevel(Parameter parameter){
		String strTarget = parameter.getTarget();
		String strSiteNo = parameter.getSiteNo();
		String strCtgIdxNm = "DISP_CTG_IDX";
		if(strSiteNo.equals("6005")){
			strCtgIdxNm = "SCOM_DISP_CTG_IDX";
		}else if((strSiteNo.equals("6001")||strSiteNo.equals("6002")) && ( strTarget.equalsIgnoreCase("all")||strTarget.equalsIgnoreCase("category") )){  //@ BOOT개발시확인 
			if(StringUtils.isEmpty(parameter.getCtgId())){
				strCtgIdxNm = "DISP_CTG_IDX,TEM_DISP_CTG_IDX";
			}else{
				if(StringUtils.isEmpty(parameter.getThemeYn())||parameter.getThemeYn().equals("N")){
					strCtgIdxNm = "DISP_CTG_IDX";
				}else{
					strCtgIdxNm = "TEM_DISP_CTG_IDX";
				}
			}
		}
		if(( strTarget.equalsIgnoreCase("all") || strTarget.equalsIgnoreCase("book") || strTarget.equalsIgnoreCase("category")) && StringUtils.isEmpty(parameter.getCtgId()) && StringUtils.isEmpty(parameter.getCtgLv())
				){
			return new Info(strCtgIdxNm,"1,2,3");
		}else{
			String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
			String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
			if(ctgLv.equals("1")){
				return new Info(strCtgIdxNm,"2,3");
			}else if(ctgLv.equals("2")){
				if(ctgLast.equals("Y")){
					return new Info(strCtgIdxNm,"2,3");
				}
				return new Info(strCtgIdxNm,"3,4");
			}else if(ctgLv.equals("3")){
				if(ctgLast.equals("Y")){
					return new Info(strCtgIdxNm,"3,4");
				}
				return new Info(strCtgIdxNm,"4");
			}else if(ctgLv.equals("4")){
				return new Info(strCtgIdxNm,"4");
			}
		}
		return new Info(strCtgIdxNm,"1,2");
	}
	public static Info getCategoryGroupBy(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		return new Info(ctgIdxNm,"1");
	}
	public static Info getMobileCategoryGroupBy(Parameter parameter, int maxlevel){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		if(maxlevel == 4){
			return new Info(ctgIdxNm,"1,2,3,4");
		}else if(maxlevel == 3){
			return new Info(ctgIdxNm,"1,2,3");

		}else if(maxlevel == 2){
			return new Info(ctgIdxNm,"1,2");

		}else if(maxlevel == 1){
			return new Info(ctgIdxNm,"1");
		}
		return new Info(ctgIdxNm,"1,2,3,4");
	}
	public static Info getRecomCategoryGroupBy(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		return new Info(ctgIdxNm,"3");
	}
	public static Info getGlobalCategoryGroupBy(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"TEM_DISP_CTG_IDX";
		String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
		String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
		String lev = "1";
		
		if(ctgLv.equals("1")) lev = "1,2,3";
        if(ctgLv.equals("2")){
        	lev = "1,2,3";
        }
        if(ctgLv.equals("3")){
            if(ctgLast.equals("Y")){
            	lev = "3";
            }else lev="3,4";
        }
        if(ctgLv.equals("4")){
        	lev="4";
        }
        else lev ="1,2,3";
		
		return new Info(ctgIdxNm,lev);
	}
	
	public static String getShppPrefix(Parameter parameter){
		StringBuilder sb 		= new StringBuilder();
		String strShpp 			= StringUtils.defaultIfEmpty(parameter.getShpp(), "");
		String strPickuSalestr 	= StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
		if(!strShpp.equals("")){
			// 여러개 토큰 처리 ( 매직픽업 퀵배송 제외 )
			if(strShpp.indexOf("|") >-1){
				sb.append("<SHPP:contains:");
				String[] s = strShpp.split("\\|");
				for(int i=0; i < s.length; i++){
					String s1 = s[i].toUpperCase();
					if(!s1.equals("QSHPP") && !s1.equals("PICKU")){
						sb.append(s1).append("|");
					}
				}
				// 아무것도 매핑안된 경우 를 제외 ( 파라메터에 매직픽업, 퀵배송 만 들어가 있는 경우 회피 )
				if(!sb.toString().equals("<SHPP:contains:")){
					sb.deleteCharAt(sb.lastIndexOf("|")).append(">");
				}else{
					sb = new StringBuilder();
				}
			}
			// 한개 토큰 중 매직픽업이나 퀵배송이 없을 시
			else if(strShpp.toUpperCase().indexOf("QSHPP") < 0 && strShpp.toUpperCase().indexOf("PICKU") < 0){
				sb.append("<SHPP:contains:").append(strShpp.toUpperCase()).append(">");
			}
			// 무조건 매직픽업 퀵배송은 따로 AND 로 처리한다.
			if(strShpp.toUpperCase().indexOf("QSHPP") >-1){
				sb.append("<SHPP:contains:QSHPP>");
			}
			if(strShpp.toUpperCase().indexOf("PICKU") >-1){
				// 매직픽업 점포가 있을 시
				if(!strPickuSalestr.equals("")){
					sb.append("<SHPP:contains:PICKU"+strPickuSalestr+">");
				}else{
					sb.append("<SHPP:contains:PICKU>");
				}
			}
		}
		return sb.toString();
	}
	
	public static Info getAdPageInfo(String strPage, String strCount, int adCount){
		int start = 0;
        int count = 0;
        try{
            start = Integer.parseInt(strPage);
            count = Integer.parseInt(strCount);
            start =  count * (start-1);
            
            if (start > adCount && count > adCount && adCount > 0) {
            	start =  start-adCount;
            	count =  count+adCount;
            }          
            
        }catch(NumberFormatException ne){}
        return new Info(start, count);
	}
	
	public static boolean isAdSearch(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
		String loadLevel = StringUtils.defaultIfEmpty(parameter.getLoadLevel(), "10");
		boolean isAdSearch = false;
		
		if ("Y".equals(parameter.getAdYn()) 
					&& ( 
							( strSiteNo.equals("6004") || strSiteNo.equals("6009") 
									|| (strSiteNo.equals("6001") && ("".equals(strFilterSiteNo) || "6001".equals(strFilterSiteNo)) ) 
									|| (strSiteNo.equals("6002") && ("".equals(strFilterSiteNo) || "6001".equals(strFilterSiteNo)) )
									|| (strSiteNo.equals("6005") && ("".equals(strFilterSiteNo) || "6001".equals(strFilterSiteNo) || "6004".equals(strFilterSiteNo)) )
							)
						)
					&& (StringUtils.isEmpty(parameter.getBrand()) && StringUtils.isEmpty(parameter.getBenefit()) && StringUtils.isEmpty(parameter.getCls()) 
							&& StringUtils.isEmpty(parameter.getClsFilter()) && StringUtils.isEmpty(parameter.getCtgId()) && StringUtils.isEmpty(parameter.getSize())
							&& StringUtils.isEmpty(parameter.getMinPrc()) && StringUtils.isEmpty(parameter.getMaxPrc()) && StringUtils.isEmpty(parameter.getShpp())   
							&& (StringUtils.isEmpty(parameter.getSort()) || StringUtils.equalsIgnoreCase("best", parameter.getSort()))
							)
					&& (StringUtils.equals(parameter.LOAD_LEVEL_GENERAL, loadLevel) 
							|| StringUtils.equals(parameter.LOAD_LEVEL_WARNING, loadLevel)
									|| StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, loadLevel)
							)
			){
				isAdSearch = true;
			}
		
		return isAdSearch;
	}

	public static boolean isRecommendSearch(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String loadLevel = StringUtils.defaultIfEmpty(parameter.getLoadLevel(), "10");
		boolean isRecommendSearch = false;
		
		if( "Y".equals(parameter.getRecommendYn()) 
					&& ( 
							( strSiteNo.equals("6004") || strSiteNo.equals("6001") || strSiteNo.equals("6004") || strSiteNo.equals("6009") || strSiteNo.equals("6005"))
						)
					&& (StringUtils.equals(parameter.LOAD_LEVEL_GENERAL, loadLevel) 
							|| StringUtils.equals(parameter.LOAD_LEVEL_WARNING, loadLevel)
									|| StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, loadLevel)
							)
			){
				isRecommendSearch = true;
			}
		
		return isRecommendSearch;
	}
	
	public static String getDivPipe(){
		return "==================================================================================================================================================================================";
	}
}
