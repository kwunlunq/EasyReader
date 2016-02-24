package com.ck.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticUtil {

	private static Client client = getClient();


	/**
	 * 取得 Elastic Search 連線
	 * 
	 * @return client
	 */
	public static Client getClient() {
		if (client == null) {
			synchronized (ElasticUtil.class) {
				if (client == null) {
					try {
//			            Settings settings = ImmutableSettings.settingsBuilder()
//			                    .put("cluster.name", "elasticsearch").build();
						Client tmpClient = TransportClient.builder().build()
								.addTransportAddress(new InetSocketTransportAddress(
												InetAddress.getByName("127.0.0.1"), 9300));
						client = tmpClient;
						System.out.println("ES Client create success.");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return client;
	}
	
	/**
	 * 建立索引
	 * @param index : database
	 * @param type  : table
	 * @param id
	 * @param obj
	 * @return bool : isCreated
	 */
	public static boolean createIndex(String index, String type, String id, byte[] json) {
		IndexResponse resp = client.prepareIndex(index, type, id).setSource(json).get();
		return resp.isCreated();
	}
	
	/**
	 * 建立索引, 預設id
	 * @param index : database
	 * @param type  : table
	 * @param obj
	 * @return bool : isCreated
	 */
	public static boolean createIndex(String index, String type, byte[] json) {
		IndexResponse resp = client.prepareIndex(index, type).setSource(json).get();
		return resp.isCreated();
	}
	
	public static String getById(String index, String type, String id) {
		return client.prepareGet(index, type, id).get().getSource().toString();
	}
	

	/**
	 * 關閉連線
	 */
	public static void closeClient() {
		if (client != null) {
			client.close();
			client = null;
		}
		System.out.println("ES Client closed");
	}
}
