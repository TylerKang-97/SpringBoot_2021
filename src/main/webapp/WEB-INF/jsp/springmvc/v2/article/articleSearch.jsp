<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <style>
        body {
            background-color: #74b9ff;
            text: white;
        }
        table {
            margin: 10px 300px;
            width: 900px;
            table-layout: fixed;
            text-align: center;
        }
        table.type01 thead th {
            background-color: #2d3436;
            padding: 10px;
            font-weight: bold;
            vertical-align: top;
            color: white;
            border-bottom: 3px solid #036;

        }
        table.type01 td {
            width: 350px;
            padding: 10px;
            vertical-align: top;
            border-bottom: 1px solid beige;
            border-right: 1px solid beige;
        }
    </style>
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
<table class="type01">
    <thead>
    <tr>
        <th scope="cols">게시글번호</th>
        <th scope="cols">게시글 제목</th>
        <th scope="cols">글쓴이</th>
        <th scope="cols">업로드 시간</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="article" items="${articleSearch}">
        <tr>
            <td>${article.articleId}.</td>
            <td><a href="./springmvc/v2/article/articleView?articleId=${article.articleId}">
                    ${article.title}</a></td>
            <td>${article.name}</td>
            <td>${article.udate}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>