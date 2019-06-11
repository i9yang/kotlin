package ssg.front.search.api.builder

import QueryAPI510.Search
import com.google.common.base.Joiner
import com.google.common.collect.Lists
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.*
import ssg.front.search.api.matcher.PrefixStringMatcher
import ssg.front.search.api.matcher.SuffixStringMatcher
import ssg.front.search.api.util.CollectionUtils
import ssg.search.collection.advertising.AdvertisingCollection
import ssg.search.collection.es.rsearch.RecommendCollection

@Component
class WisenutQueryBuilder: SsgQueryBuilder {

    private val logger = LoggerFactory.getLogger(WisenutQueryBuilder::class.java)
    lateinit private var parameter: Parameter
    private var search: Search = Search()
    private val requestQuery = StringBuilder()
    private var url = "10.203.7.77"
    private var port = 7000
    private val joiner = Joiner.on(",")

    internal val SkipWords = arrayOf(".co", "가격", "가격비교", "공구", "공동구매", "기획전", "리본데이", "매장", "먹는방법", "몰", "방문판매", "브랜드", "사용법", "사이즈", "사이트", "상설", "상설매장", "상설할인매장", "상품", "색상", "샘플", "세일", "세일기간", "세탁", "쇼핑몰", "쇼핑물", "스토어", "시즌오프", "신상", "신형가격", "싼브랜드", "아울렛", "어패럴", "온라인", "온라인매장", "온라인스토어", "이월", "이월상품", "재고처리", "재배방법", "정기세일", "종류", "중고", "직구", "직수입매장", "착샷", "추천", "코리아", "특가", "파는곳", "판매", "학생할인", "한정판매", "할인매장", "핫딜", "홈쇼핑", "효능", "할인", "크기", "신상가방", "상설할인", "인기상품", "사이즈표")

    internal val ActionWords = arrayOf("매직픽업" /* 매직픽업 필터링 */, "점포상품" /* 점포예약 + 점포택배 (ITEM_REG_DIV_CD : 20) */, "점포예약"    /* 점포예약 상품만 SHPP_TYPE_DTL_CD : 11 */, "점포택배" /* 점포택배  SHPP_TYPE_DTL_CD : 2N */, "쓱배송")

    internal val SizeDenyWords = arrayOf("가방", "글러브", "기능성티", "기모", "긴팔티", "남자티", "내의", "니트글러브", "드라이핏", "라운드티", "롱티", "맨투맨티", "바람막이", "바지", "반팔티", "백팩", "상의", "셔츠", "속옷", "수영복", "운동복", "웨어", "윈드러너", "의류", "저지", "정장", "져지", "조끼", "짐볼", "집업", "짚업", "침구", "카라티", "캡", "크루티", "트레이닝", "트레이닝바지", "트레이닝복", "트레이닝복세트", "티셔츠", "패딩", "팬티", "플리스", "하의", "후드집업", "후드티")

    internal val recipeWords = arrayOf("레시피", "요리법", "만드는법")

    internal val starfieldWords = arrayOf("스타필드", "하남", "하남점", "고양", "고양점", "코엑스")

    internal val faqWords = arrayOf("비밀번호", "아이디", "회원가입", "통합회원", "간편가입", "이름변경", "맘키즈클럽", "이벤트경품", "결제오류", "공인인증서", "회원탈퇴", "회원정보", "이벤트당첨", "상품평", "현금영수증", "증빙서류", "세금계산서발급", "ok캐쉬백", "신세계포인트", "적립금", "s포켓", "예치금", "ssg머니", "소액결제", "무이자할부", "계좌이체", "무통장입금", "비회원", "환불", "취소", "반품", "교환", "당일픽업", "품절", "상품권전환금", "예치금", "신세계포인트", "s머니", "신세계기프트카드", "휴대폰결제", "배송")

    internal val famSiteWords = arrayOf("휴무일", "휴점일", "쉬는날", "주차", "주차요금", "오시는길", "영업시간")

