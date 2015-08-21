package org.solq.dht.core.protocol.torrent;
 
/***
 * bt 是否已死亡讨论 http://tieba.baidu.com/p/2586096648
 * Torrent 协议常量
 * @author solq
 * */
public abstract class TorrentProtocol {

	
//	[{
//		creation date=1439998239, 
//		nodes=[[15107, 0], [29911, 0], [60342, 0], [16130, 0], [59718, 0], [51413, 0], [65002, 0], [54057, 0], [6881, 0], [6881, 0]], 
//		announce-list=[[http://bt.mp4ba.com:2710/announce], [udp://tracker.openbittorrent.com:80/announce], [udp://tracker.publicbt.com:80/announce], [udp://tracker.istole.it:80/announce], [http://trackers.ibzu.me/announce.php], [http://tracker1.torrentino.com/announce], [http://tracker2.torrentino.com/announce], [http://tracker3.torrentino.com/announce], [http://bt.careland.com.cn:6969/announce], [http://bt2.careland.com.cn:6969/announce], [http://bt3.careland.com.cn:6969/announce], [http://94.228.192.98/announce], [http://121.14.98.151:9090/announce], [http://henbt.com:2710/announce], [http://server1.9sheng.cn:6969/announce], [http://anisaishuu.de:2710/announce], [udp://explodie.org:6969/announce], [udp://tracker.leechers-paradise.org:6969/announce], [http://tracker.shuntv.net/announce.php], [http://tracker.tvunderground.org.ru:3218/announce], [udp://eddie4.nl:6969/announce], [http://tracker2.wasabii.com.tw:6969/announce], [udp://mgtracker.org:2710/announce], [udp://shadowshq.yi.org:6969/announce], [udp://shadowshq.eddie4.nl:6969/announce], [udp://10.rarbg.me:80/announce], [udp://9.rarbg.com:2710/announce], [udp://tracker.yify-torrents.com/announce], [udp://tracker.coppersurfer.tk:6969/announce], [http://siambit.org/announce.php], [udp://open.demonii.com:1337/announce], [udp://tracker.torrenty.org:6969/announce], [http://tracker.torrenty.org:6969/announce], [udp://tracker.publichd.eu:80/announce], [http://tracker.pimp4003.net/announce], [http://tracker.xfsub.com:6868/announce]],
//		encoding=UTF-8, 
//		created by=BitComet/1.35, 
//		announce=http://bt.mp4ba.com:2710/announce, 
//		info= 
//	}]
	
 
	public static final String KEY_CREATIONDATE="creation date";
	public static final String KEY_NODES="nodes";
	public static final String KEY_ANNOUNCELIST="announce-list";
	public static final String KEY_CREATEDBY="createdBy";
	public static final String KEY_ENCODING="encoding";
	public static final String KEY_ANNOUNCE="announce";
	public static final String KEY_INFO="info";

}
