package org.outstudio.wxtest;

import java.io.IOException;
import java.util.List;

import org.easywechat.msg.Article;
import org.easywechat.msg.BaseMsg;
import org.easywechat.msg.ImageMsg;
import org.easywechat.msg.MusicMsg;
import org.easywechat.msg.NewsMsg;
import org.easywechat.msg.RespType;
import org.easywechat.msg.TextMsg;
import org.easywechat.msg.VideoMsg;
import org.easywechat.msg.VoiceMsg;
import org.easywechat.msg.req.BaseReq;
import org.easywechat.msg.req.MenuEvent;
import org.easywechat.msg.req.TextReqMsg;
import org.easywechat.util.MessageFactory;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class WechatUtil {

	/**
	 * 模拟发送文本消息
	 * 
	 * @return 响应信息，一般为XML格式
	 * @throws IOException
	 */
	public static String sendText(String token, String url, String text)
			throws IOException {
		String realUrl = buildUrl(token, url);
		String xmlData = createTextReqMsg(text).toXml();
		
		String resp = getConn(realUrl).rawData(xmlData).execute().body();

		return resp;
	}

	/**
	 * 模拟发送菜单点击事件
	 * 
	 * @return 响应信息，一般为XML格式
	 * @throws IOException
	 */
	public static String sendClickEvent(String token, String url,
			String eventKey) throws IOException {
		String realUrl = buildUrl(token, url);
		String xmlData = createMenuClickEvent(eventKey).toXml();
		System.out.println(xmlData);

		String resp = getConn(realUrl).rawData(xmlData).execute().body();

		return resp;
	}

	public static Document getDocFromXml(String xml) {
		return Jsoup.parse(xml, "", Parser.xmlParser());
	}

	public static BaseMsg getMsgFromDoc(Document doc) throws Exception {

		String toUserName = getTagValue("ToUserName", doc);
		String fromUserName = getTagValue("FromUserName", doc);
		String rawCreateTime = getTagValue("CreateTime", doc);
		long createTime = Long.parseLong(rawCreateTime);

		String msgType = getTagValue("MsgType", doc);

		BaseMsg msg;

		if (msgType.equals(RespType.TEXT)) {
			String content = getTagValue("Content", doc);
			msg = MessageFactory.createTextMsg(content);
		} else if (msgType.equals(RespType.NEWS)) {

			NewsMsg newsMsg = MessageFactory.createNewsMsg();

			Elements items = doc.getElementsByTag("item");
			for (Element item : items) {
				String title = getTagValue("Title", item);
				String description = getTagValue("Description", item);
				String picUrl = getTagValue("PicUrl", item);
				String Url = getTagValue("Url", item);

				newsMsg.add(title, description, picUrl, Url);
			}
			msg = newsMsg;
		} else if (msgType.equals(RespType.IMAGE)) {
			String mediaId = getTagValue("MediaId", doc);
			msg = MessageFactory.createImageMsg(mediaId);
		} else if (msgType.equals(RespType.VOICE)) {
			String mediaId = getTagValue("MediaId", doc);
			msg = MessageFactory.createVoiceMsg(mediaId);
		} else if (msgType.equals(RespType.VIDEO)) {
			String mediaId = getTagValue("MediaId", doc);
			String title = getTagValue("Title", doc);
			String description = getTagValue("Description", doc);
			msg = MessageFactory.createVideoMsg(mediaId, title, description);
		} else if (msgType.equals(RespType.MUSIC)) {
			String title = getTagValue("Title", doc);
			String description = getTagValue("Description", doc);
			String musicUrl = getTagValue("MusicUrl", doc);
			String hqMusicUrl = getTagValue("HQMusicUrl", doc);
			String thumbMediaId = getTagValue("ThumbMediaId", doc);
			msg = MessageFactory.createMusicMsg(thumbMediaId, title,
					description, musicUrl, hqMusicUrl);
		} else {
			return null;
		}

		msg.setFromUserName(fromUserName);
		msg.setToUserName(toUserName);
		msg.setCreateTime(createTime);

		return msg;
	}

	public static String getPrettyString(BaseMsg msg) {
		StringBuilder sb = new StringBuilder();
		String space = "  ";

		if (msg instanceof TextMsg) {
			TextMsg textMsg = (TextMsg) msg;
			sb.append("文本消息：\n");
			sb.append(space).append("Content：").append(textMsg.getContent())
					.append("\n");
		} else if (msg instanceof ImageMsg) {
			ImageMsg imageMsg = (ImageMsg) msg;
			sb.append("图片消息：\n");
			sb.append(space).append("MediaId：").append(imageMsg.getMediaId())
					.append("\n");
		} else if (msg instanceof VoiceMsg) {
			VoiceMsg voiceMsg = (VoiceMsg) msg;
			sb.append("语音消息：\n");
			sb.append(space).append("MediaId：").append(voiceMsg.getMediaId())
					.append("\n");
		} else if (msg instanceof VideoMsg) {
			VideoMsg videoMsg = (VideoMsg) msg;
			sb.append("视频消息：\n");
			sb.append(space).append("MediaId：").append(videoMsg.getMediaId())
					.append("\n");
			sb.append(space).append("Title：").append(videoMsg.getTitle())
					.append("\n");
			sb.append(space).append("Description：")
					.append(videoMsg.getDescription()).append("\n");
		} else if (msg instanceof MusicMsg) {
			MusicMsg musicMsg = (MusicMsg) msg;
			sb.append("视频消息：\n");
			sb.append(space).append("Title：").append(musicMsg.getTitle())
					.append("\n");
			sb.append(space).append("Description：")
					.append(musicMsg.getDescription()).append("\n");
			sb.append(space).append("MusicUrl：").append(musicMsg.getMusicUrl())
					.append("\n");
			sb.append(space).append("HQMusicUrl：")
					.append(musicMsg.getHqMusicUrl()).append("\n");
			sb.append(space).append("ThumbMediaId：")
					.append(musicMsg.getThumbMediaId()).append("\n");
		} else if (msg instanceof NewsMsg) {
			NewsMsg newsMsg = (NewsMsg) msg;
			sb.append("图文消息：\n");
			List<Article> articles = newsMsg.getArticles();
			for (int i = 0; i < articles.size(); i++) {
				sb.append(space).append("第 ").append(i + 1).append(" 条：\n");

				Article article = articles.get(i);

				String title = article.getTitle();
				String description = article.getDescription();
				String picUrl = article.getPicUrl();
				String url = article.getUrl();

				if (title != null) {
					sb.append(space).append(space).append("Title：")
							.append(title).append("\n");
				}
				if (description != null) {
					sb.append(space).append(space).append("Description：")
							.append(description).append("\n");
				}
				if (picUrl != null) {
					sb.append(space).append(space).append("PicUrl：")
							.append(picUrl).append("\n");
				}
				if (url != null) {
					sb.append(space).append(space).append("Url：").append(url)
							.append("\n");
				}
			}
		}

		sb.append("\n");
		sb.append("FromUserName：").append(msg.getFromUserName()).append("\n");
		sb.append("ToUserName：").append(msg.getToUserName()).append("\n");
		long createTime = msg.getCreateTime();
		sb.append("CreateTime：").append(createTime).append("（")
				.append(TimeUtil.getDatetimeFromCreateTime(createTime))
				.append("）\n");

		return sb.toString();
	}

	/**
	 * 得到适合该项目的Connection对象
	 */
	private static Connection getConn(String url) {
		return Jsoup.connect(url).header("Content-Type", "application/json")
				.ignoreContentType(true).timeout(10000).method(Method.POST);
	}

	private static String buildUrl(String token, String rawUrl) {
		StringBuilder sb = new StringBuilder(rawUrl);

		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonce = "zzz";
		String signature = SignatureUtil.getSignature(token, timestamp, nonce);

		sb.append("?signature=").append(signature).append("&timestamp=")
				.append(timestamp).append("&nonce=").append(nonce);
		return sb.toString();
	}

	private static TextReqMsg createTextReqMsg(String content) {
		TextReqMsg msg = new TextReqMsg(content);
		buildReqBody(msg);
		return msg;
	}

	private static MenuEvent createMenuClickEvent(String eventKey) {
		MenuEvent event = new MenuEvent(eventKey);
		event.setEvent("CLICK");
		buildReqBody(event);
		return event;
	}

	private static void buildReqBody(BaseReq req) {
		req.setFromUserName("FromUserName");
		req.setToUserName("ToUserName");
		req.setCreateTime(System.currentTimeMillis() / 1000);
	}

	private static String getTagValue(String tagName, Element ele) {
		Elements eles = ele.getElementsByTag(tagName);
		return eles.isEmpty() ? null : eles.get(0).text();
	}

	public static void main(String[] args) throws IOException {

		String host = "http://sdujiuye.jd-app.com/";
		// String host = "http://127.0.0.1:8080/EasyWeixin/";
		String rawUrl = host + "coreServlet";

		String token = "out";

		String text = "ZaiXianZhaoPin";

		String resp = sendClickEvent(token, rawUrl, text);

		System.out.println(resp);

	}

}

// <xml>
// <ToUserName><![CDATA[toUser]]></ToUserName>
// <FromUserName><![CDATA[fromUser]]></FromUserName>
// <CreateTime>1348831860</CreateTime>
// <MsgType><![CDATA[text]]></MsgType>
// <Content><![CDATA[this is a test]]></Content>
// <MsgId>1234567890123456</MsgId>
// </xml>