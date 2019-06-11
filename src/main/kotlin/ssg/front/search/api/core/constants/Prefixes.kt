package ssg.front.search.api.core.constants

import ssg.front.search.api.dto.Parameter
import java.util.*

enum class Prefixes {
    SRCH_PREFIX_MALL {
        override fun getPrefix(parameter: Parameter): String {
            return "<SRCH_PREFIX:contains:SCOM>"
        }
    },
    SRCH_PREFIX {
        override fun getPrefix(parameter: Parameter): String {
            if (parameter.siteNo!!.isNotBlank()) {
                val strSiteNo = parameter.siteNo?: "6005"
                val mobileAppType = parameter.mobileAppType?: ""

                if (strSiteNo == "6005") {
                    return "<SRCH_PREFIX:contains:SCOM>"
                } else if (strSiteNo == "6004") {
                    return "<SRCH_PREFIX:contains:SHIN>"
                } else if (strSiteNo == "6009") {
                    return "<SRCH_PREFIX:contains:SD>"
                } else if (strSiteNo == "6001" || strSiteNo == "6002") {
                    if (mobileAppType == "41") { //삼성냉장고 앱일경우(only 점포상품만, 딜제외)
                        return "<SRCH_PREFIX:contains:FRG>"
                    }

                    val strFilterSiteNo = parameter.filterSiteNo?: ""
                    return if (strFilterSiteNo != "") {
                        ""
                    } else "<SRCH_PREFIX:contains:EMALL>"
                } else if (strSiteNo == "6100") {
                    return "<SRCH_PREFIX:contains:HOWDY>"
                } else if (strSiteNo == "6200") {
                    return "<SRCH_PREFIX:contains:STV>"
                } else if (strSiteNo == "6003") {
                    return "<SRCH_PREFIX:contains:BOOTS>"
                } else if (strSiteNo == "6300") {
                    return "<SRCH_PREFIX:contains:SIV>"
                }
            }
            return ""
        }
    },
    SRCH_PREFIX_GLOBAL {
        override fun getPrefix(parameter: Parameter): String {
            return "<SRCH_PREFIX:contains:GLOBAL>"
        }
    },
    SRCH_PREFIX_BSHOP {
        override fun getPrefix(parameter: Parameter): String {
            return "<SRCH_PREFIX:contains:BSHOP>"
        }
    },
    SRCH_PREFIX_DISP {
        override fun getPrefix(parameter: Parameter): String {
            if (parameter.siteNo!!.isNotBlank()) {
                val strSiteNo = parameter.siteNo?: "6005"
                if (strSiteNo == "6005") {
                    return "<SRCH_PREFIX:contains:SCOM>"
                } else if (strSiteNo == "6004") {
                    return "<SRCH_PREFIX:contains:SM>"
                } else if (strSiteNo == "6009") {
                    return "<SRCH_PREFIX:contains:SD>"
                } else if (strSiteNo == "6001") {
                    return "<SRCH_PREFIX:contains:EM>"
                } else if (strSiteNo == "6002") {
                    return "<SRCH_PREFIX:contains:TR>"
                } else if (strSiteNo == "6003") {
                    return "<SRCH_PREFIX:contains:BOOTS>"
                }
            }
            return ""
        }
    },
    FILTER_SITE_NO {
        override fun getPrefix(parameter: Parameter): String {
            if (parameter.filterSiteNo!!.isNotBlank()) {
                val strSiteNo = parameter.siteNo?: "6005"
                val strFilterSiteNo = parameter.filterSiteNo?: "6005"
                if (strSiteNo == "6005") {
                    if (strFilterSiteNo == "6004") {
                        return "<SRCH_PREFIX:contains:SCOM-SM>"
                    } else if (strFilterSiteNo == "6009") {
                        return "<SRCH_PREFIX:contains:SCOM-SD>"
                    } else if (strFilterSiteNo == "6001") {
                        return "<SRCH_PREFIX:contains:SCOM-EM>"
                    } else if (strFilterSiteNo == "6002") {
                        return "<SRCH_PREFIX:contains:SCOM-TR>"
                    } else if (strFilterSiteNo == "6003") {
                        return "<SRCH_PREFIX:contains:SCOM-BT>"
                    } else if (strFilterSiteNo == "6100") {
                        return "<SRCH_PREFIX:contains:SCOM-HD>"
                    } else if (strFilterSiteNo == "6200") {
                        return "<SRCH_PREFIX:contains:SCOM-TV>"
                    } else if (strFilterSiteNo == "6300") {
                        return "<SRCH_PREFIX:contains:SCOM-SI>"
                    }
                } else if (strSiteNo == "6001" || strSiteNo == "6002") {
                    if (strFilterSiteNo == "6001") {
                        return "<SRCH_PREFIX:contains:EM>"
                    } else if (strFilterSiteNo == "6002") {
                        return "<SRCH_PREFIX:contains:TR>"
                    } else if (strFilterSiteNo == "6200") {
                        return "<SRCH_PREFIX:contains:STV>"
                    }
                }
            }
            return ""
        }
    },
    SITE_NO_USE_FILTER {
        override fun getPrefix(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo?: "6005"
            val strFilterSiteNo = parameter.filterSiteNo?: ""
            return if (strFilterSiteNo != "") {
                "<SITE_NO:contains:$strFilterSiteNo>"
            } else {
                "<SITE_NO:contains:$strSiteNo>"
            }
        }
    },
    SITE_NO_ONLY {
        override fun getPrefix(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo?: "6005"
            return "<SITE_NO:contains:$strSiteNo>"
        }
    },
    SALESTR_LST {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            val prefix = "<SALESTR_LST:contains:"
            val emSalestrNo = userInfo.emSaleStrNo?: ""
            val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn?: "N"
            val trSalestrNo = userInfo.trSaleStrNo?: ""
            val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn?: "N"
            val bnSalestrNo = userInfo.bnSaleStrNo?: ""
            val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn?: "N"
            val strSiteNo = parameter.siteNo?: "6005"
            var deptSalestrNo = parameter.salestrNo?: "0001"
            val pickuSalestr = parameter.pickuSalestr?: ""
            val hwSalestrNo = userInfo.hwSaleStrNo?: ""
            val hwRsvtShppPsblYn = userInfo.hwRsvtShppPsblYn?: "N"
            val mobileAppType = parameter.mobileAppType?: ""
            val emDualzSalestrNo = emSalestrNo + (userInfo.emDualSaleStrNo?: "0000")

            //매직픽업 점포만 있을경우, picku점포코드 대체(매직픽업 없는 점포의 경우 shpp에서 걸러짐)
            if (pickuSalestr != "" && parameter.salestrNo!!.isEmpty()) deptSalestrNo = pickuSalestr

            if (parameter.target.equals("chat_ven_items", ignoreCase = true)) {
                return prefix + "6005|" + deptSalestrNo + ">"
            }

            if (strSiteNo == "6005") {
                return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6004") {
                return prefix + "6005|" + deptSalestrNo + ">"
            } else if (strSiteNo == "6009") {
                return "$prefix$deptSalestrNo>"
            } else if (strSiteNo == "6001") {
                //삼성냉장고 앱일경우 온라인생품 제외
                return if (mobileAppType == "41") {
                    "$prefix$emSalestrNo$emRsvtShppPsblYn|$trSalestrNo$trRsvtShppPsblYn|$bnSalestrNo$bnRsvtShppPsblYn|$emDualzSalestrNo>"
                } else prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6002") {
                return if (parameter.filterSiteNo!!.isBlank()) {
                    prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
                } else prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6100") {
                return "$prefix$hwSalestrNo$hwRsvtShppPsblYn>"
            } else if (strSiteNo == "6200") {
                return prefix + "6005" + ">"
            } else if (strSiteNo == "6003") {
                return prefix + "6005|" + bnSalestrNo + bnRsvtShppPsblYn + ">"
            } else if (strSiteNo == "6300") {
                return prefix + "6005" + ">"
            }
            return ""
        }
    },
    SALESTR_LST_GROUP {
        override fun getPrefix(parameter: Parameter): String {
            // 그룹핑 대상은 지점 검색으로 인해 필터링 되지 않도록 한다.
            val userInfo = parameter.userInfo!!
            val prefix = "<SALESTR_LST:contains:"
            val emSalestrNo = userInfo.emSaleStrNo?: ""
            val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn?: ""
            val trSalestrNo = userInfo.trSaleStrNo?: ""
            val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn?: ""
            val bnSalestrNo = userInfo.bnSaleStrNo?: ""
            val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn?: "N"
            val strSiteNo = parameter.siteNo?: "6005"
            val deptSalestrNo = "0001"
            val hwSalestrNo = userInfo.hwSaleStrNo?: ""
            val hwRsvtShppPsblYn = userInfo.hwRsvtShppPsblYn?: "N"
            val mobileAppType = parameter.mobileAppType?: ""
            val emDualzSalestrNo = emSalestrNo + (userInfo.emDualSaleStrNo?: "0000")

            if (parameter.target.equals("chat_ven_items", ignoreCase = true)) {
                return prefix + "6005|" + deptSalestrNo + ">"
            }

            if (strSiteNo == "6005") {
                return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6004" && deptSalestrNo == "0001") {
                return prefix + "6005|" + deptSalestrNo + ">"
            } else if (strSiteNo == "6004" && deptSalestrNo != "0001") {
                return "$prefix$deptSalestrNo>"
            } else if (strSiteNo == "6009") {
                return "$prefix$deptSalestrNo>"
            } else if (strSiteNo == "6001") {
                //삼성냉장고 앱일경우 온라인생품 제외
                return if (mobileAppType == "41") {
                    "$prefix$emSalestrNo$emRsvtShppPsblYn|$trSalestrNo$trRsvtShppPsblYn|$bnSalestrNo$bnRsvtShppPsblYn|$emDualzSalestrNo>"
                } else prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6002") {
                return if (parameter.filterSiteNo!!.isBlank()) {
                    prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
                } else prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
            } else if (strSiteNo == "6100") {
                return "$prefix$hwSalestrNo$hwRsvtShppPsblYn>"
            } else if (strSiteNo == "6200") {
                return prefix + "6005" + ">"
            } else if (strSiteNo == "6003") {
                return prefix + "6005|" + bnSalestrNo + bnRsvtShppPsblYn + ">"
            } else if (strSiteNo == "6300") {
                return prefix + "6005" + ">"
            }
            return ""
        }
    },
    SALESTR_LST_MALL {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            val prefix = "<SALESTR_LST:contains:"
            val emSalestrNo = userInfo.emSaleStrNo?: ""
            val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn?: ""
            val trSalestrNo = userInfo.trSaleStrNo?: ""
            val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn?: ""
            val bnSalestrNo = userInfo.bnSaleStrNo?: ""
            val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn?: "N"
            val hwSalestrNo = userInfo.hwSaleStrNo?: ""
            val hwRsvtShppPsblYn = userInfo.hwRsvtShppPsblYn?: "N"
            val deptSalestrNo = "0001"
            val emDualzSalestrNo = emSalestrNo + (userInfo.emDualSaleStrNo?: "0000")
            return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"
        }
    },
    MBR_CO_TYPE {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            val type = userInfo.mbrType?: ""
            val coId = userInfo.mbrCoId?: ""
            if (type != "" && coId != "") {
                if (type == "B2C") {
                    return "<APL_TGT_VEN_LST:contains:$coId>"
                } else if (type == "B2E") {
                    return "<APL_B2E_MBRCO_LST:contains:B2E|$coId>"
                }
            }
            return ""
        }
    },
    MBR_CO_TYPE_CONTENTS {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            val type = userInfo.mbrType?: ""
            val coId = userInfo.mbrCoId?: ""
            if (type != "" && coId != "") {
                if (type == "B2C") {
                    return "<APL_B2C_LST:contains:$coId>"
                } else if (type == "B2E") {
                    return "<APL_B2E_LST:contains:B2E|$coId>"
                }
            }
            return ""
        }
    },
    SPL_VEN_ID {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.splVenId!!.isNotBlank()) {
                "<SPL_VEN_ID:contains:" + parameter.splVenId + ">"
            } else ""
        }
    },
    LRNK_SPL_VEN_ID {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.lrnkSplVenId!!.isNotBlank()) {
                "<LRNK_SPL_VEN_ID:contains:" + parameter.lrnkSplVenId + ">"
            } else ""
        }
    },
    BRAND_ID {
        override fun getPrefix(parameter: Parameter): String {
            if (parameter.brand!!.isNotBlank()) {
                return "<BRAND_ID:contains:" + parameter.brand + ">"
            }
            return if (parameter.brandId!!.isNotBlank()) {
                "<BRAND_ID:contains:" + parameter.brandId + ">"
            } else ""
        }
    },
    BSHOP_ID {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.bshopId!!.isNotBlank()) {
                "<BSHOPID_LST:contains:" + parameter.bshopId + ">"
            } else ""
        }
    },
    DISP_CTG_LST {
        override fun getPrefix(parameter: Parameter): String {
            if (!parameter.target.equals("all", ignoreCase = true)) {
                if (parameter.dispCtgId!!.isNotBlank()) {
                    return "<DISP_CTG_LST:contains:" + parameter.dispCtgId + ">"
                } else if (parameter.ctgId!!.isNotBlank()) {
                    return "<DISP_CTG_LST:contains:" + parameter.ctgId + ">"
                }
            }
            return ""
        }
    },
    TEM_DISP_CTG_ID {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val ctgNmPrefix = StringBuilder()
            val strSiteNo = parameter.siteNo?: ""
            var ctgId = parameter.ctgId?: ""
            val parentCtgId = parameter.parentCtgId?: ""
            val ctgIds = parameter.ctgIds?: ""
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"

            val themeYn = parameter.themeYn?: ""
            // CTG IDS 부터
            // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
            if (themeYn == "Y") {
                ctgNmPrefix.append("TEM_")
            } else if (strSiteNo == "6005") {
                ctgNmPrefix.append("SCOM_")
            }
            if (ctgLv == "1") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
                else
                    ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
            } else if (ctgLv == "2") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
                else
                    ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID")
            } else if (ctgLv == "3") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID")
                else
                    ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID")
            } else if (ctgLv == "4") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID")
                else
                    ctgNmPrefix.append("TEM_DISP_CTG_DCLS_ID")
            } else {
                ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
            }
            if (ctgIds != "") {
                if (ctgLast == "Y") {
                    ctgId = parentCtgId
                    prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">")
                } else {
                    var operatorIdx = 0
                    if (ctgIds != "") {
                        prefix.append("<").append(ctgNmPrefix).append(":contains:")
                        var i = 0
                        val st = StringTokenizer(ctgIds, "|")
                        while (st.hasMoreTokens()) {
                            val c = st.nextToken()
                            if (i > 0) prefix.append("|")
                            prefix.append(c)
                            i++
                        }
                        prefix.append(">")
                        operatorIdx++
                    }
                }
            } else if (ctgId != "") {
                if (ctgLast == "Y") ctgId = parentCtgId
                prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">")
            }
            return prefix.toString()
        }
    },
    TEM_DISP_ITEM_CTG_ID {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val ctgNmPrefix = StringBuilder()
            val strSiteNo = parameter.siteNo?: ""
            val ctgId = parameter.ctgId?: ""
            val parentCtgId = parameter.parentCtgId?: ""
            val ctgIds = parameter.ctgIds?: ""
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"
            val themeYn = parameter.themeYn?: ""
            // CTG IDS 부터
            // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
            if (themeYn == "Y") {
                ctgNmPrefix.append("TEM_")
            } else if (strSiteNo == "6005") {
                ctgNmPrefix.append("SCOM_")
            }
            if (ctgLv == "1") {
                ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
            } else if (ctgLv == "2") {
                ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID")
            } else if (ctgLv == "3") {
                ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID")
            } else if (ctgLv == "4") {
                ctgNmPrefix.append("TEM_DISP_CTG_DCLS_ID")
            } else {
                ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID")
            }
            if (ctgIds != "") {
                var operatorIdx = 0
                if (ctgIds != "") {
                    prefix.append("<").append(ctgNmPrefix).append(":contains:")
                    var i = 0
                    val st = StringTokenizer(ctgIds, "|")
                    while (st.hasMoreTokens()) {
                        val c = st.nextToken()
                        if (i > 0) prefix.append("|")
                        prefix.append(c)
                        i++
                    }
                    prefix.append(">")
                    operatorIdx++
                }
            } else if (ctgId != "") {
                prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">")
            }
            return prefix.toString()
        }
    },
    DEVICE_CD {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.aplTgtMediaCd!!.isNotBlank()) {
                "<DISP_APL_RNG_TYPE_CD:contains:" + parameter.aplTgtMediaCd + ">"
            } else ""
        }
    },
    DVIC_DIV_CD {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            var dvicDivCd = userInfo.deviceDivCd?: "10"
            if (dvicDivCd != "10") {
                dvicDivCd = "20"
            }
            return "<DVIC_DIV_CD:contains:$dvicDivCd>"
        }
    },
    COLOR {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.color!!.isNotBlank()) {
                "<COLOR_LST:contains:" + parameter.color + ">"
            } else ""
        }
    },
    SIZE {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.size!!.isNotBlank()) {
                "<SIZE_LST:contains:" + parameter.size + ">"
            } else ""
        }
    },
    BSHOP {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.bshopId!!.isNotBlank()) {
                "<BSHOP_ID:contains:" + parameter.bshopId + ">"
            } else ""
        }
    },
    SRCH_PSBL_YN {
        override fun getPrefix(parameter: Parameter): String {
            //업체 채팅은 검색가능여부가 N 이더라도 노출한다.
            val strTarget = parameter.target?: ""
            return if (strTarget.equals("chat_ven_items", ignoreCase = true)) {
                ""
            } else "<SRCH_PSBL_YN:contains:Y>"
        }
    },
    SCOM_EXPSR_YN {
        override fun getPrefix(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo?: "6005"
            val strFilterSiteNo = parameter.filterSiteNo?: ""
            return if (strSiteNo == "6005" && (strFilterSiteNo == "" || strFilterSiteNo == "6005")) {
                "<SCOM_EXPSR_YN:contains:Y>"
            } else ""
        }
    },
    SCOM_EXPSR_YN_ALL {
        override fun getPrefix(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo?: "6005"
            return if (strSiteNo == "6005") {
                "<SCOM_EXPSR_YN:contains:Y>"
            } else ""
        }
    },
    SCOM_EXPSR_YN_SSG {
        override fun getPrefix(parameter: Parameter): String {
            return "<SCOM_EXPSR_YN:contains:Y>"
        }
    },
    SRCH_CTG_PREFIX {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val ctgNmPrefix = StringBuilder()
            val strSiteNo = parameter.siteNo?: ""
            var ctgId = parameter.ctgId?: ""
            val parentCtgId = parameter.parentCtgId?: ""
            val ctgIds = parameter.ctgIds?: ""
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"

            val themeYn = parameter.themeYn?: ""
            val themeCtgIds = parameter.themeCtgIds?: ""
            // CTG IDS 부터
            // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
            if (themeYn == "Y") {
                ctgNmPrefix.append("TEM_")
            } else if (strSiteNo == "6005") {
                ctgNmPrefix.append("SCOM_")
            }

            if (ctgLast == "Y" && parentCtgId != "") {
                ctgId = parentCtgId
            }

            if (ctgLv == "1") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("DISP_CTG_LCLS_ID")
                else
                    ctgNmPrefix.append("DISP_CTG_LCLS_ID")
            } else if (ctgLv == "2") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("DISP_CTG_LCLS_ID")
                else
                    ctgNmPrefix.append("DISP_CTG_MCLS_ID")
            } else if (ctgLv == "3") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("DISP_CTG_MCLS_ID")
                else
                    ctgNmPrefix.append("DISP_CTG_SCLS_ID")
            } else if (ctgLv == "4") {
                if (ctgLast == "Y")
                    ctgNmPrefix.append("DISP_CTG_SCLS_ID")
                else
                    ctgNmPrefix.append("DISP_CTG_DCLS_ID")
            } else {
                ctgNmPrefix.append("DISP_CTG_LCLS_ID")
            }
            if (ctgIds != "" || themeCtgIds != "") {
                var operatorIdx = 0
                if (ctgIds != "") {
                    prefix.append("<").append(ctgNmPrefix).append(":contains:")
                    var i = 0
                    val st = StringTokenizer(ctgIds, "|")
                    while (st.hasMoreTokens()) {
                        val c = st.nextToken()
                        if (i > 0) prefix.append("|")
                        prefix.append(c)
                        i++
                    }
                    prefix.append(">")
                    operatorIdx++
                }
                if (themeCtgIds != "") {
                    if (operatorIdx > 0) prefix.append("|")
                    prefix.append("<TEM_").append(ctgNmPrefix).append(":contains:")
                    var i = 0
                    val st = StringTokenizer(themeCtgIds, "|")
                    while (st.hasMoreTokens()) {
                        val c = st.nextToken()
                        if (i > 0) prefix.append("|")
                        prefix.append(c)
                        i++
                    }
                    prefix.append(">")
                }
            } else if (ctgId != "") {
                prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">")
            }
            return prefix.toString()
        }
    },
    BANR_TGT_DIV_CD {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.aplTgtMediaCd!!.isNotBlank()) {
                "<SHRTC_TGT_TYPE_CD:contains:" + parameter.aplTgtMediaCd + ">"
            } else ""
        }
    },
    BANR_DIV_CD {
        override fun getPrefix(parameter: Parameter): String {
            val userInfo = parameter.userInfo!!
            var devicedivcd = userInfo.deviceDivCd?: ""
            //모바일 웹/앱만 해당
            if (devicedivcd != "" && devicedivcd != "10") {
                if (Integer.parseInt(devicedivcd) > 20 || !userInfo.mobileAppNo.isNullOrEmpty()) devicedivcd = "30"
                return "<SHRTC_DIV_CD:contains:00|$devicedivcd>"
            }
            return ""
        }
    },
    SRCH_CTG_ITEM_PREFIX {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val ctgNmPrefix = StringBuilder()
            val strSiteNo = parameter.siteNo?: ""
            val ctgId = parameter.ctgId?: ""
            val ctgIds = parameter.ctgIds?: ""
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"
            val themeYn = parameter.themeYn?: ""
            val themeCtgIds = parameter.themeCtgIds?: ""
            // CTG IDS 부터
            // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
            if (themeYn == "Y") {
                ctgNmPrefix.append("TEM_")
            } else if (strSiteNo == "6005") {
                ctgNmPrefix.append("SCOM_")
            }
            if (ctgLv == "1") {
                ctgNmPrefix.append("DISP_CTG_LCLS_ID")
            } else if (ctgLv == "2") {
                ctgNmPrefix.append("DISP_CTG_MCLS_ID")
            } else if (ctgLv == "3") {
                ctgNmPrefix.append("DISP_CTG_SCLS_ID")
            } else if (ctgLv == "4") {
                ctgNmPrefix.append("DISP_CTG_DCLS_ID")
            } else if (ctgLv == "5") {
                ctgNmPrefix.append("DISP_CTG_DCLS_ID")
            }
            if (ctgIds != "" || themeCtgIds != "") {
                var operatorIdx = 0
                if (ctgIds != "") {
                    prefix.append("<").append(ctgNmPrefix).append(":contains:")
                    var i = 0
                    val st = StringTokenizer(ctgIds, "|")
                    while (st.hasMoreTokens()) {
                        val c = st.nextToken()
                        if (i > 0) prefix.append("|")
                        prefix.append(c)
                        i++
                    }
                    prefix.append(">")
                    operatorIdx++
                }
                if (themeCtgIds != "") {
                    if (operatorIdx > 0) prefix.append("|")
                    prefix.append("<TEM_").append(ctgNmPrefix).append(":contains:")
                    var i = 0
                    val st = StringTokenizer(themeCtgIds, "|")
                    while (st.hasMoreTokens()) {
                        val c = st.nextToken()
                        if (i > 0) prefix.append("|")
                        prefix.append(c)
                        i++
                    }
                    prefix.append(">")
                }
            } else if (ctgId != "") {
                prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">")
            }
            return prefix.toString()
        }
    },
    BENEFIT_FILTER {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val benefit = parameter.benefit?: ""

            if (benefit != "") {
                prefix.append("<FILTER:contains:")
                var i = 0
                val st = StringTokenizer(benefit, "|")
                while (st.hasMoreTokens()) {
                    val c = st.nextToken()
                    if (i > 0) prefix.append("|")
                    prefix.append(c.toUpperCase())
                    i++
                }
                prefix.append(">")
            }

            return prefix.toString()
        }
    },
    CLASSIFICATION_FILTER {
        override fun getPrefix(parameter: Parameter): String {
            val prefix = StringBuilder()
            val clsFilter = parameter.clsFilter?: ""

            if (clsFilter != "") {
                val i = 0
                val st = StringTokenizer(clsFilter, "^")
                while (st.hasMoreTokens()) {
                    val c = st.nextToken()

                    prefix.append("<FILTER:contains:")
                    prefix.append(c.toUpperCase())
                    prefix.append(">")
                }

            }

            return prefix.toString()
        }
    },
    USE_YN_PREFIX {
        override fun getPrefix(parameter: Parameter): String {
            return if (parameter.useYn!!.isNotBlank()) {
                "<USE_YN:contains:" + parameter.useYn + ">"
            } else ""
        }
    };

    abstract fun getPrefix(parameter: Parameter): String
}