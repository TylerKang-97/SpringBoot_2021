package kr.mjc.tyler.web.mvc;

import kr.mjc.tyler.web.dao.Article;
import kr.mjc.tyler.web.dao.ArticleDao;
import kr.mjc.tyler.web.dao.User;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class ArticleController {

    private final ArticleDao articleDao;

    @Autowired
    public ArticleController(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    /**
     * 게시글 목록 화면
     */
    public void articleList(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        String pageStr =
                Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Integer.parseInt(pageStr);
        int count = 25;
        int offset = (page - 1) * count;
        List<Article> articleList = articleDao.listArticles(offset, count);

        request.setAttribute("articleList", articleList);
        request.getRequestDispatcher("/WEB-INF/jsp/mvc/article/articleList.jsp")
                .forward(request, response);
    }

    /**
     * 게시글 보기 화면
     */
    public void articleView(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        int articleId = Integer.parseInt(request.getParameter("articleId"));
        Article article = articleDao.getArticle(articleId);
        request.setAttribute("article", article);
        request.getRequestDispatcher("/WEB-INF/jsp/mvc/article/articleView.jsp")
                .forward(request, response);
    }

    /**
     * 게시글 쓰기 화면. 로그인 안했으면 로그인 화면으로 redirect.
     */
    public void articleForm(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        // 로그인 체크
        User user = (User) session.getAttribute("USER");
        if (user == null) { // 로그인 안했으면 로그인 화면으로 redirect
            response.sendRedirect(request.getContextPath() + "/mvc/user/loginForm");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/mvc/article/articleForm.jsp")
                .forward(request, response);
    }

    /**
     * 게시글 수정 화면. 로그인 안했으면 로그인 화면으로 redirect.
     * 사용자가 다르면 401 Unauthorized.
     */
    public void articleEdit(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        // 로그인 체크
        User user = (User) session.getAttribute("USER");
        if (user == null) { // 로그인 안했으면 로그인 화면으로 redirect
            response.sendRedirect(request.getContextPath() + "/mvc/user/loginForm");
            return;
        }
        int articleId = Integer.parseInt(request.getParameter("articleId"));

        // 자기글 체크
        Article article = articleDao.getArticle(articleId);
        if (user.getUserId() == article.getUserId()) { // 사용자가 같으면 수정화면으로
            request.setAttribute("article", article);
            request.getRequestDispatcher("/WEB-INF/jsp/mvc/article/articleEdit.jsp")
                    .forward(request, response);
        } else { // 사용자가 다르면 401 Unauthorized
            response.sendError(Response.SC_UNAUTHORIZED);
        }
    }

    /**
     * 게시글 등록 액션. 로그인 안했으면 로그인 화면으로 redirect.
     */
    public void addArticle(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        // 로그인 체크
        User user = (User) session.getAttribute("USER");
        if (user == null) { // 로그인 안했으면 로그인 화면으로 redirect
            response.sendRedirect(request.getContextPath() + "/mvc/user/loginForm");
            return;
        }
        Article article = new Article();
        article.setTitle(request.getParameter("title"));
        article.setContent(request.getParameter("content"));
        // 세션의 user로 사용자 정보를 설정한다.
        article.setUserId(user.getUserId());
        article.setName(user.getName());

        articleDao.addArticle(article);
        response.sendRedirect("/mvc/article/articleList");
    }

    /**
     * 게시글 수정 액션. 로그인 안했으면 로그인 화면으로 redirect.
     * 사용자가 다르면 401 Unauthorized.
     */
    public void updateArticle(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        // 로그인 체크
        User user = (User) session.getAttribute("USER");
        if (user == null) { // 로그인 안했으면 로그인 화면으로 redirect
            response.sendRedirect(request.getContextPath() + "/mvc/user/loginForm");
            return;
        }

        Article article = new Article();
        article.setArticleId(Integer.parseInt(request.getParameter("articleId")));
        article.setTitle(request.getParameter("title"));
        article.setContent(request.getParameter("content"));
        article.setUserId(user.getUserId());

        int updatedRows = articleDao.updateArticle(article);
        if (updatedRows > 0)  // 성공하면 게시글 보기 화면으로
            response.sendRedirect(
                    request.getContextPath() + "/mvc/article/articleView?articleId=" +
                            article.getArticleId());
        else // 사용자가 다르면 수정이 안됨. 401 Unauthorized
            response.sendError(Response.SC_UNAUTHORIZED);
    }

    /**
     * 게시글 삭제 액션. 로그인 안했으면 로그인 화면으로 redirect.
     * 사용자가 다르면 401 Unauthorized.
     */
    public void deleteArticle(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        // 로그인 체크
        User user = (User) session.getAttribute("USER");
        if (user == null) { // 로그인 안했으면 로그인 화면으로 redirect
            response.sendRedirect(request.getContextPath() + "/mvc/user/loginForm");
            return;
        }

        int articleId = Integer.parseInt(request.getParameter("articleId"));

        int updatedRows = articleDao.deleteArticle(articleId, user.getUserId());
        if (updatedRows > 0)  // 성공하면 게시글 목록 화면으로
            response
                    .sendRedirect(request.getContextPath() + "/mvc/article/articleList");
        else // 사용자가 다르면 삭제가 안됨. 401 Unauthorized
            response.sendError(Response.SC_UNAUTHORIZED);
    }
}
