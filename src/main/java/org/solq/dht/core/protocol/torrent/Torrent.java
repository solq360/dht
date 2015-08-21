package org.solq.dht.core.protocol.torrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.solq.dht.core.protocol.bencode.BDecodeUtils;
import org.solq.dht.core.protocol.bencode.BObject;

/**
 * 
 * @author solq
 */
@SuppressWarnings("unchecked")
public class Torrent {
	
	private Date creationDate;
	private List<String> nodes;
	private List<List<String>> announceList;
	private String encoding;
	private String createdBy;
	private String announce;
	private String info;

	public Torrent(String filePath) {
		InputStream fs = null;
		try {
			fs = new BufferedInputStream(new FileInputStream(new File(filePath)));
			List<Object> data = BDecodeUtils.bdecodeToJson(fs);
			parse(data);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public Torrent(InputStream fs) {
//		List<BObject> data = (List<BObject>) BDecodeUtils.bdecode(fs);
//		parse(data);
	}

	private void parse(List<Object> data) {

	}
}
