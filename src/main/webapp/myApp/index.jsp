<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<c:if test="${userId eq null}">
    <a href="https://kauth.kakao.com/oauth/authorize?client_id=a9cf415eff39557471ef2d61bda1b541&redirect_uri=http://localhost:11000/kakao/login&response_type=code">
        <img src="/img/kakaoIcon.png">
    </a>
</c:if>
<c:if test="${userId ne null}">
    <h1>로그인 성공입니다</h1>
    <input type="button" value="로그아웃" onclick="location.href='/logout'">
</c:if>
</body>
</html>
