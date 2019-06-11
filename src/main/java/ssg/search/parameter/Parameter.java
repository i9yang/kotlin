package ssg.search.parameter;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.constant.Binder;

public class Parameter {
	private Logger logger = LoggerFactory.getLogger(Parameter.class);
	public Parameter(){}
	// Parameter가 아닌 new 생성자로 생성했을 경우 Parameter에 UserInfo를 Set 할 수 있도록 함
	public Parameter(FrontUserInfo userInfo){
		this.userInfo = userInfo;
	}
	
	public Parameter(Environment environment, FrontUserInfo frontUserInfo) {
		this.initParameter(environment, frontUserInfo, environment.getProperty("web.siteNo"));
    }
	
	public Parameter(Environment environment, FrontUserInfo frontUserInfo, String siteNo) {
		this.initParameter(environment, frontUserInfo, siteNo);
    }
	
	private void initParameter(Environment environment, FrontUserInfo frontUserInfo, String siteNo) {
        this.siteNo = siteNo;
        this.userInfo = frontUserInfo;
        this.url = environment.getProperty("search.engine.url");
        this.port = environment.getProperty("search.engine.port");
        this.aplTgtMediaCd = environment.getProperty("code.aplTgtMediaCd.default");
        this.loadLevel = LOAD_LEVEL_GENERAL;
    }
	
	// FrontUserInfo
	private FrontUserInfo userInfo;
	
	// Load Level
	public final String LOAD_LEVEL_GENERAL = "10";
	public final String LOAD_LEVEL_WARNING = "20";
	public final String LOAD_LEVEL_LIMIT	= "30";
	public final String LOAD_LEVEL_EXCEPT_AD_GENERAL = "40";
	public final String LOAD_LEVEL_EXCEPT_AD_WARNING = "50";
	public final String LOAD_LEVEL_EXCEPT_AD_LIMIT = "60";

	private String loadLevel = "10";

    // Default Set
    private String target;
    private String query;

    private String count;

    private String page;

    private String url = "dev-search.ssglocal.com";

    private String ESUrl;

    private String rUrl = "http://dev-esearch.ssglocal.com:9200";

    private String port = "7000";

    private String mbrPrefix;

    private String display;
    private String viewType;
    
    private String useYn;

    // Category FilterSet
    private String ctgId;
    private String ctgLv;
    private String ctgLast;
    private String parentCtgId;

    // Category Theme
    private String themeYn;

    // Category CheckBox
    private String ctgIds;
    private String themeCtgIds;

    // Category Display Info
    private String ctgDispInfo;

    // FilterSet
    private String brand;

    private String brandId;
    private String size;
    private String color;
    private String benefit;
    private String cls;
    private String shpp;
	private String venId;
    private String splVenId;
    private String lrnkSplVenId;
    private String sizeclip;
    private String pickuSalestr;
    private String bshopId;

    // Min/ Max
    private String minPrc;
    private String maxPrc;

    // EMART MALL FILTER SITE
    private String filterSiteNo;
    private String filter;

    private String include;
    private String exclude;

    private String sort;
    
    // Typo Error
    private String typoErr;
    private String oriQueryYn;
    private String oriQuery;
    private String recomQuery;

    // HighLight
    private String highlight;
    private String salestrNo;       // 영업점
    private String ItemIds;

    // 디바이스코드
    private String aplTgtMediaCd;
    private String siteShppcstPlcyDivCd;
    private String perdcSalestrNo;

	private String webDvicDivCd;

    // 정기배송
    private String perdcRsvtShppPsblYn;
    // 클럽회원Id
    private String mbrClubId;
    // 모바일 유입경로
    private String inflow;
    // book Page Check flag
    private String bookFlag;
    private String bestFlag;

    private String siteNo;

    // 치환 키워드
    private String replaceQuery;
    private String frgShppPsblYn;

    // 검색어 가이드 전용 파라메터
    private String srchItemIds;
    private String srchRecipeIds;
    private String srchPnshopIds;
    private String srchSpPriceItemIds;
    private String guideItemIds;
    private String srchRecomItemIds;

    // 전시 파라메터
    private String dispCtgId;
    private String dispCtgSubIds;

