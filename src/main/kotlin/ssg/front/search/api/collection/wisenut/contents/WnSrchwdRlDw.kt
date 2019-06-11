package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable
import ssg.search.result.SrchwdRl

class WnSrchwdRlDw: WnCollection(), Prefixable {
    override fun getName(parameter: Parameter): String {
        return "srchrldw"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "srchrldw"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SRCHWD_NM", "RL_KEYWD_NM")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("SRCHWD_NM")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SITE_NO_ONLY
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val rlList = arrayListOf<SrchwdRl>()
        var srchwdRl: SrchwdRl
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            srchwdRl = SrchwdRl()
            srchwdRl.setSrchwdNm(search.w3GetField(name, "SRCHWD_NM", i))
            srchwdRl.setRlKeywdNm(search.w3GetField(name, "RL_KEYWD_NM", i))
            rlList.add(srchwdRl)
        }
        // 기존의 연관 검색어 결과가 없는 경우에만 이 컬렉션의 결과를 사용한다.
        if (result.srchwdRlCount <= 0) {
            result.srchwdRlList = rlList
            result.srchwdRlCount = search.w3GetResultTotalCount(name)
        }
        return result
    }
}