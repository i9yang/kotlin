package ssg.search.query;

import QueryAPI510.Search;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.constant.COLLECTION;
import ssg.search.constant.SITE;
import ssg.search.constant.SORT;
import ssg.search.constant.TARGET;
import ssg.search.matcher.PrefixStringMatcher;
import ssg.search.matcher.SuffixStringMatcher;
import ssg.search.parameter.Parameter;
import ssg.search.parameter.ParameterUtil;
import ssg.search.result.*;
import ssg.search.util.CollectionUtils;

import java.util.*;

public class WiseQueryBuilder implements QueryBuilder{
	private Logger logger = LoggerFactory.getLogger(WiseQueryBuilder.class);
	private Parameter parameter;
	private Search search;
	private ParameterUtil parameterUtil = new ParameterUtil();
	private StringBuilder requestQuery = new StringBuilder();
	private boolean debug;
	private String url = "";
	private String port = "";
	
	final static String[] SkipWords = {
			".co","가격","가격비교","공구","공동구매","기획전","리본데이","매장","먹는방법","몰","방문판매","브랜드","사용법","사이즈"
			,"사이트","상설","상설매장","상설할인매장","상품","색상","샘플","세일","세일기간","세탁","쇼핑몰","쇼핑물","스토어","시즌오프"
			,"신상","신형가격","싼브랜드","아울렛","어패럴","온라인","온라인매장","온라인스토어","이월","이월상품","재고처리","재배방법"
			,"정기세일","종류","중고","직구","직수입매장","착샷","추천","코리아","특가","파는곳","판매","학생할인","한정판매","할인매장","핫딜","홈쇼핑","효능"
			,"할인","크기","신상가방","상설할인","인기상품","사이즈표"
	};
	
	final static String[] ActionWords = {
			 "매직픽업" /* 매직픽업 필터링 */
			,"점포상품" /* 점포예약 + 점포택배 (ITEM_REG_DIV_CD : 20) */
			,"점포예약"	/* 점포예약 상품만 SHPP_TYPE_DTL_CD : 11 */
			,"점포택배" /* 점포택배  SHPP_TYPE_DTL_CD : 2N */
			,"1+1"
			,"2+1"
			,"3+1"
			,"쓱배송"
	};
	
	final static String[] SizeDenyWords = {
			 "가방","글러브","기능성티","기모","긴팔티","남자티","내의","니트글러브","드라이핏","라운드티","롱티","맨투맨티"
			,"바람막이","바지","반팔티","백팩","상의","셔츠","속옷","수영복","운동복","웨어","윈드러너","의류","저지","정장","져지","조끼","짐볼"
			,"집업","짚업","침구","카라티","캡","크루티","트레이닝","트레이닝바지","트레이닝복","트레이닝복세트","티셔츠","패딩","팬티","플리스"
			,"하의","후드집업","후드티"
	};
	
