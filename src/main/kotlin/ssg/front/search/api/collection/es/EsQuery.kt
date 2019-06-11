package ssg.front.search.api.collection.es

import com.google.common.base.Joiner
import ssg.front.search.api.dto.FrontUserInfo
import ssg.front.search.api.dto.Parameter

enum class EsQuery {
    SRCH_PREFIX_MALL {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            return "SCOM"
        }
    },
    SRCH_PREFIX_VIRTUAL {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            return "VIRTUAL"
        }
    },
    SRCH_PREFIX_BSHOP {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            return "BSHOP"
        }
    },
    SRCH_PREFIX_GLOBAL {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            return "GLOBAL"
        }
    },
    SRCH_PREFIX_DISP_FIX_SITE_NO {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo

            when (strSiteNo) {
                "6005" -> return "SCOM"
                "6004" -> return "SM"
                "6009" -> return "SD"
                "6001" -> return "EM"
                "6002" -> return "TR"
                "6003" -> return "BOOTS"
                "6100" -> return "HOWDY"
                "6200" -> return "STV"
                "6300" -> return "SIV"
                else -> return ""
            }
        }
    },
    SRCH_PREFIX_DISP {
        override fun getField(parameter: Parameter): String {
            return "SRCH_PREFIX"
        }

        override fun getValue(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo
            val strFilterSiteNo = parameter.filterSiteNo ?: "6005"

            when (strSiteNo) {
                "6005" -> {
                    val value = "SCOM"

                    if (strFilterSiteNo == "6004") {
                        return "$value AND SCOM-SM"
                    } else if (strFilterSiteNo == "6009") {
                        return "$value AND SCOM-SD"
                    } else if (strFilterSiteNo == "6001") {
                        return "$value AND SCOM-EM"
                    } else if (strFilterSiteNo == "6002") {
                        return "$value AND SCOM-TR"
                    } else if (strFilterSiteNo == "6003") {
                        return "$value AND SCOM-BT"
                    } else if (strFilterSiteNo == "6100") {
                        return "$value AND SCOM-HD"
                    } else if (strFilterSiteNo == "6200") {
                        return "$value AND SCOM-TV"
                    } else if (strFilterSiteNo == "6300") {
                        return "$value AND SCOM-SI"
                    }

                    return value
                }
                "6004" -> return "SM"
                "6009" -> return "SD"
                "6001" -> return "EM"
                "6002" -> return "TR"
                "6003" -> return "BOOTS"
                "6100" -> return "HOWDY"
                "6200" -> return "STV"
                "6300" -> return "SIV"
                else -> return ""
            }
        }
    },
    DISP_CTG_LST {
        override fun getField(parameter: Parameter): String {
            return "DISP_CTG_LST"
        }

        override fun getValue(parameter: Parameter): String {
            if (!parameter.dispCtgId.isNullOrEmpty()) {
                return parameter.dispCtgId!!
            } else if (!parameter.ctgId.isNullOrEmpty()) {
                return parameter.ctgId!!
            }

            return ""
        }
    },
    DISP_CTG_SUB_LST {
        override fun getField(parameter: Parameter): String {
            return "DISP_CTG_LST"
        }

        override fun getValue(parameter: Parameter): String {
            val orJoiner = Joiner.on(" OR ").skipNulls()

            return if (!parameter.dispCtgSubIds.isNullOrEmpty()) {
                orJoiner.join(parameter.dispCtgSubIds!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            } else ""

        }
    },
    BRAND_ID {
        override fun getField(parameter: Parameter): String {
            return "BRAND_ID"
        }

        override fun getValue(parameter: Parameter): String {
            if (parameter.brand.isNullOrEmpty() && parameter.brandId.isNullOrEmpty()) {
                return ""
            }
            val sb = StringBuilder()
            val brandList: Array<String> = if (!parameter.brand.isNullOrEmpty()) {
                parameter.brand!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            } else {
                parameter.brandId!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            if (brandList.isNotEmpty()) {
                for (i in brandList.indices) {
                    if (!brandList[i].isNullOrEmpty()) {
                        if (i != 0 && brandList.size > 1) {
                            sb.append(" OR ")
                        }
                        sb.append(brandList[i])
                    }
                }
            }
            return sb.toString()
        }
    },
    SALESTR_LST {
        override fun getField(parameter: Parameter): String {
            return "SALESTR_LST"
        }

        override fun getValue(parameter: Parameter): String {
            val userInfo = parameter.userInfo
            val emSalestrNo = userInfo.emSaleStrNo
            val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn
            val trSalestrNo = userInfo.trSaleStrNo
            val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn
            val bnSalestrNo = userInfo.bnSaleStrNo
            val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn
            val strSiteNo = parameter.siteNo
            var deptSalestrNo = parameter.salestrNo ?: "0001"
            val hwSalestrNo = userInfo.hwSaleStrNo
            val hwRsvtShppPsblYn = userInfo.hwRsvtShppPsblYn
            val emDualzSalestrNo = userInfo.emDualSaleStrNo ?: "0000"
            val pickuSalestr = parameter.pickuSalestr ?: ""

            //매직픽업 점포만 있을경우, picku점포코드 대체(매직픽업 없는 점포의 경우 shpp에서 걸러짐)
            if (pickuSalestr != "" && parameter.salestrNo.isNullOrEmpty()) deptSalestrNo = pickuSalestr

            if (strSiteNo == "6005") {
                return "6005 OR $deptSalestrNo OR $emSalestrNo$emRsvtShppPsblYn OR $trSalestrNo$trRsvtShppPsblYn OR $bnSalestrNo$bnRsvtShppPsblYn OR $hwSalestrNo$hwRsvtShppPsblYn OR $emSalestrNo$emDualzSalestrNo"
            } else if (strSiteNo == "6004") {
                return "6005 OR $deptSalestrNo"
            } else if (strSiteNo == "6009") {
                return deptSalestrNo
            } else if (strSiteNo == "6001" || strSiteNo == "6002") {
                return "6005 OR $emSalestrNo$emRsvtShppPsblYn OR $trSalestrNo$trRsvtShppPsblYn OR $bnSalestrNo$bnRsvtShppPsblYn OR $emSalestrNo$emDualzSalestrNo"
            } else if (strSiteNo == "6100") {
                return hwSalestrNo + hwRsvtShppPsblYn
            } else if (strSiteNo == "6200") {
                return "6005"
            } else if (strSiteNo == "6003") {
                return "6005 OR $bnSalestrNo$bnRsvtShppPsblYn"
            } else if (strSiteNo == "6300") {
                return "6005"
            }
            return ""
        }
    },
    SALESTR_LST_MALL {
        override fun getField(parameter: Parameter): String {
            return "SALESTR_LST"
        }

        override fun getValue(parameter: Parameter): String {
            var userInfo = FrontUserInfo()
            val emSalestrNo = userInfo.emSaleStrNo ?: ""
            val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn ?: ""
            val trSalestrNo = userInfo.trSaleStrNo ?: ""
            val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn ?: ""
            val bnSalestrNo = userInfo.bnSaleStrNo ?: ""
            val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn ?: "N"
            val hwSalestrNo = userInfo.hwSaleStrNo ?: ""
            val hwRsvtShppPsblYn = userInfo.hwRsvtShppPsblYn ?: "N"
            val emDualzSalestrNo = userInfo.emDualSaleStrNo ?: "0000"
            var deptSalestrNo = "0001"

            return "6005 OR $deptSalestrNo OR $emSalestrNo$emRsvtShppPsblYn OR $trSalestrNo$trRsvtShppPsblYn OR $bnSalestrNo$bnRsvtShppPsblYn OR $hwSalestrNo$hwRsvtShppPsblYn OR $emSalestrNo$emDualzSalestrNo"
        }
    },
    SIZE {
        override fun getField(parameter: Parameter): String {
            return "SIZE_LST"
        }

        override fun getValue(parameter: Parameter): String {
            if (parameter.size.isNullOrEmpty()) {
                return ""
            }
            val sb = StringBuilder()
            var sizeList = parameter.size!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (sizeList.isNotEmpty()) {
                for (i in sizeList.indices) {
                    if (!sizeList[i].isNullOrEmpty()) {
                        if (i != 0 && sizeList.size > 1) {
                            sb.append(" OR ")
                        }
                        sb.append(sizeList[i])
                    }
                }
            }
            return sb.toString()
        }
    },
    COLOR {
        override fun getField(parameter: Parameter): String {
            return "COLOR_LST"
        }

        override fun getValue(parameter: Parameter): String {
            if (parameter.color.isNullOrEmpty()) {
                return ""
            }
            val sb = StringBuilder()
            var colorList = parameter.color!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (colorList.isNotEmpty()) {
                for (i in colorList.indices) {
                    if (!colorList[i].isNullOrEmpty()) {
                        if (i != 0 && colorList.size > 1) {
                            sb.append(" OR ")
                        }
                        sb.append(colorList[i])
                    }
                }
            }
            return sb.toString()
        }
    },
    DEVICE_CD {
        override fun getField(parameter: Parameter): String {
            return "DISP_APL_RNG_TYPE_CD"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.aplTgtMediaCd.isNullOrEmpty()) {
                parameter.aplTgtMediaCd!!
            } else ""
        }
    },
    MBR_CO_TYPE {
        override fun getField(parameter: Parameter): String {
            var userInfo = FrontUserInfo()
            val type = userInfo.mbrType ?: ""
            if (type == "B2C") {
                return "APL_TGT_VEN_LST"
            } else if (type == "B2E") {
                return "APL_B2E_MBRCO_LST"
            }
            return "APL_TGT_VEN_LST"
        }

        override fun getValue(parameter: Parameter): String {
            var userInfo = FrontUserInfo()
            val type = userInfo.mbrType ?: ""
            var value = ""
            if (type == "B2E") {
                value += "B2E OR "
            }

            if (!userInfo.mbrCoId.isNullOrEmpty()) {
                value += userInfo.mbrCoId
            }
            return value
        }
    },
    SPL_VEN_ID {
        override fun getField(parameter: Parameter): String {
            return "SPL_VEN_ID"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.splVenId.isNullOrEmpty()) {
                parameter.splVenId!!
            } else ""
        }
    },
    LRNK_SPL_VEN_ID {
        override fun getField(parameter: Parameter): String {
            return "LRNK_SPL_VEN_ID"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.splVenId.isNullOrEmpty()) {
                parameter.splVenId!!
            } else ""
        }
    },
    SCOM_EXPSR_YN {
        override fun getField(parameter: Parameter): String {
            return "SCOM_EXPSR_YN"
        }

        override fun getValue(parameter: Parameter): String {
            val strSiteNo = parameter.siteNo
            val strFilterSiteNo = parameter.filterSiteNo ?: ""
            return if (strSiteNo == "6005" && (strFilterSiteNo == "" || strFilterSiteNo == "6005")) {
                "Y"
            } else ""
        }
    },
    PRC_FILTER {
        override fun getField(parameter: Parameter): String {
            return "SELLPRC"
        }

        override fun getValue(parameter: Parameter): String {
            val strMinPrc = parameter.minPrc ?: "*"
            val strMaxPrc = parameter.maxPrc ?: "*"

            return if (strMinPrc == "*" && strMaxPrc == "*") {
                ""
            } else {
                "[ $strMinPrc TO $strMaxPrc ]"
            }
        }
    },
    GRP_ADDR_ID {
        override fun getField(parameter: Parameter): String {
            return "GRP_ADDR_ID"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.grpAddrId.isNullOrEmpty()) {
                parameter.grpAddrId!!
            } else ""
        }
    },
    SHPPCST_ID {
        override fun getField(parameter: Parameter): String {
            return "SHPPCST_ID"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.shppcstId.isNullOrEmpty()) {
                parameter.shppcstId!!
            } else ""
        }
    },
    ITEM_SITE_NO {
        override fun getField(parameter: Parameter): String {
            return "SITE_NO"
        }

        override fun getValue(parameter: Parameter): String {
            return if (parameter.target == "es_bundle" && parameter.siteNo != "6005" && !parameter.itemSiteNo.isNullOrEmpty()) {
                parameter.itemSiteNo!!
            } else ""
        }
    },
    BSHOP_ID {
        override fun getField(parameter: Parameter): String {
            return "BSHOPID_LST"
        }

        override fun getValue(parameter: Parameter): String {
            return if (!parameter.bshopId.isNullOrEmpty()) {
                parameter.bshopId!!
            } else ""
        }
    };

    abstract fun getValue(parameter: Parameter): String
    abstract fun getField(parameter: Parameter): String
}