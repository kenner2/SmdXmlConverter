package kenner.ko.game;

/**
 * I'm too lazy to cut these down to only what is needed.
 * @author kenner
 *
 */
public class Globals {
	public static final int MAX_USER 		= 1500;
	public static final int MAX_PARTY_SIZE 	= 8;
	public static final int MAX_COUNT		= 4096;
	public static final int NPC_BAND 		= 10000;
	public static final int MAX_SOCKET		= 100;
	
	public static final byte	VIEW_DISTANCE = 48;
	
	public static final float 	FLT_MIN = 1.175494351e-38F;
	
	/** EbenezerDlg globals **/
	public static final int	GAME_TIME			= 100;
	public static final int SEND_TIME			= 200;
	public static final int PACKET_CHECK 		= 300;
	public static final int ALIVE_TIME			= 400;
	public static final int MARKET_BBS_TIME 	= 1000;
	public static final int NUM_FLAG_VICTORY	= 4;
	public static final int AWARD_GOLD			= 5000;
	
	/** NPC STATES **/
	public static final byte NPC_DEAD = 0;
	
	/** ATTACK MORALS **/
	public static final byte MORAL_SELF 				= 1;
	public static final byte MORAL_FRIEND_WITHME 		= 2;
	public static final byte MORAL_FRIEND_EXCEPTME		= 3;
	public static final byte MORAL_PARTY 				= 4;
	public static final byte MORAL_NPC					= 5;
	public static final byte MORAL_PARTY_ALL			= 6;
	public static final byte MORAL_ENEMY				= 7;
	public static final byte MORAL_ALL					= 8;
	//9 ??
	public static final byte MORAL_AREA_ENEMY			= 10;
	public static final byte MORAL_AREA_FRIEND			= 11;
	public static final byte MORAL_AREA_ALL				= 12;
	public static final byte MORAL_SELF_AREA			= 13;
	public static final byte MORAL_CLAN					= 14;
	public static final byte MORAL_CLAN_ALL				= 15;
	public static final byte MORAL_UNDEAD				= 16;
	public static final byte MORAL_PET_WITHME			= 17;
	public static final byte MORAL_PET_ENEMY			= 18;
	public static final byte MORAL_ANIMAL1				= 19;
	public static final byte MORAL_ANIMAL2				= 20;
	public static final byte MORAL_ANIMAL3				= 21;
	public static final byte MORAL_ANGEL				= 22;
	public static final byte MORAL_DRAGON				= 23;
	//24 ??
	public static final byte MORAL_CORPSE_FRIEND		= 25;
	public static final byte MORAL_CORPSE_ENEMY			= 26;
	
	//ResHpType statuses
	public static final byte USER_DEAD = 3;
	
	//abnormal statuses
	public static final byte ABNORMAL_BLINKING = 4;
	
	/** MAGIC TYPES **/
	public static final byte MAGIC_EFFECTING = 3;
	
	//zones
	public static final short ZONE_KARUS 			= 1;
	public static final short ZONE_ELMORAD			= 2;
	public static final short ZONE_BRETH			= 3;
	public static final short ZONE_PIANA			= 4;
	public static final short ZONE_KA_ESLANT		= 11;
	public static final short ZONE_EL_ESLANT		= 12;
	public static final short ZONE_MORADON 			= 21;
	public static final short ZONE_DELOS			= 30;
	public static final short ZONE_BIFROST			= 31;
	public static final short ZONE_ABYSS			= 32;
	public static final short ZONE_HELL_ABYSS		= 33;
	public static final short ZONE_ARENA			= 48;
	public static final short ZONE_ORC_PRISON_QUEST	= 51;
	public static final short ZONE_BLOOD_DON_QUEST	= 52;
	public static final short ZONE_GOBLIN_QUEST		= 53;
	public static final short ZONE_CAPE_QUEST		= 54;
	public static final short ZONE_FORGOTTEN_TEMPLE	= 55;
	public static final short ZONE_MONSTER_STORAGE	= 91;
	public static final short ZONE_LUNAR_WAR		= 101;
	public static final short ZONE_DARK_LUNAR_WAR	= 102;
	public static final short ZONE_NEW_WAR			= 103;
	public static final short ZONE_SNOW_WAR			= 111;
	public static final short ZONE_COLONY_ZONE		= 201;
	public static final short ZONE_ARDREAM			= 202;
	public static final short ZONE_RONARK_LAND_BASE	= 203;
	
