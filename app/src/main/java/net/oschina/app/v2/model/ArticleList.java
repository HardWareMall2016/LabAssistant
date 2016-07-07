package net.oschina.app.v2.model;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArticleList extends Entity implements ListEntity {

    private int code;
    private String desc;
    private List<Article> mArticles = new ArrayList<Article>();

    /**
     * 收藏实体类
     */
    public static class Article implements Serializable {
        private String id;
        private String title;
        private String catid;
        private String description;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public static Article parse(JSONObject response) throws IOException, AppException {
            Article question = new Article();
            question.setId(response.optString("id"));
            question.setTitle(response.optString("title"));
            question.setCatid(response.optString("catid"));
            question.setDescription(response.optString("description"));
            return question;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        this.mArticles = articles;
    }

    public static ArticleList parse(String jsonStr) throws IOException,
            AppException {
        ArticleList questionList = new ArticleList();
        Article favorite = null;
        try {
            // 把解析的String转换成Json
            JSONObject json = new JSONObject(jsonStr);
            questionList.setCode(json.getInt("code"));
            questionList.setDesc(json.getString("desc"));
            JSONArray dataList = new JSONArray(json.getString("data"));
            // 遍历jsonArray
            for (int i = 0; i < dataList.length(); i++) {
                favorite = Article.parse(dataList.getJSONObject(i));
                questionList.getArticles().add(favorite);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    @Override
    public List<?> getList() {
        return mArticles;
    }
}
