package ssg.front.search.api.dto.result

import ssg.search.result.*

data class Result(
        var brandId: String = "1",
        var srchItemIds: String = "",
        var noResultItemRecomIds: String = "",
        var noResultItemCount: Int = 0,
        var bookItemIds: String = "",
        var advertisingCount: Int = 0,
        var libErr: Boolean = false,
        var libErrMsg: String = "",
        var skipResult: String = "",
        var dispOrdrRiseCollection: String = "",
        var morphResult: String = "",
        var mallCountMap: Map<String, String> = mapOf(),    // 몰별 카운트 정보
        var bookMallCountMap: Map<String, String> = mapOf(),    // 몰별 카운트 정보
        var minPrc: Int = 0,
        var maxPrc: Int = 0,
        var lctgId: String = "",
        var lctgNm: String = "",
        var mctgId: String = "",
        var mctgNm: String = "",
        var sctgId: String = "",
        var sctgNm: String = "",
        var dctgId: String = "",
        var dctgNm: String = "",
        var ctgSiteNo: String = "",
        var recomItemIds: String = "",
        var sppriceItemIds: String = "",
        var publicTasteIds: String = "",
        var myTasteIds: String = "",
        var virtualCtgId: String = "",

        /**
         *  count
         */
        var itemCount: Int = 0,
        var recomCount: Int = 0,
        var sppriceCount: Int = 0,
        var issueThemeCount: Int = 0,
        var bookCount:Int = 0,
        var postngCount:Int = 0,
        var pnshopCount:Int = 0,
        var pnshopSdCount:Int = 0,
        var starfieldCount:Int = 0,
        var trecipeCount:Int = 0,
        var lifeMagazineCount:Int = 0,
        var magazineCount:Int = 0,
        var eventCount:Int = 0,
        var faqCount:Int = 0,
        var famSiteCount: Int = 0,
        var brandMasterCount: Int = 0,
        var lctgCount: Int = 0,
        var mctgCount: Int = 0,
        var sctgCount: Int = 0,
        var dctgCount: Int = 0,
        var ctgViewCount: Int = 0,
        var noticeCount: Int = 0,
        var recipeCount: Int = 0,
        var spShopCount: Int = 0,
        var srchGuideCount: Int = 0,
        var srchwdRlCount: Int = 0,
        var storeCount: Int = 0,
        var banrCount: Int = 0,
        var banrGcount: Int = 0,
        var spellCount: Int = 0,
        var banrAdvertisingCount: Int = 0,

        /**
         *  flag
         */
        var resultYn:Boolean = false,
        var brandFilterDispYn:String = "N",

        /**
         *  List
         */
        var banrList: List<Banr> = listOf(),
        var myTasteList: List<Taste> = listOf(),                    // 타취결과
        var publicTasteList: List<Taste> = listOf(),               // 내취결과
        var brandMasterList: List<BrandMaster> = listOf(),
        var brandCategoryList: List<Map<String, List<Category>>> = listOf(),
        var bookList: List<Book> = listOf(),
        var recomDispCategoryList: List<Map<String, String>> = listOf(),     // 신규 카테고리 추천로직에서 사용
        var commCategoryList: List<MobileCategory> = listOf(),
        var recomCategoryList: List<Map<String, String>> = listOf(),
        var postngList: List<Postng> = listOf(),
        var eventList: List<Event> = listOf(),                       // 이벤트 리스트
        var famSiteList: List<FamSite> = listOf(),
        var faqList: List<Faq> = listOf(),
        var issueThemeList: List<IssueTheme> = listOf(),
        var lifeMagazineList: List<ShoppingMagazine> = listOf(),
        var MagazineList: List<Magazine> = listOf(),
        var pnshopList: List<Pnshop> = listOf(),
        var noticeList: List<Notice> = listOf(),
        var pnshopSdList: List<Pnshop> = listOf(),
        var recipeList: List<Recipe> = listOf(),
        var recomList: List<Recom> = listOf(),
        var sppriceList: List<Spprice> = listOf(),
        var spShopList: List<SpShop> = listOf(),
        var bookCategoryList: List<Category> = listOf(),
        var categoryList: List<Category> = listOf(),        // Category 정보
        var brandList: List<Brand> = listOf(),
        var prcGroupList: List<Prc> = listOf(),
        var sizeList: List<Size> = listOf(),
        var bshopRstList: List<Bshop> = listOf(),        //bshop 리스트
        var bookItemList: List<Item> = listOf(),
        var advertisingList: List<Advertising> = listOf(),
        var noResultItemRecomList: List<Item> = listOf(),
        var itemList: List<Item> = listOf(),
        var srchGuideList: List<SrchGuide> = listOf(),
        var srchwdRlList: List<SrchwdRl> = listOf(),
        var starfieldList: List<Starfield> = listOf(),
        var storeList: List<Store> = listOf(),
        var trecipeList: List<Trecipe> = listOf(),
        var spellList: List<Spell> = listOf(),
        var virtualCategoryMap: Map<String, String> = mapOf(),
        var banrAdvertisingList: List<BanrAdvertising> = listOf()
) {


    fun setCtgId(lv: Int, ctgId: String) {
                if (lv == 1)
                        this.lctgId = ctgId
                else if (lv == 2)
                        this.mctgId = ctgId
                else if (lv == 3)
                        this.sctgId = ctgId
                else if (lv == 4) this.dctgId = ctgId
        }

        fun setCtgNm(lv: Int, ctgNm: String) {
                if (lv == 1)
                        this.lctgNm = ctgNm
                else if (lv == 2)
                        this.mctgNm = ctgNm
                else if (lv == 3)
                        this.sctgNm = ctgNm
                else if (lv == 4) this.dctgNm = ctgNm
        }

        fun setCtgCount(lv: Int, count: Int) {
                if (lv == 1)
                        this.lctgCount = count
                else if (lv == 2)
                        this.mctgCount = count
                else if (lv == 3)
                        this.sctgCount = count
                else if (lv == 4) this.dctgCount = count
        }
}