	final static SuffixStringMatcher SkipWordSuffixMatcher 		= new SuffixStringMatcher(Lists.newArrayList(SkipWords));
	final static PrefixStringMatcher ActionWordPrefixMatcher   = new PrefixStringMatcher(Lists.newArrayList(ActionWords));
	final static SuffixStringMatcher ActionWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(ActionWords));
	final static SuffixStringMatcher SizeDenyWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(SizeDenyWords));

	public void set(Parameter parameter) {
		this.parameter = parameter;
		this.search = new Search();
		this.url = parameter.getUrl();
		this.port = parameter.getPort();

		// 기본적인 Set START
		search.w3SetCodePage("utf-8");
		
		// 엔진에 요청될 검색어 생성
		String strQuery = parameterUtil.getCommonQuery(parameter);
		String strReplace = StringUtils.defaultIfEmpty(parameter.getReplaceQuery(), "");
		String strOriQuery = StringUtils.defaultIfEmpty(parameter.getQuery(), "").replaceAll("\\p{Space}", "");

		// 확장형 검색어 강제치환
		strQuery = CollectionUtils.replaceForced(strQuery);

		if(!strReplace.equals("")){
		    strOriQuery = strReplace.replaceAll("\\p{Space}", "");
		}
		// 모든 Query에 대해 Log를 남기도록 수정하였음
		search.w3SetQueryLog(1);
		
		// 예약어 필터링 작업
		String prefixQuery = StringUtils.defaultIfEmpty(ActionWordPrefixMatcher.longestMatch(strQuery),"");
		String suffixQuery = StringUtils.defaultIfEmpty(ActionWordSuffixMatcher.longestMatch(strQuery),"");
		
		boolean isFilteringYn = false;
		
		// Rule 1. Suffix 와 Prefix 에 둘다 결과가 있는 경우 작동하지 않음
		if(!prefixQuery.equals("") && !suffixQuery.equals("")){
		}
		// Rule 2. Prefix 에 결과가 있는 경우
		if(!prefixQuery.equals("")){
			if(prefixQuery.indexOf("+")>-1){
				prefixQuery = prefixQuery.replaceAll("\\+", "\\\\+");
			}
			String tempQuery = strQuery.replaceFirst(prefixQuery, "").trim();
			if(!tempQuery.equals("")){
				search.w3SetCommonQuery(tempQuery, 0);
				if(prefixQuery.equals("매직픽업")){
					parameter.setShpp("picku");
				}else if(prefixQuery.equals("점포상품")){
					parameter.setShpp("store");
				}else if(prefixQuery.equals("점포예약")){
					parameter.setShpp("rsvt");
				}else if(prefixQuery.equals("점포택배")){
					parameter.setShpp("pack");
				}else if(prefixQuery.equals("쓱콘")){
					parameter.setShpp("con");
				}else if(prefixQuery.equalsIgnoreCase("1+1")){
					parameter.setFilter("1PLUZ");
				}else if(prefixQuery.equalsIgnoreCase("2+1")){
					parameter.setFilter("2PLUZ");
				}else if(prefixQuery.equalsIgnoreCase("3+1")){
					parameter.setFilter("3PLUZ");
				}else if(prefixQuery.equalsIgnoreCase("쓱배송")){
					parameter.setShpp("ssgem");
				}
				isFilteringYn = true;
			}
		}
		
		// Rule 3. Suffix 에 결과가 있는 경우
		if(!suffixQuery.equals("")){
			if(suffixQuery.indexOf("+")>-1){
				suffixQuery = suffixQuery.replaceAll("\\+", "\\\\+");
			}
			String tempQuery = replaceLast(strQuery,suffixQuery,"").trim();
			if(!tempQuery.equals("")){
				search.w3SetCommonQuery(tempQuery, 0);
				if(suffixQuery.equals("매직픽업")){
					parameter.setShpp("picku");
				}else if(suffixQuery.equals("점포상품")){
					parameter.setShpp("store");
				}else if(suffixQuery.equals("점포예약")){
					parameter.setShpp("rsvt");
				}else if(suffixQuery.equals("점포택배")){
					parameter.setShpp("pack");
				}else if(suffixQuery.equals("쓱콘")){
					parameter.setShpp("con");
				}else if(suffixQuery.equalsIgnoreCase("1+1")){
					parameter.setFilter("1PLUZ");
				}else if(suffixQuery.equalsIgnoreCase("2+1")){
					parameter.setFilter("2PLUZ");
				}else if(suffixQuery.equalsIgnoreCase("3+1")){
					parameter.setFilter("3PLUZ");
				}else if(suffixQuery.equalsIgnoreCase("쓱배송")){
					parameter.setShpp("ssgem");
				}
				isFilteringYn = true;
			}
		}


		if(!strQuery.equals("") && !isFilteringYn)search.w3SetCommonQuery(strQuery, 0);

		TARGET target = parameterUtil.getTarget(parameter);
		SITE   site   = parameterUtil.getSite(parameter);
		SORT   sort   = parameterUtil.getSort(parameter);
		FrontUserInfo userInfo = parameter.getUserInfo();
		// sessionInfo를 set 한다.
		search.w3SetSessionInfo("oldlibdetect",StringUtils.defaultIfEmpty(parameter.getTarget(),""),StringUtils.defaultIfEmpty(parameter.getSiteNo(),""));

		// Target 이 all 이면서 sortVo 가 있는 케이스
		if(target.equals(TARGET.ALL) && parameter.getSort()!=null && !parameter.getSort().equals("")){
			logger.info("Search Crawler Detected {} / {} / {} / {}", userInfo.getCkWhere(), userInfo.getPcId(), userInfo.getCurrentUrl(), userInfo.getRemoteAddress());
		}

	    // 모든 Query에 대해 Log를 남기도록 수정하였음
		search.w3SetQueryLog(1);

		// 하이라이트 구문 관련 세팅
		boolean highLightYn = false;
		String strHighLightYn = StringUtils.defaultIfEmpty(parameter.getHighlight(), "N");
		if(strHighLightYn.equals("Y")){
			highLightYn = true;
		}

		// 기본적인 Set END
		for( COLLECTION collection : parameterUtil.getCollectionList(parameter)){
			String name = collection.getCollectionAliasName(parameter);
			// FAQ, NOTICE, MAGAZINE, EVENT 컬렉션을 만났을 경우 하이라이트를 오픈
			if(name.equals("faq")||name.equals("notice")||name.equals("event")){
				highLightYn = true;
			}

			if((name.equals("person")||name.equals("person_item")) && !StringUtils.defaultIfEmpty(userInfo.getLoginYn(), "N").equals("Y")){
				continue;
			}
			// Alias 여부를 파악해서 Alias or Collection Add
			if(!collection.useAlias()){
				search.w3AddCollection(name);
			}else{
				search.w3AddAliasCollection(name, collection.getCollectionRealName(parameter));
			}

			//	조회에 사용되는 DocumentField Set
			search.w3SetDocumentField(name, collection.getDocumentField(parameter));
			// StringTokenizer를 이용하여, 검색 조건에 사용되는 SearchField Set
			for(StringTokenizer st = new StringTokenizer(collection.getSearchField(parameter),",");st.hasMoreTokens();){
				search.w3AddSearchField(name, st.nextToken());
			}

			if(parameter.getPage()!=null){
			    String strPage  = parameterUtil.getPage(parameter);
                String strCount = parameterUtil.getCount(parameter);

                int start = 0;
                int count = 0;

                try{
                    start = Integer.parseInt(strPage);
                    count = Integer.parseInt(strCount);
                    start =  count * (start-1);
                }catch(NumberFormatException ne){ ne.printStackTrace(); }

                // Paging
                if(name.equals("category")||name.equals("mall_disp")
                		||name.equals("book_mall_disp")||name.equals("brand_total")||name.equals("book_brand_total")
                		||name.equals("brand_mall_disp")||name.equals("book_brand_mall_disp")){
                    search.w3SetPageInfo(name, 0, 1);
                }else if(name.equals("person_item")||name.equals("person")){
                	search.w3SetPageInfo(name, 0, collection.getDefaultItemCount(parameter));
                }else{
                    // 타겟 ALL 인 경우
                    if(target.equals(TARGET.ALL)){
                        search.w3SetPageInfo(name, start, collection.getDefaultItemCount(parameter));
                    }else{
						if(target.equals(TARGET.MOBILE) && (site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT))){
                    		if(!name.equalsIgnoreCase("book")){
                    			search.w3SetPageInfo(name, start, count);
                    		}else{
                    			search.w3SetPageInfo(name, 0, count);
                    		}
                    	}else{
                    		if(target.equals(TARGET.ITEM)||target.equals(TARGET.CATEGORY)||target.equals(TARGET.MOBILE)){
                    			if(name.equalsIgnoreCase("item")){
                    				search.w3SetPageInfo(name, start, count);
                    			}else{
                    				search.w3SetPageInfo(name, start, collection.getDefaultItemCount(parameter));
                    			}
                    		}else{
                    			search.w3SetPageInfo(name, start, count);
                    		}
                    	}
                    }
                }
			}else{
			    search.w3SetPageInfo(name, 0, collection.getDefaultItemCount(parameter));
			}
			// 카테고리 부스팅
			String boost = collection.getCategoryBoost(parameter);
			if(boost!=null && !strQuery.equals("")){
				if(name.equalsIgnoreCase("mall")){
					search.w3SetBoostCategory(name, "SSGBOOST", "SUB_MATCH", strOriQuery);
				}else{
					search.w3SetBoostCategory(name, site.getCategoryBoost(), "SUB_MATCH", strOriQuery);
				}
			}
			// Collection Ranking
			String rank = collection.getCollectionRanking(parameter);
			if(rank!=null && !strQuery.equals("")){
			    search.w3SetRanking(name, "keyword", rank.concat("/").concat(strOriQuery), 0);
            }

			//Set Sort
			String strSort = collection.getCollectionSort(parameter);
			String realName = collection.getCollectionRealName(parameter);
			// 샤넬의 경우에는 무조건 신규상품순 정렬
			if(target.equals(TARGET.SPSHOP)){
				search.w3AddSortField(name, "ITEM_REG_DT", 1);
				search.w3AddSortField(name, "WEIGHT", 1);
				search.w3AddSortField(name, "RANK", 1);
			}else if(strSort!=null){
				if(sort.getSortName().equals("")||sort.getSortName().equals("BEST")){
			        if(name.equalsIgnoreCase("book")){
			            search.w3AddSortField(name,"RANK", 1);
			        }
			        else if(name.equalsIgnoreCase("notice")||name.equals("event")||name.equals("magazine")){
			        	search.w3AddSortField(name,"WRT_DATE", 1);
			        } else if (name.equalsIgnoreCase("book") || name.equalsIgnoreCase("book_disp") || name.equalsIgnoreCase("brand_disp") || name.equalsIgnoreCase("book_brand_disp")) {
			        	if(realName.equals("item")){
			        		if(site.equals(SITE.SSG)){
				        		search.w3AddSortField(name, "SCOM_DISP_BEST_SCR", 1);
				        		search.w3AddSortField(name, "ITEM_REG_DT", 1);
				        	}else if(site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
				        		search.w3AddSortField(name, "SHIN_DISP_BEST_SCR", 1);
				        		search.w3AddSortField(name, "ITEM_REG_DT", 1);
				        	}else{
				        		search.w3AddSortField(name, "EMART_DISP_BEST_SCR", 1);
				        		search.w3AddSortField(name, "ITEM_REG_DT", 1);
				        	}
			        	}else{
							search.w3AddSortField(name,"RANK", 1);
						}
			        } else if (name.equals("mall") || name.equals("category") || name.equals("book") || name.equals("mall_disp")
	                		||name.equals("brand_total")||name.equals("book_brand_total")
	                		||name.equals("brand_mall_disp")||name.equals("book_brand_mall_disp")){
			        	search.w3AddSortField(name,"RANK", 1);
			        }
			        else if(name.equals("lifemagazine")){
			        	search.w3AddSortField(name,"DISP_STRT_DTS", 1);
			        }
					else if(name.equals("spprice")){
						search.w3AddSortField(name, "WEIGHT", 1);
						search.w3AddSortField(name, "RANK", 1);
					}
			        else{
			        	// 추천순은 WEIGHT > RANK  > ITEM_REG_DT 순의 3차정렬을 사용함
			            search.w3AddSortField(name, "WEIGHT", 1);
                        search.w3AddSortField(name, "RANK", 1);
						if(!name.equals("postng")){
							search.w3AddSortField(name, "SRCH_TYPE_THRD_SCR", 1);
						}
						search.w3AddSortField(name, "ITEM_REG_DT", 1);
						search.w3AddSortField(name, "SELLPRC", 0);
					}
				}
				// 직접 SORT 파라메터가 유입된 경우
				else{
					if(realName.equals("item")||realName.equals("item_test")||realName.equals("postng")||realName.equals("book")){
						search.w3AddSortField(name, sort.getSortName(), sort.getOrder());
						search.w3AddSortField(name, "RANK", 1);
					}else{
						search.w3AddSortField(name, "RANK", 1);
					}
			    }
			}else{
				if(name.equalsIgnoreCase("mall") && !site.equals(SITE.SSG)){
	        		search.w3AddSortField(name, "WEIGHT", 1);
                    search.w3AddSortField(name, "RANK", 1);
	        	}
			    search.w3AddSortField(name,"RANK", 1);
			}
			//Set Analyzer
			search.w3SetQueryAnalyzer(name,1,1,1,1);

			//Set Prefix
			String prefix = collection.getPrefixField(parameter);
			int    prefixOperator = collection.getPrefixOperator(parameter);		// PREFIX 연산의 OR AND 여부를 결정한다.
			if(prefix!=null && !prefix.equals(""))search.w3SetPrefixQuery(name, prefix, prefixOperator);

			//가격 검색 Set Filter ( 현재 필터기능은 가격 검색에만 사용한다 )
			String filter = collection.getFilter(parameter);
			if(filter!=null && !filter.equals("") && !name.equals("event")){
			    search.w3SetPropertyGroup(name, filter, 0, 1000000000, 5);
			    String strMinPrc = StringUtils.defaultIfEmpty(parameter.getMinPrc(), "-1");
			    String strMaxPrc = StringUtils.defaultIfEmpty(parameter.getMaxPrc(), "-1");
			    // 숫자로 변환되는 경우에만 보냄
			    int minprc;
			    int maxprc;
			    try{
			        minprc = Integer.parseInt(strMinPrc);
			        maxprc = Integer.parseInt(strMaxPrc);
			    }catch(NumberFormatException ne){
			        minprc = -1;
			        maxprc = -1;
			    }
			    if(collection.getCollectionRealName(parameter).equalsIgnoreCase("item")||collection.getCollectionRealName(parameter).equalsIgnoreCase("item_test")||collection.getCollectionRealName(parameter).equalsIgnoreCase("book")){
			    	if(minprc>-1 && maxprc >-1){
				        StringBuilder fsb = new StringBuilder();
				        fsb.append("<SELLPRC:gte:").append(strMinPrc).append("><SELLPRC:lte:").append(strMaxPrc).append(">");
				        search.w3SetFilterQuery(name, fsb.toString());
				    }else if(minprc>-1){
				    	StringBuilder fsb = new StringBuilder();
				        fsb.append("<SELLPRC:gte:").append(strMinPrc).append(">");
				        search.w3SetFilterQuery(name, fsb.toString());
				    }else if(maxprc>-1){
				    	StringBuilder fsb = new StringBuilder();
				        fsb.append("<SELLPRC:lte:").append(strMaxPrc).append(">");
				        search.w3SetFilterQuery(name, fsb.toString());
				    }
			    }
			}
			// set event filterVo
			else if(name.equals("event")){
				search.w3SetFilterQuery(name, filter);
			}

			//Set MultiGroupBy
			String multiGroupBy = collection.getMultiGroupBy(parameter);
			if(multiGroupBy!=null && !multiGroupBy.equals(""))search.w3SetMultiGroupBy(name, multiGroupBy);

			//Set GroupBy
			String groupField = parameterUtil.getCategoryGroupField(collection,parameter);
			String groupDepth = parameterUtil.getCategoryGroupDepth(collection,parameter);

			for(StringTokenizer st = new StringTokenizer(groupField,",");st.hasMoreTokens();){
			    String field = st.nextToken();
			    if(groupField!=null && groupDepth!=null && !groupField.equals("") && !groupDepth.equals("")){
	                search.w3AddCategoryGroupBy(name, field, groupDepth);
	            }
			}

			//Set HighLight
			if(highLightYn)search.w3SetHighlight(name, 1, 0);
			setQueryDebug(collection);
		}
	}

	public void execute() {
		int ret = search.w3ConnectServer(url, 7000, 5000);
		search.w3ReceiveSearchQueryResult(3);
		if(ret!=0){
			requestQuery.append("\n =================================================SEARCH REQUEST TIMEOUT============================================================");
		}
		if(debug)logger.info("{}",requestQuery);
	}

	public void close() {
		search.w3CloseServer();
	}
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

	/**
	 * 검색결과 핸들링 메서드,
	 * 코드 간소화 이슈있음
	 */
	public Result result(Result result) {
		int err = search.w3GetError();
		if(err > 0){
			logger.info("=======================================================ENGINE LIB EXCEPTION : {} ======================================================= ",err);
		}
		if(search.w3GetError() < 1) {
		    StringBuilder srchItemIds = new StringBuilder();
			StringBuilder spItemIds = new StringBuilder();
			SITE site = parameterUtil.getSite(parameter);
			TARGET target = parameterUtil.getTarget(parameter);
			int itemResultCount = 0;
			int bookResultCount = 0;
			String itemResultIds = "";
			String bookResultIds = "";
			List<Item> itemResultList = null;
			List<Item> bookResultList = null;
			
			String strQuery = StringUtils.defaultIfEmpty(parameter.getQuery(), "");
			if(!strQuery.equals("")){
				String skipResult = StringUtils.defaultIfEmpty(SkipWordSuffixMatcher.longestMatch(strQuery), "");
				if(!skipResult.equals("")){
					result.setSkipResult(replaceLast(strQuery,skipResult,"").trim());
					// 해외직구는 그대로 둔다
					if(strQuery.replaceAll("\\s", "").equals("해외직구")){
						result.setSkipResult("");
					}
					// 노브랜드는 그대로 둔다.
					if(strQuery.replaceAll("\\s", "").equals("노브랜드")){
						result.setSkipResult("");
					}
					// 디스토어는 브랜드 이므로 그대로 둔다.
					if(strQuery.indexOf("디스토어")>-1){
						result.setSkipResult("");
					}
					// 점포라는 단어가 존재하는 경우 그래도 둔다.
					if(strQuery.indexOf("점포")>-1){
						result.setSkipResult("");
					}
				}
			}
			
			// 컬렉션 수 만큼 loop 시작
			for( COLLECTION collection : parameterUtil.getCollectionList(parameter)){
				String name = collection.getCollectionAliasName(parameter);
				// 상품정보 구성
				if(collection.equals(COLLECTION.ITEM)||collection.equals(COLLECTION.ITEM_TEST)){
					int count = search.w3GetResultCount(name);
					List<Item> itemList = new ArrayList<Item>();
					Item item;
		            for(int i=0;i<count;i++){
		            	item = new Item();
		            	String strItemId = search.w3GetField(name,"ITEM_ID",i);
		            	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
		            	String strItemNm = search.w3GetField(name,"ITEM_NM",i);

//		            	String dispCtgLclsId = search.w3GetField(name,"DISP_CTG_LCLS_ID",i);
//		            	String dispCtgMclsId = search.w3GetField(name,"DISP_CTG_MCLS_ID",i);
//		            	String dispCtgSclsId = search.w3GetField(name,"DISP_CTG_SCLS_ID",i);
//		            	String dispCtgDclsId = search.w3GetField(name,"DISP_CTG_DCLS_ID",i);
//		            	String dispCtgLclsNm = search.w3GetField(name,"DISP_CTG_LCLS_NM",i);
//		            	String dispCtgMclsNm = search.w3GetField(name,"DISP_CTG_MCLS_NM",i);
//		            	String dispCtgSclsNm = search.w3GetField(name,"DISP_CTG_SCLS_NM",i);
//		            	String dispCtgDclsNm = search.w3GetField(name,"DISP_CTG_DCLS_NM",i);
//
//		            	String temDispCtgLclsId = search.w3GetField(name,"TEM_DISP_CTG_LCLS_ID",i);
//                        String temDispCtgMclsId = search.w3GetField(name,"TEM_DISP_CTG_MCLS_ID",i);
//                        String temDispCtgSclsId = search.w3GetField(name,"TEM_DISP_CTG_SCLS_ID",i);
//                        String temDispCtgDclsId = search.w3GetField(name,"TEM_DISP_CTG_DCLS_ID",i);
//                        String temDispCtgLclsNm = search.w3GetField(name,"TEM_DISP_CTG_LCLS_NM",i);
//                        String temDispCtgMclsNm = search.w3GetField(name,"TEM_DISP_CTG_MCLS_NM",i);
//                        String temDispCtgSclsNm = search.w3GetField(name,"TEM_DISP_CTG_SCLS_NM",i);
//                        String temDispCtgDclsNm = search.w3GetField(name,"TEM_DISP_CTG_DCLS_NM",i);

		            	String sellprc     = search.w3GetField(name, "SELLPRC", i);
		            	String shppTypeCd = search.w3GetField(name, "SHPP_TYPE_CD", i);
		            	String shppTypeDtlCd = search.w3GetField(name, "SHPP_TYPE_DTL_CD", i);
		            	String itemRegDivCd  = search.w3GetField(name, "ITEM_REG_DIV_CD", i);

		            	String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
		            	String exusItemDivCd = search.w3GetField(name,"EXUS_ITEM_DIV_CD", i);
		            	String exusItemDtlCd = search.w3GetField(name,"EXUS_ITEM_DTL_CD", i);
		            	String shppMainCd = search.w3GetField(name,"SHPP_MAIN_CD", i);
		            	String shppMthdCd = search.w3GetField(name,"SHPP_MTHD_CD", i);
		            	String strSalestrNo = "";

		            	// 점포 매핑작업
		            	FrontUserInfo userInfo = parameter.getUserInfo();
		            	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
		            		if(salestrLst.indexOf("0001")>-1){
		            			int idx = 0;
		            			salestrLst = salestrLst.replace("0001,", "").trim();
		            			salestrLst = salestrLst.replace("0001", "").trim();
		            			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
		            				if(idx>1)break;
		            				strSalestrNo = st.nextToken().replace("D", "");
		            	            idx++;
		            			}
		            			item.setSellSalestrCnt(1);
		            		}
		            		// 0001 때문에 사실상 이럴일이 없기는 함
		            		else{
		            			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
		            			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
		            			item.setSellSalestrCnt(1);
		            		}
		            	}
		            	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
		            	else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
		                	strSalestrNo = userInfo.getEmSaleStrNo();
		                	item.setSellSalestrCnt(1);
		                }else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
		                	strSalestrNo = userInfo.getTrSaleStrNo();
		                	item.setSellSalestrCnt(1);
		                }else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
		                	strSalestrNo = userInfo.getBnSaleStrNo();
		                	item.setSellSalestrCnt(1);
		                }else{
		                	strSalestrNo = "6005";
		        			item.setSellSalestrCnt(1);
		                }
		            	item.setSalestrNo(strSalestrNo);
		            	item.setSiteNo(strSiteNo);
		            	item.setItemId(strItemId);
		            	item.setItemNm(strItemNm);