    internal val famSiteMoreWords = arrayOf(
            // 사이트명
            "이마트", "백화점", "스타필드", "트레이더스",

            // 백화점 점포명
            "강남점", "경기점", "광주신세계", "김해점", "대구신세계점", "마산점", "본점", "센텀시티점", "시코르강남역점", "영등포점", "의정부점", "인천점", "충청점", "하남점",

            // 이마트 점포명
            "가든5점", "가양점", "감삼점", "강릉점", "검단점", "경기광주점", "경산점", "계양점", "고잔점", "과천점", "광교점", "광명소하점", "광명점", "광산점", "광주점", "구로점", "구미점", "군산점", "금정점", "김천점", "김포한강점", "김해점", "남양주점", "남원점", "대전터미널점", "덕이점", "도농점", "동광주점", "동구미점", "동백점", "동인천점", "동탄점", "동해점", "둔산점", "마산점", "마포공덕점", "만촌점", "명일점", "목동점", "목포점", "묵동점", "문현점", "미아점", "반야월점", "별내점", "보라점", "보령점", "봉선점", "부천점", "부평점", "분당점", "사상점", "사천점", "산본점", "상무점", "상봉점", "상주점", "서귀포점", "서부산점", "서산점", "서수원점", "성남점", "성서점", "성수점", "세종점", "속초점", "수색점", "수서점", "수원점", "수지점", "순천점", "시지점", "시화점", "신도림점", "신월점", "신제주점", "아산점", "안동점", "안성점", "양산점", "양재점", "양주점", "여수점", "여의도점", "여주점", "역삼점", "연수점", "연제점", "영등포점", "영천점", "오산점", "왕십리점", "용산점", "용인점", "울산점", "원주점", "월계점", "월배점", "은평점", "의정부점", "이문점", "이수점", "이천점", "익산점", "인천공항점", "인천점", "일산점", "자양점", "전주점", "제주점", "제천점", "죽전점", "중동점", "진접점", "진주점", "창동점", "창원점", "천안서북점", "천안점", "천안터미널점", "천호점", "청계천점", "청주점", "춘천점", "충주점", "칠성점", "킨텍스점", "태백점", "통영점", "파주운정점", "파주점", "펜타포트점", "평촌점", "평택점", "포천점", "포항이동점", "포항점", "풍산점", "하남점", "하월곡점", "해운대점", "화성봉담점", "화정점", "흥덕점",

            // 트레이더스 점포명
            "월평점", "구성점", "송림점", "서면점", "비산점", "안산점", "천안아산점", "양산점", "수원점", "킨텍스점", "하남점", "고양점", "군포점", "김포점")

