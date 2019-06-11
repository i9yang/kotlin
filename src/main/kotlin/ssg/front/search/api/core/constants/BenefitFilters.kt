package ssg.front.search.api.core.constants

enum class BenefitFilters {
    // 이마트 점포상품
    EMART {
        override val prefix: String
            get() = "EM"
        override val field: String
            get() = "CLS"
    },
    // 오반장 상품
    OBANJANG {
        override val prefix: String
            get() = "OBANJANG"
        override val field: String
            get() = "CLS"
    },
    // 신문광고 상품
    NEWS {
        override val prefix: String
            get() = "NEWS"
        override val field: String
            get() = "CLS"
    },
    // 특가상품
    SP_PRICE {
        override val prefix: String
            get() = "SPPRICE"
        override val field: String
            get() = "CLS"
    },
    // 이마트 온라인 상품 ( SITE, SALESTR_LST 를 사용함 )
    EMARTONLINE {
        override val prefix: String
            get() = "EMON"
        override val field: String
            get() = "CLS"
    },
    // 트레이더스 상품 ( SITE, SALESTR_LST 를 사용함 )
    TRADERS {
        override val prefix: String
            get() = "TR"
        override val field: String
            get() = "CLS"
    },
    // 분스 상품 ( SITE, SALESTR_LST 를 사용함 )
    BOONS {
        override val prefix: String
            get() = "BN"
        override val field: String
            get() = "CLS"
    },
    // 신세게 몰 상품 ( SITE, SALESTR_LST 를 사용함 )
    SHINSEGAE {
        override val prefix: String
            get() = "SM"
        override val field: String
            get() = "CLS"
    },
    // 신세계 백화점 상품 ( SITE, SALESTR_LST 를 사용함 )
    DEPARTMENT {
        override val prefix: String
            get() = "SD"
        override val field: String
            get() = "CLS"
    },
    FREE {
        override val prefix: String
            get() = "FREE"
        override val field: String
            get() = "FILTER"
    },
    PRIZE {
        override val prefix: String
            get() = "PRIZE"
        override val field: String
            get() = "FILTER"
    },
    // 매직픽업
    PICKU {
        override val prefix: String
            get() = "PICKU"
        override val field: String
            get() = "SHPP"
    },
    // 퀵배송
    QSHPP {
        override val prefix: String
            get() = "QSHPP"
        override val field: String
            get() = "SHPP"
    },
    // 쓱콘
    CON {
        override val prefix: String
            get() = "CON"
        override val field: String
            get() = "SHPP"
    };

    abstract val prefix: String
    abstract val field: String
}