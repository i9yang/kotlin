package ssg.search.constant;

public enum Shpp {
	// 점포예약
    RSVT{
        public String getPrefix() {
            return "RSVT";
        }
    },
    // 무료배송
    FREE{
        public String getPrefix() {
            return "FREE";
        }
    },
    // 해외배송
    FRG{
        public String getPrefix() {
            StringBuffer bf = new StringBuffer();
            return "FRG";
        }
    },
    // 매직픽업
    PICKU{
		public String getPrefix(){
			return "PICKU";
		}
    },
    //퀵배송
    QSHPP{
    	public String getPrefix(){
    		return "QSHPP";
    	}
    }
    ;
	
    public abstract String getPrefix();
}
