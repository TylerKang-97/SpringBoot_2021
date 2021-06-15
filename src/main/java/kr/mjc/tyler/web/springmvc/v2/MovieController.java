package kr.mjc.tyler.web.springmvc.v2;


import kr.mjc.tyler.web.dao.Movie;
import kr.mjc.tyler.web.dao.MovieDao;
import kr.mjc.tyler.web.springmvc.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller("movieControllerV2")
@RequestMapping("/springmvc/v2/movie")
public class MovieController {

    private final MovieDao movieDao;

    @Autowired
    public MovieController(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    /**
     * movie 목록 화면
     */
    @GetMapping("/movieList")
    public void movieList(
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "20") int count,
                             HttpSession session, Model model) {

        int offset = (page - 1) * count;  // 목록의 시작 위치
        List<Movie> movieList = movieDao.listMovies(offset, count);
        int totalCount = movieDao.countMovies();
        model.addAttribute("movieList", movieList);
        model.addAttribute("totalCount", totalCount);

        // 현재 페이지와 카운트를 세션에 저장한다. 글보기, 글쓰기, 글삭제 후에 돌아올 목록
        session.setAttribute("currentPage", page);
        session.setAttribute("countPerPage", count);
    }

    /**
     * movie 검색 화면
     * made by Tyler
     *
     */
    @GetMapping("/movieSearch")
    public void movieSearch(@RequestParam(value = "keyword") String keyword,
                              Model model){
        List<Movie> movieSearch = movieDao.searchMovies('%' + keyword + '%');
        model.addAttribute("movieSearch", movieSearch);
    }

}