package ssg.search.result;

import ssg.search.constant.EsErrCode;

import java.util.List;
import java.util.Map;

public class Result{
	private List<Item> itemList;				// Item 정보
    private List<Category> categoryList;		// Category 정보
    private List<Category> bookCategoryList;		// Category 정보
    private List<Category> themeCategoryList;   // Theme Category 정보
    private List<Brand> brandList;				// Brand 정보
    private List<Comment> commentList;			// 상품평 정보
    private List<Faq> faqList;					// FAQ 정보
    private List<Pnshop> pnshopList;			// 기획전 정보 (일반)
    private List<Pnshop> pnshopSdList; 			// 기획전 정보 (백화점)
    private List<Recipe> recipeList;			// 레시피 정보
    private List<SpShop> spShopList;			// 샤넬 정보
    private List<Recom> recomList;				// 추천상품 정보

	private List<Book>	 bookList;				// 도서 정보
    private List<Banr>	 banrList;				// 배너 정보
    private List<SrchGuide> srchGuideList;		// 검색어 가이드 정보

    private List<Spell> spellList;				// 오타보정 정보
    private List<SrchwdRl> srchwdRlList;        // 연관검색어 정보

    private List<Postng> postngList;

    private List<Item> brandBestItemList;		// BrandBest ( Brand shop 사용 )
    private List<Item> ctgBrandBestItemList;	// ctgBrandBest ( Brand shop 사용 )
    private List<Item> bookItemList;			// Book 컬렉션이지만 Item 컬렉션처럼 Set ( Disp 사용 )
    private List<Item> noResultItemRecomList;	// 검색결과 없음 추천상품

    private List<Event> eventList;						// 이벤트 리스트
    private List<Magazine> magazineList;				// 라이프매거진 리스트
    private List<Notice> noticeList;						// 공지사항 리스트
	private List<FamSite> famSiteList;						// 패밀리 사이트 리스트

    private List<ShoppingMagazine> lifeMagazineList;				// 쇼핑매거진 리스트
    private List<BrandMaster> brandMasterList;						// PO,BO 브랜드 마스터 정보
    
    private Map<String,String> mallCountMap;	// 몰별 카운트 정보
    private Map<String,String> bookMallCountMap;	// 몰별 카운트 정보

    private List<Map<String,List<Category>>> brandCategoryList;
    private Map<String,String> brandSelectMap;
    private List<MobileCategory> commCategoryList;			//MAPI 개편에서 사용
    private List<Map<String,String>> recomCategoryList;		//MAPI 개편에서 사용
	private List<Map<String,String>> recomDispCategoryList;		// 신규 카테고리 추천로직에서 사용
	private Map<String,String> virtualCategoryMap;
	private String virtualCtgId;
	
	private List<Advertising> advertisingList;	//광고상품
	private int advertisingCount;
	
	private List<BanrAdvertising> banrAdvertisingList;	//배너광고
	private int banrAdvertisingCount;
	

    private boolean resultYn;					// 검색 결과 있음/없음

    private List<Prc> prcGroupList;             // 가격 그룹 리스트
    private int       minPrc;                   // 그룹내 최소값
    private int       maxPrc;                   // 그룹내 최대값
    
    private String srchItemIds;
    private String bookItemIds;
    private String brandBestIds;
    private String ctgBrandBestIds;
    private String noResultItemRecomIds;

    private String ctgSiteNo;
    private String lctgId;
    private String lctgNm;
    private int    lctgCount;
    private String mctgId;
    private String mctgNm;
    private int    mctgCount;
    private String sctgId;
    private String sctgNm;
    private int    sctgCount;
    private String dctgId;
    private String dctgNm;
    private int    dctgCount;
    private int    ctgViewCount;

    private int noResultItemCount;
    private int itemCount;
    private int pnshopCount;
    private int pnshopSdCount;
    private int postngCount;
    private int faqCount;
    private int recipeCount;
    private int spShopCount;
    private int bookCount;
    private int banrCount;
    private int commentCount;
    private int recomCount;
    private int spellCount;
    private int srchGuideCount;
    private int srchwdRlCount;
    private int eventCount;
    private int magazineCount;
    private int noticeCount;
    private int lifeMagazineCount;
    private int brandMasterCount;
    private int famSiteCount;

    private String spItemIds;           // SPSHOP
    private String purchItemIds;		// 구매한 상품

    private String colectId;

