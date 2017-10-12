package cn.com.gome.entity;

import java.util.Date;

/**
 * 
 * Date: 2017年9月22日 下午1:08:03
 * 
 * @author likaile
 * @desc 基本对象 存入 取出 es 测试开发用
 */
public class Message {
	public String title;
	public String content;
	public String author;
	public Date create_date;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

}