//		            	item.setDispCtgLclsId(dispCtgLclsId);
//		            	item.setDispCtgLclsNm(dispCtgLclsNm);
//		            	item.setDispCtgMclsId(dispCtgMclsId);
//		            	item.setDispCtgMclsNm(dispCtgMclsNm);
//		            	item.setDispCtgSclsId(dispCtgSclsId);
//		            	item.setDispCtgSclsNm(dispCtgSclsNm);
//		            	item.setDispCtgDclsId(dispCtgDclsId);
//		            	item.setDispCtgDclsNm(dispCtgDclsNm);
//
//		            	item.setTemDispCtgLclsId(temDispCtgLclsId);
//                        item.setTemDispCtgLclsNm(temDispCtgLclsNm);
//                        item.setTemDispCtgMclsId(temDispCtgMclsId);
//                        item.setTemDispCtgMclsNm(temDispCtgMclsNm);
//                        item.setTemDispCtgSclsId(temDispCtgSclsId);
//                        item.setTemDispCtgSclsNm(temDispCtgSclsNm);
//                        item.setTemDispCtgDclsId(temDispCtgDclsId);
//                        item.setTemDispCtgDclsNm(temDispCtgDclsNm);

		            	item.setSellprc(sellprc);
		            	item.setShppTypeCd(shppTypeCd);
		            	item.setShppTypeDtlCd(shppTypeDtlCd);
		            	item.setItemRegDivCd(itemRegDivCd);
		            	item.setExusItemDivCd(exusItemDivCd);
		            	item.setExusItemDtlCd(exusItemDtlCd);
		            	item.setShppMainCd(shppMainCd);
		            	item.setShppMthdCd(shppMthdCd);

		            	StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
		            	if(srchItemIds.indexOf(ids.toString()) < 0){
		            	    srchItemIds.append(ids);
		            	}

		                itemList.add(item);
		            }
		            // ITEM LIST
		            result.setItemList(itemList);
		            int tot = search.w3GetResultTotalCount(name);
		            result.setItemCount(tot);

		            // LEAF 일 경우 상품에서 BRAND 정보록 가져오도록 수정
                    // MULTI GROUP BY 정보를 가져온다.
                    List<Brand> brandList = new ArrayList<Brand>();
                    Brand brand;
                    String strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"),"");
                    if(!strBrandInfo.equals("")){
                        for(StringTokenizer st = new StringTokenizer(strBrandInfo,"@");st.hasMoreTokens();){
                            brand = new Brand();
                            String[] brandIds = st.nextToken().split("\\^");
                            if(brandIds.length>2){
                                brand.setBrandId(brandIds[0]);
                                brand.setBrandNm(brandIds[1].replaceAll("\\++", " "));
                                brand.setItemCount(brandIds[2]);
                                brandList.add(brand);
                            }
                        }
                        result.setBrandList(brandList);
                    }
                    // SSG의 경우에는 SITE LIST도 함께 가져온다.
                    Size size;
                    List<Size> sizeList = new ArrayList<Size>();
                    String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
                    if(!strSizeInfo.equals("")){
                    	for(Iterator<String> iter = Splitter.on("@").trimResults().split(strSizeInfo).iterator();iter.hasNext();){
                    		size = new Size();
                    		String[] sizeIds = iter.next().split("\\^");
                    		if(sizeIds.length>2){
                    			size.setSizeCd(sizeIds[0]);
                    			size.setSizeNm(sizeIds[1]);
                    			size.setSizeCount(sizeIds[2]);
                                sizeList.add(size);
                            }
                    	}
                    	Collections.sort(sizeList, new Comparator<Size>(){
            	            public int compare(Size c1,Size c2){
            	            	try{
            	            		int k1 = Integer.parseInt(c1.getSizeCd());
            	            		int k2 = Integer.parseInt(c2.getSizeCd());
            	            		if(k1<k2){
                	                    return -1;
                	                } else if(k1>k2){
                	                    return 1;
                	                } else{
                	                    return 0;
                	                }
            	            	}catch(NumberFormatException ne){
            	            		return 0;
            	            	}
            	            }
            	        });
                    	result.setSizeList(sizeList);
                    }
                    // 형태소 분석결과 ( 불용어가 체크되면 분석결과를 내려보내지 않음 )
            		if(StringUtils.isEmpty(SizeDenyWordSuffixMatcher.longestMatch(strQuery))){
            			result.setMorphResult(search.w3GetHighlightByField(name, "ITEM_NM"));
            		}else{
            			result.setMorphResult("");
            		}

                    // PROPERTY GROUP
                    if(site.equals(SITE.SSG) && target.equals(TARGET.MOBILE)){

                    }else{
                    	int propct = search.w3GetCountPropertyGroup(name);
                        int min = search.w3GetMinValuePropertyGroup(name);
                        int max = search.w3GetMaxValuePropertyGroup(name);
                        List<Prc> prcGroupList = new ArrayList<Prc>();
                        Prc prc;
                        for(int i=0;i<propct;i++){
                            prc = new Prc();
                            prc.setMinPrc(search.w3GetMinValueInPropertyGroup(name,i));
                            prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup(name,i));
                            prcGroupList.add(prc);
                        }
                        result.setMinPrc(min);
                        result.setMaxPrc(max);
                        result.setPrcGroupList(prcGroupList);
                    }
                    if(debug)logger.info("\n==================================================================================================================================================================================\nITEM_TOTAL_COUNT : {}\n\n==================================================================================================================================================================================",tot);
                    if(tot==0){
				    	parameter.debugString();
				    }
				}
				else if(collection.equals(COLLECTION.BRAND_TOTAL) || collection.equals(COLLECTION.BOOK_BRAND_TOTAL)){
					String selectedCtgId = parameter.getDispCtgId();
					String ctgLst = search.w3GetMultiGroupByResult(name, "DISP_CTG_LST");
					// Token 형태 : ctgId^ctgNms^ctgCount@ctgId^ctgNms^ctgCount
					List<Category> ctgList  = new ArrayList<Category>();
			        List<Category> ctgList1 = new ArrayList<Category>();
			        List<Category> ctgList2 = new ArrayList<Category>();
			        for(StringTokenizer grpToken = new StringTokenizer(ctgLst,"@");grpToken.hasMoreTokens();){
						String ctgElement = grpToken.nextToken();
						if(ctgElement!=null && !ctgElement.equals("")){
							int i=0;
							Category c = new Category();
							String ctgType = "";
							String ctgCls  = "";
							String ctgLv   = "";
							for(StringTokenizer ctgToken = new StringTokenizer(ctgElement,"\\^");ctgToken.hasMoreTokens();){
								String ctg = ctgToken.nextToken();
								if(ctg!=null && !ctg.equals("")){
									// CTG_ID
									if(i == 0){
										c.setCtgId(ctg);
									}
									// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
									else if(i == 1){
										String[] ctgNms = ctg.split("\\__");
										if(ctgNms.length>=6){
											ctgLv = ctgNms[1];
											c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
											c.setCtgLevel(Integer.parseInt(ctgLv));
											c.setSiteNo(ctgNms[2]);
											c.setPriorCtgId(ctgNms[5]);
											ctgCls  = ctgNms[3];
											ctgType = ctgNms[4];
										}
									}
									// ITEM_COUNT
									else if(i == 2){
										c.setCtgItemCount(Integer.parseInt(ctg));
									}
								}
								if(i>0 && i%2 == 0){
									i = 0;
								}else i++;
							}
							// 현재의 사이트와 동일할 경우에만 add
							if(parameter.getSiteNo().equals(c.getSiteNo())){
								if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals("1")){
									ctgList1.add(c);
								}else if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals("2")){
									ctgList2.add(c);
								}
							}
						}
					}
			        if(ctgList1!=null && ctgList1.size()>0 && ctgList2!=null && ctgList2.size()>0){
			        	// CATEGORY LEVEL1 SORT
				        Collections.sort(ctgList1,new Comparator<Category>() {
				        	public int compare(Category c1, Category c2){
				        		String d1 = c1.getCtgId();
				        		String d2 = c2.getCtgId();
				        		return (d1).compareTo(d2);
				        	}
						});

				        // CATEGORY LEVEL2 SORT
				        Collections.sort(ctgList2,new Comparator<Category>() {
				        	public int compare(Category c1, Category c2){
				        		String p1 = c1.getPriorCtgId();
				        		String p2 = c2.getPriorCtgId();
				        		return (p1).compareTo(p2);
				        	}
				        });

				        int totalCount = 0;
				        String selectedPriorCtgId = "";		// 선택된 1 LV 카테고리를 찾는다.
				        for (Category c1 : ctgList1) {
				        	if(selectedCtgId!=null && !selectedCtgId.equals("") && c1.getCtgId().equals(selectedCtgId)){
				        		c1.setSelectedArea("selected");
				        	}
				        	totalCount+=c1.getCtgItemCount();
				        	ctgList.add(c1);
							for (Category c2 : ctgList2) {
								if(c1.getCtgId().equals(c2.getPriorCtgId())){
									if(selectedCtgId!=null && !selectedCtgId.equals("") && c2.getCtgId().equals(selectedCtgId)){
										selectedPriorCtgId = c2.getPriorCtgId();
						        	}
									ctgList.add(c2);
								}
							}
						}
				        // 선택된 카테고리 찾기
				        if(selectedPriorCtgId!=null && !selectedPriorCtgId.equals("")){
				        	for (int i=0;i<ctgList.size();i++) {
				        		Category c = ctgList.get(i);
								if(c.getCtgId().equals(selectedPriorCtgId)){
									c.setSelectedArea("selected");
									ctgList.set(i,c);
								}
							}
				        }
			        	Category totCtg = new Category();
				        totCtg.setCtgId("0000000000");
				        totCtg.setPriorCtgId("0000000000");
				        totCtg.setCtgLevel(1);
				        totCtg.setCtgNm("전체");
				        totCtg.setCtgItemCount(totalCount);
				        ctgList.add(0, totCtg);
			        }
			        if(result.getCategoryList()!=null && result.getCategoryList().size()>0){
			        	// 기존에 카테고리가 존재하는 경우에는 아무일도 하지 않음
			        }else{
			        	if(ctgList!=null)result.setCategoryList(ctgList);
			        }
				}
				else if(collection.equals(COLLECTION.BRAND_MALL_DISP)||collection.equals(COLLECTION.BOOK_BRAND_MALL_DISP)){
					Map<String,String> mallCountMap = Maps.newHashMap();
					String strSiteResult = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SITE_NO"), "");
					String strScomResult = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"");
					if(!strSiteResult.equals("")){
						for(StringTokenizer st = new StringTokenizer(strSiteResult,"@");st.hasMoreTokens();){
							String[] mallIds = st.nextToken().split("\\^");
							mallCountMap.put(mallIds[0], mallIds[2]);
						}
					}
					if(!strScomResult.equals("")){
						for(StringTokenizer st = new StringTokenizer(strScomResult,"@");st.hasMoreTokens();){
						    String[] mallIds = st.nextToken().split("\\^");
						    if(mallIds[0].equals("Y")){
						        mallCountMap.put("6005", mallIds[2]);
						    }
						}
					}
			        int tot = search.w3GetResultTotalCount(name);
					int pre = result.getItemCount();
					if(tot>=pre){
						result.setMallCountMap(mallCountMap);
					}
				}
				else if(collection.equals(COLLECTION.BRAND_DISP)||collection.equals(COLLECTION.BOOK_BRAND_DISP)){
					// ITEM 결과
					// 상품 결과 SET
					List<Item> itemList = new ArrayList<Item>();
			        int count = search.w3GetResultCount(name);
			        for(int i=0;i<count;i++){
		            	String strItemId = search.w3GetField(name,"ITEM_ID",i);
		            	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
			        	StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(",");
			        	if(srchItemIds.indexOf(ids.toString()) < 0){
			        		srchItemIds.append(ids);
			        		itemList.add(setItemCollection(search, i, name, parameter.getUserInfo()));
			        	}
			        }
					int tot = search.w3GetResultTotalCount(name);
					int pre = result.getItemCount();
					// 1. 둘다 0 인경우
					if(tot == 0 && pre == 0){
						if(collection.equals(COLLECTION.BOOK_DISP)){
							if(debug)logger.info("\n==================================================================================================================================================================================\nDISP_TOTAL_COUNT : {}\n\n==================================================================================================================================================================================",tot);
							parameter.debugString();
						}
					}
					// 2. 이전 값이 큰 경우
					if(pre>tot){
						// 현재 아무일도 하지 않도록 함
					}
					// 3. 현재 값이 큰 경우
					if(tot>pre){
						if(debug)logger.info("\n==================================================================================================================================================================================\n{} -> DISP_TOTAL_COUNT : {}\n\n==================================================================================================================================================================================",collection.name(),tot);
						result.setItemCount(tot);
						if(result.getItemList()==null || result.getItemList().size()<1){
							result.setItemList(itemList);
							result.setSrchItemIds(srchItemIds.toString());
						}
					}
				}
				// 각 몰별 탭 결과 구성
				else if(collection.equals(COLLECTION.MALL)||collection.equals(COLLECTION.MALL_DISP)||collection.equals(COLLECTION.BOOK_MALL_DISP)){
					Map<String,String> mallCountMap = new HashMap<String,String>();
					for(StringTokenizer st = new StringTokenizer(search.w3GetMultiGroupByResult(name, "SITE_NO"),"@");st.hasMoreTokens();){
						String[] mallIds = st.nextToken().split("\\^");
						mallCountMap.put(mallIds[0], mallIds[2]);
					}
					for(StringTokenizer st = new StringTokenizer(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"@");st.hasMoreTokens();){
					    String[] mallIds = st.nextToken().split("\\^");
					    if(mallIds[0].equals("Y")){
					        mallCountMap.put("6005", mallIds[2]);
					    }
					}
					if(result.getMallCountMap()==null || result.getMallCountMap().keySet().size()<1){
						result.setMallCountMap(mallCountMap);
					}
					Size size;
                    List<Size> sizeList = new ArrayList<Size>();
                    String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
                    if(!strSizeInfo.equals("")){
                    	for(Iterator<String> iter = Splitter.on("@").trimResults().split(strSizeInfo).iterator();iter.hasNext();){
                    		size = new Size();
                    		String[] sizeIds = iter.next().split("\\^");
                    		if(sizeIds.length>2){
                    			size.setSizeCd(sizeIds[0]);
                    			size.setSizeNm(sizeIds[1]);
                    			size.setSizeCount(sizeIds[2]);
                                sizeList.add(size);
                            }
                    	}
                    	Collections.sort(sizeList, new Comparator<Size>(){
            	            public int compare(Size c1,Size c2){
            	            	try{
            	            		int k1 = Integer.parseInt(c1.getSizeCd());
            	            		int k2 = Integer.parseInt(c2.getSizeCd());
            	            		if(k1<k2){
                	                    return -1;
                	                } else if(k1>k2){
                	                    return 1;
                	                } else{
                	                    return 0;
                	                }
            	            	}catch(NumberFormatException ne){
            	            		return 0;
            	            	}
            	            }
            	        });
                    	result.setSizeList(sizeList);
                    }
                    if(collection.equals(COLLECTION.MALL) && !site.equals(SITE.SSG)){
                    	int count = search.w3GetResultCount(name);
                		List<Item> noResultItemList = Lists.newArrayList();
                		StringBuilder noResultItemIds = new StringBuilder();
                		Item item;
                        for(int i=0;i<count;i++){
                        	item = new Item();
                        	String strItemId = search.w3GetField(name,"ITEM_ID",i);
                        	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
                        	String strItemNm = search.w3GetField(name,"ITEM_NM",i);
                        	String sellprc     = search.w3GetField(name, "SELLPRC", i);
                        	String shppTypeCd = search.w3GetField(name, "SHPP_TYPE_CD", i);
                        	String shppTypeDtlCd = search.w3GetField(name, "SHPP_TYPE_DTL_CD", i);
                        	String itemRegDivCd  = search.w3GetField(name, "ITEM_REG_DIV_CD", i);

                        	String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
                        	String exusItemDivCd = search.w3GetField(name,"EXUS_ITEM_DIV_CD", i);
                        	String exusItemDtlCd = search.w3GetField(name,"EXUS_ITEM_DTL_CD", i);
                        	String shppMainCd = search.w3GetField(name,"SHPP_MAIN_CD", i);
                        	String shppMthdCd = search.w3GetField(name,"SHPP_MTHD_CD", i);
                        	String strSalestrNo = "";
                        	// 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
                        	FrontUserInfo userInfo = parameter.getUserInfo();
                        	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
                        		if(salestrLst.indexOf("0001")>-1){
                        			int idx = 0;
                        			salestrLst = salestrLst.replace("0001,", "").trim();
                        			salestrLst = salestrLst.replace("0001", "").trim();
                        			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
                        				if(idx>1)break;
                        				strSalestrNo = st.nextToken().replace("D", "");
                        	            idx++;
                        			}
                        			item.setSellSalestrCnt(1);
                        		}
                        		// 0001 때문에 사실상 이럴일이 없기는 함
                        		else{
                        			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
                        			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
                        			item.setSellSalestrCnt(1);
                        		}
                        	}
                        	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
                            else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
                            	strSalestrNo = userInfo.getEmSaleStrNo();
                            	item.setSellSalestrCnt(1);
                            }else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
                            	strSalestrNo = userInfo.getTrSaleStrNo();
                            	item.setSellSalestrCnt(1);
                            }else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
                            	strSalestrNo = userInfo.getBnSaleStrNo();
                            	item.setSellSalestrCnt(1);
                            }else{
                            	strSalestrNo = "6005";
                    			item.setSellSalestrCnt(1);
                            }
                        	item.setSalestrNo(strSalestrNo);
                        	item.setSiteNo(strSiteNo);
                        	item.setItemId(strItemId);
                        	item.setItemNm(strItemNm);

                        	item.setSellprc(sellprc);
                        	item.setShppTypeCd(shppTypeCd);
                        	item.setShppTypeDtlCd(shppTypeDtlCd);
                        	item.setItemRegDivCd(itemRegDivCd);
                        	item.setExusItemDivCd(exusItemDivCd);
                        	item.setExusItemDtlCd(exusItemDtlCd);
                        	item.setShppMainCd(shppMainCd);
                        	item.setShppMthdCd(shppMthdCd);

                        	StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
                        	if(noResultItemIds.indexOf(ids.toString()) < 0){
                        		noResultItemIds.append(ids);
                        	}
                        	noResultItemList.add(item);
                        }
                    	result.setNoResultItemRecomList(noResultItemList);
                    	result.setNoResultItemRecomIds(noResultItemIds.toString());
                    }
				}
				// 브랜드 ( 모바일 신몰, 신백, SSG 에서만 현재 사용함 )
				else if(collection.equals(COLLECTION.BRAND)){
				    List<Brand> brandList = new ArrayList<Brand>();
                    Brand brand;
                    String strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"),"");
                    if(!strBrandInfo.equals("")){
                        for(StringTokenizer st = new StringTokenizer(strBrandInfo,"@");st.hasMoreTokens();){
                            brand = new Brand();
                            String[] brandIds = st.nextToken().split("\\^");
                            if(brandIds.length>2){
                                brand.setBrandId(brandIds[0]);
                                brand.setBrandNm(brandIds[1].replaceAll("\\++", " "));
                                brand.setItemCount(brandIds[2]);
                                brandList.add(brand);
                            }
                        }
                        result.setBrandList(brandList);
                    }
                    // PROPERTY GROUP
                    if(site.equals(SITE.SSG) && target.equals(TARGET.MOBILE)){
                    	int propct = search.w3GetCountPropertyGroup(name);
                        int min = search.w3GetMinValuePropertyGroup(name);
                        int max = search.w3GetMaxValuePropertyGroup(name);
                        List<Prc> prcGroupList = new ArrayList<Prc>();
                        Prc prc;
                        for(int i=0;i<propct;i++){
                            prc = new Prc();
                            prc.setMinPrc(search.w3GetMinValueInPropertyGroup(name,i));
                            prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup(name,i));
                            prcGroupList.add(prc);
                        }
                        result.setMinPrc(min);
                        result.setMaxPrc(max);
                        result.setPrcGroupList(prcGroupList);
                    }
				}
				// 상품평
				else if(collection.equals(COLLECTION.POSTNG)){
                    int count = search.w3GetResultCount(name);
                    List<Postng> postngList = new ArrayList<Postng>();
                    Postng postng;
                    for(int i=0;i<count;i++){
                        postng = new Postng();
                        String strItemId = search.w3GetField(name,"ITEM_ID",i);
                        String strSiteNo = search.w3GetField(name,"SITE_NO",i);
                        String strItemNm = search.w3GetField(name,"ITEM_NM",i);
                        String strPostngId = search.w3GetField(name,"POSTNG_ID",i);
                        String strPostngNm = search.w3GetField(name,"POSTNG_TITLE_NM",i);
                        String strPostngEvalScr = search.w3GetField(name,"POSTNG_EVAL_SCR",i);
                        String strPostngWrtpeIdnfId = search.w3GetField(name, "POSTNG_WRTPE_IDNF_ID", i);
                        String strSalestrLst = search.w3GetField(name, "SALESTR_LST", i);
                        String strSalestrNo = "";
                        String itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i);

                        String sellprc     = search.w3GetField(name, "SELLPRC", i);

                        postng.setSiteNo(strSiteNo);
                        postng.setItemId(strItemId);
                        postng.setItemNm(strItemNm);
                        postng.setPostngId(strPostngId);
                        postng.setPostngTitleNm(strPostngNm);
                        postng.setPostngEvalScr(strPostngEvalScr);
                        postng.setPostngWrtpeIdnfId(strPostngWrtpeIdnfId);

                        postng.setSellprc(sellprc);
                        
                        // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
                    	FrontUserInfo userInfo = parameter.getUserInfo();
                    	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
                    		if(strSalestrLst.indexOf("0001")>-1){
                    			int idx = 0;
                    			strSalestrLst = strSalestrLst.replace("0001,", "").trim();
                    			strSalestrLst = strSalestrLst.replace("0001", "").trim();
                    			for(StringTokenizer st = new StringTokenizer(strSalestrLst," ");st.hasMoreTokens();){
                    				if(idx>1)break;
                    				strSalestrNo = st.nextToken().replace("D", "");
                    	            idx++;
                    			}
                    		}
                    		else{
                    			strSalestrNo = strSalestrLst.replaceAll("\\p{Space}", "").replace("D", "");
                    			strSalestrNo = strSalestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
                    		}
                    	}
                    	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
                        else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getEmSaleStrNo();
                        }else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getTrSaleStrNo();
                        }else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getBnSaleStrNo();
                        }else{
                        	strSalestrNo = "6005";
                        }
                    	postng.setSalestrNo(strSalestrNo);

                        StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
                        if(srchItemIds.indexOf(ids.toString()) < 0){
                            srchItemIds.append(ids);
                        }
                        postngList.add(postng);
                    }
                    // POSTNG LIST
                    result.setPostngList(postngList);
                    result.setPostngCount(search.w3GetResultTotalCount(name));
                }
				// 추천상품 정보 구성
				else if(collection.equals(COLLECTION.RECOM)){
					List<Recom> recomList = new ArrayList<Recom>();
					int count = search.w3GetResultCount(name);
					Recom recom;
					for(int i=0;i<count;i++){
						recom = new Recom();
		            	String strItemId = search.w3GetField(name,"ITEM_ID",i);
		            	String strItemNm = search.w3GetField(name,"ITEM_NM",i);
		            	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
                        String dispOrdr    = search.w3GetField(name, "DISP_ORDR", i);

		            	recom.setItemId(strItemId);
		            	recom.setItemNm(strItemNm);
		            	recom.setSiteNo(strSiteNo);
		            	recom.setDispOrdr(dispOrdr);
		            	
		            	String strSalestrNo = "";
		            	
		            	// 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
                    	FrontUserInfo userInfo = parameter.getUserInfo();
                    	if(strSiteNo.equals("6001")){
                        	strSalestrNo = userInfo.getEmSaleStrNo();
                        }else if(strSiteNo.equals("6002")){
                        	strSalestrNo = userInfo.getTrSaleStrNo();
                        }else if(strSiteNo.equals("6003")){
                        	strSalestrNo = userInfo.getBnSaleStrNo();
                        }else{
                        	strSalestrNo = "6005";
                        }

		            	StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
                        if(srchItemIds.indexOf(ids.toString()) < 0){
                            srchItemIds.append(ids);
                        }

		                recomList.add(recom);
					}
					result.setRecomList(recomList);
					result.setRecomCount(search.w3GetResultTotalCount(name));
				}
				// 카테고리 정보 구성
				else if(collection.equals(COLLECTION.CATEGORY)||collection.equals(COLLECTION.BOOK_CATEGORY)){
					// BRAND
					// MULTI GROUP BY 정보를 가져온다. (현재 브랜드로 고정)
		            List<Brand> brandList = new ArrayList<Brand>();
		            Brand brand;
		            String strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"),"");
		            if(!strBrandInfo.equals("")){
		                for(StringTokenizer st = new StringTokenizer(strBrandInfo,"@");st.hasMoreTokens();){
	                        brand = new Brand();
	                        String[] brandIds = st.nextToken().split("\\^");
	                        if(brandIds.length>2){
	                            brand.setBrandId(brandIds[0]);
	                            brand.setBrandNm(brandIds[1].replaceAll("\\++", " "));
	                            brand.setItemCount(brandIds[2]);
	                            brandList.add(brand);
	                        }
	                    }
	                    result.setBrandList(brandList);
		            }
		            // SSG의 경우에는 SITE LIST도 함께 가져온다.
                    Size size;
                    List<Size> sizeList = new ArrayList<Size>();
                    String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
                    if(!strSizeInfo.equals("")){
                    	for(Iterator<String> iter = Splitter.on("@").trimResults().split(strSizeInfo).iterator();iter.hasNext();){
                    		size = new Size();
                    		String[] sizeIds = iter.next().split("\\^");
                    		if(sizeIds.length>2){
                    			size.setSizeCd(sizeIds[0]);
                    			size.setSizeNm(sizeIds[1]);
                    			size.setSizeCount(sizeIds[2]);
                                sizeList.add(size);
                            }
                    	}
                    	Collections.sort(sizeList, new Comparator<Size>(){
            	            public int compare(Size c1,Size c2){
            	            	try{
            	            		int k1 = Integer.parseInt(c1.getSizeCd());
            	            		int k2 = Integer.parseInt(c2.getSizeCd());
            	            		if(k1<k2){
                	                    return -1;
                	                } else if(k1>k2){
                	                    return 1;
                	                } else{
                	                    return 0;
                	                }
            	            	}catch(NumberFormatException ne){
            	            		return 0;
            	            	}
            	            }
            	        });
                    	result.setSizeList(sizeList);
                    }
		            // CATEGORY
					List<Category> retCtgList = new ArrayList<Category>();           /* 리턴되는 CategoryList */
					List<Category> retThemeCtgList = new ArrayList<Category>();      /* 리턴되는 Theme CategoryList */
					List<Category> ctgList = new ArrayList<Category>();              /* SSG의 경우 여러 DEPTH의 카테고리가 들어올 수 있으므로 */
					String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
                    String ctgId  = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
					String ctgIds = parameterUtil.getCtgIds(parameter);
					String themeCtgIds = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");
					String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");

					String ctgGroupField   = getCategoryGroupField(COLLECTION.CATEGORY,parameter);
			        /* comparator */
			        Comparator<Category> comparator = ctgSort();

			        /* ctgIds, themeCtgIds */
                    if(!ctgIds.equals("")||!themeCtgIds.equals("")){
                        int lv = 0;
                        int totCnt = 0;
                        try{
                            lv = Integer.parseInt(ctgLv);
                        }catch(NumberFormatException e){
                            lv = 1;
                        }
                        Category category = null;
                        /* 테마 카테고리 추가로 인해 여러개의 필드가 있을 수 있음 */
                        for(StringTokenizer ctgGroupFieldToken = new StringTokenizer(ctgGroupField,",");ctgGroupFieldToken.hasMoreTokens();){
                            String ctgGroup = ctgGroupFieldToken.nextToken();
                            int count = search.w3GetCategoryCount(name, ctgGroup, lv);
                            String strTk = "";
                            for(int k=0;k<count;k++){
                                strTk = search.w3GetCategoryName(name, ctgGroup, lv, k);
                                /* CTG_IDS 의 경우 테마와 일반 카테고리를 함께 선택한 경우
                                 * 다른 카테고리가 가지고 있는 상품들의 카테고리가 함께 딸려 나올 수 있으므로
                                 * 결과에는 내가 선택한 카테고리만 해당되도록 테마카테고리를 수정한다.
                                 * */
                                if(ctgGroup.startsWith("TEM_")){
                                    String[] ids = themeCtgIds.split("\\|");
                                    for (String id : ids) {
                                        if(strTk.indexOf(id)>-1){
                                            category = new Category();
                                            category.setThemeYn("Y");
                                            category.setCtgLevel(lv);
                                            category.tokenizeCtg(strTk);
                                            category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,ctgGroup, lv, k));
                                            category.setHasChild(true);
                                            retThemeCtgList.add(category);
                                            totCnt += search.w3GetDocumentCountInCategory(name,ctgGroup, lv, k);
                                        }
                                    }
                                }else{
                                    String[] ids = ctgIds.split("\\|");
                                    for (String id : ids) {
                                        if(strTk.indexOf(id)>-1){
                                            category = new Category();
                                            category.setThemeYn("N");
                                            category.setCtgLevel(lv);
                                            category.tokenizeCtg(strTk);
                                            category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,ctgGroup, lv, k));
                                            category.setHasChild(true);
                                            retCtgList.add(category);
                                            totCnt += search.w3GetDocumentCountInCategory(name,ctgGroup, lv, k);
                                        }
                                    }
                                }
                                if(retCtgList!=null)Collections.sort(retCtgList,comparator);
                                if(retThemeCtgList!=null)Collections.sort(retThemeCtgList,comparator);
                            }
                            // 1 Depth에서 선택검색하지 않은 경우에는 Depth를 보여준다.
                            if(strTk!=null && !strTk.equals("")){
                                int k = 1;
                                if(collection.equals(COLLECTION.BOOK_CATEGORY)){
                                    if(strTk.indexOf(":")>-1){
                                        for(StringTokenizer ctgSt = new StringTokenizer(strTk,":");ctgSt.hasMoreTokens();){
                                            String tk = ctgSt.nextToken();
                                            String[] splitStr = tk.split("\\^");
                                            if(k>1 && k<lv){
                                                result.setCtgSiteNo(splitStr[0]);
                                                result.setCtgId(k, splitStr[1]);
                                                result.setCtgNm(k, splitStr[2]);
                                                if(k==lv-1){
                                                    result.setCtgCount(lv, totCnt);
                                                }
                                                k++;
                                            }
                                        }
                                    }
                                }else{
                                    if(strTk.indexOf(":")>-1){
                                        for(StringTokenizer ctgSt = new StringTokenizer(strTk,":");ctgSt.hasMoreTokens();){
                                            String tk = ctgSt.nextToken();
                                            String[] splitStr = tk.split("\\^");
                                            if(k<lv){
                                                result.setCtgSiteNo(splitStr[0]);
                                                result.setCtgId(k, splitStr[1]);
                                                result.setCtgNm(k, splitStr[2]);
                                                if(k==lv-1){
                                                    result.setCtgCount(lv, totCnt);
                                                }
                                                k++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        result.setCategoryList(retCtgList);
                        result.setThemeCategoryList(retThemeCtgList);
                    }else{
			            /* 테마 카테고리 추가로 인해 여러개의 필드가 있을 수 있음 */
			            for(StringTokenizer ctgGroupFieldToken = new StringTokenizer(ctgGroupField,",");ctgGroupFieldToken.hasMoreTokens();){
			                ctgList = new ArrayList<Category>();
			                String groupField = ctgGroupFieldToken.nextToken();
			                String themeYn    = "N";
			                if(groupField.startsWith("TEM"))themeYn = "Y";
			                /* View Level을 받아옴. SSG ALL의 경우처럼 두가지 레벨을 모두 받아와야 할 필요가 있을때가 있음 */
			                String viewLevel = getCategoryViewDepth(COLLECTION.CATEGORY, parameter);
			                if(viewLevel.indexOf(",")>-1){
			                    Category category;
			                    int depth = 1;
			                    int count = search.w3GetCategoryCount(name, groupField, depth);
			                    List<Category> category1DepthList = new ArrayList<Category>();
			                    List<Category> category2DepthList = new ArrayList<Category>();
			                    // 1 Depth
			                    for(int k=0;k<count;k++){
			                        category = new Category();
			                        category.setCtgLevel(depth);
			                        category.tokenizeCtg(search.w3GetCategoryName(name, groupField, depth, k));
			                        category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,groupField, depth, k));
			                        category.setHasChild(true);
			                        category1DepthList.add(category);
			                    }
			                    Collections.sort(category1DepthList,comparator);
			                    // 2 Depth
			                    depth = 2;
			                    count = search.w3GetCategoryCount(name, groupField, depth);
			                    for(int k=0;k<count;k++){
			                        category = new Category();
			                        category.setCtgLevel(depth);
			                        category.tokenizeCtg(search.w3GetCategoryName(name, groupField, depth, k));
			                        category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,groupField, depth, k));
			                        category.setHasChild(true);
			                        category2DepthList.add(category);
			                    }
			                    Collections.sort(category2DepthList,comparator);
			                    for (Category c : category1DepthList) {
			                        retCtgList.add(c);
			                        for (Category c2 : category2DepthList) {
			                            if(c.getCtgId().equals(c2.getCtgLclsId())){
			                                retCtgList.add(c2);
			                            }else
			                                continue;
			                        }
			                    }
			                    result.setCtgViewCount(category1DepthList.size());
			                }else{
			                    int viewLv = 0;
			                    try{
			                        viewLv = Integer.parseInt(viewLevel);
			                    }catch(NumberFormatException e){
			                        viewLv = 0;
			                    }
			                    /* 카테고리 SET */
			                    int count = search.w3GetCategoryCount(name, groupField, viewLv);
			                    for(int k=0;k<count;k++){
			                        Category c = new Category();
			                        c.tokenizeCtg(search.w3GetCategoryName(name, groupField, viewLv, k));
			                        c.setCtgItemCount(search.w3GetDocumentCountInCategory(name,groupField, viewLv, k));
			                        c.setCtgLevel(viewLv);
			                        c.setThemeYn(themeYn);
			                        ctgList.add(c);
			                    }
			                    /* 카테고리 SET이 종료되면 바로 SORT 한다. */
			                    if(ctgList!=null && ctgList.size()>0){
			                        /* ITEM COUNT DESC SORT */
			                        Collections.sort(ctgList,comparator);
			                        /* EMALL 일반 전시 카테고리의 경우 개별 소트로직 적용 */
			                        if(themeYn.equals("N") && (parameter.getSiteNo().equals("6001")||parameter.getSiteNo().equals("6002")||parameter.getSiteNo().equals("6003"))){
			                            ctgList = emallSort(ctgList);
			                        }
			                    }
			                    /* 카테고리 자식찾기 ( ctgLastYn이 N인 경우에는 다음 뎁스를 무조건 가져온다고 가정하고 ) */
			                    if(ctgLast.equals("N")){
			                        viewLv = viewLv + 1;
			                        count = search.w3GetCategoryCount(name, groupField, viewLv);
			                        for(int j=0;j<ctgList.size();j++){
			                            Category c = ctgList.get(j);
			                            for(int k=0;k<count;k++){
			                                String nm = search.w3GetCategoryName(name, groupField, viewLv, k);
			                                if(nm.indexOf(c.getCtgId())>-1){
			                                    c.setHasChild(true);
			                                    ctgList.set(j, c);
			                                    break;
			                                }
			                            }
			                        }
			                    }
			                    if(themeYn.equals("N"))retCtgList.addAll(ctgList);
                                else retThemeCtgList.addAll(ctgList);
			                    result.setCtgViewCount(ctgList.size());
			                }
			            }

			            /* Result에 들어갈 카테고리( 경로정보 ) set */
			            if(!ctgLv.equals("0") && !ctgId.equals("")){
			                int lv = 0;
			                try{
			                    lv = Integer.parseInt(ctgLv);
			                }catch(NumberFormatException e){
			                    lv = 1;
			                }
			                int count = search.w3GetCategoryCount(name, ctgGroupField, lv);
			                for(int k=0;k<count;k++){
			                    String nm = search.w3GetCategoryName(name, ctgGroupField, lv, k);
			                    int itemCount = search.w3GetDocumentCountInCategory(name,ctgGroupField, lv, k);
			                    if(nm.indexOf(ctgId)>-1){
			                        if(nm.indexOf(":")>-1){
			                            int tkLv = 1;
			                            for(StringTokenizer resultToken = new StringTokenizer(nm,"\\:");resultToken.hasMoreTokens();){
			                                String[] strSplit = resultToken.nextToken().split("\\^");
			                                result.setCtgSiteNo(strSplit[0]);
	                                        result.setCtgId(tkLv, strSplit[1]);
	                                        result.setCtgNm(tkLv, strSplit[2]);
	                                        tkLv++;
			                            }
			                        }else{
			                            String[] strSplit = nm.split("\\^");
			                            result.setCtgSiteNo(strSplit[0]);
			                            result.setCtgId(lv, strSplit[1]);
			                            result.setCtgNm(lv, strSplit[2]);
			                        }
			                        result.setCtgCount(lv,itemCount);
			                    }
			                }
			            }
			            result.setCategoryList(retCtgList);
			            result.setThemeCategoryList(retThemeCtgList);
			        }
				}
				// FAQ 구성
				else if(collection.equals(COLLECTION.FAQ)){
					List<Faq> faqList = new ArrayList<Faq>();
					Faq faq;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						faq = new Faq();
						faq.setPostngId(search.w3GetField(name, "POSTNG_ID", i));
						faq.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						faq.setPostngCntt(search.w3GetField(name, "POSTNG_CNTT", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						faq.setPostngBrwsCnt(search.w3GetField(name, "POSTNG_BRWS_CNT", i));
						faqList.add(faq);
					}
					result.setFaqList(faqList);
					result.setFaqCount(search.w3GetResultTotalCount(name));
				}
				// SSG의 도서 타겟인경우에는 몰별 탭을 내려준다.
				else if(collection.equals(COLLECTION.MALL_BOOK)){
				    Map<String,String> mallCountMap = new HashMap<String,String>();
                    for(StringTokenizer st = new StringTokenizer(search.w3GetMultiGroupByResult(name, "SITE_NO"),"@");st.hasMoreTokens();){
                        String[] mallIds = st.nextToken().split("\\^");
                        mallCountMap.put(mallIds[0], mallIds[2]);
                    }
                    for(StringTokenizer st = new StringTokenizer(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"@");st.hasMoreTokens();){
                        String[] mallIds = st.nextToken().split("\\^");
                        if(mallIds[0].equals("Y")){
                            mallCountMap.put("6005", mallIds[2]);
                        }
                    }
                    result.setMallCountMap(mallCountMap);
				}
				// BOOK 구성
				else if(collection.equals(COLLECTION.BOOK)){
					List<Book> bookList = new ArrayList<Book>();
					Book book;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						book = new Book();
						String strSiteNo = search.w3GetField(name, "SITE_NO", i);
						String strItemId = search.w3GetField(name, "ITEM_ID", i);
						String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
						String strSalestrNo = "";
						String itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i);
						
						book.setSiteNo(strSiteNo);
						book.setItemId(strItemId);
						book.setItemNm(search.w3GetField(name,   "ITEM_NM", i));
						book.setAuthorNm(search.w3GetField(name, "AUTHOR_NM", i));
						book.setTrltpeNm(search.w3GetField(name, "TRLTPE_NM", i));
						book.setPubscoNm(search.w3GetField(name, "PUBSCO_NM", i));
						book.setFxprc(search.w3GetField(name, "FXPRC", i));

//						String dispCtgLclsId = search.w3GetField(name,"DISP_CTG_LCLS_ID",i);
//                        String dispCtgMclsId = search.w3GetField(name,"DISP_CTG_MCLS_ID",i);
//                        String dispCtgSclsId = search.w3GetField(name,"DISP_CTG_SCLS_ID",i);
//                        String dispCtgDclsId = search.w3GetField(name,"DISP_CTG_DCLS_ID",i);
//                        String dispCtgLclsNm = search.w3GetField(name,"DISP_CTG_LCLS_NM",i);
//                        String dispCtgMclsNm = search.w3GetField(name,"DISP_CTG_MCLS_NM",i);
//                        String dispCtgSclsNm = search.w3GetField(name,"DISP_CTG_SCLS_NM",i);
//                        String dispCtgDclsNm = search.w3GetField(name,"DISP_CTG_DCLS_NM",i);

                        String sellprc = search.w3GetField(name,"SELLPRC",i);
                        String shppTypeDtlCd = search.w3GetField(name,"SHPP_TYPE_DTL_CD",i);

//                        book.setDispCtgLclsId(dispCtgLclsId);
//                        book.setDispCtgLclsNm(dispCtgLclsNm);
//                        book.setDispCtgMclsId(dispCtgMclsId);
//                        book.setDispCtgMclsNm(dispCtgMclsNm);
//                        book.setDispCtgSclsId(dispCtgSclsId);
//                        book.setDispCtgSclsNm(dispCtgSclsNm);
//                        book.setDispCtgDclsId(dispCtgDclsId);
//                        book.setDispCtgDclsNm(dispCtgDclsNm);
                        book.setSellprc(sellprc);
                        book.setShppTypeDtlCd(shppTypeDtlCd);
                        
                        // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
                    	FrontUserInfo userInfo = parameter.getUserInfo();
                    	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
                    		if(salestrLst.indexOf("0001")>-1){
                    			int idx = 0;
                    			salestrLst = salestrLst.replace("0001,", "").trim();
                    			salestrLst = salestrLst.replace("0001", "").trim();
                    			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
                    				if(idx>1)break;
                    				strSalestrNo = st.nextToken().replace("D", "");
                    	            idx++;
                    			}
                    		}
                    		else{
                    			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
                    			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
                    		}
                    	}
                    	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
                        else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getEmSaleStrNo();
                        }else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getTrSaleStrNo();
                        }else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
                        	strSalestrNo = userInfo.getBnSaleStrNo();
                        }else{
                        	strSalestrNo = "6005";
                        }
                    	book.setSalestrNo(strSalestrNo);

						StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
                        if(srchItemIds.indexOf(ids.toString()) < 0){
                            srchItemIds.append(ids);
                        }

						bookList.add(book);
					}
					// 신세계몰의 도서 타겟인 경우에는 가격 그룹을 내려준다.
					if((target.equals(TARGET.BOOK)||target.equals(TARGET.MOBILE_BOOK)) && ( site.equals(SITE.SHINSEGAE) || site.equals(SITE.DEPARTMENT) )){
					    // PROPERTY GROUP
	                    int propct = search.w3GetCountPropertyGroup("book");
	                    int min = search.w3GetMinValuePropertyGroup("book");
	                    int max = search.w3GetMaxValuePropertyGroup("book");
	                    List<Prc> prcGroupList = new ArrayList<Prc>();
	                    Prc prc;
	                    for(int i=0;i<propct;i++){
	                        prc = new Prc();
	                        prc.setMinPrc(search.w3GetMinValueInPropertyGroup("book",i));
	                        prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup("book",i));
	                        prcGroupList.add(prc);
	                    }
	                    result.setMinPrc(min);
	                    result.setMaxPrc(max);
	                    result.setPrcGroupList(prcGroupList);
					}
					result.setBookList(bookList);
					result.setBookCount(search.w3GetResultTotalCount(name));
				}
				// RECIPE 구성
				else if(collection.equals(COLLECTION.RECIPE)){
					List<Recipe> recipeList = new ArrayList<Recipe>();
					Recipe recipe;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						recipe = new Recipe();
						recipe.setRecipeNo(search.w3GetField(name, "RECIPE_NO", i));
						recipe.setRecipeNm(search.w3GetField(name, "RECIPE_NM", i));
						recipe.setCookClsCd(search.w3GetField(name, "COOK_CLS_CD", i));
						recipe.setImgPathNm(search.w3GetField(name, "IMG_PATH_NM", i));
						recipe.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
						recipe.setImgRplcWordNm(search.w3GetField(name, "IMG_RPLC_WORD_NM", i));
						recipe.setReadyTimeCd(search.w3GetField(name, "READY_TIME_CD", i));
						recipe.setRqrmTimeCd(search.w3GetField(name, "RQRM_TIME_CD", i));
						recipe.setCookDifclvlCd(search.w3GetField(name, "COOK_DIFCLVL_CD", i));
						recipe.setRecipeWrtpeId(search.w3GetField(name, "RECIPE_WRTPE_ID", i));
						recipe.setRecipeBrwsCnt(search.w3GetField(name, "RECIPE_BRWS_CNT", i));
						recipe.setRecipeRcmdCnt(search.w3GetField(name, "RECIPE_RCMD_CNT", i));
						recipe.setEtcIngrdNm(search.w3GetField(name, "ETC_INGRD_NM", i));
						recipe.setCookTgtAgegrpCd(search.w3GetField(name, "COOK_TGT_AGEGRP_CD", i));
						recipeList.add(recipe);
					}
					result.setRecipeList(recipeList);
					result.setRecipeCount(search.w3GetResultTotalCount(name));
				}
				// PNSHOP 구성
				else if(collection.equals(COLLECTION.PNSHOP)){
					List<Pnshop> pnshopList = new ArrayList<Pnshop>();
					Pnshop pnshop;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						pnshop = new Pnshop();
						pnshop.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						pnshop.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i));
						pnshop.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i));
						pnshop.setDispCmptTypeDtlLst(search.w3GetField(name, "DISP_CMPT_TYPE_DTL_LST", i));
						pnshop.setImgFileNm1(search.w3GetField(name, "IMG_FILE_NM1", i));
						pnshop.setImgFileNm2(search.w3GetField(name, "IMG_FILE_NM2", i));
						pnshop.setImgFileNm3(search.w3GetField(name, "IMG_FILE_NM3", i));
						pnshop.setImgFileNm4(search.w3GetField(name, "IMG_FILE_NM4", i));
						pnshop.setModDts(search.w3GetField(name, "MOD_DTS", i));
						pnshop.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i));
						pnshop.setOriSiteNo(search.w3GetField(name, "ORI_SITE_NO", i));
						pnshop.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i));
						pnshop.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i));
						pnshop.setSubtitlNm1(search.w3GetField(name, "SUBTITL_NM_1", i));
						pnshop.setSubtitlNm2(search.w3GetField(name, "SUBTITL_NM_2", i));
						pnshop.setOsmuYn(search.w3GetField(name, "OSMU_YN", i));
						pnshop.setPnshopTypeCd(search.w3GetField(name, "PNSHOP_TYPE_CD", i));
						pnshopList.add(pnshop);
					}
					result.setPnshopCount(search.w3GetResultTotalCount(name));
					result.setPnshopList(pnshopList);
				}
				// PNSHOP_SD 구성
				else if(collection.equals(COLLECTION.PNSHOP_SD)){
				    List<Pnshop> pnshopSdList = new ArrayList<Pnshop>();
				    Pnshop pnshop;
				    int count = search.w3GetResultCount(name);
				    for(int i=0;i<count;i++){
				        pnshop = new Pnshop();
				        String oriSiteNo = search.w3GetField(name, "ORI_SITE_NO", i);
				        if(oriSiteNo!=null && oriSiteNo.equals("6009")){
				            pnshop.setSiteNo(search.w3GetField(name, "SITE_NO", i));
				            pnshop.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i));
				            pnshop.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i));
				            pnshop.setDispCmptTypeDtlLst(search.w3GetField(name, "DISP_CMPT_TYPE_DTL_LST", i));
				            pnshop.setImgFileNm1(search.w3GetField(name, "IMG_FILE_NM1", i));
				            pnshop.setImgFileNm2(search.w3GetField(name, "IMG_FILE_NM2", i));
				            pnshop.setImgFileNm3(search.w3GetField(name, "IMG_FILE_NM3", i));
				            pnshop.setImgFileNm4(search.w3GetField(name, "IMG_FILE_NM4", i));
				            pnshop.setModDts(search.w3GetField(name, "MOD_DTS", i));
				            pnshop.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i));
				            pnshop.setOriSiteNo(search.w3GetField(name, "ORI_SITE_NO", i));
				            pnshop.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i));
							pnshop.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i));
							pnshop.setSubtitlNm1(search.w3GetField(name, "SUBTITL_NM_1", i));
							pnshop.setSubtitlNm2(search.w3GetField(name, "SUBTITL_NM_2", i));
							pnshop.setOsmuYn(search.w3GetField(name, "OSMU_YN", i));
							pnshop.setPnshopTypeCd(search.w3GetField(name, "PNSHOP_TYPE_CD", i));
				            pnshopSdList.add(pnshop);
				        }
                    }
				    result.setPnshopSdCount(search.w3GetResultTotalCount(name));
                    result.setPnshopSdList(pnshopSdList);
				}
				// SPSHOP 구성
				else if(collection.equals(COLLECTION.SPSHOP)){
					List<SpShop> spShopList = new ArrayList<SpShop>();
					SpShop spShop;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						spShop = new SpShop();
						spShop.setItemId(search.w3GetField(name, "ITEM_ID", i));
						spShop.setItemNm(search.w3GetField(name, "ITEM_NM", i));
						spShop.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						spShop.setSellprc(search.w3GetField(name, "SELLPRC", i));
						spItemIds.append(search.w3GetField(name, "SITE_NO", i)).append(":").append(search.w3GetField(name, "ITEM_ID", i)).append(":1002").append(",");
						spShopList.add(spShop);
					}
					result.setSpShopList(spShopList);
					result.setSpShopCount(search.w3GetResultTotalCount(name));
				}
				// SHOPPING_MAGAZINE 구성
				else if(collection.equals(COLLECTION.LIFEMAGAZINE)){
					
					List<ShoppingMagazine> shoppingMagazineList = new ArrayList<ShoppingMagazine>();
					ShoppingMagazine shoppingMagazine;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						shoppingMagazine = new ShoppingMagazine();
						
						shoppingMagazine.setShpgMgzId(search.w3GetField(name, "SHPG_MGZ_ID", i));
						shoppingMagazine.setShpgMgzNm(search.w3GetField(name, "SHPG_MGZ_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						shoppingMagazine.setShpgMgzTypeCd(search.w3GetField(name, "SHPG_MGZ_TYPE_CD", i));
						shoppingMagazine.setSrchwd(search.w3GetField(name, "SRCHWD", i));
						shoppingMagazine.setDispStrtDts(search.w3GetField(name, "DISP_STRT_DTS", i));
						shoppingMagazine.setDispEndDts(search.w3GetField(name, "DISP_END_DTS", i));
						shoppingMagazine.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
						shoppingMagazine.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i));
						shoppingMagazine.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i));
						shoppingMagazine.setLnkdUrl(search.w3GetField(name, "LNKD_URL", i));
						shoppingMagazine.setBanrDesc(search.w3GetField(name, "BANR_DESC", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						shoppingMagazine.setAplSiteNoLst(search.w3GetField(name, "APL_SITE_NO_LST", i));
					
						shoppingMagazineList.add(shoppingMagazine);
					}
					result.setLifeMagazineCount(search.w3GetResultTotalCount(name));
					result.setLifeMagazineList(shoppingMagazineList);
				}
				// NOTICE 구성
				else if(collection.equals(COLLECTION.NOTICE)){
					List<Notice> NoticeList = new ArrayList<Notice>();
					Notice notice;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						notice = new Notice();
						notice.setPostngId(search.w3GetField(name, "POSTNG_ID", i));
						notice.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						notice.setWrtDate(search.w3GetField(name, "WRT_DATE", i));
						notice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						notice.setSiteNm(search.w3GetField(name, "SITE_NM", i));
						notice.setNewImg(search.w3GetField(name, "NEWIMG", i));
						notice.setPageNo(search.w3GetField(name, "PAGENO", i));
						NoticeList.add(notice);
					}
					result.setNoticeCount(search.w3GetResultCount(name));
					result.setNoticeList(NoticeList);
				}
				// BANR 구성
				else if(collection.equals(COLLECTION.BANR)){
					List<Banr> banrList = new ArrayList<Banr>();
					Banr banr;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						banr = new Banr();
						banr.setSrchCritnId(search.w3GetField(name, "SRCH_CRITN_ID", i));
						banr.setShrtcDivCd(search.w3GetField(name, "SHRTC_DIV_CD", i));
						banr.setShrtcTgtTypeCd(search.w3GetField(name, "SHRTC_TGT_TYPE_CD", i));
						banr.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
						banr.setBanrRplcTextNm(search.w3GetField(name, "BANR_RPLC_TEXT_NM", i));
						banr.setLiquorYn(search.w3GetField(name, "LIQUOR_YN", i));
						banr.setPopYn(search.w3GetField(name,"POP_YN",i));
						
						//링크뒤에 자동으로 쿼리 넘김({}있을때만 처리되도록)
						String linkUrl = search.w3GetField(name, "LINK_URL", i);
						if(linkUrl.indexOf("query={}") >-1){
							linkUrl = linkUrl.substring(0,linkUrl.indexOf("{}")) + parameter.getQuery();
						}
						banr.setLinkUrl(linkUrl);
						banrList.add(banr);
					}
					result.setBanrList(banrList);
					result.setBanrCount(search.w3GetResultTotalCount(name));
				}
				// BANR EVER 구성
				else if(collection.equals(COLLECTION.BANR_EVER)){
					List<Banr> banrEverList = new ArrayList<Banr>();
					int banrCount       = search.w3GetResultCount("banr");
					// BANR 컬렉션에 데이터가 없는 경우에만 발생한다.
					if(banrCount<1){
						Banr banr;
						int count = search.w3GetResultCount(name);
						for(int i=0;i<count;i++){
							banr = new Banr();
							banr.setSrchCritnId(search.w3GetField(name, "SRCH_CRITN_ID", i));
							banr.setShrtcDivCd(search.w3GetField(name, "SHRTC_DIV_CD", i));
							banr.setShrtcTgtTypeCd(search.w3GetField(name, "SHRTC_TGT_TYPE_CD", i));
							banr.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
							banr.setBanrRplcTextNm(search.w3GetField(name, "BANR_RPLC_TEXT_NM", i));
							banr.setLiquorYn(search.w3GetField(name, "LIQUOR_YN", i));
							banr.setPopYn(search.w3GetField(name,"POP_YN",i));
							
							//링크뒤에 자동으로 쿼리 넘김({}있을때만 처리되도록)
							String linkUrl = search.w3GetField(name, "LINK_URL", i);
							if(linkUrl.indexOf("query={}") >-1){
								linkUrl = linkUrl.substring(0,linkUrl.indexOf("{}")) + parameter.getQuery();
							}
							banr.setLinkUrl(linkUrl);
							
							// BANR의 PREFIX가 OR 조건으로 인해 정확하게 맞지 않으므로,
							// SITE_NO와 EVER_BANR_YN이 조건을 충족시키는 경우에만 추가하도록 한다.
							String everBanrYn = search.w3GetField(name, "EVER_BANR_YN", i);
							String siteNo 	  = search.w3GetField(name, "SITE_NO", i);
							if(everBanrYn!=null && everBanrYn.equals("Y") && siteNo!=null && siteNo.equals(site.getSiteNo())){
								banrEverList.add(banr);
							}
						}
						if(banrEverList.size()>0)result.setBanrList(banrEverList);
						if(banrEverList.size()>0)result.setBanrCount(banrEverList.size());
					}
				}
				// SRCHGUIDE 구성
				else if(collection.equals(COLLECTION.SRCHGUIDE)){
					List<SrchGuide> srchGuideList = new ArrayList<SrchGuide>();
					SrchGuide srchGuide;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						srchGuide = new SrchGuide();
						srchGuide.setSrchCritnId(search.w3GetField(name, "SRCH_CRITN_ID", i));
						srchGuide.setCritnSrchwdNm(search.w3GetField(name, "CRITN_SRCHWD_NM", i));
						srchGuide.setSrchTypeCd(search.w3GetField(name, "SRCH_TYPE_CD", i));
						srchGuide.setPnshop(search.w3GetField(name, "PNSHOP", i));
						srchGuide.setIssueItem(search.w3GetField(name, "ISSUE_ITEM", i));
						srchGuide.setSocial(search.w3GetField(name, "SOCIAL", i));
						srchGuide.setRecipe(search.w3GetField(name, "RECIPE", i));
						srchGuide.setTgtItem(search.w3GetField(name, "TGT_ITEM", i));
						srchGuide.setKeywdNmLst(search.w3GetField(name, "KEYWD_NM_LST", i));
						srchGuide.setItemIdLst(search.w3GetField(name, "ITEM_ID_LST", i));
						srchGuide.setShrtcTgtId(search.w3GetField(name, "SHRTC_TGT_TYPE_CD", i));
						srchGuide.setShrtcTgtTypeCd(search.w3GetField(name, "SHRTC_TGT_ID", i));
						srchGuide.setLinkUrl(search.w3GetField(name, "LINK_URL", i));
						srchGuideList.add(srchGuide);
					}
					result.setSrchGuideList(srchGuideList);
					result.setSrchGuideCount(search.w3GetResultTotalCount(name));
				}
				// SPELL 구성
				else if(collection.equals(COLLECTION.SPELL)){
					List<Spell> spellList = new ArrayList<Spell>();
					Spell spell;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						spell = new Spell();
						spell.setCritnSrchwdNm(search.w3GetField(name, "CRITN_SRCHWD_NM", i));
						spell.setRplcKeywdNm(search.w3GetField(name, "RPLC_KEYWD_NM", i));
						spellList.add(spell);
					}
					result.setSpellList(spellList);
					result.setSpellCount(search.w3GetResultTotalCount(name));
				}
				// 연관검색어 SRCHWDRL 구성
				else if(collection.equals(COLLECTION.SRCHWDRL)){
				    List<SrchwdRl> rlList = new ArrayList<SrchwdRl>();
				    SrchwdRl srchwdRl;
				    int count = search.w3GetResultCount(name);
                    for(int i=0;i<count;i++){
                        srchwdRl = new SrchwdRl();
                        srchwdRl.setSrchwdNm(search.w3GetField(name, "SRCHWD_NM", i));
                        srchwdRl.setRlKeywdNm(search.w3GetField(name, "RL_KEYWD_NM", i));
                        rlList.add(srchwdRl);
                    }
                    result.setSrchwdRlList(rlList);
                    result.setSrchwdRlCount(search.w3GetResultTotalCount(name));
				}
				// 연관검색어 SRCHWDRLDW 구성
				else if(collection.equals(COLLECTION.SRCHWDRLDW)){
					List<SrchwdRl> rlList = new ArrayList<SrchwdRl>();
					SrchwdRl srchwdRl;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						srchwdRl = new SrchwdRl();
						srchwdRl.setSrchwdNm(search.w3GetField(name, "SRCHWD_NM", i));
						srchwdRl.setRlKeywdNm(search.w3GetField(name, "RL_KEYWD_NM", i));
						rlList.add(srchwdRl);
					}
					// 기존의 연관 검색어 결과가 없는 경우에만 이 컬렉션의 결과를 사용한다.
					if(result.getSrchwdRlCount() <= 0){
						result.setSrchwdRlList(rlList);
						result.setSrchwdRlCount(search.w3GetResultTotalCount(name));
					}
				}
				// EVENT 컬렉션 구성
				else if(collection.equals(COLLECTION.EVENT)){
					List<Event> eventList = new ArrayList<Event>();
					int count = search.w3GetResultCount(name);
					Event event;
					for(int i=0;i<count;i++){
						event = new Event();
						event.setPromId(search.w3GetField(name, "PROM_ID", i));
						event.setPromNm(search.w3GetField(name, "PROM_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
						event.setPromEnfcStrtDts(search.w3GetField(name, "PROM_ENFC_STRT_DTS", i));
						event.setPromEnfcEndDts(search.w3GetField(name, "PROM_ENFC_END_DTS", i));
						event.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						event.setOfferKindCd(search.w3GetField(name, "OFFER_KIND_CD", i));
						event.setPromTypeCd(search.w3GetField(name, "PROM_TYPE_CD", i));
						event.setEvntTypeCd(search.w3GetField(name, "EVNT_TYPE_CD", i));
						event.setLinkUrl(search.w3GetField(name, "LINK_URL", i));
						eventList.add(event);
					}
					result.setEventCount(search.w3GetResultTotalCount(name));
					result.setEventList(eventList);
				}
				// P&G 등의 가상스토어 컬렉션 구성
				else if(collection.equals(COLLECTION.VIRTUAL)){
					String selectedCtgId = parameter.getDispCtgId();
					String ctgLst = search.w3GetMultiGroupByResult(name, "DISP_CTG_LST");
					// Token 형태 : ctgId^ctgNms^ctgCount@ctgId^ctgNms^ctgCount
					List<Category> ctgList  = new ArrayList<Category>();
			        List<Category> ctgList1 = new ArrayList<Category>();
			        List<Category> ctgList2 = new ArrayList<Category>();
			        for(StringTokenizer grpToken = new StringTokenizer(ctgLst,"@");grpToken.hasMoreTokens();){
						String ctgElement = grpToken.nextToken();
						if(ctgElement!=null && !ctgElement.equals("")){
							int i=0;
							Category c = new Category();
							String ctgType = "";
							String ctgCls  = "";
							String ctgLv   = "";
							for(StringTokenizer ctgToken = new StringTokenizer(ctgElement,"\\^");ctgToken.hasMoreTokens();){
								String ctg = ctgToken.nextToken();
								if(ctg!=null && !ctg.equals("")){
									// CTG_ID
									if(i == 0){
										c.setCtgId(ctg);
									}
									// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
									else if(i == 1){
										String[] ctgNms = ctg.split("\\__");
										if(ctgNms!=null && ctgNms.length>4){
											ctgLv = ctgNms[1];
											c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
											c.setCtgLevel(Integer.parseInt(ctgLv));
											c.setSiteNo(ctgNms[2]);
											c.setPriorCtgId(ctgNms[5]);
											ctgCls  = ctgNms[3];
											ctgType = ctgNms[4];
										}else{
											System.out.println("null founded" + ctg);
										}
									}
									// ITEM_COUNT
									else if(i == 2){
										c.setCtgItemCount(Integer.parseInt(ctg));
									}
								}
								if(i>0 && i%2 == 0){
									i = 0;
								}else i++;
							}
							// 현재의 사이트와 동일할 경우에만 add
							if(parameter.getSiteNo().equals(c.getSiteNo())){
								if(ctgLv.equals("3") && selectedCtgId.equals(c.getCtgId())){
									ctgList1.add(c);
								}else if(ctgLv.equals("4") && selectedCtgId.equals(c.getPriorCtgId())){
									ctgList2.add(c);
								}
							}
						}
					}
			        if(ctgList1!=null && ctgList1.size()>0 && ctgList2!=null && ctgList2.size()>0){
				        // CATEGORY LEVEL2 ID 순 SORT
				        Collections.sort(ctgList2,new Comparator<Category>() {
				        	public int compare(Category c1, Category c2){
				        		String p1 = c1.getCtgId();
				        		String p2 = c2.getCtgId();
				        		return (p1).compareTo(p2);
				        	}
				        });

				        ctgList.addAll(ctgList1);
				        ctgList.addAll(ctgList2);

				        result.setItemCount(search.w3GetResultTotalCount(name));
				        result.setCategoryList(ctgList);
			        }
				}
				//SPPRICE 구성(해바,오반장,딜상품)
				else if(collection.equals(COLLECTION.SPPRICE)){
					List<Spprice> spList = Lists.newArrayList();
					Spprice spprice;
					int count = search.w3GetResultCount(name);
					StringBuilder srchSppriceItemIds = new StringBuilder();
					for(int i=0;i<count;i++){
						spprice = new Spprice();
						spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						spprice.setItemId(search.w3GetField(name, "ITEM_ID", i));
						spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i));
						spprice.setSpType(search.w3GetField(name, "SP_TYPE", i));
						StringBuilder ids = new StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",");
						if(srchSppriceItemIds.indexOf(ids.toString()) < 0){
							if(!spprice.getSpType().equals("30")){
								srchSppriceItemIds.append(ids);
								spList.add(spprice);
							}
			        	}
					}
					
					//딜상품 추가하기 위해 한번 더 돌림(해바 +딜상품, 오반장 +딜상품 일 경우 해바, 오반장만 우선으로 보여줌)
					for(int i=0;i<count;i++){
						spprice = new Spprice();
						spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
						spprice.setItemId(search.w3GetField(name, "ITEM_ID", i));
						spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i));
						spprice.setSpType(search.w3GetField(name, "SP_TYPE", i));
						StringBuilder ids = new StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",");
						if(srchSppriceItemIds.indexOf(ids.toString()) < 0){
							if(spprice.getSpType().equals("30")){
								srchSppriceItemIds.append(ids);
								spList.add(spprice);
							}
						}
					}
					
					result.setSppriceList(spList);
					result.setSppriceItemIds(srchSppriceItemIds.toString()); //가이드와 동일한 형식(itemId만 가지고 select)
					result.setSppriceCount(spList.size());
				}
				// ISSUETHEME 구성
				else if(collection.equals(COLLECTION.ISSUETHEME)){
					List<IssueTheme> issueThemeList = Lists.newArrayList();
					IssueTheme issueTheme;
					int count = search.w3GetResultCount(name);
					for(int i=0;i<count;i++){
						issueTheme = new IssueTheme();
						issueTheme.setCuraId(search.w3GetField(name, 	"CURA_ID", i));
						issueTheme.setCuraSrchTypeCd(search.w3GetField(name, 	"CURA_SRCH_TYPE_CD", i));
						issueTheme.setSiteNo(search.w3GetField(name, 	"SITE_NO", i));
						issueTheme.setTgtSrchwdNm(search.w3GetField(name, 	"TGT_SRCHWD_NM", i));
						issueTheme.setCritnSrchwdNm(search.w3GetField(name, 	"CRITN_SRCHWD_NM", i));
						issueTheme.setRplcKeywdNm(search.w3GetField(name, 	"RPLC_KEYWD_NM", i));
						issueTheme.setItemIdLst(search.w3GetField(name, 	"ITEM_ID_LST", i));
						issueTheme.setDispOrdr(search.w3GetField(name, 	"DISP_ORDR", i));
						issueTheme.setDispStrtDts(search.w3GetField(name, 	"DISP_STRT_DTS", i));
						issueTheme.setDispEndDts(search.w3GetField(name, 	"DISP_END_DTS", i));
						issueThemeList.add(issueTheme);
					}
					result.setIssueThemeList(issueThemeList);
					result.setIssueThemeCount(search.w3GetResultTotalCount(name));
				}
			}
			// 컬렉션 수 만큼 loop 끝

			// DISP 인 경우 ITEM이냐 BOOK 이냐 결정
			if(itemResultCount > 0 || bookResultCount > 0){
				if(itemResultCount>=bookResultCount){
					result.setItemCount(itemResultCount);
					result.setItemList(itemResultList);
					result.setSrchItemIds(itemResultIds);
				}else{
					result.setItemCount(bookResultCount);
					result.setItemList(bookResultList);
					result.setSrchItemIds(bookResultIds);
				}
			}

			// SITE_NO:ITEM_ID 만
			result.setSrchItemIds(srchItemIds.toString());
			// SPSHOP
			result.setSpItemIds(spItemIds.toString());

			// 검색결과 있음 없음
			if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS) ){
				if(result.getItemCount()+result.getBookCount()+result.getRecomCount()+result.getRecipeCount()+result.getCommentCount()+result.getPnshopCount()+result.getFaqCount() > 0){
					result.setResultYn(true);
				}
			}else if(site.equals(SITE.SSG) && !target.equals(TARGET.BOOK) && !target.equals(TARGET.MOBILE_BOOK) && !target.equals(TARGET.MOBILE) ){
				if(result.getItemCount()+result.getRecomCount()+result.getRecipeCount()+result.getCommentCount()+result.getPnshopCount()+result.getFaqCount()+result.getMagazineCount()+result.getEventCount()+result.getNoticeCount() + result.getIssueThemeCount()> 0){
					result.setResultYn(true);
				}
			}else{
			    if(target.equals(TARGET.ALL)||target.equals(TARGET.MOBILE)||target.equals(TARGET.ITEM)){
		    		if(result.getItemCount()>0){
	                    result.setResultYn(true);
	                }
			    }else if(target.equals(TARGET.BOOK)||target.equals(TARGET.MOBILE_BOOK)){
		            if(result.getBookCount()>0){
		                result.setResultYn(true);
		            }
			    }
			}
        }
        return result;
	}
	private void setQueryDebug(COLLECTION collection){
		String name = collection.getCollectionAliasName(parameter);
		requestQuery.append("\n==================================================================================================================================================================================\ncollection name : ").append(name);
		if(!collection.useAlias()){
			requestQuery.append("\nw3AddCollection : ").append(name);
		}else requestQuery.append("\nw3AddAliasCollection : ").append(name).append(" , realName : ").append(collection.getCollectionRealName(parameter));
		
		String strQuery = parameterUtil.getCommonQuery(parameter);
		
		// 예약어 필터링 작업
		String prefixQuery = StringUtils.defaultIfEmpty(ActionWordPrefixMatcher.longestMatch(strQuery),"");
		String suffixQuery = StringUtils.defaultIfEmpty(ActionWordSuffixMatcher.longestMatch(strQuery),"");
		
		boolean isFilteringYn = false;
		
		// Rule 1. Suffix 와 Prefix 에 둘다 결과가 있는 경우 작동하지 않음
		if(!prefixQuery.equals("") && !suffixQuery.equals("")){
		}
		// Rule 2. Prefix 에 결과가 있는 경우
		if(!prefixQuery.equals("")){
			String tempQuery = strQuery.replaceFirst(prefixQuery, "").trim();
			requestQuery.append("\nw3SetComonQuery : ").append(tempQuery);
			if(prefixQuery.equals("매직픽업")){
				parameter.setShpp("picku");
			}else if(prefixQuery.equals("점포상품")){
				parameter.setShpp("store");
			}else if(prefixQuery.equals("점포예약")){
				parameter.setShpp("rsvt");
			}else if(prefixQuery.equals("점포택배")){
				parameter.setShpp("pack");
			}
			isFilteringYn = true;
		}
		
		// Rule 3. Suffix 에 결과가 있는 경우
		if(!suffixQuery.equals("")){
			String tempQuery = replaceLast(strQuery,suffixQuery,"").trim();
			if(!tempQuery.equals("")){
				requestQuery.append("\nw3SetComonQuery : ").append(tempQuery);
				if(suffixQuery.equals("매직픽업")){
					parameter.setShpp("picku");
				}else if(suffixQuery.equals("점포상품")){
					parameter.setShpp("store");
				}else if(suffixQuery.equals("점포예약")){
					parameter.setShpp("rsvt");
				}else if(suffixQuery.equals("점포택배")){
					parameter.setShpp("pack");
				}
				isFilteringYn = true;
			}
		}

		if(!strQuery.equals("") && !isFilteringYn){
			requestQuery.append("\nw3SetComonQuery : ").append(parameterUtil.getCommonQuery(parameter));
		}
		
		requestQuery.append("\nw3SetDocumentField : ").append(collection.getDocumentField(parameter));
		requestQuery.append("\nw3SetSearchField : ").append(collection.getSearchField(parameter));

		// Paging
		if(parameterUtil.getTarget(parameter).equals(TARGET.ALL)){
			requestQuery.append("\nw3SetPageInfo : ").append(collection).append(" pageVo ").append("0").append(" count ").append(collection.getDefaultItemCount(parameter));
		}else{
            requestQuery.append("\nw3SetPageInfo : ").append(collection).append(" pageVo ").append(parameterUtil.getPage(parameter)).append(" count ").append(parameterUtil.getCount(parameter));
		}
		if(collection.getPrefixField(parameter)!=null)requestQuery.append("\nw3SetPrefixQuery : ").append(collection.getPrefixField(parameter));
		if(collection.getFilter(parameter)!=null){
		    requestQuery.append("\nw3SetFilterQuery : ");
		    String strMinPrc = StringUtils.defaultIfEmpty(parameter.getMinPrc(), "-1");
            String strMaxPrc = StringUtils.defaultIfEmpty(parameter.getMaxPrc(), "-1");
            // 숫자로 변환되는 경우에만 보냄
            int minprc;
            int maxprc;
            try{
                minprc = Integer.parseInt(strMinPrc);
                maxprc = Integer.parseInt(strMaxPrc);
            }catch(NumberFormatException ne){
                minprc = 0;
                maxprc = 0;
            }
            if(minprc>-1 && maxprc >-1){
            	requestQuery.append("<SELLPRC:gte:").append(strMinPrc).append("><SELLPRC:lt:").append(strMaxPrc).append(">");
		    }else if(minprc>-1){
		    	requestQuery.append("<SELLPRC:gte:").append(strMinPrc).append(">");
		    }else if(maxprc>-1){
		    	requestQuery.append("<SELLPRC:lte:").append(strMaxPrc).append(">");
		    }
		}
		if(collection.getMultiGroupBy(parameter)!=null)requestQuery.append("\nw3SetMultiGroupBy : ").append(collection.getMultiGroupBy(parameter));
		if(parameterUtil.getHighlight(parameter).equals("Y"))requestQuery.append("\nw3w3SetHighlight : ").append("Y");

		// 카테고리 부스팅
        if(collection.getCategoryBoost(parameter)!=null){
            SITE site = parameterUtil.getSite(parameter);
            requestQuery.append("\nw3SetBoostCategory : ").append(name).append(", SUB_MATCH, ").append(site.getCategoryBoost());
        }

        //Set Sort
        if(collection.getCollectionSort(parameter)!=null){
            SORT sort = parameterUtil.getSort(parameter);
            if(sort.getSortName().equals("")||sort.getSortName().equals("BEST")){
                if(name.equalsIgnoreCase("book")){
                    requestQuery.append("\nw3AddSortField : ").append("RANK, 1");
                }else{
                    requestQuery.append("\nw3AddSortField : ").append("WEIGHT").append(", ").append("RANK");
                }
            }else{
                requestQuery.append("\nw3AddSortField : ").append(sort.getSortName()).append(", ").append(sort.getOrder());
            }
        }else{
            requestQuery.append("\nw3AddSortField : ").append("RANK, 1");
        }
		if(collection.getFilter(parameter)!=null){

		}

		requestQuery.append("\n==================================================================================================================================================================================");
	}
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	private String getCategoryGroupField(COLLECTION collection, Parameter parameter){
        SITE   site   = parameterUtil.getSite(parameter);
        String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
        String themeYn = StringUtils.defaultIfEmpty(parameter.getThemeYn(), "N");
        String themeCtgIds = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");
        String ctgIds = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
        if(collection.equals(COLLECTION.CATEGORY)){
            if(site.equals(SITE.SSG)){
                return "SCOM_DISP_CTG_IDX";
            }else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                if(ctgLv.equals("0")){
                    return "DISP_CTG_IDX,TEM_DISP_CTG_IDX";
                }else{
                    if(!ctgIds.equals("") || !themeCtgIds.equals("")){
                        if(ctgIds.equals("")){
                            return "TEM_DISP_CTG_IDX";
                        }else if(themeCtgIds.equals("")){
                            return "DISP_CTG_IDX";
                        }else{
                            return "DISP_CTG_IDX,TEM_DISP_CTG_IDX";
                        }
                    }else{
                        if(themeYn.equals("Y")){
                            return "TEM_DISP_CTG_IDX";
                        }else{
                            return "DISP_CTG_IDX";
                        }
                    }
                }
            }else{
                return "DISP_CTG_IDX";
            }
        }else if(collection.equals(COLLECTION.BOOK_CATEGORY)){
            return "DISP_CTG_IDX";
        }
        return "DISP_CTG_IDX";
    }
    private int[] getCategoryDepth(Parameter parameter){
        int[] depth = null;
        TARGET target  = parameterUtil.getTarget(parameter);
        SITE   site    = parameterUtil.getSite(parameter);
        String ctgLv   = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
        String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
        if(target.equals(TARGET.ALL)){
            if(site.equals(SITE.SSG)){
                return new int[]{1,2,3};
            }else{
                return new int[]{1,2};
            }
        }else if(target.equals(TARGET.CATEGORY)){
            if(ctgLv.equals("1"))return new int[]{1,2,3};
            if(ctgLv.equals("2")){
                return new int[]{2,3,4};
            }
            if(ctgLv.equals("3")){
                if(ctgLast.equals("Y")){
                    return new int[]{3};
                }else return new int[]{3,4};
            }
            if(ctgLv.equals("4")){
                return new int[]{4};
            }
            else return new int[]{1,2};
        }else if(target.equals(TARGET.BOOK)){
            if(ctgLv.equals("1"))return new int[]{1,2,3};
            if(ctgLv.equals("2"))return new int[]{2,3};
            if(ctgLv.equals("3"))return new int[]{3,4};
            if(ctgLv.equals("4"))return new int[]{4};
            else return new int[]{1,2,3};
        }else if(target.equals(TARGET.MOBILE)){
            return new int[]{1};
        }else if(target.equals(TARGET.MOBILE_BOOK)){
            return new int[]{2};
        }
        return depth;
    }
    private String getCategoryViewDepth(COLLECTION collection, Parameter parameter){
        TARGET target = parameterUtil.getTarget(parameter);
        SITE   site   = parameterUtil.getSite(parameter);
        String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
        String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
        if(collection.equals(COLLECTION.CATEGORY)||collection.equals(COLLECTION.BOOK_CATEGORY)){
            if(target.equals(TARGET.CATEGORY)){
                if(ctgLv.equals("0")){
                    if(site.equals(SITE.SSG)){
                        return "1,2";
                    }
                    return "1";
                }
                else if(ctgLv.equals("1"))return "2";
                else if(ctgLv.equals("2")){
                    if(ctgLast.equals("Y"))return "2";
                    else return "3";
                }
                else if(ctgLv.equals("3")){
                    if(ctgLast.equals("Y"))return "3";
                    else return "4";
                }
                else if(ctgLv.equals("4")){
                    if(ctgLast.equals("Y"))return "4";
                    else return "5";
                }
                return ctgLv;
            }else if(target.equals(TARGET.ALL)){
                if(site.equals(SITE.SSG)){
                    return "1,2";
                }else return "1";
            }else if(target.equals(TARGET.BOOK)){
                if(ctgLv.equals("1"))return "2";
                if(ctgLv.equals("2")){
                    if(ctgLast.equals("Y"))return "2";
                    else return "3";
                }
                if(ctgLv.equals("3")){
                    if(ctgLast.equals("Y"))return "3";
                    else return "4";
                }
                if(ctgLv.equals("4")){
                    if(ctgLast.equals("Y"))return "4";
                    else return "5";
                }
                return "2";
            }else if(target.equals(TARGET.MOBILE)){
                return "1";
            }else if(target.equals(TARGET.MOBILE_BOOK)){
                return "2";
            }
        }
        return null;
    }
    private Comparator<Category> ctgSort(){
        /* comparator */
        return new Comparator<Category>(){
            public int compare(Category c1,Category c2){
                if(c1.getCtgItemCount()>c2.getCtgItemCount()){
                    return -1;
                } else if(c1.getCtgItemCount()<c2.getCtgItemCount()){
                    return 1;
                } else{
                    return 0;
                }
            }
        };
    }
    private List<Category> emallSort(List<Category> ctgList){
        List<Category> retList = new ArrayList<Category>();
        int emIdx = -1;
        int trIdx = -1;
        int bnIdx = -1;
        int totalIdx = -1;
        for (int i=0;i<ctgList.size();i++) {
            Category c = ctgList.get(i);
            if(c.getSiteNo().equals("6001") && emIdx<0){
                emIdx = i;
            }else if(c.getSiteNo().equals("6002") && trIdx<0){
                trIdx = i;
            }else if(c.getSiteNo().equals("6003") && bnIdx<0){
                bnIdx = i;
            }
            retList.add(c);
        }

        Category em = null;
        if(emIdx>-1)em = retList.get(emIdx);
        Category tr = null;
        if(trIdx>-1)tr = retList.get(trIdx);
        Category bn = null;
        if(bnIdx>-1)bn = retList.get(bnIdx);

        // 이마트 있음
        if(emIdx > -1){
            retList.remove(em);
            retList.add(0,em);
            totalIdx = 0;
        }
        // 트레이더스 있음
        if(trIdx > -1){
            retList.remove(tr);
            totalIdx = totalIdx+1;
            retList.add(totalIdx, tr);
        }
        // 분스 있음
        if(bnIdx > -1){
            retList.remove(bn);
            retList.add(totalIdx+1, bn);
        }

        return retList;
    }
    /**
     * 상품컬렉션 SET
     * @param search
     * @param i
     * @return
     */
    private Item setItemCollection(Search search, int i, String name, FrontUserInfo userInfo){
    	Item item = new Item();
    	String strItemId = search.w3GetField(name,"ITEM_ID",i);
    	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
    	String strItemNm = search.w3GetField(name,"ITEM_NM",i);

//    	String dispCtgLclsId = search.w3GetField(name,"DISP_CTG_LCLS_ID",i);
//    	String dispCtgMclsId = search.w3GetField(name,"DISP_CTG_MCLS_ID",i);
//    	String dispCtgSclsId = search.w3GetField(name,"DISP_CTG_SCLS_ID",i);
//    	String dispCtgDclsId = search.w3GetField(name,"DISP_CTG_DCLS_ID",i);
//    	String dispCtgLclsNm = search.w3GetField(name,"DISP_CTG_LCLS_NM",i);
//    	String dispCtgMclsNm = search.w3GetField(name,"DISP_CTG_MCLS_NM",i);
//    	String dispCtgSclsNm = search.w3GetField(name,"DISP_CTG_SCLS_NM",i);
//    	String dispCtgDclsNm = search.w3GetField(name,"DISP_CTG_DCLS_NM",i);
//
//    	String temDispCtgLclsId = search.w3GetField(name,"TEM_DISP_CTG_LCLS_ID",i);
//        String temDispCtgMclsId = search.w3GetField(name,"TEM_DISP_CTG_MCLS_ID",i);
//        String temDispCtgSclsId = search.w3GetField(name,"TEM_DISP_CTG_SCLS_ID",i);
//        String temDispCtgDclsId = search.w3GetField(name,"TEM_DISP_CTG_DCLS_ID",i);
//        String temDispCtgLclsNm = search.w3GetField(name,"TEM_DISP_CTG_LCLS_NM",i);
//        String temDispCtgMclsNm = search.w3GetField(name,"TEM_DISP_CTG_MCLS_NM",i);
//        String temDispCtgSclsNm = search.w3GetField(name,"TEM_DISP_CTG_SCLS_NM",i);
//        String temDispCtgDclsNm = search.w3GetField(name,"TEM_DISP_CTG_DCLS_NM",i);

    	String sellprc     = search.w3GetField(name, "SELLPRC", i);
    	String shppTypeDtlCd = search.w3GetField(name, "SHPP_TYPE_DTL_CD", i);

    	String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
    	String exusItemDivCd = search.w3GetField(name, "EXUS_ITEM_DIV_CD", i);
    	String exusItemDtlCd = search.w3GetField(name, "EXUS_ITEM_DTL_CD", i);
    	String shppMainCd = search.w3GetField(name,"SHPP_MAIN_CD",i);
    	String shppMthdCd = search.w3GetField(name,"SHPP_MTHD_CD",i);

    	// 점포 매핑작업
    	if(strSiteNo.equals("6009")){
    		if(salestrLst.indexOf(" ")>-1){
    			int idx = 0;
    			salestrLst = salestrLst.replace("0001,", "").trim();
    			salestrLst = salestrLst.replace("0001", "").trim();
    			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
    				if(idx>1)break;
    	            item.setSalestrNo(st.nextToken().replace("D", ""));
    	            item.setSellSalestrCnt(1);
    	            idx++;
    			}
    		}
    		// 0001 때문에 사실상 이럴일이 없기는 함
    		else{
    			item.setSalestrNo(salestrLst.replaceAll("\\p{Space}", "").replace("D", ""));
    			item.setSellSalestrCnt(1);
    		}
    	}
    	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
        else if(strSiteNo.equals("6001")){
        	item.setSalestrNo(userInfo.getEmSaleStrNo());
        	item.setSellSalestrCnt(1);
        }else if(strSiteNo.equals("6002")){
        	item.setSalestrNo(userInfo.getTrSaleStrNo());
        	item.setSellSalestrCnt(1);
        }else if(strSiteNo.equals("6003")){
        	item.setSalestrNo(userInfo.getBnSaleStrNo());
        	item.setSellSalestrCnt(1);
        }else{
        	item.setSalestrNo("6005");
			item.setSellSalestrCnt(1);
        }
    	item.setSiteNo(strSiteNo);
    	item.setItemId(strItemId);
    	item.setItemNm(strItemNm);

