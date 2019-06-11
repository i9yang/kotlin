package ssg.front.search.api.dto.result.test

import java.util.StringTokenizer

data class Category (
        var siteNo: String,        // 사이트 NO
        var ctgId: String,        // 카테고리 ID
        var ctgNm: String,        // 카테고리 명

        var ctgItemCount: Int = 0,    // 카테고리에 포함된 상품 수
        var ctgLevel: Int = 0,        // 카테고리 레벨

        var isHasChild: Boolean = false,        // 자식 존재 여부

        var ctgLclsId: String,
        var ctgMclsId: String,
        var ctgSclsId: String,
        var ctgDclsId: String,

        var themeYn: String,         // 테마카테고리 여부
        var priorCtgId: String,        // 부모카테고리ID
        var selectedArea: String,    // 선택된 영역 여부 ( 브랜드SHOP에서 사용 )

        var childCategoryList: List<Category>,
        var recomYn : String = "N"
)
{

    fun tokenizeCtg(ctg: String) {
        var lv = 1
        val st = StringTokenizer(ctg, ":")
        while (st.hasMoreTokens()) {
            val tk = st.nextToken()
            val sp = tk.split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            this.siteNo = sp[0]
            this.ctgId = sp[1]
            this.ctgNm = sp[2]

            if (lv == 1) {
                this.ctgLclsId = sp[1]
            } else if (lv == 2) {
                this.ctgMclsId = sp[1]
            } else if (lv == 3) {
                this.ctgSclsId = sp[1]
            } else if (lv == 4) {
                this.ctgDclsId = sp[1]
            }
            lv++
        }
    }

    fun getLvlCtgId(lv: Int): String? {
        if (lv == 1) {
            return this.ctgLclsId
        } else if (lv == 2) {
            return this.ctgMclsId
        } else if (lv == 3) {
            return this.ctgSclsId
        } else if (lv == 4) {
            return this.ctgDclsId
        }
        return null
    }
}