    // 최근 검색어 seq
    private String srchSeq;

    // 기획전 구성여부 ( BRAND_SHOP 에서 배너의 존재유무를 나타냄 )
    private String pnshopYn;

    //적용범위 대상(sfc)
    private String aplRngTgtCd;

    // MAPI 여부
    private String mobileApiYn = "N";
    
    //오반장,해바 구분
    private String spType;
    
    private String shpgMgzTypeCd;	//쇼핑매거진 타입 D324 10:매거진, 20:매장습격 30:생활의발견

    // cache key
    private String rplcCacheKey;
    private String shrtcCacheKey;
    private String srchKwdCacheKey;
    private String mbrKwdCacheKey;
    private String obanjangCacheKey;
    private String newsCacheKey;
    private String spPriceCacheKey;
    private String itemCacheKey;
    private String optnCacheKey;
    private String brandCacheKey;
    private String bshopCacheKey;
    private String srchPopKwdCacheKey;
    
    //큐레이션 관련
    private String clsFilter; 		//다면분류
    private String curaId;

    private String prefixFilter;
    private String srchInspStatCd;
    private String srchStrtRegDts;
    private String srchEndRegDts;
    
    private double srchVer = 1.0;  //searchVersion
    private String apiVer;
    
    private String srchFailYn;
    
    //하우디 저널용
    private String jpage;
    private String jcount;
    
    private String srchModId;
    
    //레시피용
    private String vodYn;
    
    private String itemCacheExpireTime = "300";

    private String itemImgSize;

    private String categoryCollectionName = "item";

    // 사이즈등의 키워드관련 사용여부를 조회할때 쓰는 SRCH_OPTN 테이블의 속성값
    private String srchOptnTypeCd;

    // 메타 정보를 가져올 SQL 리스트 Binder immutable set 대상으로 한다.
    private ImmutableSet<Binder> bindKeys;

    // 엔진에만 존재하고 상품의 SQL에는 결과가 없는 경우 노출여부 -> 초기값이 true 이고 꼭 노출하고 싶지 않을 경우에만 false
    private boolean outofstockDisplayYn = true;
    
    //모바일 앱 구분
    private String mobileAppType;
    
    private String adYn = "N";	//파라메터로 광고여부를 전달 받는다 
    private String tagYn = "N";	//파라메터로 태그노출여부를 전달 받는다
    private String itemDeduplicationYn = "N";		//내취에 중복을 상품에서 제거여부

    private boolean isAdSearch = false;  //파라메터가 Y인경우도 해당사이트가 아니면 false  
    private int adSearchItemCount = 2;

	// 묶음배송
	private String grpAddrId;
	private String shppcstId;
	private String itemSiteNo;

	public String getItemSiteNo() {
		return itemSiteNo;
	}

	public void setItemSiteNo(String itemSiteNo) {
		this.itemSiteNo = itemSiteNo;
	}

	//내취타취
	private String tasteItemIds;
    private String taste;
	private String recommendYn;
	
	//히스토리상품관련 표준카테고리
	private String stdCtgIds;
	
	//상시배너엔진분리로인한 상시배너 호출여부컬럼
	private boolean isBanrEverSearch = false;
	
	// 스타필드
	private String offlineStrId;
	private String offlineBldgId;
	private String offlineFloId;
	private String sfStrLclsCtgId;
	private String sfStrMclsCtgId;
	
	// 식품상품군여부(삼성냉장고에서 사용)
	private String foodGroupYn;
	
	private String testRequest;

	public String getGrpAddrId() {
		return grpAddrId;
	}

	public void setGrpAddrId(String grpAddrId) {
		this.grpAddrId = grpAddrId;
	}

	public String getShppcstId() {
		return shppcstId;
	}

	public void setShppcstId(String shppcstId) {
		this.shppcstId = shppcstId;
	}

    public String getMbrPrefix() {
        return mbrPrefix;
    }
    public void setMbrPrefix(String mbrPrefix) {
        this.mbrPrefix = mbrPrefix;
    }
    public String getBestFlag() {
        return bestFlag;
    }

    public void setBestFlag(String bestFlag) {
        this.bestFlag = bestFlag;
    }

