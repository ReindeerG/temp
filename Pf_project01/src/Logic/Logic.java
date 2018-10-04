package Logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CardNPlayer {
	private String userid;
	private int cardresult;
	public CardNPlayer(String userid, int cardresult) {
		this.userid=userid; this.cardresult=cardresult;
	}
	public String getUserid() {
		return userid;
	}
	public int getCardresult() {
		return cardresult;
	}
	public void setCardresult(int cardresult) {
		this.cardresult=cardresult;
	}
}

public class Logic {
	public static int Result (int card1, int card2) {
		Set<Integer> set = new HashSet<>();
		set.add(card1); set.add(card2);
		if(set.contains(5)&&set.contains(15)) {
			return 93800;	// »ïÆÈ±¤¶¯
		} else if (set.contains(1)&&set.contains(15)) {
			return 81800;	// ÀÏÆÈ±¤¶¯
		} else if (set.contains(1)&&set.contains(5)) {
			return 81300;	// ÀÏ»ï±¤¶¯
		} else if (set.contains(19)&&set.contains(20)) {
			return 210;		// Àå¶¯
		} else if (set.contains(17)&&set.contains(18)) {
			return 209;		// ±¸¶¯
		} else if (set.contains(15)&&set.contains(16)) {
			return 208;		// ÆÈ¶¯
		} else if (set.contains(13)&&set.contains(14)) {
			return 207;		// Ä¥¶¯
		} else if (set.contains(11)&&set.contains(12)) {
			return 206;		// À°¶¯
		} else if (set.contains(9)&&set.contains(10)) {
			return 205;		// ¿À¶¯
		} else if (set.contains(7)&&set.contains(8)) {
			return 204;		// »ç¶¯
		} else if (set.contains(5)&&set.contains(6)) {
			return 203;		// »ï¶¯
		} else if (set.contains(3)&&set.contains(4)) {
			return 202;		// ÀÌ¶¯
		} else if (set.contains(1)&&set.contains(2)) {
			return 201;		// ÀÏ¶¯
		} else if (set.contains(1)&&(set.contains(3)||set.contains(4))||set.contains(2)&&(set.contains(3)||set.contains(4))) {
			return 7812;		// ¾Ë¸®
		} else if (set.contains(1)&&(set.contains(7)||set.contains(8))||set.contains(2)&&(set.contains(7)||set.contains(8))) {
			return 7814;		// µ¶»ç
		} else if (set.contains(1)&&(set.contains(17)||set.contains(18))||set.contains(2)&&(set.contains(17)||set.contains(18))) {
			return 7819;		// ±¸»æ
		} else if (set.contains(1)&&(set.contains(19)||set.contains(20))||set.contains(2)&&(set.contains(19)||set.contains(20))) {
			return 7910;		// Àå»æ
		} else if (set.contains(7)&&(set.contains(19)||set.contains(20))||set.contains(8)&&(set.contains(19)||set.contains(20))) {
			return 7940;		// Àå»ç
		} else if (set.contains(7)&&(set.contains(11)||set.contains(12))||set.contains(8)&&(set.contains(11)||set.contains(12))) {
			return 7946;		// ¼¼·ú
		} else if (set.contains(7)&&set.contains(13)) {
			return 64171;		// ¾ÏÇà¾î»ç
		} else if (set.contains(5)&&set.contains(13)) {
			return 63171;		// ¶¯ÀâÀÌ
		} else if (set.contains(7)&&set.contains(17)) {
			return 64191;		// ¸Û±¸»ç
		} else if (set.contains(7)&&set.contains(18)||set.contains(8)&&set.contains(17)||set.contains(8)&&set.contains(18)) {
			return 64090;		// ±¸»ç
		} else {
			if(card1==1||card1==2) card1=1;
			else if(card1==3||card1==4) card1=2;
			else if(card1==5||card1==6) card1=3;
			else if(card1==7||card1==8) card1=4;
			else if(card1==9||card1==10) card1=5;
			else if(card1==11||card1==12) card1=6;
			else if(card1==13||card1==14) card1=7;
			else if(card1==15||card1==16) card1=8;
			else if(card1==17||card1==18) card1=9;
			else if(card1==19||card1==20) card1=10;
			if(card2==1||card2==2) card2=1;
			else if(card2==3||card2==4) card2=2;
			else if(card2==5||card2==6) card2=3;
			else if(card2==7||card2==8) card2=4;
			else if(card2==9||card2==10) card2=5;
			else if(card2==11||card2==12) card2=6;
			else if(card2==13||card2==14) card2=7;
			else if(card2==15||card2==16) card2=8;
			else if(card2==17||card2==18) card2=9;
			else if(card2==19||card2==20) card2=10;
			if ((card1+card2)%10==9) {
				return 19;		// ¾ÆÈ©²ı
			} else if ((card1+card2)%10==8) {
				return 18;		// ¿©´ü²ı
			} else if ((card1+card2)%10==7) {
				return 17;		// ÀÏ°ö²ı
			} else if ((card1+card2)%10==6) {
				return 16;		// ¿©¼¸²ı
			} else if ((card1+card2)%10==5) {
				return 15;		// ´Ù¼¸²ı
			} else if ((card1+card2)%10==4) {
				return 14;		// ³×²ı
			} else if ((card1+card2)%10==3) {
				return 13;		// ¼¼²ı
			} else if ((card1+card2)%10==2) {
				return 12;		// µÎ²ı
			} else if ((card1+card2)%10==1) {
				return 11;		// ÇÑ²ı
			} else if ((card1+card2)%10==0) {
				return 10;		// ¸ÁÅë
			} else {
				return -1;
			}
		}
	}
	
