package kr.mjc.tyler.web.dao;

import lombok.Data;

@Data
public class Movie {
    String movieCd;
    String title;
    String titleEn;
    String productionYear;
    String releaseDate;
    String nation;
    String genre;


  /*
    public String getContentHtml() {
    return content.replaceAll("\n", "<br>");
    }
   */

    }