//    	item.setDispCtgLclsId(dispCtgLclsId);
//    	item.setDispCtgLclsNm(dispCtgLclsNm);
//    	item.setDispCtgMclsId(dispCtgMclsId);
//    	item.setDispCtgMclsNm(dispCtgMclsNm);
//    	item.setDispCtgSclsId(dispCtgSclsId);
//    	item.setDispCtgSclsNm(dispCtgSclsNm);
//    	item.setDispCtgDclsId(dispCtgDclsId);
//    	item.setDispCtgDclsNm(dispCtgDclsNm);
//
//    	item.setTemDispCtgLclsId(temDispCtgLclsId);
//        item.setTemDispCtgLclsNm(temDispCtgLclsNm);
//        item.setTemDispCtgMclsId(temDispCtgMclsId);
//        item.setTemDispCtgMclsNm(temDispCtgMclsNm);
//        item.setTemDispCtgSclsId(temDispCtgSclsId);
//        item.setTemDispCtgSclsNm(temDispCtgSclsNm);
//        item.setTemDispCtgDclsId(temDispCtgDclsId);
//        item.setTemDispCtgDclsNm(temDispCtgDclsNm);

    	item.setSellprc(sellprc);
    	item.setShppTypeDtlCd(shppTypeDtlCd);
    	item.setExusItemDivCd(exusItemDivCd);
    	item.setExusItemDtlCd(exusItemDtlCd);
    	item.setShppMainCd(shppMainCd);
    	item.setShppMthdCd(shppMthdCd);
    	return item;
    }

	public void debug() {
		logger.info(requestQuery.toString());
	}
}
