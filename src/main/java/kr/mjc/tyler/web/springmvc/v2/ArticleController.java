package kr.mjc.tyler.web.springmvc.v2;

import kr.mjc.tyler.web.dao.Article;
import kr.mjc.tyler.web.dao.ArticleDao;
import kr.mjc.tyler.web.dao.User;
import kr.mjc.tyler.web.dao.UserDao;
import kr.mjc.tyler.web.springmvc.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller("articleControllerV2")
@RequestMapping("/springmvc/v2/article")
public class ArticleController {

    private final ArticleDao articleDao;

    @Autowired
    public ArticleController(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    /**
     * 게시글 목록 화면
     */
    @GetMapping("/articleList")
    public void articleList( //listArticles
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int count,
            HttpSession session, Model model) {

        int offset = (page - 1) * count;  // 목록의 시작 위치
        List<Article> articleList = articleDao.listArticles(offset, count);
        int totalCount = articleDao.countArticles();
        model.addAttribute("articleList", articleList);
        model.addAttribute("totalCount", totalCount);

        // 현재 페이지와 카운트를 세션에 저장한다. 글보기, 글쓰기, 글삭제 후에 돌아올 목록
        session.setAttribute("currentPage", page);
        session.setAttribute("countPerPage", count);
    }

    /**
     * 내가 작성한 게시글 목록 화면
     @GetMapping("/articleMy")
     public void articleMy(int userId, @SessionAttribute("USER") User user, Model model) {
     List<Article> articleMy = articleDao.listMyArticles(userId);
     model.addAttribute("articleMy", articleMy);

     }
     HttpSession session;
     String userID = (String) session.getAttribute("userId");
     @RequestParam(required = false, defaultValue = "1")
     // article.setUserId(user.getUserId());
     */
    /**
     * 사용자가 작성한 게시글 목록 화면
     * made by tyler
     */

    @GetMapping("/articleMy")
    public void articleMy(@SessionAttribute("USER") User user,
                          HttpSession session, Model model) {
        int userId = user.getUserId();
        List<Article> articleMy = articleDao.listMyArticles(userId);
        model.addAttribute("articleMy", articleMy);
        session.setAttribute("currentUser", userId);
    }

    /**
     * 게시글 검색 화면
     * made by Tyler
     */
    @GetMapping("/articleSearch")
    public void articleSearch(@RequestParam(value = "keyword") String keyword,
                              Model model){
        List<Article> articleSearch = articleDao.searchArticles(keyword);
        model.addAttribute("articleSearch", articleSearch);
    }
    /**
     * 게시글 보기 화면
     */
    @GetMapping("/articleView")
    public void articleView(int articleId, Model model) {
        Article article = articleDao.getArticle(articleId);
        model.addAttribute("article", article);
    }

    /**
     * 게시글 수정 화면
     */
    @GetMapping("/articleEdit")
    public String articleEdit(int articleId,
                              @SessionAttribute("USER") User user, Model model) {
        // 자기글 체크
        Article article = articleDao.getArticle(articleId);
        if (user.getUserId() != article.getUserId()) // 사용자가 다르면 401 Unauthorized
            throw new UnauthorizedException();

        model.addAttribute("article", article);
        return "springmvc/v2/article/articleEdit";
    }

    /**
     * 게시글 등록 액션
     */
    @PostMapping("/addArticle")
    public String addArticle(Article article, @SessionAttribute("USER") User user,
                             @SessionAttribute(required = false) String countPerPage) {
        // 세션의 user로 사용자 정보를 설정한다.
        article.setUserId(user.getUserId());
        article.setName(user.getName());

        articleDao.addArticle(article);

        // 등록 후에 목록의 1페이지로 간다.
        return "redirect:/springmvc/v2/article/articleList?page=1&count=" +
                Optional.ofNullable(countPerPage).orElse("");
    }

    /**
     * 게시글 수정 액션
     */
    @PostMapping("/updateArticle")
    public String updateArticle(Article article,
                                @SessionAttribute("USER") User user) {
        article.setUserId(user.getUserId());

        int updatedRows = articleDao.updateArticle(article);
        if (updatedRows == 0)  // 사용자가 다르면 수정이 안됨. 401 Unauthorized
            throw new UnauthorizedException();

        return "redirect:/springmvc/v2/article/articleView?articleId=" +
                article.getArticleId();
    }

    /**
     * 게시글 삭제 액션
     */
    @GetMapping("/deleteArticle")
    public String deleteArticle(int articleId,
                                @SessionAttribute("USER") User user,
                                @SessionAttribute(required = false) String currentPage,
                                @SessionAttribute(required = false) String countPerPage) {
        int updatedRows = articleDao.deleteArticle(articleId, user.getUserId());
        if (updatedRows == 0) {// 사용자가 다르면 삭제가 안됨. 401 Unauthorized
            throw new UnauthorizedException();
        }

        // 삭제 후에 목록의 원래 페이지로 간다.
        return String
                .format("redirect:/springmvc/v2/article/articleList?page=%s&count=%s",
                        Optional.ofNullable(currentPage).orElse(""),
                        Optional.ofNullable(countPerPage).orElse(""));
    }
}