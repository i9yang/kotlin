package ssg.search.constant;

public enum BenefitFilters {
	// 이마트 점포상품
    EMART{
        public String getPrefix() {
            return "EM";
        }
		public String getField(){
			return "CLS";
		}
    },
    // 오반장 상품
    OBANJANG{
        public String getPrefix() {
            return "OBANJANG";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 신문광고 상품
    NEWS{
        public String getPrefix() {
            return "NEWS";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 특가상품
    SP_PRICE{
        public String getPrefix() {
            return "SPPRICE";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 이마트 온라인 상품 ( SITE, SALESTR_LST 를 사용함 )
    EMARTONLINE{
        public String getPrefix() {
            return "EMON";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 트레이더스 상품 ( SITE, SALESTR_LST 를 사용함 )
    TRADERS{
        public String getPrefix() {
            return "TR";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 분스 상품 ( SITE, SALESTR_LST 를 사용함 )
    BOONS{
        public String getPrefix() {
            return "BN";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 신세게 몰 상품 ( SITE, SALESTR_LST 를 사용함 )
    SHINSEGAE{
        public String getPrefix() {
            return "SM";
        }
        public String getField(){
        	return "CLS";
		}
    },
    // 신세계 백화점 상품 ( SITE, SALESTR_LST 를 사용함 )
    DEPARTMENT{
        public String getPrefix() {
            return "SD";
        }
        public String getField(){
        	return "CLS";
		}
    },
    FREE{
		public String getPrefix(){
			return "FREE";
		}
		public String getField(){
			return "FILTER";
		}
    },
    PRIZE{
		public String getPrefix(){
			return "PRIZE";
		}
		public String getField(){
			return "FILTER";
		}
    },
    // 매직픽업
    PICKU{
    	public String getPrefix() {
    		return "PICKU";
    	}
    	public String getField() {
    		return "SHPP";
    	}
    },
    // 퀵배송
    QSHPP{
    	public String getPrefix() {
    		return "QSHPP";
    	}
    	public String getField() {
    		return "SHPP";
    	}
    },
    // 쓱콘
    CON{
    	public String getPrefix() {
    		return "CON";
    	}
    	public String getField() {
    		return "SHPP";
    	}
    }
    ;
    public abstract String getPrefix();
    public abstract String getField();
}

