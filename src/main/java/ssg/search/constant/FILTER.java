package ssg.search.constant;

@Deprecated
public enum FILTER {
    // 이마트 점포상품
    EMART{
        public String getPrefix() {
            return "EM";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 오반장 상품
    OBANJANG{
        public String getPrefix() {
            return "OBANJANG";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 신문광고 상품
    NEWS{
        public String getPrefix() {
            return "NEWS";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 특가상품
    SP_PRICE{
        public String getPrefix() {
            return "SPPRICE";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 이마트 온라인 상품 ( SITE, SALESTR_LST 를 사용함 )
    EMARTONLINE{
        public String getPrefix() {
            return "EMON";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 트레이더스 상품 ( SITE, SALESTR_LST 를 사용함 )
    TRADERS{
        public String getPrefix() {
            return "TR";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 분스 상품 ( SITE, SALESTR_LST 를 사용함 )
    BOONS{
        public String getPrefix() {
            return "BN";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 신세게 몰 상품 ( SITE, SALESTR_LST 를 사용함 )
    SHINSEGAE{
        public String getPrefix() {
            return "SM";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 신세계 백화점 상품 ( SITE, SALESTR_LST 를 사용함 )
    DEPARTMENT{
        public String getPrefix() {
            return "SD";
        }
        public String getType() {
            return "CLS";
        }
    },
    // 점포예약
    RSVT{
        public String getPrefix() {
            return "RSVT";
        }
        public String getType() {
            return "SHPP";
        }
    },
    // 무료배송
    FREE{
        public String getPrefix() {
            return "FREE";
        }
        public String getType() {
            return "SHPP";
        }
    },
    // 해외배송
    FRG{
        public String getPrefix() {
            StringBuffer bf = new StringBuffer();
            return "FRG";
        }
        public String getType() {
            return "SHPP";
        }
    },
    PICKU{
    	public String getPrefix() {
    		return "PICKU";
    	}
    	public String getType() {
    		return "SHPP";
    	}
    },
    PACK{
    	public String getPrefix() {
    		return "PACK";
    	}
    	public String getType() {
    		return "SHPP";
    	}
    },
    STORE{
    	public String getPrefix() {
    		return "STORE";
    	}
    	public String getType() {
    		return "SHPP";
    	}
    }, 
    QSHPP{
    	public String getPrefix() {
    		return "QSHPP";
    	}
    	public String getType() {
    		return "SHPP";
    	}
    }
    ;
    
    public abstract String getPrefix();
    public abstract String getType();
}
