package ssg.front.search.api.core.constants

import ssg.front.search.api.base.Sort
import ssg.front.search.api.dto.Parameter

enum class Sorts {
    WEIGHT {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("WEIGHT", 1)
        }
    },
    RANK {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("RANK", 1)
        }
    },
    SALE {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("SALE_QTY", 1)
        }
    },
    SCR {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("RECOM_EVAL_SCR", 1)
        }
    },
    CNT {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("RECOM_EVAL_CNT", 1)
        }
    },
    REGDT {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("ITEM_REG_DT", 1)
        }
    },
    RATE {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("RATE", 1)
        }
    },
    PRCDSC {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("SELLPRC", 1)
        }
    },
    PRCASC {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("SELLPRC", 0)
        }
    },
    UPRCASC {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("UNIT_PRC", 0)
        }
    },
    DISP_BEST {
        override fun getSort(parameter: Parameter): Sort? {
            val strSiteNo = parameter.siteNo

            return if (strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6300") {
                Sort("SHIN_DISP_BEST_SCR", 1)
            } else if (strSiteNo == "6001" || strSiteNo == "6002" || strSiteNo == "6003" || strSiteNo == "6200" || strSiteNo == "6100") {  //@ BOOT개발시확인
                Sort("EMART_DISP_BEST_SCR", 1)
            } else if (strSiteNo == "6005") {
                Sort("SCOM_DISP_BEST_SCR", 1)
            } else {
                null
            }
        }
    },
    BEST_SCR {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("SRCH_TYPE_THRD_SCR", 1)
        }
    },
    BEST {
        override fun getSort(parameter: Parameter): Sort? {
            return null
        }
    },
    WRT_DATE {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("WRT_DATE", 1)
        }
    },
    DISP_STRT_DTS {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("DISP_STRT_DTS", 1)
        }
    },
    VIRTUAL_CNT {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("CNT", 1)
        }
    },
    THRD {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("SRCH_TYPE_THRD_SCR", 1)
        }
    },
    TRECIPE_BEST_SCR {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("TRECIPE_BEST_SCR", 0)
        }
    },
    REG_DTS {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("REG_DTS", 1)
        }
    },
    CTGORDR {
        override fun getSort(parameter: Parameter): Sort {
            return Sort("DISP_CTG_ORDR.DISP_ORDR", 0)
        }
    };

    abstract fun getSort(parameter: Parameter): Sort?
}