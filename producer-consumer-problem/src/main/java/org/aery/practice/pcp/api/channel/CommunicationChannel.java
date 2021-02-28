package org.aery.practice.pcp.api.channel;

import org.aery.practice.pcp.error.CommunicationFinishedException;

public interface CommunicationChannel {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	public Class<? extends CommunicationChannel> getChannelType();

	public String getChannelId();

	public boolean isFinish();

	/** {@link #receive(long)} */
	public default String receiveImmediately() throws CommunicationFinishedException {
		return receive(0);
	}

	/**
	 * 從溝通管道取得訊息
	 * 
	 * @param waitingMs
	 *            等多久之後thread會被喚醒, 若小於等於0則表示立即回應
	 * @return 接受到的訊息, 回傳null則表示沒有訊息
	 * @throws CommunicationFinishedException
	 *             溝通管道已經被關閉則會拋出此錯誤
	 */
	public String receive(long waitingMs) throws CommunicationFinishedException;

	/**
	 * 從溝通管道送出訊息
	 * 
	 * @param msg
	 *            要送出的訊息
	 * @throws CommunicationFinishedException
	 *             溝通管道已經被關閉則會拋出此錯誤
	 */
	public void send(String msg) throws CommunicationFinishedException;

	/** 關閉此溝通管道 */
	public void finish();

	/* [instance] getter/setter */

}