	/** ZONE EVENTS **/
	public static final byte ZONE_CHANGE			= 1;
	public static final byte ZONE_TRAP_DEAD			= 2;
	public static final byte ZONE_TRAP_AREA			= 3;
	public static final int  ZONE_TRAP_INTERVAL		= 1;
	public static final int  ZONE_TRAP_DAMAGE		= 10;
	
	/**  BBS Stuff **/
	public static final int MAX_BBS_POST	= 500;
	public static final int MAX_BBS_TITLE	= 20;
	public static final int MAX_BBS_MESSAGE	= 40;
	
	public static final int REGION_BUFF_SIZE = 1024 * 16;
	

	/**  	QUEST 		**/
	public static final short 	MAX_EVENT				= 2000;
	public static final short 	MAX_EVENT_SIZE 			= 400;
	public static final short 	MAX_EVENT_NUM			= 2000;
	public static final byte 	MAX_EXEC_INT 			= 30;
	public static final byte 	MAX_LOGIC_ELSE_INT		= 10;
	public static final byte 	MAX_MESSAGE_EVENT 		= 10;
	public static final byte 	MAX_COUPON_ID_LENGTH	= 20;
	public static final byte 	MAX_CURRENT_EVENT		= 20;
	public static final byte 	MAX_CHECK_EVENT			= 5;
	
	/** 	Knights		**/
	public static final byte	CHIEF					= 1;
	public static final byte	VICECHIEF				= 2;
	public static final byte	TRAINEE					= 3;
	public static final byte	COMMAND_CAPTAIN			= 100;
	public static final byte	CLAN_TYPE				= 1;
	public static final byte	KNIGHTS_TYPE			= 2;
	public static final byte 	MAX_CLAN				= 24;
	public static final short	MAX_KNIGHTS_BANK		= 200;
	public static final short 	MAX_KNIGHTS_MARK		= 512;
	

	/** shared memory queue **/
	public static final byte	SHARED_E			= 0;
	public static final byte	SHARED_R			= 1;
	public static final byte	SHARED_W			= 2;
	public static final byte	SHARED_RW			= 3;
	public static final int 	SMQ_BROKEN 			= 10000;
	public static final int  	SMQ_FULL			= 10001;
	public static final int  	SMQ_EMPTY			= 10002;
	public static final int  	SMQ_PKTSIZEOVER 	= 10003;
	public static final int  	SMQ_WRITING			= 10004;
	public static final int  	SMQ_READING			= 10005;
	public static final int  	SMQ_INVALID			= 10006;
	public static final byte	PAGE_NOACCESS		= 1;
	public static final byte 	PAGE_READONLY		= 2;
	public static final byte 	PAGE_READWRITE		= 4;
	public static final byte 	PAGE_WRITECOPY		= 8;
	
	/** FILE MAPPING ACCESS **/
	public static final int 	FILE_MAP_ALL_ACCESS	= 0xF001F;

	
	/** BATTLE ZONE STUFF **/
	public static final byte 	BATTLEZONE_OPEN = 0;
	public static final byte	BATTLEZONE_CLOSE = 1;
	public static final byte 	DECLARE_WINNER	= 2;
	public static final byte	DECLARE_BAN		= 3;
	public static final byte 	WAR_SYSTEM_CHAT = 8;
	public static final byte 	SEND_ALL 		= 3;
	
	public static final char 	AG_BATTLE_EVENT = 'A';
	public static final byte 	BATTLE_MAP_EVENT_RESULT = 2;
	public static final byte  	BATTLE_EVENT_RESULT = 3;
	
	
	/**  IDS  **/
	public static final byte 	IDS_KARUS_CATCH_1	= 102;
	public static final byte 	IDS_KARUS_CATCH_2	= 103;
	public static final byte 	IDS_ELMORAD_CATCH_1	= 104;
	public static final byte 	IDS_ELMORAD_CATCH_2	= 105;
	public static final byte 	IDS_KARUS_PATHWAY 	= 106;
	public static final byte 	IDS_ELMORAD_PATHWAY = 107;
	
	//sort later.
	public static final short	NPC_REGEN_TIME 		= 5000;
	public static final byte 	NORMAL_OBJECT 		= 0;
	public static final byte	SPECIAL_OBJECT 		= 1;
	public static final int 	MAX_MAP_SIZE		= 10000;
	public static final int 	MAX_MAGIC_TYPE3 	= 20;
	public static final int 	MAX_MAGIC_TYPE4 	= 9;
	public static final int 	MAX_PATH_LINE 		= 100;
	public static final int 	NPC_MAX_PATH_LIST 	= 100;
}
