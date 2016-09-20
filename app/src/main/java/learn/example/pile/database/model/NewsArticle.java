package learn.example.pile.database.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created on 2016/9/20.
 */
@Table(name = "NewsArticles")
public class NewsArticle extends Model{

    public NewsArticle() {
        super();
    }

    @Column(name = "docid",unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    public String docid;

    @Column(name = "html")
    public String html;
}
