package kr.mjc.tyler.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MovieDao {

    private static final String LIST_MOVIES = """
      select movieCd, title, titleEn, productionYear, left(releaseDate,16) releaseDate, nation, genre
      from movie order by movieCd desc limit ?,?""";


    private static final String SEARCH_MOVIES = """
      select movieCd, title, titleEn, productionYear, left(releaseDate,16) releaseDate, nation, genre
      from movie where title like ? """;

    public static final String COUNT_MOVIES = """
      select count(movieCd) from movie""";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private RowMapper<Movie> rowMapper = new BeanPropertyRowMapper<>(
            Movie.class);

    @Autowired
    public MovieDao(JdbcTemplate jdbcTemplate,
                      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * 게시글 목록
     */
    public List<Movie> listMovies(int offset, int count) {
        return jdbcTemplate.query(LIST_MOVIES, rowMapper, offset, count);
    }

    /**
     * 게시글 검색
     */
    public List<Movie> searchMovies(String title) {
        return jdbcTemplate.query(SEARCH_MOVIES, rowMapper, title);
    }
    /**
     * 게시글 건수
     */
    public int countMovies() {
        return jdbcTemplate.queryForObject(COUNT_MOVIES, Integer.class);
    }



}
