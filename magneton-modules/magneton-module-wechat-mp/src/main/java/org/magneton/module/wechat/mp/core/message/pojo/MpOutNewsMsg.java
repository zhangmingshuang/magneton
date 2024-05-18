package org.magneton.module.wechat.mp.core.message.pojo;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.magneton.module.wechat.mp.core.message.annotation.OutMsgType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 图文消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString
@OutMsgType("news")
public class MpOutNewsMsg extends MpOutMsg {

	/**
	 * 图文消息列表，10条以内。
	 * <p>
	 * 多图文消息中会有大图和小图的区别，第一个item中的图片为大图，其他item中的图片为小图。
	 * <p>
	 * 单图文消息示例： <pre>
	 *     图片标题
	 *     日期
	 *     ****************
	 *     *** 大图的图片 ***
	 *     ****************
	 *     图片的描述信息
	 *     查看全文
	 * </pre>
	 * <p>
	 * 不定图片和链接的情况：没有图片，则不会显示日期，因为没有带链接，也不会显示查看全文 <pre>
	 *     图片标题
	 *     图片的描述信息
	 * </pre>
	 * <p>
	 * 多图文，描述内容不会在返回中显示 <pre>
	 * ******************
	 * ****** 大图 *******
	 * ** 图片标题    *****
	 * ******************
	 * * 图片2标题 * 图片2 *
	 * * 图片3标题 * 图片3 *
	 * ******************
	 * </pre>
	 */
	private List<Article> articles;

	public static MpOutNewsMsg of(List<Article> articles) {
		MpOutNewsMsg mpOutNewsMsg = new MpOutNewsMsg();
		mpOutNewsMsg.setArticles(articles);
		return mpOutNewsMsg;
	}

	public static MpOutNewsMsg of(Article article) {
		MpOutNewsMsg mpOutNewsMsg = new MpOutNewsMsg();
		mpOutNewsMsg.setArticles(Lists.newArrayList(article));
		return mpOutNewsMsg;
	}

	@Override
	public WxMpXmlOutMessage toWxMpXmlOutMessage(WxMpXmlMessage inMessage) {
		if (CollectionUtil.isEmpty(this.articles)) {
			return null;
		}
		if (this.articles.size() > 10) {
			throw new IllegalArgumentException("图文消息列表，10条以内");
		}

		List<WxMpXmlOutNewsMessage.Item> items = Lists.newArrayList();
		for (Article article : this.articles) {
			WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
			item.setDescription(article.getDescription());
			item.setPicUrl(article.getPicUrl());
			item.setTitle(article.getTitle());
			item.setUrl(article.getUrl());
			items.add(item);
		}
		return WxMpXmlOutMessage.NEWS().fromUser(inMessage.getToUser()).toUser(inMessage.getFromUser()).articles(items)
				.build();
	}

	@Setter
	@Getter
	@ToString
	public static final class Article {

		/**
		 * 图文消息标题.
		 */
		@Nullable
		private String title;

		/**
		 * 图文消息描述.
		 */
		@Nullable
		private String description;

		/**
		 * 图文消息封面图片.
		 * <p>
		 * 支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
		 */
		@Nullable
		private String picUrl;

		/**
		 * 点击图文消息跳转链接.
		 */
		@Nullable
		private String url;

		public static Article of(String title, String description, String picUrl, String url) {
			Article article = new Article();
			article.setTitle(title);
			article.setDescription(description);
			article.setPicUrl(picUrl);
			article.setUrl(url);
			return article;
		}

		public static Article of(String title, String description, String picUrl) {
			Article article = new Article();
			article.setTitle(title);
			article.setDescription(description);
			article.setPicUrl(picUrl);
			return article;
		}

		public static Article of(String title, String description) {
			Article article = new Article();
			article.setTitle(title);
			article.setDescription(description);
			return article;
		}

		public static Article of(String title) {
			Article article = new Article();
			article.setTitle(title);
			return article;
		}

	}

}