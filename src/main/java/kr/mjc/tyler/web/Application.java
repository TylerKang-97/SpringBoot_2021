package kr.mjc.tyler.web;

import kr.mjc.tyler.web.springmvc.v2.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ServletComponentScan // WebServlet을 스캔함
@Slf4j
public class Application implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    InterceptorRegistration loginReg =
            registry.addInterceptor(new LoginInterceptor());
    loginReg.addPathPatterns("/**/userInfo");
    loginReg.addPathPatterns("/**/articleForm");
    loginReg.addPathPatterns("/**/articleEdit");
    loginReg.addPathPatterns("/**/deleteArticle");
    loginReg.addPathPatterns("/**/articleMy");
    loginReg.addPathPatterns("/**/articleSearch");
    log.info("LoginInterceptor를 등록했습니다");
  }
}
