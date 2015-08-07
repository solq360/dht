package org.solq.dht.core.dht;

/***
 * DHTServer接口
 * @author solq
 * */
public interface IDHTServer {

	
	
	public void start();
	public void stop();
	
	public void bootstrap();
	
	public int getAllPeerCount(); 
	
	public int getAllBatPeerCount(); 
}