    public String getMbrClubId() {
        return mbrClubId;
    }

    public void setMbrClubId(String mbrClubId) {
        this.mbrClubId = mbrClubId;
    }

    public String getAplTgtMediaCd() {
        return aplTgtMediaCd;
    }

    public void setAplTgtMediaCd(String aplTgtMediaCd) {
        this.aplTgtMediaCd = aplTgtMediaCd;
    }

	public String getWebDvicDivCd() {
		return webDvicDivCd;
	}

	public void setWebDvicDivCd(String webDvicDivCd) {
		this.webDvicDivCd = webDvicDivCd;
	}

	public String getSplVenId() {
        return splVenId;
    }

    public void setSplVenId(String splVenId) {
        this.splVenId = splVenId;
    }

    public String getLrnkSplVenId() {
		return lrnkSplVenId;
	}
	public void setLrnkSplVenId(String lrnkSplVenId) {
		this.lrnkSplVenId = lrnkSplVenId;
	}
	public FrontUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(FrontUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getSrchSeq() {
        return srchSeq;
    }
    public void setSrchSeq(String srchSeq) {
        this.srchSeq = srchSeq;
    }
    public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getCtgId() {
		return ctgId;
	}
	public void setCtgId(String ctgId) {
		this.ctgId = ctgId;
	}
	public String getCtgLv() {
		return ctgLv;
	}
	public void setCtgLv(String ctgLv) {
		this.ctgLv = ctgLv;
	}
	public String getCtgLast() {
		return ctgLast;
	}
	public void setCtgLast(String ctgLast) {
		this.ctgLast = ctgLast;
	}
	public String getParentCtgId() {
		return parentCtgId;
	}
	public void setParentCtgId(String parentCtgId) {
		this.parentCtgId = parentCtgId;
	}
	public String getCtgIds() {
		return ctgIds;
	}
	public void setCtgIds(String ctgIds) {
		this.ctgIds = ctgIds;
	}
	public String getThemeCtgIds() {
        return themeCtgIds;
    }
    public void setThemeCtgIds(String themeCtgIds) {
        this.themeCtgIds = themeCtgIds;
    }
    public String getCtgDispInfo() {
		return ctgDispInfo;
	}
	public void setCtgDispInfo(String ctgDispInfo) {
		this.ctgDispInfo = ctgDispInfo;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
		this.brand = brandId;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSizeclip() {
		return sizeclip;
	}
	public void setSizeclip(String sizeclip) {
		this.sizeclip = sizeclip;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBenefit() {
		return benefit;
	}
	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	public String getExclude() {
		return exclude;
	}
	public void setExclude(String exclude) {
		this.exclude = exclude;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getHighlight() {
		return highlight;
	}
	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	public String getSalestrNo() {
		return salestrNo;
	}
	public void setSalestrNo(String salestrNo) {
		this.salestrNo = salestrNo;
	}
	public String getItemIds() {
		return ItemIds;
	}
	public void setItemIds(String itemIds) {
		ItemIds = itemIds;
	}
	public String getBookFlag() {
		return bookFlag;
	}
	public void setBookFlag(String bookFlag) {
		this.bookFlag = bookFlag;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public String getSiteShppcstPlcyDivCd() {
		return siteShppcstPlcyDivCd;
	}
	public void setSiteShppcstPlcyDivCd(String siteShppcstPlcyDivCd) {
		this.siteShppcstPlcyDivCd = siteShppcstPlcyDivCd;
	}
	public String getFilterSiteNo() {
		return filterSiteNo;
	}
	public void setFilterSiteNo(String filterSiteNo) {
		this.filterSiteNo = filterSiteNo;
	}
	public String getFrgShppPsblYn() {
		return frgShppPsblYn;
	}
	public void setFrgShppPsblYn(String frgShppPsblYn) {
		this.frgShppPsblYn = frgShppPsblYn;
	}
	public String getReplaceQuery() {
		return replaceQuery;
	}
	public void setReplaceQuery(String replaceQuery) {
		this.replaceQuery = replaceQuery;
	}
	
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getShpp() {
		return shpp;
	}
	public void setShpp(String shpp) {
		this.shpp = shpp;
	}

	public String getPerdcSalestrNo() {
		return perdcSalestrNo;
	}

	public void setPerdcSalestrNo(String perdcSalestrNo) {
		this.perdcSalestrNo = perdcSalestrNo;
	}
	public String getPerdcRsvtShppPsblYn() {
		return perdcRsvtShppPsblYn;
	}

	public void setPerdcRsvtShppPsblYn(String perdcRsvtShppPsblYn) {
		this.perdcRsvtShppPsblYn = perdcRsvtShppPsblYn;
	}

	public String getSrchItemIds() {
		return srchItemIds;
	}

	public void setSrchItemIds(String srchItemIds) {
		this.srchItemIds = srchItemIds;
	}

	public String getSrchRecipeIds() {
		return srchRecipeIds;
	}

	public void setSrchRecipeIds(String srchRecipeIds) {
		this.srchRecipeIds = srchRecipeIds;
	}

	public String getSrchPnshopIds() {
		return srchPnshopIds;
	}

	public void setSrchPnshopIds(String srchPnshopIds) {
		this.srchPnshopIds = srchPnshopIds;
	}

	public String getSrchSpPriceItemIds() {
		return srchSpPriceItemIds;
	}

	public void setSrchSpPriceItemIds(String srchSpPriceItemIds) {
		this.srchSpPriceItemIds = srchSpPriceItemIds;
	}

    public String getInflow() {
        return inflow;
    }

    public void setInflow(String inflow) {
        this.inflow = inflow;
    }
    public String getMinPrc() {
        return minPrc;
    }
    public void setMinPrc(String minPrc) {
        this.minPrc = minPrc;
    }
    public String getMaxPrc() {
        return maxPrc;
    }
    public void setMaxPrc(String maxPrc) {
        this.maxPrc = maxPrc;
    }
    public String getDispCtgId() {
        return dispCtgId;
    }
    public void setDispCtgId(String dispCtgId) {
        this.dispCtgId = dispCtgId;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseYn() {
    	return useYn;
    }
    public void setUseYn(String useYn) {
    	this.useYn = useYn;
    }
    
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
    public String getThemeYn() {
        return themeYn;
    }
    public void setThemeYn(String themeYn) {
        this.themeYn = themeYn;
    }
    public String getRplcCacheKey() {
        return rplcCacheKey;
    }
    public void setRplcCacheKey(String rplcCacheKey) {
        this.rplcCacheKey = rplcCacheKey;
    }
    public String getShrtcCacheKey() {
        return shrtcCacheKey;
    }
    public void setShrtcCacheKey(String shrtcCacheKey) {
        this.shrtcCacheKey = shrtcCacheKey;
    }
    public String getSrchKwdCacheKey() {
        return srchKwdCacheKey;
    }
    public void setSrchKwdCacheKey(String srchKwdCacheKey) {
        this.srchKwdCacheKey = srchKwdCacheKey;
    }
	public String getMbrKwdCacheKey() {
		return mbrKwdCacheKey;
	}
	public void setMbrKwdCacheKey(String mbrKwdCacheKey) {
		this.mbrKwdCacheKey = mbrKwdCacheKey;
	}
	public String getPnshopYn() {
		return pnshopYn;
	}
	public void setPnshopYn(String pnshopYn) {
		this.pnshopYn = pnshopYn;
	}
	public String getObanjangCacheKey() {
		return obanjangCacheKey;
	}
	public void setObanjangCacheKey(String obanjangCacheKey) {
		this.obanjangCacheKey = obanjangCacheKey;
	}
	public String getNewsCacheKey() {
		return newsCacheKey;
	}
	public void setNewsCacheKey(String newsCacheKey) {
		this.newsCacheKey = newsCacheKey;
	}
	public String getSpPriceCacheKey() {
		return spPriceCacheKey;
	}
	public void setSpPriceCacheKey(String spPriceCacheKey) {
		this.spPriceCacheKey = spPriceCacheKey;
	}
	public String getItemCacheKey() {
		return itemCacheKey;
	}
	public void setItemCacheKey(String itemCacheKey) {
		this.itemCacheKey = itemCacheKey;
	}
	public String getItemCacheExpireTime() {
		return itemCacheExpireTime;
	}
	public void setItemCacheExpireTime(String itemCacheExpireTime) {
		this.itemCacheExpireTime = itemCacheExpireTime;
	}
	public String getItemImgSize() {
		return itemImgSize;
	}
	public void setItemImgSize(String itemImgSize) {
		this.itemImgSize = itemImgSize;
	}
	public String getCategoryCollectionName() {
		return categoryCollectionName;
	}
	public void setCategoryCollectionName(String categoryCollectionName) {
		this.categoryCollectionName = categoryCollectionName;
	}
	public int getPageToInt() {
		return this.page!=null?Integer.parseInt(this.page):999;
	}
	public void debugString(){
		logger.info("====================================================================================================================");
		logger.info("SEARCH PARAMETER DEBUG");
		logger.info("target   : {}",this.target);
		logger.info("query    : {}",this.query);
		logger.info("pageVo     : {}",this.page);
		logger.info("count    : {}",this.count);
		logger.info("userInfo : chnlId {}, ckwhere {}, emsalestrno {}, emrsvt {}",this.userInfo.getChnlId(),this.userInfo.getCkWhere(),this.userInfo.getEmSaleStrNo(),this.userInfo.getEmRsvtShppPsblYn());
		logger.info("filterSet : filterVo {}, size {}, brand {}, benefit {}, cls {}, shpp {}, splVendId [}, lrnkSplVenId {}, minprc {}, maxprc {}",this.filter,this.size,this.brand,this.benefit,this.cls,this.shpp,this.splVenId,this.lrnkSplVenId,this.minPrc,this.maxPrc);
		logger.info("mediacd : {}",this.aplTgtMediaCd);
		logger.info("url : {}, port {}",this.url,this.port);
		logger.info("ctgInfo : dispCtgId {}, ctgId {}, ctgLv {}, ctgLast {}, parentctgId {}",this.dispCtgId,this.ctgId,this.ctgLv,this.ctgLast,this.parentCtgId);
		logger.info("filterSiteNo : {}",this.filterSiteNo);
		logger.info("====================================================================================================================");
	}
	public String getAplRngTgtCd() {
		return aplRngTgtCd;
	}
	public void setAplRngTgtCd(String aplRngTgtCd) {
		this.aplRngTgtCd = aplRngTgtCd;
	}
	public String getSrchOptnTypeCd() {
		return srchOptnTypeCd;
	}
	public void setSrchOptnTypeCd(String srchOptnTypeCd) {
		this.srchOptnTypeCd = srchOptnTypeCd;
	}
	public String getOptnCacheKey() {
		return optnCacheKey;
	}
	public void setOptnCacheKey(String optnCacheKey) {
		this.optnCacheKey = optnCacheKey;
	}
	public boolean isOutofstockDisplayYn() {
		return outofstockDisplayYn;
	}
	public void setOutofstockDisplayYn(boolean outofstockDisplayYn) {
		this.outofstockDisplayYn = outofstockDisplayYn;
	}
	public ImmutableSet<Binder> getBindKeys() {
		return bindKeys;
	}
	public void setBindKeys(ImmutableSet<Binder> bindKeys) {
		this.bindKeys = bindKeys;
	}
	public String getMobileApiYn(){
		return mobileApiYn;
	}
	public void setMobileApiYn(String mobileApiYn){
		this.mobileApiYn = mobileApiYn;
	}
	public String getGuideItemIds(){
		return guideItemIds;
	}
	public void setGuideItemIds(String guideItemIds){
		this.guideItemIds = guideItemIds;
	}
	public String getBrandCacheKey() {
		return brandCacheKey;
	}
	public void setBrandCacheKey(String brandCacheKey) {
		this.brandCacheKey = brandCacheKey;
	}
	public String getPickuSalestr() {
		return pickuSalestr;
	}
	public void setPickuSalestr(String pickuSalestr) {
		this.pickuSalestr = pickuSalestr;
	}
	public String getBshopId() {
		return bshopId;
	}
	public void setBshopId(String bshopId) {
		this.bshopId = bshopId;
	}
	public String getBshopCacheKey() {
		return bshopCacheKey;
	}
	public void setBshopCacheKey(String bshopCacheKey) {
		this.bshopCacheKey = bshopCacheKey;
	}
	public String getViewType(){
		return viewType;
	}
	public void setViewType(String viewType){
		this.viewType = viewType;
	}
	public String getTypoErr(){
		return typoErr;
	}
	public void setTypoErr(String typoErr){
		this.typoErr = typoErr;
	}
	public String getOriQueryYn(){
		return oriQueryYn;
	}
	public void setOriQueryYn(String oriQueryYn){
		this.oriQueryYn = oriQueryYn;
	}
	public String getOriQuery(){
		return oriQuery;
	}
	public void setOriQuery(String oriQuery){
		this.oriQuery = oriQuery;
	}
	public String getRecomQuery(){
		return recomQuery;
	}
	public void setRecomQuery(String recomQuery){
		this.recomQuery = recomQuery;
	}
	public String getLoadLevel(){
		return loadLevel;
	}
	public void setLoadLevel(String loadLevel){
		this.loadLevel = loadLevel;
	}
	public String getSpType() {
		return spType;
	}
	public void setSpType(String spType) {
		this.spType = spType;
	}
	public String getClsFilter() {
		return clsFilter;
	}
	public void setClsFilter(String clsFilter) {
		this.clsFilter = clsFilter;
	}
	public String getPrefixFilter() {
		return prefixFilter;
	} 
	public void setPrefixFilter(String prefixFilter) {
		this.prefixFilter = prefixFilter;
	}
	public String getSrchInspStatCd() {
		return srchInspStatCd;
	}
	public void setSrchInspStatCd(String srchInspStatCd) {
		this.srchInspStatCd = srchInspStatCd;
	}
	public String getSrchStrtRegDts() {
		return srchStrtRegDts;
	}
	public void setSrchStrtRegDts(String srchStrtRegDts) {
		this.srchStrtRegDts = srchStrtRegDts;
	}
	public String getSrchEndRegDts() {
		return srchEndRegDts;
	}
	public void setSrchEndRegDts(String srchEndRegDts) {
		this.srchEndRegDts = srchEndRegDts;
	}
	public String getESUrl() {
		return ESUrl;
	}
	public void setESUrl(String ESUrl) {
		this.ESUrl = ESUrl;
	}
	public String getShpgMgzTypeCd() {
		return shpgMgzTypeCd;
	}
	public void setShpgMgzTypeCd(String shpgMgzTypeCd) {
		this.shpgMgzTypeCd = shpgMgzTypeCd;
	}
	public String getSrchPopKwdCacheKey() {
		return srchPopKwdCacheKey;
	}
	public void setSrchPopKwdCacheKey(String srchPopKwdCacheKey) {
		this.srchPopKwdCacheKey = srchPopKwdCacheKey;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public String getCuraId() {
		return curaId;
	}
	public void setCuraId(String curaId) {
		this.curaId = curaId;
	}
	public double getSrchVer() {
		return srchVer;
	}
	public void setSrchVer(double srchVer) {
		this.srchVer = srchVer;
	}
	public String getSrchFailYn() {
		return srchFailYn;
	}
	public void setSrchFailYn(String srchFailYn) {
		this.srchFailYn = srchFailYn;
	}
	public String getJpage() {
		return jpage;
	}
	public void setJpage(String jpage) {
		this.jpage = jpage;
	}
	public String getJcount() {
		return jcount;
	}
	public void setJcount(String jcount) {
		this.jcount = jcount;
	}
	public String getMobileAppType() {
		return mobileAppType;
	}
	public void setMobileAppType(String mobileAppType) {
		this.mobileAppType = mobileAppType;
	}
	public String getApiVer() {
		return apiVer;
	}
	public void setApiVer(String apiVer) {
		this.apiVer = apiVer;
	}
	public String getSrchModId() {
		return srchModId;
	}
	public void setSrchModId(String srchModId) {
		this.srchModId = srchModId;
	}
	public String getAdYn() {
		return adYn;
	}
	public void setTagYn(String tagYn) {
		this.tagYn = tagYn;
	}
	public String getTagYn() {
		return tagYn;
	}
	public void setAdYn(String adYn) {
		this.adYn = adYn;
	}
	public boolean isAdSearch() {
		return isAdSearch;
	}
	public void setAdSearch(boolean isAdSearch) {
		this.isAdSearch = isAdSearch;
	}
	public int getAdSearchItemCount() {
		return adSearchItemCount;
	}
	public void setAdSearchItemCount(int adSearchItemCount) {
		this.adSearchItemCount = adSearchItemCount;
	}
	public String getItemDeduplicationYn() {
		return itemDeduplicationYn;
	}
	public void setItemDeduplicationYn(String itemDeduplicationYn) {
		this.itemDeduplicationYn = itemDeduplicationYn;
	}
	public String getVodYn() {
		return vodYn;
	}
	public void setVodYn(String vodYn) {
		this.vodYn = vodYn;
	}
		public String getSrchRecomItemIds() {
		return srchRecomItemIds;
	}
	public void setSrchRecomItemIds(String srchRecomItemIds) {
		this.srchRecomItemIds = srchRecomItemIds;
	}
	
	public String getTasteItemIds() {
		return tasteItemIds;
	}
	public void setTasteItemIds(String tasteItemIds) {
		this.tasteItemIds = tasteItemIds;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	public String getRecommendYn() {
		return recommendYn;
	}
	public void setRecommendYn(String recommendYn) {
		this.recommendYn = recommendYn;
	}
	public boolean isAllowCode(String code) {
		if (this.LOAD_LEVEL_GENERAL.equals(code) || this.LOAD_LEVEL_WARNING.equals(code) || this.LOAD_LEVEL_LIMIT.equals(code) 
				|| this.LOAD_LEVEL_EXCEPT_AD_GENERAL.equals(code) || this.LOAD_LEVEL_EXCEPT_AD_WARNING.equals(code) || this.LOAD_LEVEL_EXCEPT_AD_LIMIT.equals(code)) {
			return true;
		}
		return false;
	}
	public String getrUrl() {
		return rUrl;
	}
	public void setrUrl(String rUrl) {
		this.rUrl = rUrl;
	}
	public String getStdCtgIds() {
		return stdCtgIds;
	}
	public void setStdCtgIds(String stdCtgIds) {
		this.stdCtgIds = stdCtgIds;
	}
	public boolean isBanrEverSearch() {
		return isBanrEverSearch;
	}
	public void setBanrEverSearch(boolean isBanrEverSearch) {
		this.isBanrEverSearch = isBanrEverSearch;
	}
	public String getOfflineStrId() {
		return offlineStrId;
	}
	public void setOfflineStrId(String offlineStrId) {
		this.offlineStrId = offlineStrId;
	}
	public String getOfflineBldgId() {
		return offlineBldgId;
	}
	public void setOfflineBldgId(String offlineBldgId) {
		this.offlineBldgId = offlineBldgId;
	}
	public String getOfflineFloId() {
		return offlineFloId;
	}
	public void setOfflineFloId(String offlineFloId) {
		this.offlineFloId = offlineFloId;
	}
	public String getSfStrLclsCtgId() {
		return sfStrLclsCtgId;
	}
	public void setSfStrLclsCtgId(String sfStrLclsCtgId) {
		this.sfStrLclsCtgId = sfStrLclsCtgId;
	}
	public String getSfStrMclsCtgId() {
		return sfStrMclsCtgId;
	}
	public void setSfStrMclsCtgId(String sfStrMclsCtgId) {
		this.sfStrMclsCtgId = sfStrMclsCtgId;
	}
	public String getTestRequest() {
		return testRequest;
	}
	public void setTestRequest(String testRequest) {
		this.testRequest = testRequest;
	}
	public String getDispCtgSubIds() {
		return dispCtgSubIds;
	}
	public void setDispCtgSubIds(String dispCtgSubIds) {
		this.dispCtgSubIds = dispCtgSubIds;
	}
	public String getFoodGroupYn() {
		return foodGroupYn;
	}
	public void setFoodGroupYn(String foodGroupYn) {
		this.foodGroupYn = foodGroupYn;
	}
}
