package ssg.search.query;

import QueryAPI510.Search;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.advertising.AdvertisingCollection;
import ssg.search.collection.es.rsearch.RecommendCollection;
import ssg.search.constant.Targets;
import ssg.search.function.*;
import ssg.search.matcher.PrefixStringMatcher;
import ssg.search.matcher.SuffixStringMatcher;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Item;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WisenutQueryBuilder implements QueryBuilder{
	private Logger logger = LoggerFactory.getLogger(WisenutQueryBuilder.class);
	private Parameter parameter;
	private Search search;
	private StringBuilder requestQuery = new StringBuilder();
	private boolean debug = true;
	private String url = "";
	private int port = 7000;
	private Joiner joiner = Joiner.on(",");
	
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
			,"쓱배송"
	};
	
	final static String[] SizeDenyWords = {
			 "가방","글러브","기능성티","기모","긴팔티","남자티","내의","니트글러브","드라이핏","라운드티","롱티","맨투맨티"
			,"바람막이","바지","반팔티","백팩","상의","셔츠","속옷","수영복","운동복","웨어","윈드러너","의류","저지","정장","져지","조끼","짐볼"
			,"집업","짚업","침구","카라티","캡","크루티","트레이닝","트레이닝바지","트레이닝복","트레이닝복세트","티셔츠","패딩","팬티","플리스"
			,"하의","후드집업","후드티"
	};
	
	final static String[] recipeWords = {
			"레시피", "요리법", "만드는법"
	};

	final static String[] starfieldWords = {
			"스타필드", "하남", "하남점", "고양", "고양점", "코엑스"
	};

	final static String[] faqWords = {
			"비밀번호","아이디","회원가입","통합회원","간편가입","이름변경","맘키즈클럽","이벤트경품","결제오류","공인인증서",
			"회원탈퇴","회원정보","이벤트당첨","상품평","현금영수증","증빙서류","세금계산서발급","ok캐쉬백","신세계포인트",
			"적립금","s포켓","예치금","ssg머니","소액결제","무이자할부","계좌이체","무통장입금","비회원","환불","취소","반품",
			"교환","당일픽업","품절","상품권전환금","예치금","신세계포인트","s머니","신세계기프트카드","휴대폰결제","배송" 
	};
	
	final static String[] famSiteWords = {
			"휴무일","휴점일","쉬는날","주차","주차요금","오시는길","영업시간"
	};	
	
	final static String[] famSiteMoreWords = {
			 // 사이트명
			 "이마트","백화점","스타필드","트레이더스",
			 
			 // 백화점 점포명
			 "강남점", "경기점", "광주신세계", "김해점", "대구신세계점", "마산점", "본점", "센텀시티점", "시코르강남역점", "영등포점", "의정부점", "인천점", "충청점", "하남점",

			 // 이마트 점포명
			 "가든5점","가양점","감삼점","강릉점","검단점","경기광주점","경산점","계양점","고잔점","과천점","광교점","광명소하점",
			 "광명점","광산점","광주점","구로점","구미점","군산점","금정점","김천점","김포한강점","김해점","남양주점","남원점",
			 "대전터미널점","덕이점","도농점","동광주점","동구미점","동백점","동인천점","동탄점","동해점","둔산점","마산점","마포공덕점",
			 "만촌점","명일점","목동점","목포점","묵동점","문현점","미아점","반야월점","별내점","보라점","보령점","봉선점","부천점",
			 "부평점","분당점","사상점","사천점","산본점","상무점","상봉점","상주점","서귀포점","서부산점","서산점","서수원점","성남점",
			 "성서점","성수점","세종점","속초점","수색점","수서점","수원점","수지점","순천점","시지점","시화점","신도림점","신월점","신제주점",
			 "아산점","안동점","안성점","양산점","양재점","양주점","여수점","여의도점","여주점","역삼점","연수점","연제점","영등포점","영천점",
			 "오산점","왕십리점","용산점","용인점","울산점","원주점","월계점","월배점","은평점","의정부점","이문점","이수점","이천점","익산점",
			 "인천공항점","인천점","일산점","자양점","전주점","제주점","제천점","죽전점","중동점","진접점","진주점","창동점","창원점","천안서북점",
			 "천안점","천안터미널점","천호점","청계천점","청주점","춘천점","충주점","칠성점","킨텍스점","태백점","통영점","파주운정점","파주점",
			 "펜타포트점","평촌점","평택점","포천점","포항이동점","포항점","풍산점","하남점","하월곡점","해운대점","화성봉담점","화정점","흥덕점",
			 
			 // 트레이더스 점포명
			 "월평점", "구성점", "송림점", "서면점", "비산점", "안산점", "천안아산점", "양산점", "수원점", "킨텍스점", "하남점", "고양점", "군포점", "김포점"
	};
			
	final static SuffixStringMatcher SkipWordSuffixMatcher 		= new SuffixStringMatcher(Lists.newArrayList(SkipWords));
	final static PrefixStringMatcher ActionWordPrefixMatcher   = new PrefixStringMatcher(Lists.newArrayList(ActionWords));
	final static SuffixStringMatcher ActionWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(ActionWords));
	final static SuffixStringMatcher SizeDenyWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(SizeDenyWords));
	
	final static PrefixStringMatcher recipeWordPrefixMatcher   = new PrefixStringMatcher(Lists.newArrayList(recipeWords));
	final static SuffixStringMatcher recipeWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(recipeWords));
	final static PrefixStringMatcher starfieldWordPrefixMatcher   = new PrefixStringMatcher(Lists.newArrayList(starfieldWords));
	final static SuffixStringMatcher starfieldWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(starfieldWords));
	final static PrefixStringMatcher faqWordPrefixMatcher   = new PrefixStringMatcher(Lists.newArrayList(faqWords));
	final static SuffixStringMatcher faqWordSuffixMatcher 	= new SuffixStringMatcher(Lists.newArrayList(faqWords));
	
	/**
	 * 기본적인 값 세팅
	 */
	public void set(Parameter parameter){
		this.search 	= new Search();
		this.parameter 	= parameter;
		this.url 		= parameter.getUrl();
		if(!StringUtils.isBlank(parameter.getPort())){
			this.port = Integer.parseInt(parameter.getPort());
		}

		String  strQuery = CollectionUtils.getCommonQuery(parameter);
		Targets targets = CollectionUtils.getTargets(parameter);
		

		// 확장형 검색어 강제치환
		strQuery = CollectionUtils.replaceForced(strQuery);

		// 기본적인 Set START
		search.w3SetCodePage("utf-8");
		FrontUserInfo userInfo = parameter.getUserInfo(); 

		// sessionInfo를 set 한다.
		String ctgId 	= StringUtils.defaultIfEmpty(parameter.getCtgId(),"");
		String brandId 	= StringUtils.defaultIfEmpty(parameter.getBrand(),"");
		String mbrId = "";
		if(userInfo!=null){
			mbrId 	= StringUtils.defaultIfEmpty(userInfo.getMbrId(),"");
		}
		search.w3SetSessionInfo(ctgId,brandId,mbrId);

	    // test_request 이 Y인 쿼리 빠고는 모두 로그 남기도록 
		String testRequest = StringUtils.defaultIfEmpty(parameter.getTestRequest(), "");

		if(testRequest.equals("Y")) {
			search.w3SetQueryLog(0);
		} else {
			search.w3SetQueryLog(1);
		}
		
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
				}else if(prefixQuery.equals("퀵배송")){
					parameter.setShpp("qshpp");
				}else if(prefixQuery.equals("쓱콘")){
					parameter.setShpp("con");
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
				}else if(suffixQuery.equals("퀵배송")){
					parameter.setShpp("qshpp");
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

		for(Iterator<Collection> iter = targets.getCollectionSet(parameter).iterator();iter.hasNext();){
			Collection collection = iter.next();
			// Login 이 필요한 Collection에서 Login 여부가 N 인 컬렉션의 행동을 취하지 않음  // 개인화 컬렉션은 wisenut에서 사용안함
						if((collection instanceof LoginNecessary && !StringUtils.defaultIfEmpty(userInfo.getLoginYn(), "N").equals("Y")) || collection instanceof RecommendCollection || collection instanceof AdvertisingCollection){
				continue;
			}
			
			// Debug 를 위한 준비
			requestQuery.append("\n");
			requestQuery.append(CollectionUtils.getDivPipe());

			// Alias 여부를 파악해서 Alias or Collection Adnd
			String name = collection.getCollectionAliasName(parameter);
			String engineNm = collection.getCollectionName(parameter);
			
			if(!strQuery.equals("")){
				requestQuery.append("\nw3SetCommonQuery : ").append(strQuery);
			}

			if(!engineNm.equals(name)){
				search.w3AddAliasCollection(name, engineNm);
				requestQuery.append("\nw3AddAliasCollection : ").append(name).append(", ").append(engineNm).append("\n");
			}else{
				search.w3AddCollection(engineNm);
				requestQuery.append("\nw3AddCollection : ").append(engineNm).append("\n");
			}

			// 조회에 사용되는 DocumentField Set
			search.w3SetDocumentField(name, joiner.join(collection.getDocumentField(parameter)));
			requestQuery.append("w3SetDocumentField : ").append(joiner.join(collection.getDocumentField(parameter))).append("\n");

			// 검색 조건에 사용되는 SearchField Set
			search.w3SetSearchField(name, joiner.join(collection.getSearchField(parameter)));
			requestQuery.append("w3SetSearchField : ").append(joiner.join(collection.getSearchField(parameter))).append("\n");

			// Paging 처리를 시행해야 하는 컬렉션인 경우
			if(collection instanceof Pageable){
				Info info = ((Pageable)collection).getPage().apply(parameter);
				if(info!=null && info.getStart()>-1){
					search.w3SetPageInfo(name, info.getStart(), info.getCount());
					requestQuery.append("w3SetPageInfo : ").append(info.getStart()).append(", ").append(info.getCount()).append("\n");
				}
			}
			// 아닌경우 무조건 0 pageVo 1개 상품
			else{
				search.w3SetPageInfo(name, 0, 1);
				requestQuery.append("w3SetPageInfo : ").append("0, 1").append("\n");
			}
			// 카테고리 부스팅
			if(collection instanceof Boostable){
				Info info = ((Boostable)collection).getBoost().apply(parameter);
				if(info!=null && CollectionUtils.getOriQuery(parameter)!=null && !CollectionUtils.getOriQuery(parameter).equals("")){
					search.w3SetBoostCategory(name, info.getPredicate(), "SUB_MATCH", CollectionUtils.getOriQuery(parameter));
					requestQuery.append("w3SetBoostCategory : ").append(info.getPredicate()).append(", SUB_MATCH, ").append(CollectionUtils.getOriQuery(parameter)).append("\n");
				}
			}

			// Collection Ranking
			if(collection instanceof Rankable){
				Info info = ((Rankable)collection).getRank().apply(parameter);
				if(info!=null && CollectionUtils.getOriQuery(parameter)!=null && !CollectionUtils.getOriQuery(parameter).equals("")){
					search.w3SetRanking(name, "keyword", info.getPredicate(), 0);
					requestQuery.append("w3SetRanking : ").append(info.getPredicate()).append(", 0").append("\n");
				}
			}

			// Sort
			if(collection instanceof Sortable){
				Info info = ((Sortable)collection).getSort().apply(parameter);
				List<Sort> sortList = info.getSortList();
				// SORT 의 경우에는 다중 소팅기능을 사용하기 위해 여러개의 소팅을 차례대로 셋한다.
				if(sortList!=null && sortList.size()>0){
					for (Sort sort : sortList) {
						search.w3AddSortField(name, sort.getSortName(), sort.getOperator());
						requestQuery.append("w3AddSortField : ").append(sort.getSortName()).append(", ").append(sort.getOperator()).append("\n");
					}
				}
				// NULL 의 경우 방어
				else{
					search.w3AddSortField(name,"RANK", 1);
					requestQuery.append("w3AddSortField : ").append("RANK, 1").append("\n");
				}
			}else{
				search.w3AddSortField(name,"RANK", 1);
				requestQuery.append("w3AddSortField : ").append("RANK, 1").append("\n");
			}

			//Set Analyzer
			search.w3SetQueryAnalyzer(name,1,1,1,1);

			// Set Prefix
			if(collection instanceof Prefixable){
				Info info = ((Prefixable)collection).getPrefix().apply(parameter);
				if(info!=null && StringUtils.isNotEmpty(info.getPredicate())){
					search.w3SetPrefixQuery(name,info.getPredicate(),info.getOperator());
					requestQuery.append("w3SetPrefixQuery : ").append(info.getPredicate()).append(", ").append(info.getOperator()).append("\n");
				}
			}

			// Set Filter
			if(collection instanceof Filterable){
				Info info = ((Filterable)collection).getFilter().apply(parameter);
				if(info!=null && StringUtils.isNotEmpty(info.getPredicate())){
					search.w3SetFilterQuery(name, info.getPredicate());
					requestQuery.append("w3SetFilterQuery : ").append(info.getPredicate()).append("\n");
				}
			}

			// Set MultiGroupBy
			if(collection instanceof Groupable){
				Info info = ((Groupable)collection).getGroup().apply(parameter);
				if(info!=null && StringUtils.isNotEmpty(info.getPredicate())){
					search.w3SetMultiGroupBy(name, info.getPredicate());
					requestQuery.append("w3SetMultiGroupBy : ").append(info.getPredicate()).append("\n");
				}
			}

			// Set PropertyGroup
			if(collection instanceof PropertyGroupable){
				Info info = ((PropertyGroupable)collection).getPropertyGroup().apply(parameter);
				if(info!=null && StringUtils.isNotEmpty(info.getPredicate())){
					search.w3SetPropertyGroup(name, info.getPredicate(), 0, 1000000000, 5);
					requestQuery.append("w3SetPropertyGroup : ").append(info.getPredicate()).append(", 0, 1000000000, 5").append("\n");
				}
			}

			// Set CategoryGroupBy
			if(collection instanceof CategoryGroupable){
				Info info = ((CategoryGroupable)collection).getCategoryGroup().apply(parameter);
				String ctgIdxNm = info.getPredicate();
				if(info!=null && StringUtils.isNotEmpty(ctgIdxNm)){
					if(ctgIdxNm.indexOf(",")>-1){
						String[] idxs = ctgIdxNm.split(",");
						for(int i=0;i<idxs.length;i++){
							search.w3AddCategoryGroupBy(name, idxs[i], info.getCategoryDepth());
							requestQuery.append("w3AddCategoryGroupBy : ").append(idxs[i]).append(", ").append(info.getCategoryDepth()).append("\n");
						}
					}else{
						search.w3AddCategoryGroupBy(name, info.getPredicate(), info.getCategoryDepth());
						requestQuery.append("w3AddCategoryGroupBy : ").append(info.getPredicate()).append(", ").append(info.getCategoryDepth()).append("\n");
					}
				}
			}

			// Set Highlight, Snippet
			if(collection instanceof Highlightable && StringUtils.defaultIfEmpty(parameter.getHighlight(), "N").equals("Y")){
				if(collection instanceof Snippet){
					search.w3SetHighlight(name, 1, 1);
					requestQuery.append("w3SetHighlight : 1, 1").append("\n");
				}else{
					search.w3SetHighlight(name, 1, 0);
					requestQuery.append("w3SetHighlight : 1, 0").append("\n");
				}
			}

			requestQuery.append(CollectionUtils.getDivPipe());
		}
	}
	public void execute(){
		int ret = search.w3ConnectServer(url, port, 5000);
		search.w3ReceiveSearchQueryResult(3);
		if(ret!=0){
			requestQuery.append("\n =================================================SEARCH REQUEST TIMEOUT============================================================");
		}
	}
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

	public Result result(Result result){
		// ERROR 발생여부
		int err = search.w3GetError();
		if(err > 0){
			requestQuery.append("\n===========================================================ENGINE LIB EXCEPTION : ").append(err).append("============================================================================================ ");
		}
		if(requestQuery.indexOf("SEARCH REQUEST TIMEOUT")>-1 || requestQuery.indexOf("ENGINE LIB EXCEPTION")>-1){
			result.setLibErr(true);
		}
		if(search.w3GetError() < 1) {
			
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
					// 디스토어, a스토어는 브랜드 이므로 그대로 둔다.
					if(strQuery.indexOf("디스토어")>-1 || strQuery.indexOf("에이스토어")>-1 || strQuery.indexOf("애플스토어")>-1 || strQuery.indexOf("a스토어")>-1 || strQuery.indexOf("A스토어")>-1){
						result.setSkipResult("");
					}
					// 점포라는 단어가 존재하는 경우 그래도 둔다.
					if(strQuery.indexOf("점포")>-1){
						result.setSkipResult("");
					}
				}
				
				// 컬렉션 노출 순서 조정
				String dispOrdrRiseCollection = "";
				
				// 오늘은 E요리
				String recipePrefixQuery = StringUtils.defaultIfEmpty(recipeWordPrefixMatcher.longestMatch(strQuery),"");
				String recipeSuffixQuery = StringUtils.defaultIfEmpty(recipeWordSuffixMatcher.longestMatch(strQuery),"");
				if(!recipePrefixQuery.equals("") || !recipeSuffixQuery.equals("")){
					dispOrdrRiseCollection = "trecipe";
				}else {
					// 스타필드
					String starfieldPrefixQuery = StringUtils.defaultIfEmpty(starfieldWordPrefixMatcher.longestMatch(strQuery),"");
					String starfieldSuffixQuery = StringUtils.defaultIfEmpty(starfieldWordSuffixMatcher.longestMatch(strQuery),"");
					if(!starfieldPrefixQuery.equals("") || !starfieldSuffixQuery.equals("")){
						dispOrdrRiseCollection = "starfield";
					}else {
						// 웹사이트검색
						for(String famSiteWord : famSiteWords) {
							if(strQuery.contains(famSiteWord)) {
								for(String famSiteMoreWord: famSiteMoreWords) {
									if(strQuery.contains(famSiteMoreWord)) {
										dispOrdrRiseCollection = "fam_site";
									}
								}
							}
						}
						// FAQ
						if(dispOrdrRiseCollection.equals("")) {
							String faqPrefixQuery = StringUtils.defaultIfEmpty(faqWordPrefixMatcher.longestMatch(strQuery),"");
							String faqSuffixQuery = StringUtils.defaultIfEmpty(faqWordSuffixMatcher.longestMatch(strQuery),"");
							if(!faqPrefixQuery.equals("") || !faqSuffixQuery.equals("")){
								dispOrdrRiseCollection = "faq";
							}					
						}
					}
				}
				result.setDispOrdrRiseCollection(dispOrdrRiseCollection);
			}
			
			Targets targets = CollectionUtils.getTargets(parameter);
			// Collection 수만큼 Set Result
			for(Iterator<Collection> iter = CollectionUtils.getTargets(parameter).getCollectionSet(parameter).iterator();iter.hasNext();){
				Collection collection = iter.next();
				result = collection.getResult(search, collection.getCollectionAliasName(parameter), parameter, result);
				// Morph Result Set -> 형태소 분석결과 ( 불용어가 체크되면 분석결과를 내려보내지 않음 )
				if(collection instanceof Morphable && StringUtils.isEmpty(SizeDenyWordSuffixMatcher.longestMatch(strQuery))){
					result.setMorphResult(
						search.w3GetHighlightByField(collection.getCollectionAliasName(parameter), ((Morphable) collection).getMorph().apply(parameter).getPredicate())
					);
				}
			}
			// ResultYn SET

			// Disp Collection Item <-> Book Swap ( 전시는 도서와 상품이 혼재되어 있으므로 스왑하는 방식을 사용한다. )
			if(CollectionUtils.containsTarget(targets, Targets.DISP, Targets.BRAND_DISP , Targets.MOBILE_BRAND , Targets.MOBILE_BRAND_ITEM)){
				int itemTotalCount = result.getItemCount();
				int bookTotalCount = result.getBookCount();
				List<Item> bookItemList = result.getBookItemList();
				Map<String,String> bookMallCountMap = result.getBookMallCountMap();
				String bookItemIds = result.getBookItemIds();
				if(bookTotalCount > 0 && itemTotalCount<bookTotalCount && bookItemList!=null && bookItemList.size()>0 && bookItemIds!=null){
					result.setItemCount(bookTotalCount);
					result.setItemList(bookItemList);
					result.setSrchItemIds(bookItemIds);
					result.setMallCountMap(bookMallCountMap);
				}
				// 둘다 0 인 경우에는 mallCountMap을 비교해본다.
				else if(itemTotalCount ==0 && bookTotalCount ==0){
					Map<String,String> itemMallCountMap = result.getMallCountMap();
					if(itemMallCountMap==null && bookMallCountMap!=null){
						result.setItemCount(bookTotalCount);
						result.setItemList(bookItemList);
						result.setSrchItemIds(bookItemIds);
						result.setMallCountMap(bookMallCountMap);
					}else if(itemMallCountMap!=null && bookMallCountMap!=null){
						try{
							String ssgBookCount = bookMallCountMap.get("6005");
							String ssgItemCount = itemMallCountMap.get("6005");
							int bookCount = 0;
							int itemCount = 0;
							if(ssgItemCount!=null){
								itemCount = Integer.parseInt(ssgItemCount);
							}
							if(ssgBookCount!=null){
								bookCount = Integer.parseInt(ssgBookCount);
							}
							if(bookCount>itemCount){
								result.setItemCount(bookTotalCount);
								result.setItemList(bookItemList);
								result.setSrchItemIds(bookItemIds);
								result.setMallCountMap(bookMallCountMap);
							}
						}catch(NumberFormatException e){}
					}
				}
				// 모든 양식이 똑같지만 브랜드 인덱스 매장은 카테고리 정보가 하나 더 있음
				if(targets.equals(Targets.BRAND_DISP) ||  targets.equals(Targets.MOBILE_BRAND)){
					List<Category> bookCategoryList = result.getBookCategoryList();
					if(bookTotalCount > 0 && itemTotalCount<bookTotalCount && bookItemList!=null && bookItemList.size()>0 && bookItemIds!=null){
						result.setCategoryList(bookCategoryList);
					}
				}
			}

			if(CollectionUtils.containsTarget(targets, Targets.MOBILE, Targets.MOBILE_ALL, Targets.MOBILE_BOOK, Targets.MOBILE_ITEM, Targets.ALL, Targets.ITEM, Targets.BOOK, 
													Targets.CHAT_SEARCH_ALL, Targets.CHAT_GIFT_ALL , Targets.MOBILE_OMNI_ALL, Targets.CHAT_OMNI_ALL, Targets.CHAT_GIFT_OMNI_ALL)){
				
				int resultCount = 0;
				
				if (targets.equals(Targets.ALL)) {
					resultCount = result.getItemCount() + result.getRecomCount() + result.getSppriceCount() + result.getIssueThemeCount() 
									+ result.getBookCount() + result.getPostngCount() + result.getPnshopCount() + result.getPnshopSdCount()
									+ result.getStarfieldCount() + result.getTrecipeCount() + result.getLifeMagazineCount() + result.getEventCount()
					                + result.getFaqCount();
					//SSG,신몰,신백,이마트 경우만 SSG웹사이트 포함
					if(StringUtils.equals("6005", parameter.getSiteNo()) || StringUtils.equals("6004", parameter.getSiteNo()) || StringUtils.equals("6009", parameter.getSiteNo())
					                || StringUtils.equals("6001", parameter.getSiteNo())) {
					    resultCount = resultCount + result.getFamSiteCount();
					}
				} else if (targets.equals(Targets.MOBILE_ALL)) {
                    resultCount = result.getItemCount()  + result.getSppriceCount() + result.getIssueThemeCount() 
                                    + result.getBookCount() + result.getPnshopCount() + result.getPnshopSdCount()
                                    + result.getStarfieldCount() + result.getTrecipeCount() + result.getLifeMagazineCount();
				} else if (targets.equals(Targets.MOBILE_OMNI_ALL) || targets.equals(Targets.CHAT_OMNI_ALL)) {
				    if(StringUtils.equals("6200", parameter.getSiteNo()) || StringUtils.equals("6003", parameter.getSiteNo())) {
				        resultCount = result.getItemCount()  + result.getSppriceCount() + result.getBookCount() 
				                    + result.getStarfieldCount() + result.getTrecipeCount() + result.getLifeMagazineCount();
				    }else {
				        resultCount = result.getItemCount()  + result.getSppriceCount() + result.getBookCount() 
				                    + result.getStarfieldCount() + result.getTrecipeCount() + result.getLifeMagazineCount()
				                    + result.getPnshopCount() + result.getPnshopSdCount();
				    }
				} else if(targets.equals(Targets.MOBILE)) {
                    resultCount = result.getItemCount();
                    
                    //SSG,신몰,신백,이마트 경우만 SSG웹사이트 포함
                    if(StringUtils.equals("6005", parameter.getSiteNo()) || StringUtils.equals("6004", parameter.getSiteNo()) || StringUtils.equals("6009", parameter.getSiteNo())
                                    || StringUtils.equals("6001", parameter.getSiteNo())) {
                        resultCount = resultCount + result.getFamSiteCount();
                    }
                    
				} else if(targets.equals(Targets.MOBILE_BOOK)) {
                    resultCount = result.getBookCount();
                }else {
					resultCount = result.getItemCount() + result.getBookCount();
				}
				
				// 스타필드는 컬렉션이 스타필드 컬렉션 뿐
				if (StringUtils.equals("6400", parameter.getSiteNo())) {
					resultCount = result.getStarfieldCount();
				}
				
				if(resultCount <= 0){
					result.setResultYn(false);
				}else{
					result.setResultYn(true);
				}
				String itemIds = StringUtils.defaultIfEmpty(result.getSrchItemIds(), "");
				String bookItemIds = StringUtils.defaultIfEmpty(result.getBookItemIds(), "");
				// 해당 Target들은 ITEM과 BOOK의 결과가 뒤섞이도록 ItemIds를 조정한다.
				result.setSrchItemIds(itemIds.concat(bookItemIds));
				// BOOK 관련 타겟에서는 CategoryList를 변경해준다. ( 모바일쪽 방어로직 )
				if(CollectionUtils.containsTarget(targets, Targets.BOOK, Targets.MOBILE_BOOK)){
					if(result.getBookCount()>0){
						if(result.getBookCategoryList()!=null && result.getBookCategoryList().size()>0){
							result.setCategoryList(result.getBookCategoryList());
						}
					}
				}
			}
		}
		return result;
	}

	public void close(){
		search.w3CloseServer();
	}

	public void debug() {
		logger.info(requestQuery.toString());
	}
}