    private String morphResult;			// 형태소 분석결과 저장
    private String skipResult;				// 부가검색어 인식
    private String useSizeYn;			// 사이즈 필터링 검색 노출 여부
    private List<Size> sizeList;		// 사이즈 그루핑 결과 리스트
    
    private List<Bshop> bshopList; 			//bshop 리스트
    private List<Bshop> bshopRstList;		//bshop 리스트
    private int bshopCount; 
    
    private List<Spprice> sppriceList; 		//오반장,해바 리스트
    private int sppriceCount;
    private String sppriceItemIds;
    
    private List<Store> storeList;			//store(전문관)리스트
    private int storeCount;
	private int elapsedTime;
	
	private List<IssueTheme> issueThemeList;//이슈테마 리스트
	private int issueThemeCount;

	private List<Trecipe> trecipeList;
	private int trecipeCount;
 
	private String recomItemIds; //추천상품 아이디
 
    private boolean libErr = false;				// 라이브러리 에러여부
    private EsErrCode esErrCode;				// 엘라스틱 에러 코드

    private int banrGcount;		//일반배너 카운트
    private int banrEcount;		//상시배너 카운트

    private String taste;
    //타취결과
    private String publicTasteIds;				
    private List<Taste> publicTasteList;				// 내취결과
    //내취결과
    private String myTasteIds;					
    private List<Taste> myTasteList;					// 타취결과
    
    private List<Starfield> starfieldList; 	// 스타필드 리스트
    private int starfieldCount;				// 스타필드 카운트
    
    //브랜드필터노출여부 판단
    private String brandFilterDispYn;

    // 노출우선순위 조정 컬렉션
    private String dispOrdrRiseCollection;
    
	public void setCtgId(int lv,String ctgId){
		if(lv==1)this.lctgId = ctgId;
		else if(lv==2)this.mctgId = ctgId;
		else if(lv==3)this.sctgId = ctgId;
		else if(lv==4)this.dctgId = ctgId;
	}
	public void setCtgNm(int lv,String ctgNm){
		if(lv==1)this.lctgNm = ctgNm;
		else if(lv==2)this.mctgNm = ctgNm;
		else if(lv==3)this.sctgNm = ctgNm;
		else if(lv==4)this.dctgNm = ctgNm;
	}
	public void setCtgCount(int lv,int count){
		if(lv==1)this.lctgCount = count;
		else if(lv==2)this.mctgCount = count;
		else if(lv==3)this.sctgCount = count;
		else if(lv==4)this.dctgCount = count;
	}

