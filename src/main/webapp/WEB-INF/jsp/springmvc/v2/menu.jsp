<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    a {
        color: darkgreen;
    }
    span {
        color: darkgray;
    }
    nav {
        background-color: lightblue;
    }
</style>
<nav><span style="font-weight: bold;">[Spring MVC v2]</span> <a href="./">홈</a>
    <a href="./springmvc/v2/user/userList">사용자</a>
    <a href="./springmvc/v2/article/articleList">게시글</a>
    <a href="./springmvc/v2/article/articleSearch?keyword=">게시글 검색</a>

    <c:choose>
        <c:when test="${!empty sessionScope.USER}"><!-- 로그인을 했으면 -->
            <a href="./springmvc/v2/user/userInfo">${sessionScope.USER.name}</a>님
            <a href="./springmvc/v2/user/logout">로그아웃</a>
            <a href="./springmvc/v2/article/articleMy">${sessionScope.USER.name}님의게시글</a>

        </c:when>
        <c:otherwise>
            <a href="./springmvc/v2/user/loginForm">로그인</a>
            <a href="./springmvc/v2/user/userForm">회원가입</a>
        </c:otherwise>
    </c:choose>
</nav>