	public static int[] ResultSet(int card1, int card2, int card3) {
		int[] temp = new int[4];
		temp[0] = Result(card1, card2);
		temp[1] = Result(card1, card3);
		temp[2] = Result(card2, card3);
		temp[3] = 0;
		return temp;
	}
	
	public static String lastName(int result) {
		switch(result) {
		case 93800: return "3¡¤8±¤¶¯";
		case 81800: return "1¡¤8±¤¶¯";
		case 81300: return "1¡¤3±¤¶¯";
		
		case 210: return "Àå¶¯(10¶¯)";
		case 209: return "9¶¯";
		case 208: return "8¶¯";
		case 207: return "7¶¯";
		case 206: return "6¶¯";
		case 205: return "5¶¯";
		case 204: return "4¶¯";
		case 203: return "3¶¯";
		case 202: return "2¶¯";
		case 201: return "1¶¯";
		
		case 7812: return "¾Ë¸®(1¡¤2)";
		case 7814: return "µ¶»ç(1¡¤4)";
		case 7819: return "±¸»æ(9¡¤1)";
		case 7910: return "Àå»æ(10¡¤1)";
		case 7940: return "Àå»ç(10¡¤4)";
		case 7946: return "¼¼·ú(4¡¤6)";
		
		case 64171: return "4¡¤7¾ÏÇà¾î»ç";
		case 63171: return "3¡¤7¶¯ÀâÀÌ";
		case 64191: return "¸ÛÅÖ±¸¸®±¸»ç";
		case 64090: return "±¸»ç(9¡¤4)";
		
		case 19: return "°©¿À(9²ı)";
		case 18: return "8²ı";
		case 17: return "7²ı";
		case 16: return "6²ı";
		case 15: return "5²ı";
		case 14: return "4²ı";
		case 13: return "3²ı";
		case 12: return "2²ı";
		case 11: return "1²ı";
		case 10: return "¸ÁÅë(0²ı)";
		}
		return null;
	}
	
	public static ArrayList<String> GameResult(int num, String userid1, int cardresult1, String userid2, int cardresult2, String userid3, int cardresult3, String userid4, int cardresult4) {
		CardNPlayer c1 = new CardNPlayer(userid1, cardresult1);
		CardNPlayer c2 = new CardNPlayer(userid2, cardresult2);
		CardNPlayer c3 = new CardNPlayer(userid3, cardresult3);
		CardNPlayer c4 = new CardNPlayer(userid4, cardresult4);
		List<CardNPlayer> players = new ArrayList<>();
		List<Integer> cards = new ArrayList<>();
		players.add(c1); cards.add(cardresult1);
		players.add(c2); cards.add(cardresult2);
		if(num>2) players.add(c3); cards.add(cardresult3);
		if(num>3) players.add(c4); cards.add(cardresult4);
		ArrayList<String> result = new ArrayList<>();
		
		if(cards.contains(93800)) {
			for(CardNPlayer n : players) {
				if(n.getCardresult()==93800) {
					result.add(n.getUserid());
					return result;
				}
			}
		} else if (cards.contains(81800)) {
			if(cards.contains(64171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==64171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==81800) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(81300)) {
			if(cards.contains(64171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==64171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==81300) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(210)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==210) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(209)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==209) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(208)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==208) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(207)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==207) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(206)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==206) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(205)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==205) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(204)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==204) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(203)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==203) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(202)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==202) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(201)) {
			if(cards.contains(64191)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else if(cards.contains(63171)) {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						result.add(n.getUserid());
						return result;
					}
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==201) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7812)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7812) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7814)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7814) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7819)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7819) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7910)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7910) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7940)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7940) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(7946)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
					return result;
				}
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==7946) {
						result.add(n.getUserid());
						return result;
					}
				}
			}
		} else if (cards.contains(19)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==19) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(18)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==18) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(17)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==17) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(16)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==16) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(15)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==15) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(14)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==14) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(13)||cards.contains(64191)||cards.contains(64090)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==13) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(12)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==12) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(11)||cards.contains(64171)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==64171) {
						n.setCardresult(11);
					}
				}
				for(CardNPlayer n : players) {
					if(n.getCardresult()==11) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		} else if (cards.contains(10)||cards.contains(63171)) {
			if(cards.contains(64191)||cards.contains(64090)) {
				for(CardNPlayer n : players) {
					result.add(n.getUserid());
				}
				return result;
			} else {
				for(CardNPlayer n : players) {
					if(n.getCardresult()==63171) {
						n.setCardresult(10);
					}
				}
				for(CardNPlayer n : players) {
					if(n.getCardresult()==10) {
						result.add(n.getUserid());
					}
				}
				return result;
			}
		}
		return result;
	}
}
