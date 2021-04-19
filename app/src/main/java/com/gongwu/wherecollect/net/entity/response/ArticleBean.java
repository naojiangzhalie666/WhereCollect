package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class ArticleBean {
    private String type;
    private List<ArticleChildBean> articles;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ArticleChildBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleChildBean> articles) {
        this.articles = articles;
    }
}
