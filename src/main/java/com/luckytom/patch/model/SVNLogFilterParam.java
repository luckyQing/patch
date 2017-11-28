package com.luckytom.patch.model;

/**
 * svn日志过滤参数
 * 
 * @author liyulin
 * @version 1.0 2017年11月28日 下午6:22:05
 */
public class SVNLogFilterParam {
	/** 提交开始时间 */
	private String startTime;
	/** 提交截止时间 */
	private String endTime;
	/** 提交作者 */
	private String author;

	public SVNLogFilterParam() {
		super();
	}

	public SVNLogFilterParam(String startTime, String endTime, String author) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.author = author;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "SVNLogFilterParam [startTime=" + startTime + ", endTime=" + endTime + ", author=" + author + "]";
	}

}
