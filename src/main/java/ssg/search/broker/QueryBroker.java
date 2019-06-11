package ssg.search.broker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssg.search.constant.Targets;
import ssg.search.parameter.Parameter;
import ssg.search.parameter.ParameterUtil;
import ssg.search.query.AdvertisingQueryBuilder;
import ssg.search.query.ESQueryBuilder;
import ssg.search.query.QueryBuilder;
import ssg.search.query.RecommendQueryBuilder;
import ssg.search.query.WiseQueryBuilder;
import ssg.search.query.WisenutQueryBuilder;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

/**
 * QueryBroker Class 는 엔진과 관계없이 가장 최상위의 검색을 담당하는 Class 이다.
 * Parameter 에 정의된 Target 과 Site 단위로 검색엔진을 설정하고, 검색결과를 실행한다.
 * @author 131544
 */
public class QueryBroker {
	private Logger logger = LoggerFactory.getLogger(QueryBroker.class);
	private Parameter parameter;
	private ParameterUtil parameterUtil;
	private Result result;
	private boolean debug = true;
	public QueryBroker(Parameter parameter){
        this.parameter = parameter;
    }
	/**
	 * BROKER.Broke();
	 */
	public void broke(){
		result = new Result();
		parameterUtil = new ParameterUtil();
	}
	/**
	 * getSearchAction에 정의된 SearchAction[엔진단위]에 따라
	 * QueryBuilder를 실행한다.
	 * Result Object 는 계속 addOn 된다.
	 */
	public void execute(){
		for(QueryBuilder queryBuilder : getSearchAction(parameter)){
			queryBuilder.set(parameter);
			queryBuilder.execute();
			result = queryBuilder.result(result);
			queryBuilder.close();
			queryBuilder.debug();
		}
	}
	public Result getResult(){
		return this.result;
	}
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	/**
	 * Target, Site 단위에 따라 Operation 할 Action 을 정의한다.
	 * @return
	 */
	private QueryBuilder[] getSearchAction(Parameter parameter){
		Targets targets = CollectionUtils.getTargets(parameter);
		// 검색 PC 개편 작업 이후부터는 무조건 신규
		if(parameter.getSrchVer()>1.0){
			boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
			
			if (isAdSearch){
				return new QueryBuilder[]{new AdvertisingQueryBuilder(), new RecommendQueryBuilder() , new WisenutQueryBuilder()};
			}else{
				return new QueryBuilder[]{ new RecommendQueryBuilder(), new WisenutQueryBuilder()};
			} 
		}
		// 일단 테스트용
		if(targets.equals(Targets.TASTE)){
			return new QueryBuilder[]{new RecommendQueryBuilder()};
		}
		// FAQ 신규
		if(targets.equals(Targets.FAQ)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		// BRAND BO 요청용
		if(targets.equals(Targets.BRANDMASTER)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		// 검색 품질관리 컬렉션, 가이드의 경우에는 신규 라이브러리
		if( targets.equals(targets.SRCH_QUAL_ITEM) || targets.equals(targets.ISSUETHEME)|| targets.equals(targets.CHAT_VEN_ITEMS)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		if(targets.equals(Targets.ES_VIRTUAL) || targets.equals(Targets.ES_DISP) || targets.equals(Targets.ES_DISP_ITEM) ||
				targets.equals(Targets.ES_BRAND_DISP) ||
				targets.equals(Targets.ES_PSNZ_BRAND_ITEM) ||
				targets.equals(Targets.ES_GLOBAL_BRAND) || targets.equals(Targets.ES_GLOBAL_CATEGORY) ||
				targets.equals(Targets.ES_BSHOP_DISP) || targets.equals(Targets.ES_BSHOP_DISP_ITEM) ||
				targets.equals(Targets.ES_BUNDLE) || targets.equals(Targets.ES_SPCSHOP) || targets.equals(Targets.ES_MOBILE_BRAND) ||
				targets.equals(Targets.ES_MOBILE_BRAND_ITEM)) {
			return new QueryBuilder[]{new ESQueryBuilder()};
		}
		// 현재 전시카테고리, SSG 브랜드 인덱스 매장, 모바일만 신규라이브러리에 해당
		if(targets.equals(Targets.DISP) || targets.equals(Targets.DISP_ITEM) || targets.equals(Targets.DISP_DTL)
				|| targets.equals(Targets.GLOBAL_BRAND) || targets.equals(Targets.GLOBAL_ITEM) || targets.equals(Targets.GLOBAL) || targets.equals(Targets.GLOBAL_CATEGORY)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		else if(targets.equals(Targets.BRAND_DISP)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		else if(targets.equals(Targets.MOBILE_ALL)||targets.equals(Targets.MOBILE)||targets.equals(Targets.MOBILE_ITEM)||targets.equals(Targets.MOBILE_DTL)||targets.equals(Targets.MOBILE_DTL_BRAND)
				||targets.equals(Targets.MOBILE_BOOK)||targets.equals(Targets.BOOK) || targets.equals(Targets.MOBILE_BRAND) || targets.equals(Targets.MOBILE_BRAND_ITEM) || targets.equals(Targets.MOBILE_BRAND_DTL)
				||targets.equals(Targets.MOBILE_NORESULT)||targets.equals(Targets.MOBILE_RECOM_ITEM)||targets.equals(Targets.MOBILE_PNSHOP) || targets.equals(Targets.MOBILE_BRAND_ALL) 
				||targets.equals(Targets.CHAT_SEARCH_ALL)||targets.equals(Targets.CHAT_SEARCH_ITEM)||targets.equals(Targets.CHAT_GIFT_ALL)||targets.equals(Targets.CHAT_GIFT_DTL) || targets.equals(Targets.CHAT_GIFT_ITEM)  
				||targets.equals(Targets.MOBILE_OMNI_ALL )||targets.equals(Targets.MOBILE_OMNI_DTL )||targets.equals(Targets.MOBILE_OMNI_BOOK) || targets.equals(Targets.MOBILE_OMNI_ITEM)
				||targets.equals(Targets.CHAT_OMNI_ALL )||targets.equals(Targets.CHAT_OMNI_ITEM )||targets.equals(Targets.CHAT_OMNI_DTL) 
				||targets.equals(Targets.CHAT_GIFT_OMNI_ALL )||targets.equals(Targets.CHAT_GIFT_OMNI_ITEM )||targets.equals(Targets.CHAT_GIFT_OMNI_DTL) || targets.equals(Targets.MOBILE_BRAND_OMNI_DTL)  
				 ){
			if(parameter.getAplTgtMediaCd()!=null && parameter.getAplTgtMediaCd().equals("20")){
				boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
				
				if (isAdSearch) {
					return new QueryBuilder[]{new AdvertisingQueryBuilder(), new RecommendQueryBuilder() , new WisenutQueryBuilder()};
				} else {
					return new QueryBuilder[]{ new RecommendQueryBuilder(), new WisenutQueryBuilder()};
				}
			}
		}
		else if(targets.equals(Targets.PNSHOP)){
			if(
				parameter.getSiteNo()!=null && StringUtils.contains("6004,6009", parameter.getSiteNo()) &&
				parameter.getAplTgtMediaCd()!=null && parameter.getAplTgtMediaCd().equals("20")){
				return new QueryBuilder[]{new WisenutQueryBuilder()};
			}
		}
		//신규앱(매장습격)
		else if(targets.equals(Targets.BSHOP) || targets.equals(Targets.BSHOP_BRAND) || targets.equals(Targets.BSHOP_ITEM) || targets.equals(Targets.BSHOP_DISP) || targets.equals(Targets.BSHOP_DISP_ITEM)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		//모바일내취타취
		else if(targets.equals(Targets.MOBILE_TASTE)){
			return new QueryBuilder[]{new WisenutQueryBuilder(), new RecommendQueryBuilder()};
		}
		//광고개별호출
		else if(targets.equals(Targets.AD_CPC) ||  targets.equals(Targets.AD_CPC_EXT)){
			return new QueryBuilder[]{new AdvertisingQueryBuilder()};
		}
		// 상품만 호출하는 경우 찾기
		else if(targets.equals(Targets.ITEM)){
			return new QueryBuilder[]{new WisenutQueryBuilder()};
		}
		return new QueryBuilder[]{new WiseQueryBuilder()};
	}
}
