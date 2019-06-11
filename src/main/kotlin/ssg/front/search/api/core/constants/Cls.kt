package ssg.front.search.api.core.constants

enum class Cls {
    // 이마트 점포상품
    EMART {
        override val prefix: String
            get() = "EM"
    },
    // 오반장 상품
    OBANJANG {
        override val prefix: String
            get() = "OBANJANG"
    },
    // 신문광고 상품
    NEWS {
        override val prefix: String
            get() = "NEWS"
    },
    // 특가상품
    SP_PRICE {
        override val prefix: String
            get() = "SPPRICE"
    },
    // 이마트 온라인 상품 ( SITE, SALESTR_LST 를 사용함 )
    EMARTONLINE {
        override val prefix: String
            get() = "EMON"
    },
    // 트레이더스 상품 ( SITE, SALESTR_LST 를 사용함 )
    TRADERS {
        override val prefix: String
            get() = "TR"
    },
    // 분스 상품 ( SITE, SALESTR_LST 를 사용함 )
    BOONS {
        override val prefix: String
            get() = "BN"
    },
    // 신세게 몰 상품 ( SITE, SALESTR_LST 를 사용함 )
    SHINSEGAE {
        override val prefix: String
            get() = "SM"
    },
    // 신세계 백화점 상품 ( SITE, SALESTR_LST 를 사용함 )
    DEPARTMENT {
        override val prefix: String
            get() = "SD"
    };

    abstract val prefix: String
}