    internal val SkipWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*SkipWords))
    internal val ActionWordPrefixMatcher = PrefixStringMatcher(Lists.newArrayList(*ActionWords))
    internal val ActionWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*ActionWords))
    internal val SizeDenyWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*SizeDenyWords))

    internal val recipeWordPrefixMatcher = PrefixStringMatcher(Lists.newArrayList(*recipeWords))
    internal val recipeWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*recipeWords))
    internal val starfieldWordPrefixMatcher = PrefixStringMatcher(Lists.newArrayList(*starfieldWords))
    internal val starfieldWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*starfieldWords))
    internal val faqWordPrefixMatcher = PrefixStringMatcher(Lists.newArrayList(*faqWords))
    internal val faqWordSuffixMatcher = SuffixStringMatcher(Lists.newArrayList(*faqWords))

    override fun execute(parameter: Parameter, res: Result) {
        logger.info("===== execute WisenutQueryBuilder =====")
        this.parameter = parameter
        this.url = parameter.url
        this.port = parameter.port.toInt()


        var strQuery = CollectionUtils.getCommonQuery(parameter)
        var targets = CollectionUtils.getTargets(parameter)

        // 확장형 검색어 강제치환
        strQuery = CollectionUtils.replaceForced(strQuery)

        // 기본적인 Set START
        search.w3SetCodePage("utf-8")
//        var userInfo = parameter.userInfo

        var ctgId = parameter.ctgId ?: ""
        var brandId = parameter.brandId ?: ""
//        var mbrId = parameter.userInfo!!.mbrId?: ""
        // 임시
        var mbrId = "test"

        search.w3SetSessionInfo(ctgId, brandId, mbrId)

        var testRequest = parameter.testRequest ?: ""
        if (testRequest == "Y") {
            search.w3SetQueryLog(0)
        } else {
            search.w3SetQueryLog(1)
        }

        var prefixQuery = ActionWordPrefixMatcher.longestMatch(strQuery) ?: ""
        var suffixQuery = ActionWordSuffixMatcher.longestMatch(strQuery) ?: ""

        var isFilteringYn = false

        // Rule 1. Suffix 와 Prefix 에 둘다 결과가 있는 경우 작동하지 않음
        if (prefixQuery != "" && suffixQuery != "") {
        }
        // Rule 2. Prefix 에 결과가 있는 경우
        if (prefixQuery != "") {
            if (prefixQuery.indexOf("+") > -1) {
                prefixQuery = prefixQuery.replace("\\+".toRegex(), "\\\\+")
            }
            val tempQuery = strQuery.replaceFirst(prefixQuery.toRegex(), "").trim { it <= ' ' }
            if (tempQuery != "") {
                search.w3SetCommonQuery(tempQuery, 0)
                if (prefixQuery == "매직픽업") {
                    parameter.shpp = "picku"
                } else if (prefixQuery == "점포상품") {
                    parameter.shpp = "store"
                } else if (prefixQuery == "점포예약") {
                    parameter.shpp = "rsvt"
                } else if (prefixQuery == "점포택배") {
                    parameter.shpp = "pack"
                } else if (prefixQuery == "퀵배송") {
                    parameter.shpp = "qshpp"
                } else if (prefixQuery == "쓱콘") {
                    parameter.shpp = "con"
                } else if (prefixQuery.equals("쓱배송", ignoreCase = true)) {
                    parameter.shpp = "ssgem"
                }
                isFilteringYn = true
            }
        }

        // Rule 3. Suffix 에 결과가 있는 경우
        if (suffixQuery != "") {
            if (suffixQuery.indexOf("+") > -1) {
                suffixQuery = suffixQuery.replace("\\+".toRegex(), "\\\\+")
            }
            val tempQuery = replaceLast(strQuery, suffixQuery, "").trim({ it <= ' ' })
            if (tempQuery != "") {
                search.w3SetCommonQuery(tempQuery, 0)
                if (suffixQuery == "매직픽업") {
                    parameter.shpp = "picku"
                } else if (suffixQuery == "점포상품") {
                    parameter.shpp = "store"
                } else if (suffixQuery == "점포예약") {
                    parameter.shpp = "rsvt"
                } else if (suffixQuery == "점포택배") {
                    parameter.shpp = "pack"
                } else if (suffixQuery == "퀵배송") {
                    parameter.shpp = "qshpp"
                } else if (suffixQuery == "쓱콘") {
                    parameter.shpp = "con"
                } else if (suffixQuery.equals("1+1", ignoreCase = true)) {
                    parameter.shpp = "1PLUZ"
                } else if (suffixQuery.equals("2+1", ignoreCase = true)) {
                    parameter.shpp = "2PLUZ"
                } else if (suffixQuery.equals("3+1", ignoreCase = true)) {
                    parameter.shpp = "3PLUZ"
                } else if (suffixQuery.equals("쓱배송", ignoreCase = true)) {
                    parameter.shpp = "ssgem"
                }
                isFilteringYn = true
            }
        }

        if (strQuery != "" && !isFilteringYn) search.w3SetCommonQuery(strQuery, 0)

        Targets.valueOf(parameter.target.toUpperCase())
                .getCollectionSet(parameter)
                .filterIsInstance<WnCollection>()
                .filterNot { collection -> collection is LoginNecessary && collection is RecommendCollection || collection is AdvertisingCollection }
                .forEach { collection ->
                    // Debug 를 위한 준비
                    requestQuery.append("\n")
                    requestQuery.append(CollectionUtils.getDivPipe())

                    // Alias 여부를 파악해서 Alias or Collection Add
                    val name = collection.getAliasName(parameter)
                    val engineNm = collection.getName(parameter)

                    if (strQuery != "") {
                        requestQuery.append("\nw3SetCommonQuery : ").append(strQuery)
                    }

                    if (engineNm != name) {
                        search.w3AddAliasCollection(name, engineNm)
                        requestQuery.append("\nw3AddAliasCollection : ").append(name).append(", ").append(engineNm).append("\n")
                    } else {
                        search.w3AddCollection(engineNm)
                        requestQuery.append("\nw3AddCollection : ").append(engineNm).append("\n")
                    }

                    // 조회에 사용되는 DocumentField Set
                    search.w3SetDocumentField(name, joiner.join(collection.getDocumentField(parameter)))
                    requestQuery.append("w3SetDocumentField : ").append(joiner.join(collection.getDocumentField(parameter))).append("\n")

                    // 검색 조건에 사용되는 SearchField Set
                    search.w3SetSearchField(name, joiner.join(collection.getSearchField(parameter)))
                    requestQuery.append("w3SetSearchField : ").append(joiner.join(collection.getSearchField(parameter))).append("\n")

                    // Paging 처리를 시행해야 하는 컬렉션인 경우
                    if (collection is Pageable) {
                        val pageVo = collection.pageVo(parameter)
                        if (pageVo.start > -1) {
                            search.w3SetPageInfo(name, pageVo.start, pageVo.count)
                            requestQuery.append("w3SetPageInfo : ").append(pageVo.start).append(", ").append(pageVo.count).append("\n")
                        }
                    } else {
                        search.w3SetPageInfo(name, 0, 1)
                        requestQuery.append("w3SetPageInfo : ").append("0, 1").append("\n")
                    }// 아닌경우 무조건 0 pageVo 1개 상품

                    // 카테고리 부스팅
                    if (collection is Boostable) {
                        val boostVo = collection.boostVo(parameter)
                        if (CollectionUtils.getOriQuery(parameter).length > 0) {
                            search.w3SetBoostCategory(name, boostVo.boost, "SUB_MATCH", CollectionUtils.getOriQuery(parameter))
                            requestQuery.append("w3SetBoostCategory : ").append(boostVo.boost).append(", SUB_MATCH, ").append(CollectionUtils.getOriQuery(parameter)).append("\n")
                        }
                    }

                    // Collection Ranking
                    if (collection is Rankable) {
                        val rankVo = collection.rankVo(parameter)
                        if (CollectionUtils.getOriQuery(parameter).length > 0) {
                            search.w3SetRanking(name, "keyword", rankVo.rank, 0)
                            requestQuery.append("w3SetRanking : ").append(rankVo.rank).append(", 0").append("\n")
                        }
                    }

                    // Sort
                    if (collection is Sortable) {
                        val sortVo: SortVo = collection.sortVo(parameter)
                        val sortList = sortVo.sortList
                        // SORT 의 경우에는 다중 소팅기능을 사용하기 위해 여러개의 소팅을 차례대로 셋한다.
                        if (sortList.size > 0) {
                            for (sort in sortList) {
                                search.w3AddSortField(name, sort!!.sortName, sort.operator)
                                requestQuery.append("w3AddSortField : ").append(sort.sortName).append(", ").append(sort.operator).append("\n")
                            }
                        } else {
                            search.w3AddSortField(name, "RANK", 1)
                            requestQuery.append("w3AddSortField : ").append("RANK, 1").append("\n")
                        }// NULL 의 경우 방어
                    } else {
                        search.w3AddSortField(name, "RANK", 1)
                        requestQuery.append("w3AddSortField : ").append("RANK, 1").append("\n")
                    }

                    //Set Analyzer
                    search.w3SetQueryAnalyzer(name, 1, 1, 1, 1)

                    // Set Prefix
                    if (collection is Prefixable) {
                        val prefixVo = collection.prefixVo(parameter)
                        if (prefixVo.prefix.isNotEmpty()) {
                            search.w3SetPrefixQuery(name, prefixVo.prefix, prefixVo.operator)
                            requestQuery.append("w3SetPrefixQuery : ").append(prefixVo.prefix).append(", ").append(prefixVo.operator).append("\n")
                        }
                    }

                    // Set Filter
                    if (collection is Filterable) {
                        val filterVo = collection.filterVo(parameter)
                        if (filterVo.filter.isNotEmpty()) {
                            search.w3SetFilterQuery(name, filterVo.filter)
                            requestQuery.append("w3SetFilterQuery : ").append(filterVo.filter).append("\n")
                        }
                    }

                    // Set MultiGroupBy
                    if (collection is Groupable) {
                        val groupVo = collection.groupVo(parameter)
                        if (groupVo.group.isNotEmpty()) {
                            search.w3SetMultiGroupBy(name, groupVo.group)
                            requestQuery.append("w3SetMultiGroupBy : ").append(groupVo.group).append("\n")
                        }
                    }

                    // Set PropertyGroup
                    if (collection is PropertyGroupable) {
                        val propertyGroupVo = collection.propertyGroupVo(parameter)
                        if (propertyGroupVo.propertyGroup.isNotEmpty()) {
                            search.w3SetPropertyGroup(name, propertyGroupVo.propertyGroup, 0, 1000000000, 5)
                            requestQuery.append("w3SetPropertyGroup : ").append(propertyGroupVo.propertyGroup).append(", 0, 1000000000, 5").append("\n")
                        }
                    }

                    // Set CategoryGroupBy
                    if (collection is CategoryGroupable) {
                        val categoryGroupVo = collection.categoryGroupVo(parameter)
                        val ctgIdxNm = categoryGroupVo.ctgIdxNm
                        if (ctgIdxNm.isNotEmpty()) {
                            if (ctgIdxNm.indexOf(",") > -1) {
                                val idxs = ctgIdxNm.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (i in idxs.indices) {
                                    search.w3AddCategoryGroupBy(name, idxs[i], categoryGroupVo.categoryDepth)
                                    requestQuery.append("w3AddCategoryGroupBy : ").append(idxs[i]).append(", ").append(categoryGroupVo.categoryDepth).append("\n")
                                }
                            } else {
                                search.w3AddCategoryGroupBy(name, ctgIdxNm, categoryGroupVo.categoryDepth)
                                requestQuery.append("w3AddCategoryGroupBy : ").append(ctgIdxNm).append(", ").append(categoryGroupVo.categoryDepth).append("\n")
                            }
                        }
                    }

                    // Set Highlight, Snippet
                    if (collection is Highlightable && (parameter.highlight ?: "N") == "Y") {
                        if (collection is Snippet) {
                            search.w3SetHighlight(name, 1, 1)
                            requestQuery.append("w3SetHighlight : 1, 1").append("\n")
                        } else {
                            search.w3SetHighlight(name, 1, 0)
                            requestQuery.append("w3SetHighlight : 1, 0").append("\n")
                        }
                    }

                    requestQuery.append(CollectionUtils.getDivPipe())
        }

        val ret = search.w3ConnectServer(url, port, 30000)
        logger.info("wisenut call return value : " + ret)
        search.w3ReceiveSearchQueryResult(3)
        if (ret != 0) {
            requestQuery.append("\n =================================================SEARCH REQUEST TIMEOUT============================================================")
        }

        this.result(res)
    }

    fun replaceLast(text: String, regex: String?, replacement: String): String {
        return text.replaceFirst("(?s)(.*)$regex".toRegex(), "$1$replacement")
    }

    fun result(result: Result) {
        logger.info("===== result WisenutQueryBuilder =====")
        logger.info(result.toString())
        logger.info(requestQuery.toString())  // debug
        // ERROR 발생여부
        val err = search.w3GetError()
        if (err > 0) {
            requestQuery.append("\n===========================================================ENGINE LIB EXCEPTION : ").append(err).append("============================================================================================ ")
        }
        if (requestQuery.indexOf("SEARCH REQUEST TIMEOUT") > -1 || requestQuery.indexOf("ENGINE LIB EXCEPTION") > -1) {
            result.libErr = true
        }
        if (search.w3GetError() < 1) {
            val strQuery = parameter.query?:""
            if (strQuery != "") {
                val skipResult = StringUtils.defaultIfEmpty(SkipWordSuffixMatcher.longestMatch(strQuery), "")
                if (skipResult != "") {
                    result.skipResult = replaceLast(strQuery, skipResult, "").trim { it <= ' ' }
                    // 해외직구는 그대로 둔다
                    if (strQuery.replace("\\s".toRegex(), "") == "해외직구") {
                        result.skipResult = ""
                    }
                    // 노브랜드는 그대로 둔다.
                    if (strQuery.replace("\\s".toRegex(), "") == "노브랜드") {
                        result.skipResult = ""
                    }
                    // 디스토어, a스토어는 브랜드 이므로 그대로 둔다.
                    if (strQuery.indexOf("디스토어") > -1 || strQuery.indexOf("에이스토어") > -1 || strQuery.indexOf("애플스토어") > -1 || strQuery.indexOf("a스토어") > -1 || strQuery.indexOf("A스토어") > -1) {
                        result.skipResult = ""
                    }
                    // 점포라는 단어가 존재하는 경우 그래도 둔다.
                    if (strQuery.indexOf("점포") > -1) {
                        result.skipResult = ""
                    }
                }

                // 컬렉션 노출 순서 조정
                var dispOrdrRiseCollection = ""

                // 오늘은 E요리
                val recipePrefixQuery = StringUtils.defaultIfEmpty(recipeWordPrefixMatcher.longestMatch(strQuery), "")
                val recipeSuffixQuery = StringUtils.defaultIfEmpty(recipeWordSuffixMatcher.longestMatch(strQuery), "")
                if (recipePrefixQuery != "" || recipeSuffixQuery != "") {
                    dispOrdrRiseCollection = "trecipe"
                } else {
                    // 스타필드
                    val starfieldPrefixQuery = StringUtils.defaultIfEmpty(starfieldWordPrefixMatcher.longestMatch(strQuery), "")
                    val starfieldSuffixQuery = StringUtils.defaultIfEmpty(starfieldWordSuffixMatcher.longestMatch(strQuery), "")
                    if (starfieldPrefixQuery != "" || starfieldSuffixQuery != "") {
                        dispOrdrRiseCollection = "starfield"
                    } else {
                        // 웹사이트검색
                        for (famSiteWord in famSiteWords) {
                            if (strQuery.contains(famSiteWord)) {
                                for (famSiteMoreWord in famSiteMoreWords) {
                                    if (strQuery.contains(famSiteMoreWord)) {
                                        dispOrdrRiseCollection = "fam_site"
                                    }
                                }
                            }
                        }
                        // FAQ
                        if (dispOrdrRiseCollection == "") {
                            val faqPrefixQuery = StringUtils.defaultIfEmpty(faqWordPrefixMatcher.longestMatch(strQuery), "")
                            val faqSuffixQuery = StringUtils.defaultIfEmpty(faqWordSuffixMatcher.longestMatch(strQuery), "")
                            if (faqPrefixQuery != "" || faqSuffixQuery != "") {
                                dispOrdrRiseCollection = "faq"
                            }
                        }
                    }
                }
                result.dispOrdrRiseCollection = dispOrdrRiseCollection
            }

            val targets = Targets.valueOf(parameter.target.toUpperCase())
            targets
                    .getCollectionSet(parameter)
                    .filterIsInstance<WnCollection>()
                    .filterNot { collection -> collection is LoginNecessary && collection is RecommendCollection || collection is AdvertisingCollection }
                    .forEach { collection ->
                collection.getResult(search, collection.getAliasName(parameter), parameter, result)!!
//              Morph Result Set -> 형태소 분석결과 ( 불용어가 체크되면 분석결과를 내려보내지 않음 )
                if (collection is Morphable && StringUtils.isEmpty(SizeDenyWordSuffixMatcher.longestMatch(strQuery))) {
                    result.morphResult = search.w3GetHighlightByField(collection.getAliasName(parameter), collection.morphVo(parameter).morph)
                }
            }
            // ResultYn SET

            // Disp Collection Item <-> Book Swap ( 전시는 도서와 상품이 혼재되어 있으므로 스왑하는 방식을 사용한다. )
            if(targets == Targets.BRAND_DISP || targets == Targets.BRAND_DISP || targets == Targets.MOBILE_BRAND || targets == Targets.MOBILE_BRAND_ITEM){
                val itemTotalCount = result.itemCount
                val bookTotalCount = result.bookCount
                val bookItemList = result.bookItemList
                val bookMallCountMap = result.bookMallCountMap
                val bookItemIds = result.bookItemIds
                if (bookTotalCount > 0 && itemTotalCount < bookTotalCount && bookItemList.size > 0 && bookItemIds.length > 0) {
                    result.itemCount = bookTotalCount
                    result.itemList = bookItemList
                    result.srchItemIds = bookItemIds
                    result.mallCountMap = bookMallCountMap
                } else if (itemTotalCount == 0 && bookTotalCount == 0) {
                    val itemMallCountMap = result.mallCountMap
                    if(bookMallCountMap.isNotEmpty()){
                        if(itemMallCountMap.isEmpty()){
                            result.itemCount = bookTotalCount
                            result.itemList = bookItemList
                            result.srchItemIds = bookItemIds
                            result.mallCountMap = bookMallCountMap
                        }else{
                            try {
                                val ssgBookCount = bookMallCountMap.get("6005")
                                val ssgItemCount = itemMallCountMap.get("6005")
                                var bookCount = 0
                                var itemCount = 0
                                if (ssgItemCount != null) {
                                    itemCount = Integer.parseInt(ssgItemCount)
                                }
                                if (ssgBookCount != null) {
                                    bookCount = Integer.parseInt(ssgBookCount)
                                }
                                if (bookCount > itemCount) {
                                    result.itemCount = bookTotalCount
                                    result.itemList = bookItemList
                                    result.srchItemIds = bookItemIds
                                    result.mallCountMap = bookMallCountMap
                                }
                            } catch (e: NumberFormatException) {
                            }
                        }
                    }
                }// 둘다 0 인 경우에는 mallCountMap을 비교해본다.
                // 모든 양식이 똑같지만 브랜드 인덱스 매장은 카테고리 정보가 하나 더 있음
                if (targets == Targets.BRAND_DISP || targets == Targets.MOBILE_BRAND) {
                    val bookCategoryList = result.bookCategoryList
                    if (bookTotalCount > 0 && itemTotalCount < bookTotalCount && bookItemList.size > 0 && bookItemIds.length > 0) {
                        result.categoryList = bookCategoryList
                    }
                }
            }

            if (CollectionUtils.containsTarget(targets, Targets.MOBILE, Targets.MOBILE_ALL, Targets.MOBILE_BOOK, Targets.MOBILE_ITEM, Targets.ALL, Targets.ITEM, Targets.BOOK,
                            Targets.CHAT_SEARCH_ALL, Targets.CHAT_GIFT_ALL, Targets.MOBILE_OMNI_ALL, Targets.CHAT_OMNI_ALL, Targets.CHAT_GIFT_OMNI_ALL)) {

                var resultCount: Int

                if (targets == Targets.ALL) {
                    resultCount = (result.itemCount + result.recomCount + result.sppriceCount + result.issueThemeCount
                            + result.bookCount + result.postngCount + result.pnshopCount + result.pnshopSdCount
                            + result.starfieldCount + result.trecipeCount + result.lifeMagazineCount + result.eventCount
                            + result.faqCount)
                    //SSG,신몰,신백,이마트 경우만 SSG웹사이트 포함
                    if ("6005".equals(parameter.siteNo) || "6004".equals(parameter.siteNo) || "6009".equals(parameter.siteNo)
                            || "6001".equals(parameter.siteNo)) {
                        resultCount = resultCount + result.famSiteCount
                    }
                } else if (targets == Targets.MOBILE_ALL) {
                    resultCount = (result.itemCount + result.sppriceCount + result.issueThemeCount
                            + result.bookCount + result.pnshopCount + result.pnshopSdCount
                            + result.starfieldCount + result.trecipeCount + result.lifeMagazineCount)
                } else if (targets == Targets.MOBILE_OMNI_ALL || targets == Targets.CHAT_OMNI_ALL) {
                    if ("6200".equals(parameter.siteNo) || "6003".equals(parameter.siteNo)) {
                        resultCount = (result.itemCount + result.sppriceCount + result.bookCount
                                + result.starfieldCount + result.trecipeCount + result.lifeMagazineCount)
                    } else {
                        resultCount = (result.itemCount + result.sppriceCount + result.bookCount
                                + result.starfieldCount + result.trecipeCount + result.lifeMagazineCount
                                + result.pnshopCount + result.pnshopSdCount)
                    }
                } else if (targets == Targets.MOBILE) {
                    resultCount = result.itemCount

                    //SSG,신몰,신백,이마트 경우만 SSG웹사이트 포함
                    if ("6005".equals(parameter.siteNo) || "6004".equals(parameter.siteNo) || "6009".equals(parameter.siteNo)
                            || "6001".equals(parameter.siteNo)) {
                        resultCount = resultCount + result.famSiteCount
                    }
                } else if (targets == Targets.MOBILE_BOOK) {
                    resultCount = result.bookCount
                } else {
                    resultCount = result.itemCount + result.bookCount
                }

                // 스타필드는 컬렉션이 스타필드 컬렉션 뿐
                if ("6400".equals(parameter.siteNo)) {
                    resultCount = result.starfieldCount
                }

                if (resultCount <= 0) {
                    result.resultYn = false
                } else {
                    result.resultYn = true
                }
                val itemIds = result.srchItemIds
                val bookItemIds = result.bookItemIds
                // 해당 Target들은 ITEM과 BOOK의 결과가 뒤섞이도록 ItemIds를 조정한다.
                result.srchItemIds = itemIds + bookItemIds
                // BOOK 관련 타겟에서는 CategoryList를 변경해준다. ( 모바일쪽 방어로직 )
                if (CollectionUtils.containsTarget(targets, Targets.BOOK, Targets.MOBILE_BOOK)) {
                    if (result.bookCount > 0) {
                        if (result.bookCategoryList.size > 0) {
                            result.categoryList = result.bookCategoryList
                        }
                    }
                }
            }
        }
        search.w3CloseServer()

        result.resultYn = true
    }
}