package ssg.front.search.api.dto.result.test

import com.google.common.collect.Lists

class MobileCategory (
    var dispCtgLvl: String,
    var dispCtgId: String,
    var dispCtgNm: String,
    var priorDispCtgId: String,
    var siteNo: String,
    var itemCount: String,
    var hasChild: String,
    var children: MutableList<MobileCategory> = Lists.newArrayList()
)
{
    fun add(c: MobileCategory) {
        this.children.add(c)
    }
}

