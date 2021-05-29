<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
</head>
<body>
<%@ include file="/WEB-INF/jsp/springmvc/v2/menu.jsp" %>
<h3>검색</h3>
<form action="./springmvc/v2/article/articleSearch" method="get">
    <p><input type="text" name="keyword" placeholder="제목" required autofocus/></p>
    <%-- 게시글 검색 (아직 full-text search만 가능함(2021.05.29) --%>
    <p>
        <button type="submit">검색</button>
    </p>
</form>
<c:forEach var="article" items="${articleSearch}">
    <p style="margin: 0;"><a
            href="./springmvc/v2/article/articleView?articleId=${article.articleId}">
            ${article.articleId}. ${article.title}</a> / ${article.name},
            ${article.udate}</p>
</c:forEach>

</body>
</html>