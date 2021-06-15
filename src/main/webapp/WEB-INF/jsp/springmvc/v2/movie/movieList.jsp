<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="count" value="${empty param.count? 20: param.count}"/>
<c:set var="page" value="${empty param.page? 1: param.page}"/>
<c:set var="maxPage" value="${Math.ceil(totalCount / count).intValue()}"/>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <style>
        body {
            background-color: #74b9ff;
            text: white;
        }
        body h2 {
            margin: 0px;
            text-align: center;
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

        form {
            width: 300px;
            margin: 10px 900px;
            text-align: right;
        }
        div {
            width: 300px;
            margin: 10px 600px;
            text-align: center;
        }
        p.counter {
            text-align: center;
        }

    </style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/springmvc/v2/menu.jsp" %>
<h2>영화 목록</h2>
<p class="counter">총 ${totalCount}건, ${maxPage}페이지</p>

<form action="./springmvc/v2/movie/movieSearch" method="get">
    <span><input type="text" name="keyword" placeholder="제목" required autofocus/></span>
    <%-- 게시글 검색 (아직 full-text search만 가능함(2021.05.29) --%>
    <span>
        <button type="submit">검색</button>
    </span>
</form>


<form name="listCountForm" method="get">
    <p>
        <button id="btnPrev" type="submit"
                <c:if test="${page <= 1}">disabled</c:if>>이전
        </button>
        <input type="number" name="page" value="${page}" min="1"
               max="${maxPage}" style="width:50px;" readonly/>
        <button id="btnNext" type="submit"
                <c:if test="${page >= maxPage}">disabled</c:if>>다음
        </button>
        <input type="number" name="count" value="${count}" min="10"
               style="width:50px;" step="10"/>행씩
    </p>
</form>


<table class="type01">
    <thead>
    <tr>
        <th width="80px" scope="cols">영화번호</th>
        <th width="150px" scope="cols">영화제목</th>
        <th width="190px" scope="cols">(영어제목)</th>
        <th width="100px" scope="cols">개봉일자</th>
        <th width="100px" scope="cols">releseDate</th>
        <th width="100px" scope="cols">국가</th>
        <th width="100px" scope="cols">장르</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="movie" items="${movieList}">
        <tr>
            <td>${movie.movieCd}.</td>
            <td>${movie.title}</td>
            <td>${movie.titleEn}</td>
            <td class="type02">${movie.productionYear}</td>
            <td>${movie.releaseDate}</td>
            <td>${movie.nation}</td>
            <td>${movie.genre}</td>

        </tr>
    </c:forEach>
    </tbody>
</table>



<script>
    document.getElementById("btnPrev").onclick = function () {
        document.forms["form1"]["page"].value--;
    };
    document.getElementById("btnNext").onclick = function () {
        document.forms["form1"]["page"].value++;
    };
    document.forms["form1"]["count"].onchange = function () {
        document.forms["form1"].submit();
    }
</script>
</body>
</html>