	public String getCtgSiteNo() {
		return ctgSiteNo;
	}
	public void setCtgSiteNo(String ctgSiteNo) {
		this.ctgSiteNo = ctgSiteNo;
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public List<Category> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	public List<Category> getBookCategoryList(){
		return bookCategoryList;
	}
	public void setBookCategoryList(List<Category> bookCategoryList){
		this.bookCategoryList = bookCategoryList;
	}
	public List<Category> getThemeCategoryList() {
        return themeCategoryList;
    }
    public void setThemeCategoryList(List<Category> themeCategoryList) {
        this.themeCategoryList = themeCategoryList;
    }
    public List<Brand> getBrandList() {
		return brandList;
	}
	public void setBrandList(List<Brand> brandList) {
		this.brandList = brandList;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public List<Faq> getFaqList() {
		return faqList;
	}
	public void setFaqList(List<Faq> faqList) {
		this.faqList = faqList;
	}
	public List<Pnshop> getPnshopList() {
		return pnshopList;
	}
	public void setPnshopList(List<Pnshop> pnshopList) {
		this.pnshopList = pnshopList;
	}
	public List<Pnshop> getPnshopSdList() {
		return pnshopSdList;
	}
	public void setPnshopSdList(List<Pnshop> pnshopSdList) {
		this.pnshopSdList = pnshopSdList;
	}
	public List<Recipe> getRecipeList() {
		return recipeList;
	}
	public void setRecipeList(List<Recipe> recipeList) {
		this.recipeList = recipeList;
	}
	public List<SpShop> getSpShopList() {
		return spShopList;
	}
	public void setSpShopList(List<SpShop> spShopList) {
		this.spShopList = spShopList;
	}
	public List<Recom> getRecomList() {
		return recomList;
	}
	public void setRecomList(List<Recom> recomList) {
		this.recomList = recomList;
	}
	public List<Book> getBookList() {
		return bookList;
	}
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	public List<Banr> getBanrList() {
		return banrList;
	}
	public void setBanrList(List<Banr> banrList) {
		this.banrList = banrList;
	}
	public List<Spell> getSpellList() {
		return spellList;
	}
	public void setSpellList(List<Spell> spellList) {
		this.spellList = spellList;
	}
	public Map<String, String> getMallCountMap() {
		return mallCountMap;
	}
	public void setMallCountMap(Map<String, String> mallCountMap) {
		this.mallCountMap = mallCountMap;
	}
	public boolean isResultYn() {
		return resultYn;
	}
	public void setResultYn(boolean resultYn) {
		this.resultYn = resultYn;
	}
	public String getLctgId() {
		return lctgId;
	}
	public void setLctgId(String lctgId) {
		this.lctgId = lctgId;
	}
	public String getLctgNm() {
		return lctgNm;
	}
	public void setLctgNm(String lctgNm) {
		this.lctgNm = lctgNm;
	}
	public int getLctgCount() {
		return lctgCount;
	}
	public void setLctgCount(int lctgCount) {
		this.lctgCount = lctgCount;
	}
	public String getMctgId() {
		return mctgId;
	}
	public void setMctgId(String mctgId) {
		this.mctgId = mctgId;
	}
	public String getMctgNm() {
		return mctgNm;
	}
	public void setMctgNm(String mctgNm) {
		this.mctgNm = mctgNm;
	}
	public int getMctgCount() {
		return mctgCount;
	}
	public void setMctgCount(int mctgCount) {
		this.mctgCount = mctgCount;
	}
	public String getSctgId() {
		return sctgId;
	}
	public void setSctgId(String sctgId) {
		this.sctgId = sctgId;
	}
	public String getSctgNm() {
		return sctgNm;
	}
	public void setSctgNm(String sctgNm) {
		this.sctgNm = sctgNm;
	}
	public int getSctgCount() {
		return sctgCount;
	}
	public void setSctgCount(int sctgCount) {
		this.sctgCount = sctgCount;
	}
	public String getDctgId() {
		return dctgId;
	}
	public void setDctgId(String dctgId) {
		this.dctgId = dctgId;
	}
	public String getDctgNm() {
		return dctgNm;
	}
	public void setDctgNm(String dctgNm) {
		this.dctgNm = dctgNm;
	}
	public int getDctgCount() {
		return dctgCount;
	}
	public void setDctgCount(int dctgCount) {
		this.dctgCount = dctgCount;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public int getPnshopCount() {
		return pnshopCount;
	}
	public void setPnshopCount(int pnshopCount) {
		this.pnshopCount = pnshopCount;
	}
	public int getPnshopSdCount() {
		return pnshopSdCount;
	}
	public void setPnshopSdCount(int pnshopSdCount) {
		this.pnshopSdCount = pnshopSdCount;
	}
	public int getFaqCount() {
		return faqCount;
	}
	public void setFaqCount(int faqCount) {
		this.faqCount = faqCount;
	}
	public int getRecipeCount() {
		return recipeCount;
	}
	public void setRecipeCount(int recipeCount) {
		this.recipeCount = recipeCount;
	}
	public int getSpShopCount() {
		return spShopCount;
	}
	public void setSpShopCount(int spShopCount) {
		this.spShopCount = spShopCount;
	}
	public int getBookCount() {
		return bookCount;
	}
	public void setBookCount(int bookCount) {
		this.bookCount = bookCount;
	}
	public int getBanrCount() {
		return banrCount;
	}
	public void setBanrCount(int banrCount) {
		this.banrCount = banrCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getRecomCount() {
		return recomCount;
	}
	public void setRecomCount(int recomCount) {
		this.recomCount = recomCount;
	}
	public int getSpellCount() {
		return spellCount;
	}
	public void setSpellCount(int spellCount) {
		this.spellCount = spellCount;
	}
	public List<SrchGuide> getSrchGuideList() {
		return srchGuideList;
	}
	public void setSrchGuideList(List<SrchGuide> srchGuideList) {
		this.srchGuideList = srchGuideList;
	}
	public int getSrchGuideCount() {
		return srchGuideCount;
	}
	public void setSrchGuideCount(int srchGuideCount) {
		this.srchGuideCount = srchGuideCount;
	}
    public String getSrchItemIds() {
        return srchItemIds;
    }
    public void setSrchItemIds(String srchItemIds) {
        this.srchItemIds = srchItemIds;
    }
    public List<Postng> getPostngList() {
        return postngList;
    }
    public void setPostngList(List<Postng> postngList) {
        this.postngList = postngList;
    }
    public int getPostngCount() {
        return postngCount;
    }
    public void setPostngCount(int postngCount) {
        this.postngCount = postngCount;
    }
    public String getSpItemIds() {
        return spItemIds;
    }
    public void setSpItemIds(String spItemIds) {
        this.spItemIds = spItemIds;
    }
    public List<SrchwdRl> getSrchwdRlList() {
        return srchwdRlList;
    }
    public void setSrchwdRlList(List<SrchwdRl> srchwdRlList) {
        this.srchwdRlList = srchwdRlList;
    }
    public int getSrchwdRlCount() {
        return srchwdRlCount;
    }
    public void setSrchwdRlCount(int srchwdRlCount) {
        this.srchwdRlCount = srchwdRlCount;
    }
    public List<Prc> getPrcGroupList() {
        return prcGroupList;
    }
    public void setPrcGroupList(List<Prc> prcGroupList) {
        this.prcGroupList = prcGroupList;
    }
    public int getMinPrc() {
        return minPrc;
    }
    public void setMinPrc(int minPrc) {
        this.minPrc = minPrc;
    }
    public int getMaxPrc() {
        return maxPrc;
    }
    public void setMaxPrc(int maxPrc) {
        this.maxPrc = maxPrc;
    }
	public String getBrandBestIds() {
		return brandBestIds;
	}
	public void setBrandBestIds(String brandBestIds) {
		this.brandBestIds = brandBestIds;
	}
	public String getCtgBrandBestIds() {
		return ctgBrandBestIds;
	}
	public void setCtgBrandBestIds(String ctgBrandBestIds) {
		this.ctgBrandBestIds = ctgBrandBestIds;
	}
	public int getCtgViewCount() {
		return ctgViewCount;
	}
	public void setCtgViewCount(int ctgViewCount) {
		this.ctgViewCount = ctgViewCount;
	}
	public List<Item> getBrandBestItemList() {
		return brandBestItemList;
	}
	public void setBrandBestItemList(List<Item> brandBestItemList) {
		this.brandBestItemList = brandBestItemList;
	}
	public List<Item> getCtgBrandBestItemList() {
		return ctgBrandBestItemList;
	}
	public void setCtgBrandBestItemList(List<Item> ctgBrandBestItemList) {
		this.ctgBrandBestItemList = ctgBrandBestItemList;
	}
	public String getPurchItemIds() {
		return purchItemIds;
	}
	public void setPurchItemIds(String purchItemIds) {
		this.purchItemIds = purchItemIds;
	}
	public List<Event> getEventList() {
		return eventList;
	}
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
	public int getEventCount() {
		return eventCount;
	}
	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}
	public List<Magazine> getMagazineList() {
		return magazineList;
	}
	public void setMagazineList(List<Magazine> magazineList) {
		this.magazineList = magazineList;
	}
	public List<Notice> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}
	public int getMagazineCount() {
		return magazineCount;
	}
	public void setMagazineCount(int magazineCount) {
		this.magazineCount = magazineCount;
	}
	public int getNoticeCount() {
		return noticeCount;
	}
	public void setNoticeCount(int noticeCount) {
		this.noticeCount = noticeCount;
	}
	public String getColectId() {
		return colectId;
	}
	public void setColectId(String colectId) {
		this.colectId = colectId;
	}
	public String getUseSizeYn() {
		return useSizeYn;
	}
	public void setUseSizeYn(String useSizeYn) {
		this.useSizeYn = useSizeYn;
	}
	public List<Size> getSizeList() {
		return sizeList;
	}
	public void setSizeList(List<Size> sizeList) {
		this.sizeList = sizeList;
	}
	public String getMorphResult() {
		return morphResult;
	}
	public void setMorphResult(String morphResult) {
		this.morphResult = morphResult;
	}
	public List<Item> getBookItemList() {
		return bookItemList;
	}
	public void setBookItemList(List<Item> bookItemList) {
		this.bookItemList = bookItemList;
	}
	public String getBookItemIds() {
		return bookItemIds;
	}
	public void setBookItemIds(String bookItemIds) {
		this.bookItemIds = bookItemIds;
	}
	public Map<String, String> getBookMallCountMap() {
		return bookMallCountMap;
	}
	public void setBookMallCountMap(Map<String, String> bookMallCountMap) {
		this.bookMallCountMap = bookMallCountMap;
	}
	public boolean isLibErr() {
		return libErr;
	}
	public void setLibErr(boolean libErr) {
		this.libErr = libErr;
	}
	public List<Item> getNoResultItemRecomList(){
		return noResultItemRecomList;
	}
	public void setNoResultItemRecomList(List<Item> noResultItemRecomList){
		this.noResultItemRecomList = noResultItemRecomList;
	}
	public String getNoResultItemRecomIds(){
		return noResultItemRecomIds;
	}
	public void setNoResultItemRecomIds(String noResultItemRecomIds){
		this.noResultItemRecomIds = noResultItemRecomIds;
	}
	public List<Map<String,List<Category>>> getBrandCategoryList() {
		return brandCategoryList;
	}
	public void setBrandCategoryList(List<Map<String,List<Category>>> brandCategoryList) {
		this.brandCategoryList = brandCategoryList;
	}
	public Map<String, String> getBrandSelectMap() {
		return brandSelectMap;
	}
	public void setBrandSelectMap(Map<String, String> brandSelectMap) {
		this.brandSelectMap = brandSelectMap;
	}
	public List<Bshop> getBshopList() {
		return bshopList;
	}
	public void setBshopList(List<Bshop> bshopList) {
		this.bshopList = bshopList;
	}
	public int getBshopCount() {
		return bshopCount;
	}
	public void setBshopCount(int bshopCount) {
		this.bshopCount = bshopCount;
	}
	public int getNoResultItemCount(){
		return noResultItemCount;
	}
	public void setNoResultItemCount(int noResultItemCount){
		this.noResultItemCount = noResultItemCount;
	}
	public List<Bshop> getBshopRstList() {
		return bshopRstList;
	}
	public void setBshopRstList(List<Bshop> bshopRstList) {
		this.bshopRstList = bshopRstList;
	}
	public String getSkipResult(){
		return skipResult;
	}
	public void setSkipResult(String skipResult){
		this.skipResult = skipResult;
	}
	public List<Spprice> getSppriceList() {
		return sppriceList;
	}
	public void setSppriceList(List<Spprice> sppriceList) {
		this.sppriceList = sppriceList;
	}
	public int getSppriceCount() {
		return sppriceCount;
	}
	public void setSppriceCount(int sppriceCount) {
		this.sppriceCount = sppriceCount;
	}
	public String getSppriceItemIds() {
		return sppriceItemIds;
	}
	public void setSppriceItemIds(String sppriceItemIds) {
		this.sppriceItemIds = sppriceItemIds;
	}
	public List<Store> getStoreList() {
		return storeList;
	}
	public void setStoreList(List<Store> storeList) {
		this.storeList = storeList;
	}
	public int getStoreCount() {
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	public List<MobileCategory> getCommCategoryList() {
		return commCategoryList;
	}
	public void setCommCategoryList(List<MobileCategory> commCategoryList) {
		this.commCategoryList = commCategoryList;
	}
	public List<Map<String, String>> getRecomCategoryList() {
		return recomCategoryList;
	}
	public void setRecomCategoryList(List<Map<String, String>> recomCategoryList) {
		this.recomCategoryList = recomCategoryList;
	}

	public List<Map<String, String>> getRecomDispCategoryList() {
		return recomDispCategoryList;
	}

	public void setRecomDispCategoryList(List<Map<String, String>> recomDispCategoryList) {
		this.recomDispCategoryList = recomDispCategoryList;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public List<ShoppingMagazine> getLifeMagazineList() {
		return lifeMagazineList;
	}
	public void setLifeMagazineList(List<ShoppingMagazine> lifeMagazineList) {
		this.lifeMagazineList = lifeMagazineList;
	}
	public int getLifeMagazineCount() {
		return lifeMagazineCount;
	}
	public void setLifeMagazineCount(int lifeMagazineCount) {
		this.lifeMagazineCount = lifeMagazineCount;
	}
	public List<IssueTheme> getIssueThemeList() {
		return issueThemeList;
	}
	public void setIssueThemeList(List<IssueTheme> issueThemeList) {
		this.issueThemeList = issueThemeList;
	}
	public int getIssueThemeCount() {
		return issueThemeCount;
	}
	public void setIssueThemeCount(int issueThemeCount) {
		this.issueThemeCount = issueThemeCount;
	}

	public Map<String, String> getVirtualCategoryMap() {
		return virtualCategoryMap;
	}

	public void setVirtualCategoryMap(Map<String, String> virtualCategoryMap) {
		this.virtualCategoryMap = virtualCategoryMap;
	}
	
	public String getVirtualCtgId() {
		return virtualCtgId;
	}
	public void setVirtualCtgId(String virtualCtgId) {
		this.virtualCtgId = virtualCtgId;
	}
	public List<Trecipe> getTrecipeList() {
		return trecipeList;
	}
	public void setTrecipeList(List<Trecipe> trecipeList) {
		this.trecipeList = trecipeList;
	}
	public int getTrecipeCount() {
		return trecipeCount;
	}
	public void setTrecipeCount(int trecipeCount) {
		this.trecipeCount = trecipeCount;
	}
	public List<Advertising> getAdvertisingList() {
		return advertisingList;
	}
	public void setAdvertisingList(List<Advertising> advertisingList) {
		this.advertisingList = advertisingList;
	}
	public int getAdvertisingCount() {
		return advertisingCount;
	}
	public void setAdvertisingCount(int advertisingCount) {
		this.advertisingCount = advertisingCount;
	}
	
	public String getRecomItemIds() {
		return recomItemIds;
	}
	public void setRecomItemIds(String recomItemIds) {
		this.recomItemIds = recomItemIds;
	}
	public int getBanrGcount() {
		return banrGcount;
	}
	public void setBanrGcount(int banrGcount) {
		this.banrGcount = banrGcount;
	}
	public int getBanrEcount() {
		return banrEcount;
	}
	public void setBanrEcount(int banrEcount) {
		this.banrEcount = banrEcount;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	public String getPublicTasteIds() {
		return publicTasteIds;
	}
	public void setPublicTasteIds(String publicTasteIds) {
		this.publicTasteIds = publicTasteIds;
	}
	public List<Taste> getPublicTasteList() {
		return publicTasteList;
	}
	public void setPublicTasteList(List<Taste> publicTasteList) {
		this.publicTasteList = publicTasteList;
	}
	public String getMyTasteIds() {
		return myTasteIds;
	}
	public void setMyTasteIds(String myTasteIds) {
		this.myTasteIds = myTasteIds;
	}
	public List<Taste> getMyTasteList() {
		return myTasteList;
	}
	public void setMyTasteList(List<Taste> myTasteList) {
		this.myTasteList = myTasteList;
	}
	public List<BrandMaster> getBrandMasterList() {
		return brandMasterList;
	}
	public void setBrandMasterList(List<BrandMaster> brandMasterList) {
		this.brandMasterList = brandMasterList;
	}
	public int getBrandMasterCount() {
		return brandMasterCount;
	}
	public void setBrandMasterCount(int brandMasterCount) {
		this.brandMasterCount = brandMasterCount;
	}
	public List<Starfield> getStarfieldList() {
		return starfieldList;
	}
	public void setStarfieldList(List<Starfield> starfieldList) {
		this.starfieldList = starfieldList;
	}
	public int getStarfieldCount() {
		return starfieldCount;
	}
	public void setStarfieldCount(int starfieldCount) {
		this.starfieldCount = starfieldCount;
	}
	public List<BanrAdvertising> getBanrAdvertisingList() {
		return banrAdvertisingList;
	}
	public void setBanrAdvertisingList(List<BanrAdvertising> banrAdvertisingList) {
		this.banrAdvertisingList = banrAdvertisingList;
	}

	public List<FamSite> getFamSiteList() {
		return famSiteList;
	}

	public void setFamSiteList(List<FamSite> famSiteList) {
		this.famSiteList = famSiteList;
	}

	public int getFamSiteCount() {
		return famSiteCount;
	}

	public void setFamSiteCount(int famSiteCount) {
		this.famSiteCount = famSiteCount;
	}

	public int getBanrAdvertisingCount() {
		return banrAdvertisingCount;
	}
	public void setBanrAdvertisingCount(int banrAdvertisingCount) {
		this.banrAdvertisingCount = banrAdvertisingCount;
	}

	public EsErrCode getEsErrCode() {
		return esErrCode;
	}

	public void setEsErrCode(EsErrCode esErrCode) {
		this.esErrCode = esErrCode;
	}
	
    public String getBrandFilterDispYn() {
        return brandFilterDispYn;
    }
    public void setBrandFilterDispYn(String brandFilterDispYn) {
        this.brandFilterDispYn = brandFilterDispYn;
    }
	public String getDispOrdrRiseCollection() {
		return dispOrdrRiseCollection;
	}
	public void setDispOrdrRiseCollection(String dispOrdrRiseCollection) {
		this.dispOrdrRiseCollection = dispOrdrRiseCollection;
	}
}
